public class BidMain {

  public static String whatToBid(Hand cards, BidList prevBids){
		String highBid = prevBids.getLastBid();
		BidList allyBids = prevBids.getAllyBids();
		String highAlly = allyBids.getLastBid();
		boolean winning = highAlly.equals(highBid);
		//checks to see if no one has bid
		if(highBid == null)
			return openingBid(cards, prevBids.getNumberBids()+1);
		//checks to see if your team is winning the bid
		if(winning)
			return raisingBid(cards, allyBids);
		//only ran if you are currently loosing in the bids
		return counterBid(cards, prevBids, allyBids);
	}

	//ran if no one has bid yet
	public static String openingBid(Hand cards, int seat){
		int count, temp, tvalue, tsuit;
		int hcp = cards.getHCP();
		int handPoints;
		//Suits are in order power
		int[] numSuit = new int[4];
		int[] suitOrder = new int[4];
		//gets number of cards in each suit, and says which suits has the most cards
		numSuit = getNumSuit(cards);
		suitOrder = orderTheSuits(numSuit);
		
		//strong hands
		if(hcp >= 22){
			return "2C";
		}
		//handles bids with abnormally long suits
		else if(numSuit[suitOrder[0]] >= 10 && cards.getHCPInSuit(suitOrder[0]+1) + (numSuit[suitOrder[0]] - 10)*2.5 >= 9){
			return "2C";
		}
		
		
		//hand points are your hcp plus the number of cards in your 2 most common suits
		handPoints = hcp + numSuit[suitOrder[0]] + numSuit[suitOrder[1]];
		//checks for NT opening, 15-17 HCP and a well rounded hand
		if(hcp >= 15){
			temp = 1;
			for(count = 0; count < numSuit.length; count++){
				if(numSuit[count] < 2){
					temp = 0;
				}
				if(cards.getHCPInSuit(count+1) < 1){
					if(temp == 1){
						temp = 2;
					}
					else{
						temp = 0;
					}
				}
			}
			if(temp >= 1){
				if(hcp <= 17)
					return "1N";
				else if(hcp >= 20 && hcp <= 21)
					return "2N";
				else if(hcp >= 25 && hcp <= 27)
					return "3N";
			}
		}
		//if your first, second or third seat
		if(seat <= 3){
			//if your hand points are more than 20 in a suit then you bid, else pass
			if(handPoints >= 20){
				//if you have more than 5 in your most common suit then you bid
				if(numSuit[suitOrder[0]] >= 5)
					return ("1" + numToLet(suitOrder[0])); 
				//if you have 3-3 in monors bid 1C
				else if(numSuit[0] == 3 && numSuit[1] == 3)
					return "1C";
				//if you have more diamonds than spades,but not 5 card suits, bid 1D
				else if(numSuit[1] >= numSuit[0])
					return "1D";
				//only possiblity left is you have more clubs than diamonds, and no 5 card suits bid 1C
				else
					return "1C";
			}
			//if your third seat, third seat is allowed to open with a weaker hand than the other 2
			if(seat == 3){
				//checks to see if you have enough hcp
				if(hcp < 10)
					return "Pass";
				//checks to see how strong spades
				//first is HCP in spades, then number of spades, then number of weakest suit
				if(cards.getHCPInSuit('s') >= 9 && numSuit[3] >= 4 && numSuit[suitOrder[3]] >= 2){
					return "1S";
				}
				//checks fullness of all other suits, first checking the number of weakest suit
				else if(numSuit[suitOrder[3]] >= 3){
					//checks highest card in each suit
					tvalue = -1;
					tsuit = -1;
					for(count = 0; count < 4; count++){
						//adds one to make up for the difference in which the suits are stored
						//checking to see if one suit is to weak
						temp = cards.getHighestInSuit(count + 1);
						if(temp < 10)
							return "Pass";
						//finding the stongest suit
						temp = cards.getHCPInSuit(count + 1);
						if(temp >= 6 && temp >= tvalue){
							tvalue = temp;
							tsuit = count;
						}
					}
				
					//checks to see if any suit is strong enough
					if(tsuit >= 0){
						return ("1" + numToLet(tsuit+1));
					}
					//no suit was strong enough
					return "Pass";
				
				}
				//one of the suits is to weak
				else{
					return "Pass";
				}
			}
			return "Pass";
		}
		//if your fourth seat, follows rule of 15, add hcp to number of spades
		else{
			if(hcp + numSuit[3] >= 15){
				return "1S";
			}
			return "Pass";
		}
	}
	
	//ran to raise bids for more points
	public static String raisingBid(Hand cards, BidList allyBids){
		String highBid = allyBids.getLastBid();
		int value = highBid.charAt(0) - '0'; //value of the last called bid
		char suit = highBid.charAt(1); //suit of the last called bid
		int suitI; //int version of called suit
		Bid bid; //used whenever we need to follow a bid
		
		//finds support points so appropriate bid can be made
		suitI = letToNum(suit);
		
		//checks for a sign off bid, aka a game winning contract on the second bid
		if(allyBids.getNumberRealBids() == 2){
			if(suitI == 1 || suitI == 2){
				if(value == 5)
					return "Pass";
			}
			else if(suitI == 3 || suitI == 4){
				if(value == 4)
					return "Pass";
			}
			else{
				if(value == 3)
					return "Pass";
			}
		}
		
		//checks to see if first bid was 1NT
		bid = allyBids.getFirstBidAsBid();
		if(bid.getSuit() == 'N' && bid.getValue() == 1){
			return oneNTLead(cards, allyBids);
		}
		//checks to see if any other NT was lead
		else if(bid.getSuit() == 'N'){
			return offsetNTLead(cards, allyBids, bid.getValue());
		}
		//if first bid was 2C meaning the ally has a strong hand
		else if(bid.getSuit() == 'C' && bid.getValue() == 2){
			return twoClubLead(cards, allyBids);
		}
		
		//if no trump was called last
		if(suitI == 0){
			return lastBidNT(cards, allyBids);
		}
		
				
		//runs if the current called suit is a major or minor and is the first responce
		if(allyBids.getNumberRealBids() == 1){
			return secondBid(cards, allyBids);
		}
		//runs in all other cases
		else{
			return moreThanTwoBids(cards, allyBids);
		}
		
	}
	
