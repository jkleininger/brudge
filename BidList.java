public class BidList{

	int numBids;
	Bid startBid;
	Bid lastBid;
	//these never should contain pass
	int numRealBids;
	Bid firstRealBid;
	Bid lastRealBid;

	public BidList(){
		numBids = 0;
		numRealBids = 0;
		lastBid = null;
		startBid = null;
		firstRealBid = null;
		lastRealBid = null;
	}
	
	void addBid(int value, char suit){
		Bid tempBid = new Bid(value, suit, lastBid);
		
		if(numBids == 0){
			startBid = tempBid;
			lastBid = startBid;
			firstRealBid = startBid;
			lastRealBid = startBid;
			numBids ++;
			numRealBids ++;
		}
		else{
			if(numRealBids == 0){
				firstRealBid = tempBid;
			}
			lastBid.setNextBid(tempBid);
			lastBid = lastBid.getNextBid();
			lastRealBid = lastBid;
			numBids ++;
			numRealBids ++;
		}
	}
	
	//used in case of pass or double
	void addBid(char pass){
		Bid tempBid;
		if(pass != 'p' && pass != 'P' && pass != 'd' && pass != 'D'){
			System.out.println("Not a valid Bid");
			return;
		}
		//1 put in following line to prevent error
		if(pass == 'p' || pass == 'P')
			tempBid = new Bid(1,'P', lastBid);
		else
			tempBid = new Bid(1,'D', lastBid);
	
		if(numBids == 0){
			startBid = tempBid;
			lastBid = startBid;
			numBids ++;
		}
		else{
			lastBid.setNextBid(tempBid);
			lastBid = lastBid.getNextBid();
			numBids ++;
			
			if(pass == 'd' || pass == 'D'){
				lastRealBid = lastBid;
				numRealBids ++;
			}
		}
	}

	String getFirstBid(){
		if(firstRealBid == null)
			return null;
		String output = "";
		output += firstRealBid.getValue();
		output += firstRealBid.getSuit();
		return output;		
	}
	
	String getLastBid(){
		if(lastRealBid == null)
			return null;
		String output = "";
		output += lastRealBid.getValue();
		output += lastRealBid.getSuit();
		return output;
	}
	
	Bid getFirstBidAsBid(){
		return firstRealBid;
	}
	
	Bid getActualLastBid(){
		return lastBid;
	}
	
	int getNumberBids(){
		return numBids;
	}
	
	//removes passes from bid count
	int getNumberRealBids(){
		return numRealBids;
	}
	
	//makes a BidList of only you and your partner's Bids
	//if there is an odd number of bids in the return value that means your
	//partner bid before you did
	BidList getAllyBids(){
		BidList allyBids = new BidList();
		BidList allyBidsB = new BidList();
		Bid curBid = lastBid;
		//gets all the bids, but due to the way they are gathered they are backwards
		for(int count = 0; count < numBids; count++){
			if(count % 2 == 1){
				allyBidsB.addBid(curBid.getValue(), curBid.getSuit());
			}
			curBid = curBid.getPrevBid();
		}
		//makes bids forwards
		curBid = allyBidsB.getActualLastBid();
		for(int count = 0; count < allyBidsB.getNumberBids(); count ++){
			allyBids.addBid(curBid.getValue(), curBid.getSuit());
			curBid = curBid.getPrevBid();
		}
		
		return allyBids;
	}
}