public class Bid{

	int value;
	int suit;
	char cSuit;
	Bid nextBid;
	Bid prevBid;


	public Bid(int tvalue, char tsuit, Bid pBid){
		Bid bid;

    if(tvalue<1||tvalue>7) { 
      System.out.println("Invalid Bid Value " + tvalue);
      return;
    }

    value = tvalue;
    tsuit = Character.toUpperCase(tsuit);
    cSuit = tsuit;
		
		if(tsuit == 'C'){
			suit = 1;
		}
		else if(tsuit == 'D'){
			suit = 2;
		}
		else if(tsuit == 'H'){
			suit = 3;
		}
		else if(tsuit == 'S'){
			suit = 4;
		}
		else if(tsuit == 'N'){
			suit = 5;
		}
		else if(tsuit == 'P'){
			suit = 0;
			value = 1;
		}
		else if(tsuit == 'D'){
			//set this bid to the last non passing bid
			bid = prevBid;
			if(bid == null){
				System.out.println("not a valid time to bid double");
				return;
			}
			suit = bid.getIntSuit();
			while(suit == 0){
				bid = bid.getPrevBid();
				if(bid == null){
					System.out.println("not a valid time to bid double");
					return;
				}
				suit = bid.getIntSuit();
			}
			cSuit = bid.getSuit();
			value = bid.getValue();
		}
		else{
			suit = 0;
			cSuit = 'O';
			value = 0;
			System.out.println("not a valid suit");
			return;
		}
 
 		prevBid = pBid;
 		nextBid = null;
	}
	
	//this method can be used for pass or double
	public Bid(char tsuit, Bid pBid){
		Bid bid;	
				
		if(tsuit == 'P' || tsuit == 'p'){
			suit = 0;
			cSuit = 'P';
			value = 1;
		}
		else if(tsuit == 'D' || tsuit == 'd'){
			//set this bid to the last non passing bid
			bid = prevBid;
			if(bid == null){
				System.out.println("not a valid time to bid double");
				return;
			}
			suit = bid.getIntSuit();
			while(suit == 0){
				bid = bid.getPrevBid();
				if(bid == null){
					System.out.println("not a valid time to bid double");
					return;
				}
				suit = bid.getIntSuit();
			}
			cSuit = bid.getSuit();
			value = bid.getValue();
		}
		else{
			suit = 0;
			cSuit = 'O';
			value = 0;
			System.out.println("not a valid suit");
			return;
		}
 
 		prevBid = pBid;
 		nextBid = null;
	}
	
	void setNextBid(Bid tBid){
		nextBid = tBid;
	}
	
	void setPrevBid(Bid tBid){
		prevBid = tBid;
	}
	
	void printBid(){
		if(suit == 0)
			System.out.print("P");
		else
			System.out.print(value + "" + cSuit);
	}
	
	Bid getNextBid(){
		return nextBid;
	}
	
	Bid getPrevBid(){
		return prevBid;
	}
	
	int getIntSuit(){
		return suit;
	}
	
	char getSuit(){
		return cSuit;
	}
	
	int getValue(){
		return value;
	}
}