	//ran to counter opponents bids
	public static String counterBid(Hand cards, BidList prevBids, BidList allyBids){
		String potential = "";
		int pvalue; //value of the potential
		char psuit; //suit of the potential
		int psuitI;
		String highBid = prevBids.getLastBid();
		int value = highBid.charAt(0) - '0'; //value of the last called bid
		char suit = highBid.charAt(1); //suit of the last called bid
		int suitI;
		
		//passes if both you and your ally have both already passed before or your ally has passed twice
		if(prevBids.getNumberRealBids() - allyBids.getNumberRealBids() >= 3)
			return "Pass";
		
		//makes determine if you have a legal call easier
		suitI = letToNum(suit);
		if(suitI == 0)
			suitI = 5;
		
		//find what your bid would be if the opponent never bidded
		if(allyBids.getNumberRealBids() == 0)
			potential = openingBid(cards, prevBids.getNumberBids()+1);
		else
			potential = raisingBid(cards, allyBids);
		//if pass was suggested then pass
		if(potential.equals("Pass")){
			return "Pass";
		}
		//else check if your bid is still legal
		else{
			pvalue = potential.charAt(0) - '0'; //value of the last called bid
			psuit = potential.charAt(1); //suit of the last called bid
			
			psuitI = letToNum(psuit);
				if(psuitI == 0)
				psuitI = 5;
			
			if(pvalue > value)
				return potential;
			if(pvalue < value)
				return "Pass";
			if(psuitI > suitI)
				return potential;
			//double if your opponent bid what you wanted to
			if(psuitI == suitI)
				return "DBL";
		}
		
		return "Pass";
	}
	
	public static String twoClubLead(Hand cards, BidList allyBids){
		String highBid = allyBids.getLastBid();
		int value = highBid.charAt(0) - '0'; //value of the last called bid
		char suit = highBid.charAt(1); //suit of the last called bid
		int supPoints; //support points
		int suitI = letToNum(suit); //int version of called suit
		int temp;
		Bid bid;
		int[] numSuit = new int[4]; //number of cards in each suit
		int[] suitOrder = new int[4]; //suits ordered from most cards to least cards
		
		//gets number of cards in each suit, and says which suits has the most cards
		numSuit = getNumSuit(cards);
		suitOrder = orderTheSuits(numSuit);
		
		//first responce, should never be passed
		if(allyBids.getNumberRealBids() == 1){
			for(temp = 0; temp < 4 && numSuit[suitOrder[temp]] >= 5; temp++){
				//needs 2 of the top 3 cards
				if(cards.getHCsInSuit(suitOrder[temp]+1) >= 6 && cards.getHCsInSuit(suitOrder[temp]+1) != 9){
					//if it is a mojor
					if(suitOrder[temp] >= 2)
						return ("2" + numToLet(temp+1));
					else
						return ("3" + numToLet(temp+1));
				}
			}
			if(cards.getHCP() >= 8)
				return "2N";
			return "2D";
		}
		
		//sets bid to second call
		bid = allyBids.getFirstBidAsBid();
		bid = bid.getNextBid();
		
		//if the second bid was 2D
		if(bid.getSuit() == 'D' && bid.getValue() == 2){
			if(allyBids.getNumberRealBids() == 2){
				if(numSuit[suitOrder[0]] < 5 && cards.getHCP() <= 24)
					return "2N";
				else if(numSuit[suitOrder[0]] < 5 && cards.getHCP() >= 25)
					return "3N";
				//sees if you are really strong in one suit
				for(temp = 0; temp < 4 && numSuit[suitOrder[temp]] >= 5; temp++){
					if(cards.getHCPInSuit(suitOrder[temp]+1)*2 + numSuit[suitOrder[temp]] >= 19){
						if(suitOrder[temp] >= 3)
							return ("3" + numToLet(suitOrder[temp]+1));
						else
							return ("4" + numToLet(suitOrder[temp]+1));
					}
					else if(cards.getHCPInSuit(suitOrder[temp]+1)*2 + numSuit[suitOrder[temp]] >= 15){
						if(suitOrder[temp] >= 3)
							return ("2" + numToLet(suitOrder[temp]+1));
						else
							return ("3" + numToLet(suitOrder[temp]+1));
					}
				}
				return "2H";
			}
			else if(allyBids.getNumberRealBids() >= 3){
				//sets bid to the third call
				bid = bid.getNextBid();
				//a no trump was called
				if(bid.getSuit() == 'N'){
					return offsetNTLead(cards, allyBids, value);
				}
				//2H was called 3rd
				else if(bid.getSuit() == 'H' && bid.getValue() == 2){
					if(allyBids.getNumberRealBids() == 3){
						if(cards.getHCP() < 4)
							return "3C";
						else
							return "2S";
					}
					else if(allyBids.getNumberRealBids() == 4){
						for(temp = 0; temp < 4 && numSuit[suitOrder[temp]] >= 5; temp++){
							//if 3 of clubs was response to second negative
							if(suitI == 0){
								if(suitOrder[temp] == 0)
									return "Pass";
								else
									return ("3" + numToLet(suitOrder[temp]+1));
							}
							//if 2 of Spades was response to second negative
							else{
								if(suitOrder[temp] >= 3)
									return ("4" + numToLet(suitOrder[temp]+1));
								else
									return ("5" + numToLet(suitOrder[temp]+1));
							}
						}
						return "3N";
					}
					else
						return "Pass";
				}
				//not no trump or 2H was called 3rd
				else if(allyBids.getNumberRealBids() == 3)
					return ((value + 1) + "" + suit);
				else
					return "Pass";
			}
		}
		return "Pass";
	}
	
