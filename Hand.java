public class Hand{

	//values manage the cards
	int numCards;
	Card[] cards;
	//values manage the hand info
	int HCP;
	int[] numSuit;
	int[] HCPSuit;
	//tells you what high cards a suit has by adding adjusted values to a single int
	//J=1,Q=2,K=4,A=8, so a 13 means you have a JKA
	int[] HCSuit;
	//says what the highest card in each suit is
	int[] highestSuit;

	//initialize
	public Hand(){
		cards = new Card[13];
		
		numCards = 0;
		HCP = 0;
		//making num suit count to 4 was just more convenient
		numSuit = new int[5];
		HCPSuit = new int[5];
		HCSuit = new int[5];
		highestSuit = new int[5];
		for(int count = 0; count < numSuit.length; count++){
			numSuit[count] = 0;
			HCPSuit[count] = 0;
			HCSuit[count] = 0;
			highestSuit[count] = 0;
		}
	}
	
	//adds a card to the hand
	void addCard(Card newCard){
		if(numCards < 13){
			cards[numCards] = newCard;
			numCards ++;
			processCard(newCard);
		}
		else
			System.out.println("To many cards");
	}
	
	//HCP,numSuit,HCPSuit,HCSuit
	//Maintains data when card is added
	void processCard(Card newCard){
		int value = newCard.getValue();
		int suit = newCard.getIntSuit();
		int adjValue = 1;
		int goesTo = 0;
		
		numSuit[suit] ++;
		
		if(value > 10){
			HCP += value - 10;
			HCPSuit[suit] += value - 10;
			//J=0,Q=1,K=2,A=3
			goesTo = (value - 11);	
			//raises 2 to the power of goesTo
			for(int count = 0; count<goesTo; count++){
				adjValue *= 2;
			}
			HCSuit[suit] += adjValue;
		}
		
		//updates biggest card in each suit
		if(value > highestSuit[suit])
			highestSuit[suit] = value;
	}
	
	//standard getStatements
	int getNumberOfCards(){
		return numCards;
	}
	
	Card getCard(int card){
		if(card < numCards && card >= 0)
			return cards[card];
		else
			System.out.println("Card does not exist");
		return null;
	}
	
	int getHCP(){
		return HCP;
	}
	
	//the following get statements can take int value or charcter
	int getNumberOfSuit(int suit){
		if(suit <= 4 && suit > 0)
			return numSuit[suit];
		else if(suit == 'c' || suit == 'C')
			return numSuit[1];
		else if(suit == 'd' || suit == 'D')
			return numSuit[2];
		else if(suit == 'h' || suit == 'H')
			return numSuit[3];
		else if(suit == 's' || suit == 'S')
			return numSuit[4];
		else
			System.out.println("Not a valid Suit");
		return -1;
	}
	
	int getHCPInSuit(int suit){
		if(suit <= 4 && suit > 0)
			return HCPSuit[suit];
		else if(suit == 'c' || suit == 'C')
			return HCPSuit[1];
		else if(suit == 'd' || suit == 'D')
			return HCPSuit[2];
		else if(suit == 'h' || suit == 'H')
			return HCPSuit[3];
		else if(suit == 's' || suit == 'S')
			return HCPSuit[4];
		else
			System.out.println("Not a valid Suit");
		return -1;
	}
	
	int getHCsInSuit(int suit){
		if(suit <= 4 && suit > 0)
			return HCSuit[suit];
		else if(suit == 'c' || suit == 'C')
			return HCSuit[1];
		else if(suit == 'd' || suit == 'D')
			return HCSuit[2];
		else if(suit == 'h' || suit == 'H')
			return HCSuit[3];
		else if(suit == 's' || suit == 'S')
			return HCSuit[4];
		else
			System.out.println("Not a valid Suit");
		return -1;
	}
	
	int getHighestInSuit(int suit){
		if(suit <= 4 && suit > 0)
			return highestSuit[suit];
		else if(suit == 'c' || suit == 'C')
			return highestSuit[1];
		else if(suit == 'd' || suit == 'D')
			return highestSuit[2];
		else if(suit == 'h' || suit == 'H')
			return highestSuit[3];
		else if(suit == 's' || suit == 'S')
			return highestSuit[4];
		else
			System.out.println("Not a valid Suit");
		return -1;
	}
	
}