	public static String secondBid(Hand cards, BidList allyBids){
		String highBid = allyBids.getLastBid();
		int value = highBid.charAt(0) - '0'; //value of the last called bid
		char suit = highBid.charAt(1); //suit of the last called bid
		int supPoints; //support points
		int suitI; //int version of called suit
		int cardsInSuit; //# of cards in a suit
		int posSuit; //short for possible suit
		int temp; //all temp variable data does not matter for more than 6 lines
		String tempS;
		char tempC;
		int handPoints; //over all value of hand
		//Suits are in order power
		int[] numSuit = new int[4]; //number of cards in each suit
		int[] suitOrder = new int[4]; //suits ordered from most cards to least cards
		
		//gets number of cards in each suit, and says which suits has the most cards
		numSuit = getNumSuit(cards);
		suitOrder = orderTheSuits(numSuit);
		
		//finds support points so appropriate bid can be made
		supPoints = cards.getHCP();
		suitI = letToNum(suit);

		//the -1 is for translation purposes
		cardsInSuit = numSuit[suitI-1];
		
		if(suitI >= 3 && value == 1){
			//next call depends on number of cards in the suit that is trump
			if(cardsInSuit >= 4){
				//adds support points if a number of cards in a suit is small
				for(int i = 0; i < numSuit.length; i++){
					if(numSuit[i] <= 2){
						if(numSuit[i] == 0)
							supPoints += 5;
						else if(numSuit[i] == 1){
							//don't add as many points if the cards in question already give points
							temp = cards.getHCPInSuit(numToLet(i));
							//aces still get points because they are high cards
							if(temp == 4)
								temp = 6;
							else if(temp <= 3)
								temp = 3;
							supPoints += temp;
						}
						else{
							//don't add as many points if the cards in question already give points
							temp = cards.getHCPInSuit(numToLet(i));
							//working honors still get points, non-working do not
							if(temp >= 4)
								temp += 1;
							else if(temp <= 1)
								temp = 1;
							supPoints += temp;
						}
					}
				}
			}
			else if(cardsInSuit == 3){
				for(int i = 0; i < numSuit.length; i++){
					if(numSuit[i] <= 2){
						if(numSuit[i] == 0)
							supPoints += 3;
						else if(numSuit[i] == 1){
							//don't add as many points if the cards in question already give points
							temp = cards.getHCPInSuit(numToLet(i));
							//aces still get points because they are high cards
							if(temp == 4)
								temp = 6;
							else if(temp <= 2)
								temp = 2;
							supPoints += temp;
						}
						else{
							//don't add as many points if the cards in question already give points
							temp = cards.getHCPInSuit(numToLet(i));
							//working honors still get points, non-working do not
							if(temp >= 4)
								temp += 1;
							else if(temp <= 1)
								temp = 1;
							supPoints += temp;
						}
					}
				}
			}
			//with less than 3 cards in the current called trump, support points is equal to your HCP
			
			//if your hand is really bad, then pass
			if(supPoints < 6){
				return "Pass";
			}
			//runs if you have 6-10 support Points
			else if(supPoints <= 10){
				//if you have 5 or more in the suit called jump bid to 4 of the suit
				if(cardsInSuit >=5){
					return ("4" + suit);
				}
				//if you have 3 or more in the suit called support your partner
				else if(cardsInSuit >= 3){
					return ("2" + suit);
				}
				//if your ally called hearts, but your hearts are bad, but you have spades call 1S
				else if(suitI == 3 && numSuit[3] >= 4 && cards.getHCPInSuit('S') >= 4){
					return "1S";
				}
				//else raise to 1 no trump
				else{
					return "1N";
				}
			}
			//runs if you have 11-12 support points
			else if(supPoints <= 12){
				//support you partner whenever possible
				if(cardsInSuit >= 5){
					return ("4" + suit);
				}
				else if((cardsInSuit >= 4) || (cardsInSuit >= 3 && cards.getHCPInSuit(suit) >= 3)){
					return ("3" + suit);
				}
				//if you don't have the suit called but you have a better one raise in a way to suggest a new one
				//first if is for calling spades if hearts was called, next if is for all other circumstances
				else if(suitI == 3 && numSuit[3] >= 4 && cards.getHCPInSuit(4) >= 3){
					return "1S";
				}
				else{
					//if your greatest suit is 4 return you lowest 4 length suit, if it is 5 or more, return the greatest
					if(numSuit[suitOrder[0]] <= 4){
						if(numSuit[suitOrder[2]] == 4){
							return ("2" + numToLet(suitOrder[2]));
						}
						else if(numSuit[suitOrder[1]] == 4){
							return ("2" + numToLet(suitOrder[1]));
						}
						else{
							return ("2" + numToLet(suitOrder[0]));
						}
					}
					else{
						return ("2" + numToLet(suitOrder[0]));
					}
				}
			}
			//runs if you have 13-18 support points
			else if(supPoints <= 18){
				handPoints = cards.getHCP() + numSuit[suitOrder[0]] + numSuit[suitOrder[1]];
				//Jacoby 2NT, this shows a hand worthy of opening, and 4+ cards in the suit the partner bid
				if(cardsInSuit >= 4 && handPoints >= 20){
					return "2N";
				}
				//bid the lowest possible number of your most common suit, unless that was the opener's suit
				//if it is the same, bid the next most powerful suit with the same number of cards
				//else bid 2C
				temp = suitOrder[0];
				if(temp != suitI - 1){
					if(numSuit[suitOrder[0]] == numSuit[suitOrder[1]]){
						temp = suitOrder[1];
					}
					else{
						//makes you bid 2C
						temp = 0;
					}
				}
				//checks if opening was hearts, and spades are desired to bid
				if(temp == 3){
					return "1S";
				}
				else if(temp == 2){
					return "2H";
				}
				else if(temp == 1){
					return "2D";
				}
				else{
					return "2C";
				}
			}
			//runs if you have 19+ support points, slam zone
			else{
				//you need to do a jump bid to your most powerful suit that is not the led bid
				//sets posSuit to the longest suit as long as it is not the led suit
				if(suitOrder[0] != suitI - 1 ){
					posSuit = suitOrder[0];
				}
				else{
					posSuit = suitOrder[1];
				}
				for(temp = 0; temp < 4; temp ++){
					//if the suit is stong enough then looking for a new suit is stopped
					if(suitOrder[temp]!=suitI-1 && numSuit[suitOrder[temp]]>=3 && cards.getHCPInSuit(suitOrder[temp]+1)>=6){
						posSuit = suitOrder[temp];
						temp = 4;
					}
				}
				//now the skip bid is bid
				//checks for hearts to spades skip
				if(suitI == 3 && posSuit == 3){
					return "2S";
				}
				else{
					return ("3" + numToLet(posSuit+1));
				}
			}
		}
		//runs if the current called suit is a minor and is the first responce
		else if(suitI <= 2 && value == 1){
			//supPoints is just your HCP
			handPoints = supPoints;
			if(handPoints < 6){
				return "Pass";
			}
			//responding with minimum, 6-10 support
			else if(handPoints <= 10){
				//if you have good support for the minor bid 2 of that suit
				supPoints = numSuit[suitI-1] + cards.getHCPInSuit(suitI);
				if(numSuit[suitI-1] >= 4 && supPoints >= 9){
					return ("2" + suitI);
				}
				//if that fails, return your next weakest 4 card suit a 1 level if possible
				//aka, bid up the line
				temp = suitI - 1;
				temp ++;
				while(temp < 4){
					if(numSuit[temp] >= 4){
						return ("1" + numToLet(temp));
					}
					temp ++;
				}
				//else return 1NT
				return "1N";
			}
			//responding to 1 minor with invitational hand
			else if(handPoints <= 12){
				//show a good major, if possible: good is atleast 4 cards, and some HCP
				if(numSuit[2] >= 4 && cards.getHCPInSuit('H') >= 4){
					return "1H";
				}
				else if(numSuit[3] >= 4 && cards.getHCPInSuit('S') >= 4){
					return "1S";
				}
				//settle for the diamonds if it is good and ally called clubs
				else if(numSuit[1] >= 4 && suitI == 1 && cards.getHCPInSuit('D') >= 4){
					return "1D";
				}
				//if you have a good clubs and diamond was bid raise to 2C, must be better than before due to the 2
				if(numSuit[0] >= 5 && suitI == 2 && cards.getHCPInSuit('C') >= 4){
					return "2C";
				}
				//support the call if you don't have any other good suits and the one called is good
				if(numSuit[suitI-1] >= 5 && cards.getHCPInSuit(suit) >= 1){
					return ("3" + suit);
				}
				//if none of these options work bit 1NT
				return "1N";
			}
			//responding to 1 minor with game-forcing hand
			else if(handPoints <= 17){
				//return 2NT if no 4 card major and 13-15 points, 3NT if 16-17 points
				if(numSuit[2] < 4 && numSuit[3] < 4){
					if(handPoints <= 15){
						return "2N";
					}
					else{
						return "3N";
					}
				}
				//if hearts have atleast 4 bid 1H
				if(numSuit[2] >= 4){
					return "1H";
				}
				//else return 1S
				return "1S";
			}
			//reponding in slam zone, 18+
			else{
				//do a jump bid, preferably shifting, preferably to a major
				if(numSuit[3] >= 5 && cards.getHCPInSuit('S') >= 6){
					return "2S";
				}
				else if(numSuit[2] >= 5 && cards.getHCPInSuit('H') >= 6){
					return "2H";
				}
				else if(numSuit[1] >= 5 && suitI == 1 && cards.getHCPInSuit('D') >= 6){
					return "2D";
				}
				else if(numSuit[0] >= 5 && suitI == 2 && cards.getHCPInSuit('C') >= 6){
					return "3C";
				}
				//check to see if suit called is a good one
				if(numSuit[suitI-1] >= 5 && cards.getHCPInSuit(suit) >= 6){
					return ("3" + suit);
				}
				//else call your longest suit with atleast 4 points
				posSuit = 0;
				while(posSuit < 4){
					if(cards.getHCPInSuit(suitOrder[posSuit]+1) >= 4){
						if(suitOrder[posSuit]+1 > suitI){
							return ("2" + numToLet(suitOrder[posSuit]+1));
						}
						else{
							return ("3" + numToLet(suitOrder[posSuit]+1));
						}
					}
				}
				//else return 3 of called suit
				return ("3" + suit);
			}
		}
		return "Pass";
	}
	
	public static String lastBidNT(Hand cards, BidList allyBids){
		String highBid = allyBids.getLastBid();
		int value = highBid.charAt(0) - '0'; //value of the last called bid
		char suit = highBid.charAt(1); //suit of the last called bid
		int supPoints; //support points
		int handPoints; //over all value of hand
		Bid bid;
		int suitI; //int version of called suit
		int cardsInSuit; //# of cards in a suit
		int posSuit; //short for possible suit
		int temp; //all temp variable data does not matter for more than 6 lines
		String tempS;
		char tempC;
		char tempC2;
		//Suits are in order power
		int[] numSuit = new int[4]; //number of cards in each suit
		int[] suitOrder = new int[4]; //suits ordered from most cards to least cards
		
		//gets number of cards in each suit, and says which suits has the most cards
		numSuit = getNumSuit(cards);
		suitOrder = orderTheSuits(numSuit);
		
		//finds support points so appropriate bid can be made
		supPoints = cards.getHCP();
		suitI = letToNum(suit);
		
		//sets up bid
		bid = allyBids.getFirstBidAsBid();
		
		//if 1NT was led
		if(value == 1 && allyBids.getNumberRealBids() == 1){
			//add bonus support points for long lengthed suits
			temp = 1;
			for(int i = 0; temp < 4; temp++){
				if(numSuit[i] >= 5){
					supPoints += 1;
					temp = 0;
					if(numSuit[i] == 6){
						supPoints += 2;
					}
					else if(numSuit[i] >= 7){
						supPoints += 4;
					}
				}
			}
			//if your hand is flat, loose a point, if flat and low pointed then pass
			if(temp >= 1){
				supPoints -= 1;
				if(supPoints < 8){
					return "Pass";
				}
			}
			//checks for long strong major
			if(numSuit[2] >= 6 && cards.getHCsInSuit('H') >= 6){
				return "3H";
			}
			if(numSuit[3] >= 6 && cards.getHCsInSuit('S') >= 6){
				return "3S";
			}
			//checks for Jacoby Transfer
			//transfer to heats
			if(numSuit[2] >= 5 && cards.getHCPInSuit('H') >= 3){
				return "2D";
			}
			//transfer to spades
			if(numSuit[3] >= 5 && cards.getHCPInSuit('S') >= 3){
				return "2H";
			}
			//if you have a 4 card major, bid 2C, start of Stayman
			if(numSuit[2] >= 4 || numSuit[3] >= 4){
				return "2C";
			}
			//checks for long strong minor
			if(numSuit[0] >= 6 && cards.getHCsInSuit('C') >= 6){
				return "3C";
			}
			if(numSuit[1] >= 6 && cards.getHCsInSuit('D') >= 6){
				return "3D";
			}
			//checks for long weak minors
			if((numSuit[0] >= 7 && cards.getHCsInSuit('C') < 6)  || (numSuit[1] >= 7 && cards.getHCsInSuit('D') < 6)){
				return "2S";
			}
			//2NT - invitational
			if(cards.getHCP() >= 8 && cards.getHCP() <= 9 && numSuit[suitOrder[3]] <= 2){
				return "2N";
			}
			
			return "Pass";
		}
		//checks for a reverse after your ally bids 1NT in responce to your bid
		if(value == 1 && allyBids.getNumberRealBids() == 2){
			//checks for a reverse call to a lower suit
			for(temp = bid.getIntSuit() - 2; temp >= 0; temp --){
				if(numSuit[temp] >= 4 && cards.getHCPInSuit(temp+1) >= 4){
					return ("2" + numToLet(temp+1));
				}
			}
			//checks for a reverse into a higher suit
			for(temp = bid.getIntSuit() - 2; temp < 4 && supPoints >= 17; temp ++){
				if(numSuit[temp] >= 4 && numSuit[temp] + cards.getHCPInSuit(temp+1) >= 9){
					return ("2" + numToLet(temp+1));
				}
			}
		}
		//checks if a reverse has been done before, requires atleast 3 bids
		if(allyBids.getNumberRealBids() >= 3){
			tempC2 = bid.getNextBid().getNextBid().getSuit();
			tempC = bid.getNextBid().getSuit();
			if(bid.getSuit() != tempC && bid.getSuit() != tempC2 && tempC != tempC2){
				//value must be at 2 on the third bid, and your suit must have a higher value that your opponent's suit
				temp = letToNum(tempC);
				if(temp == 0)
					temp = 5;
				if(letToNum(tempC2) == 0)
					temp -= 5;
				else
					temp -= letToNum(tempC2);
					
				if(temp > 0 && bid.getNextBid().getNextBid().getValue() == 2){
					return reverse(cards, allyBids, bid.getSuit(), tempC, tempC2);
				}
			}
		}
			
		tempS = allyBids.getFirstBid();
		tempC = tempS.charAt(1);
		posSuit = letToNum(tempC);
		//responce to Jacoby 2NT
		//if statement confirms Jacoby 2NT
		if(value == 2 && allyBids.getNumberRealBids() == 2 && posSuit >= 3){
			//jump to game, to show a balanced minimum
			handPoints = cards.getHCP();
			if(handPoints <= 14 && numSuit[suitOrder[3]] >= 2){
				return ("4" + numToLet(posSuit));
			}
			//bid 3NT if 15-17 points and no singleton or void, shows slam interest, usually 5422 hand setup
			else if(handPoints >= 15 && handPoints <= 17 && numSuit[suitOrder[3]] >= 2){
				return "3N";
			}
			//rebid of opening at 3 level shows 18+ HCP
			else if(handPoints >= 18 && numSuit[suitOrder[3]] >= 2){
				return ("3" + numToLet(posSuit));
			}
			//bid 4 level of anouther suit if you have anouther 5 card suit with a good comp
			else if(numSuit[suitOrder[1]] >= 5 && cards.getHCPInSuit(suitOrder[1] + 1) >= 6){
				return ("4" + numToLet(suitOrder[1]));
			}
			//in all other cases bid 3 the suit that you have a void or a singleton
			else{
				return ("3" + numToLet(suitOrder[3]));
			}
		}
		return "Pass";
	}
	
	//ran if you want to treat a higher bid as 1NT
	public static String offsetNTLead(Hand cards, BidList allyBids, int offset){
		Bid bid;
		BidList newAllyBids = new BidList();
		int temp;
		int afterOff;
		//bid before offset is re-applied
		String rawBid;
		int value;
		char suit;
		
		temp = 0;
		bid = allyBids.getFirstBidAsBid();
		//remakes the bidlist with lower values and no bids before the first NT bid equal to or greater than the offset
		while(bid != null){
			if(bid.getSuit() == 'N')
				temp = 1;
			afterOff = bid.getValue() - offset + 1;
			//makes sure that a found NT isn't to low
			if(temp == 1 && afterOff <= 0)
				temp = 0;
			if(temp == 1){
				newAllyBids.addBid(afterOff, bid.getSuit());
			}
		}
		//returns pass, if no NT bid was found, this should never happen
		if(temp == 0)
			return "Pass";
			
		rawBid = oneNTLead(cards, newAllyBids);
		
		if(rawBid.equals("Pass"))
			return "Pass";
		else{
			value = rawBid.charAt(0) - '0';
			suit = rawBid.charAt(1);
			//calculates offset into value
			value = value + offset - 1;
			//makes sure value isn't to high
			if(value > 7)
				value = 7;
			 
			return (value + "" + suit);
		}
	}
	
	//ran if 1NT was the first thing bid
	public static String oneNTLead(Hand cards, BidList allyBids){
		String highBid = allyBids.getLastBid();
		int value = highBid.charAt(0) - '0'; //value of the last called bid
		char suit = highBid.charAt(1); //suit of the last called bid
		int supPoints; //support points
		int suitI; //int version of called suit
		int cardsInSuit; //# of cards in a suit
		int posSuit; //short for possible suit
		int temp; //all temp variable data does not matter for more than 6 lines
		Bid bid; //used whenever we need to follow a bid
		//Suits are in order power
		int[] numSuit = new int[4]; //number of cards in each suit
		int[] suitOrder = new int[4]; //suits ordered from most cards to least cards
		
		//gets number of cards in each suit, and says which suits has the most cards
		numSuit = getNumSuit(cards);
		suitOrder = orderTheSuits(numSuit);
		bid = allyBids.getFirstBidAsBid();
		
		//finds support points so appropriate bid can be made
		supPoints = cards.getHCP();
		suitI = letToNum(suit);
	
		//checks for only one bid
		if(allyBids.getNumberRealBids() == 1){
			return lastBidNT(cards, allyBids);
		}
		//checks what to do next based of second bid
		bid = bid.getNextBid();
		//checks for Stayman
		if(bid.getSuit() == 'C' && bid.getValue() == 2){
			//Stayman has been used
			//if first bid after stayman has been enacted
			if(allyBids.getNumberRealBids() == 2){
				//bid 2H if you have 4+ hearts, 2S for 4+ spades, and 2D for neither
				if(numSuit[2] >= 4){
					return "2H";
				}
				else if(numSuit[3] >= 4){
					return "2S";
				}
				else{
					return "2D";
				}
			}
			//if responce to partner after he has told you what major he has 4 cards in
			if(allyBids.getNumberRealBids() == 3 && value == 2){
				//bids are dependant on what your ally has 4 of and how many of it you have
				if(suitI >= 3){
					//temp will remeber if you have atleast 10 HCP
					temp = 0;
					if(supPoints >= 10){
						temp = 1;
					}
					if(numSuit[suitI-1] <= 3){
						return ((2+temp) + "N");
					}
					else{
						return ((3+temp) + "" + suit);
					}
				}
				//means ally bid 2D meaning he has no 4 card majors
				else{
					//if you have a weak hand do 2N
					if(supPoints <= 9 && numSuit[3] <= 4 && numSuit[2] <= 4){
						return "2N";
					}
					//if you have no more than 4 in any suit bid 3N
					if(numSuit[3] <= 4 && numSuit[2] <= 4){
						return "3N";
					}
					//if a have greater than 4 in one suit and 2D is called then special calls are made
					if(supPoints <= 9){
						if(numSuit[2] >= 5){
							return "2H";
						}
						//spades must have more than 5
						else{
							return "2S";
						}
					}
					//more than 9 support points
					else{
						if(numSuit[3] >= 5 && numSuit[2] >= 4){
							return "3S";
						}
						else if(numSuit[2] >= 5 && numSuit[3] >= 4){
							return "3H";
						}
						else if(numSuit[2] >= 5){
							return "2H";
						}
						else{
							return "2S";
						}
					}
				}
			}
			//moves bid to the 3th bid, aka 2 of D,H, or S
			bid = bid.getNextBid();
			//finishing touches to stayman
			if(allyBids.getNumberRealBids() == 4){
				//4th bid was one passed by function
				temp = bid.getIntSuit();
				//if I called spades or hearts earlier
				if(temp >= 3){
					//at a winning value in a good suit
					if(temp == suitI && value >= 4){
						return "Pass";
					}
					//if value was raised to 3NT
					else if(suitI == 0 && value == 3){
						//return the opposite call from earlier if I have 4 of the other suit as well
						//if i called hearts earlier
						if(temp == 3){
							if(numSuit[3] >= 4){
								return "4S";
							}
							else{
								return "Pass";
							}
						}
						//if i called spades
						else{
							if(numSuit[2] >= 4){
								return "4H";
							}
							else{
								return "Pass";
							}
						}
					}
					//bid was raised to a non-winning level
					else{
						//if my bid was raided to 2NT
						if(suitI == 0){
							return "Pass";
						}
						//if my bid was raised in the same suit to an un-winning level and i have a strong hand
						else if(numSuit[suitI-1] >= 5 && cards.getHCPInSuit(suitI) >= 7){
							return ("4" + suit);
						}
						//i only have an ok hand
						else{
							return "Pass";
						}
					}
				}
				//I called 2D
				else{
					//if suit was changed to NT pass
					if(suitI == 0){
						return "Pass";
					}
					//if my ally called 3H or 3S
					if(suitI >= 3 && value == 3){
						//if I have 3 of the called suit raise the suit, else call NT
						if(numSuit[suitI-1] >= 3){
							return ("4" + suit);
						}
						else{
							return "3N";
						}
					}
					//if my ally called 2H or 2S
					if(suitI >= 3 && value == 2){
						if(supPoints <= 14 && numSuit[suitI-1] >= 3){
							return "Pass";
						}
						else if(supPoints <= 14 && numSuit[suitI-1] <= 2){
							return "2N";
						}
						else if(supPoints <= 16 && numSuit[suitI-1] >= 3){
							return ("3" + suit);
						}
						else if(supPoints <= 16 && numSuit[suitI-1] <= 2){
							return "2N";
						}
						else if(numSuit[suitI-1] >= 3){
							return ("4" + suit);
						}
						else{
							return "3N";
						}
					}
				}
			}
			//your bid should already be chosen by this time
			return "Pass";
		}
		
		//Jacoby Transfer has been used
		if(bid.getValue() == 2 && (bid.getSuit() == 'H' || bid.getSuit() == 'D')){
			//if only 2 bids have occured, then the next bid needs to be the transfer bid
			if(allyBids.getNumberRealBids() == 2){
				//transfer to spades
				if(bid.getSuit() == 'H'){
					return "2S";
				}
				//transfer to hearts
				else{
					return "2H";
				}
			}
			//first bid after the transfer
			if(allyBids.getNumberRealBids() == 3 && value == 2){
				//if hearts is the current suit
				if(suitI == 3){
					//both hearts and spades are good for you
					if(numSuit[3] >= 5 && cards.getHCPInSuit('S') >= 3){
						return "2S";
					}
					//hand is to weak to go on
					else if(supPoints < 7){
						return "Pass";
					}
					//invitational strengthed hand
					else if(supPoints < 10){
						if(numSuit[2] >= 6 && cards.getHCPInSuit('H') >= 4){
							return "3H";
						}
						else{
							return "2N";
						}
					}
					//game forcing hand
					else{
						if(numSuit[1] >= 5 && cards.getHCPInSuit('D') >= 4){
							return "3D";
						}
						else{
							return "3N";
						}
					}
				}
				//if spades is the current suit
				else{
					//hand is to weak to go on
					if(supPoints < 7){
						return "Pass";
					}
					//invitational strengthed hand
					else if(supPoints < 10){
						if(numSuit[3] >= 6 && cards.getHCPInSuit('S') >= 4){
							return "3S";
						}
						else{
							return "2N";
						}
					}
					//game forcing hand
					else{
						if(numSuit[1] >= 5 && cards.getHCPInSuit('D') >= 4){
							return "3D";
						}
						else{
							return "3N";
						}
					}
				}
			}
			//at least 4 bids have been done
			else if(allyBids.getNumberRealBids() >= 4){
				//brings bid to the 4th slot
				bid = bid.getNextBid();
				bid = bid.getNextBid();
				//if 2 of spades was bid 4th, so that the ally has hearts and spades
				if(bid.getSuit() == 'S' && bid.getValue() == 2){
					//if you are on the fifth bid
					if(allyBids.getNumberRealBids() == 4){
						//checks to see if hand is weak
						if(numSuit[3] < 4 && numSuit[2] < 4){
							return "2N";
						}
						//gets relative spade value
						temp = 0;
						if(numSuit[3] >= 4){
							temp += numSuit[3];
							temp += cards.getHCPInSuit('S');
						}
						//compares to relative strength of hearts
						if(numSuit[2] >= 4){
							temp -= numSuit[2];
							temp -= cards.getHCPInSuit('H');
						}
						
						//spades is better
						if(temp >= 0){
							temp = numSuit[3] + cards.getHCPInSuit('S');
							if(temp < 8){
								return "Pass";
							}
							else if(temp < 10){
								return "3S";
							}
							else{
								return "4S";
							}
						}
						//hearts is better
						else{
							temp = numSuit[2] + cards.getHCPInSuit('H');
							if(temp < 10){
								return "3H";
							}
							else{
								return "4H";
							}
						}
					}
					//6th bid after 2S and a game forcing hand
					else if(allyBids.getNumberRealBids() == 5){
						//checks to see if partner already raised it high enough
						if(value >= 4 || (value == 3 && suitI == 0)){
							return "Pass";
						}
						//partner didn't bet enough
						if(suitI == 0 && supPoints >= 10){
							return "3N";
						}
						else if(numSuit[suitI-1] >= 5 && cards.getHCPInSuit(suitI) >= 4){
							return ("4" + suit);
						}
						else{
							return "Pass";
						}
					}
					return "Pass";
				}
				//2NT was the 4th bid in after a Jacoby transfer
				else if(bid.getSuit() == 'N' && bid.getValue() == 2){
					//if the it is the fifth bid
					if(allyBids.getNumberRealBids() == 4){
						//transfer bid back to the third bid
						bid = bid.getPrevBid();
						//if spades was called
						if(bid.getSuit() == 'S'){
							//if my spades are weak
							if(numSuit[3] < 4){
								if(supPoints >= 10){
									return "3N";
								}
								else{
									return "Pass";
								}
							}
							//spades are strong
							else{
								if(cards.getHCPInSuit('S') >= 3){
									return "4S";
								}
								else{
									return "3S";
								}
							}
						}
						//hearts was called 3rd
						else{
							//if my hearts are weak
							if(numSuit[2] < 4){
								if(supPoints >= 10){
									return "3N";
								}
								else{
									return "Pass";
								}
							}
							//hearts are strong
							else{
								if(cards.getHCPInSuit('H') >= 3){
									return "4H";
								}
								else{
									return "3H";
								}
							}
						}
					}
					//a contract should have been agreed upon by now
					return "Pass";
				}
				//4th bid was 3NT
				else if(bid.getSuit() == 'N' && bid.getValue() == 3){
					//if the it is the fifth bid
					if(allyBids.getNumberRealBids() == 4){
						//transfer bid back to the third bid
						bid = bid.getPrevBid();
						//if spades was called
						if(bid.getSuit() == 'S'){
							//if my spades are weak
							if(numSuit[3] < 4){
								return "Pass";
							}
							//spades are strong
							else{
								return "4S";
							}
						}
						//hearts was called 3rd
						else{
							//if my hearts are weak
							if(numSuit[2] < 4){
								return "Pass";
							}
							//hearts are strong
							else{
								return "4H";
							}
						}
					}
					//a contract should have been agreed upon by now
					return "Pass";
				}
			}
		}
		//2S was bid second, meaning a long minor exists
		else if(bid.getValue() == 2 && bid.getSuit() == 'S'){
			//first bid after 2S
			if(allyBids.getNumberRealBids() == 2){
				//puppet Bid
				return "3C";
			}
			//bid after the 3C puppet bid
			//checks to see if suit was ment to be D
			if(numSuit[1] >= 7){
				return "3D";
			}
			//all other cases are pass
			return "Pass";
		}
		return "Pass";
	}
	
	public static String moreThanTwoBids(Hand cards, BidList allyBids){
		String highBid = allyBids.getLastBid();
		int value = highBid.charAt(0) - '0'; //value of the last called bid
		char suit = highBid.charAt(1); //suit of the last called bid
		int handPoints; //support points
		int suitI; //int version of called suit
		int cardsInSuit; //# of cards in a suit
		int posSuit; //short for possible suit
		int temp; //all temp variable data does not matter for more than 6 lines
		char tempC;
		char tempC2;
		Bid bid; //used whenever we need to follow a bid
		//Suits are in order power
		int[] numSuit = new int[4]; //number of cards in each suit
		int[] suitOrder = new int[4]; //suits ordered from most cards to least cards
		
		//gets number of cards in each suit, and says which suits has the most cards
		numSuit = getNumSuit(cards);
		suitOrder = orderTheSuits(numSuit);
		bid = allyBids.getFirstBidAsBid();
		
		//finds hand points so appropriate bid can be made
		handPoints = cards.getHCP();
		suitI = letToNum(suit);
		
		//calculates hand points
		for(int i = 0; i < numSuit.length; i++){
			if(numSuit[i] <= 2){
				if(numSuit[i] == 0)
					handPoints += 5;
				else if(numSuit[i] == 1){
					//don't add as many points if the cards in question already give points
					temp = cards.getHCPInSuit(numToLet(i));
					//aces still get points because they are high cards
					if(temp == 4)
						temp = 6;
					else if(temp <= 3)
						temp = 3;
					handPoints += temp;
				}
				else{
					//don't add as many points if the cards in question already give points
					temp = cards.getHCPInSuit(numToLet(i));
					//working honors still get points, non-working do not
					if(temp >= 4)
						temp += 1;
					else if(temp <= 1)
						temp = 1;
					handPoints += temp;
				}
			}
		}
		
		//checks if partner responded low
		if(value == 1){
			//if you like the suit your partner called
			if(numSuit[suitI-1] >= 4){
				if(cards.getHCPInSuit(suitI)+numSuit[suitI-1] >= 8){
					return ("3" + suit);
				}
				else{
					return ("2" + suit);
				}
			}
			//checks if you like any suit higher than the one your partner called
			for(temp = suitI; temp < numSuit.length; temp++){
				if(numSuit[temp] >= 4 && cards.getHCPInSuit(temp+1) >= 4){
					return ("1" + numToLet(temp+1));
				}
			}
			//checks for a reverse call to a lower suit
			for(temp = bid.getIntSuit() - 2; temp >= 0; temp --){
				if(numSuit[temp] >= 4 && cards.getHCPInSuit(temp+1) >= 4){
					return ("2" + numToLet(temp+1));
				}
			}
			//checks for a reverse into a higher suit
			for(temp = bid.getIntSuit() - 2; temp < suitI-1 && handPoints >= 17; temp ++){
				if(numSuit[temp] >= 4 && numSuit[temp] + cards.getHCPInSuit(temp+1) >= 9){
					return ("2" + numToLet(temp+1));
				}
			}
			//checks if the original suit you called is really good
			if(numSuit[bid.getIntSuit()-1] >= 5 && cards.getHCPInSuit(bid.getIntSuit()) >= 7){
				return ("2" + bid.getSuit());
			}
			//checks if you need to bid 1N after 3 bids
			if(allyBids.getNumberRealBids() == 3 && suitI != 0 && cards.getHCP() >= 6 && cards.getHCP() <= 9){
				tempC = bid.getNextBid().getSuit();
				if(bid.getSuit() != tempC && bid.getSuit() != suit && tempC != suit){
					//makes sure your ally's calls are bad for you, made into seperate if statments for readability
					if(numSuit[bid.getIntSuit()-1] + cards.getHCPInSuit(bid.getSuit()) < 7){
						if(numSuit[suitI-1] + cards.getHCPInSuit(suit) < 7){
							return "1N";
						}
					}
				}
			}
		}
		//partner liked what was opened
		if(bid.getSuit() == bid.getNextBid().getSuit()){
			if(allyBids.getNumberRealBids() == 2){
				//checks if you can tell your ally about anouther good suit
				for(posSuit = 0; posSuit < suitI-1; posSuit++){
					if(numSuit[posSuit] >= 4 && cards.getHCPInSuit(posSuit+1) >= 4){
						return ((value + 1) + "" + numToLet(posSuit+1));
					}
				}
				//sees if NT needs to be bid
				if(cards.getHCP() >= 16 && numSuit[suitI-1] <= 5){
					return (value + "N");
				}
				//raises the same suit even higher
				if(value == 2 && (numSuit[suitI-1] >= 5 || cards.getHCPInSuit(suitI) >= 5)){
					return ("4" + suit);
				}
				else if(numSuit[suitI-1] >= 4 || (numSuit[suitI-1] >= 3 && cards.getHCPInSuit(suitI) >= 6)){
					return ((value + 1) + "" + suit);
				}
				else{
					return "Pass";
				}
			}
			else if(allyBids.getNumberRealBids() == 3){
				
			}
		}
		//checks for a reverse, requires atleast 3 bids
		if(allyBids.getNumberRealBids() >= 3){
			tempC2 = bid.getNextBid().getNextBid().getSuit();
			tempC = bid.getNextBid().getSuit();
			if(bid.getSuit() != tempC && bid.getSuit() != tempC2 && tempC != tempC2){
				//value must be at 2 on the third bid, and your suit must have a higher value than ally's suit
				temp = letToNum(tempC);
				if(temp == 0)
					temp = 5;
				if(letToNum(tempC2) == 0)
					temp -= 5;
				else
					temp -= letToNum(tempC2);
					
				if(temp > 0 && bid.getNextBid().getNextBid().getValue() == 2){
					return reverse(cards, allyBids, bid.getSuit(), tempC, tempC2);
				}
			}
		}
		
		//checks if bid was forcing, bidding a new suit is forcing for 1 round
		//this should be the last thing checked before passing
		temp = 0;
		while(temp == 0 && bid != null){
			if(bid.getSuit() == suit && bid.getNextBid() != null){
				temp = 1;
			}
			bid = bid.getNextBid();
		}
		//if the bid was found to be forcing, call 1 higher than the current bid
		if(temp > 0){
			if(suitI != 0 && suitI != 4)
				return (value + "" + numToLet(suitI+1));
			else if(suitI == 4)
				return (value + "N");
			else
				return ((value+1) + "D");
		}
		
		return "Pass";
	}
	
	//handles what happens when a reverse occurs
	public static String reverse(Hand cards, BidList allyBids, char suit1, char suit2, char suit3){
		String highBid = allyBids.getLastBid();
		int value = highBid.charAt(0) - '0'; //value of the last called bid
		char suit = highBid.charAt(1); //suit of the last called bid
		int handPoints = cards.getHCP();
		Bid bid;
		int temp;
		//int versions of all the suits called
		int suitI = letToNum(suit);
		int suitI1 = letToNum(suit1);
		int suitI2 = letToNum(suit2);
		int suitI3 = letToNum(suit3);
		//Suits are in order power
		int[] numSuit = new int[4]; //number of cards in each suit
		int[] suitOrder = new int[4]; //suits ordered from most cards to least cards
		
		//gets number of cards in each suit, and says which suits has the most cards
		numSuit = getNumSuit(cards);
		suitOrder = orderTheSuits(numSuit);
		bid = allyBids.getFirstBidAsBid();
		
		//sets bid to the 4th bid, there has to be atleast 3 bids to get into this method,
		//if there is only 3 then bid is null
		bid = bid.getNextBid();
		bid = bid.getNextBid();
		bid = bid.getNextBid();
		
		//first bid after the reverse
		if(allyBids.getNumberRealBids() == 3){
			//game forcing hands
			if(cards.getHCP() >= 8){
				//attempt to show your partner a stoper
				for(temp = suitI3; temp <= 4;temp++){
					//make sure it is a suit that is yet to be called
					if(temp != suitI1 && temp != suitI2 && temp != suitI3){
						//check for a stoper in the uncalled suit
						if(cards.getHCPInSuit(temp) >= 4)
							return ("2" + numToLet(temp));
					}
				}
				//checks if you have 6+ of your original call
				if(suitI3 != 0 && suitI2 > suitI3 && numSuit[suitI2-1] >= 6){
					return ("3" + suit2);
				}
				//ask your ally to bid 3NT
				if(suitI1 != 0 && suitI2 != 0 && suitI3 != 0){
					//temp will be the suit yet to be bid
					temp = 10 - suitI1 - suitI2 - suitI3;
					//checks if temp's suit is a legal bid
					if(temp > suitI3)
						return ("2" + numToLet(temp));
				}
				//all other bids with this strong of a hand need to be 3NT
				return "3N";
			}
			//worse than game forcing hands
			//check for Lebensohl after Opener's Reverse or recall your call, weak hand in both the opponents calls
			if(numSuit[suitI1-1]<=3&&numSuit[suitI3-1]<=3&&cards.getHCPInSuit(suit1)<=3&&cards.getHCPInSuit(suit3) <=3){
				if(numSuit[suitI2-1] >= 5 && numSuit[suitI2-1] + cards.getHCPInSuit(suitI2) >= 10)
					return ("2" + suit2);
				else
					return "2N";
			}
			//this bid was forced
			return "2N";
		}
		
		//if the 4th bid was 2NT
		if(allyBids.getNumberRealBids() >= 4 && bid.getSuit() == 'N' && bid.getValue() == 2){
			//if this is the 5th bid then you must bid your lowest suit you called before
			if(allyBids.getNumberRealBids() == 4){
				if(suitI1 < suitI3)
					return ("3" + suit1);
				else
					return ("3" + suit3);
			}
			//if this is the 6th bid, choose which of the 2 suits are better for you
			else if(allyBids.getNumberRealBids() == 5 && (suitI == suitI1 || suitI == suitI3)){
				//sets suit3 to the higher of the 2 suits so that math can be done easily
				if(suitI1 > suitI3){
					temp = suitI1;
					suitI1 = suitI3;
					suitI3 = temp;
				}
				
				//determines which suit is better
				if(numSuit[suitI1-1] > numSuit[suitI3-1])
					return "Pass";
				else if(numSuit[suitI1-1] < numSuit[suitI3-1])
					return ("3" + numToLet(suitI3));
				else if(cards.getHCPInSuit(suitI1) < cards.getHCPInSuit(suitI3))
					return ("3" + numToLet(suitI3));
				else
					return "Pass";
			}
			return "Pass";
		}
		
		//if a no trump call was requested
		if(allyBids.getNumberRealBids() == 4 && suitI1 != 0 && suitI2 != 0 && suitI3 != 0 && suitI != 0 && value == 2){
			//checks to make sure they are all different using magic math
			if(suitI1 + suitI2 + suitI3 + suit == 10)
				return "3N";
		}
		
		//checks if the 4th bid was showing a stoper
		temp = bid.getIntSuit();
		if(temp >= 1 && temp <= 4 && suitI2 == 0 && temp != suitI1 && temp != suitI3 && bid.getValue() == 2){
			//choose the suit that is easier to win with
			if(allyBids.getNumberRealBids() == 4){
				if(suitI1 >= 3 && suitI3 < 3)
					return ("4" + suit1);
				else if(suitI3 >= 3 && suitI1 < 3)
					return ("4" + suit3);
				else{
					temp = 0;
					if(numSuit[suitI1-1] >= 4)
						temp += numSuit[suitI1-1];
					if(numSuit[suitI3-1] >= 4)
						temp -= numSuit[suitI3-1];
					temp += cards.getHCPInSuit(suitI1);
					temp -= cards.getHCPInSuit(suitI3);
					if(temp < 0){
						if(suitI3 >= 3)
							return ("4" + suit3);
						else
							return ("5" + suit3);
					}
					else{
						if(suitI1 >= 3)
							return ("4" + suit1);
						else
							return ("5" + suit1);
					}
				}
			}
		}
		
		
		return "Pass";
	}
	
	//makes 1 to C, 2 to D, 3 to H and 4 to S, used to convert numbers to suits
	public static char numToLet(int numb){
		if(numb == 0)
			return 'N';
		else if(numb == 1)
			return 'C';
		else if(numb == 2)
			return 'D';
		else if(numb == 3)
			return 'H';
		else
			return 'S';
	}
	
	//makes C to 1, D to 2, H to 3 and S to 4, used to convert numbers to suits
	public static int letToNum(char let){
		if(let == 'N' || let == 'n')
			return 0;
		else if(let == 'C' || let == 'c')
			return 1;
		else if(let == 'D' || let == 'd')
			return 2;
		else if(let == 'H' || let == 'h')
			return 3;
		else
			return 4;
	}
	
	//uses 0 for C, 1 for D, 2 for H, and 3 for S
	public static int[] getNumSuit(Hand cards){
		int[] numSuit = new int[4];
		numSuit[0] = cards.getNumberOfSuit('c');
		numSuit[1] = cards.getNumberOfSuit('d');
		numSuit[2] = cards.getNumberOfSuit('h');
		numSuit[3] = cards.getNumberOfSuit('s');
		return numSuit;
	}
	
	//uses 0 for C, 1 for D, 2 for H, and 3 for S
	public static int[] orderTheSuits(int[] numSuit){
		int count,count2,temp;
		int[] suitOrder = new int[4];
		//finds the order of the suits from most cards to least cards
		//higher valued suits get placed earlier in the case of ties
		for(count = 0; count < suitOrder.length; count++){
			temp = count;
			for(count2 = count;count2 > 0; count2--){
				if(numSuit[count] >= numSuit[suitOrder[count2-1]]){
					suitOrder[count2] = suitOrder[count2-1];
					temp = count2-1;
				} 
			}
			suitOrder[temp] = count;
		}
		
		return suitOrder;
	}
}
