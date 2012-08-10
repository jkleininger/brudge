public class Bid{

	int value;
	int suit;
	char cSuit;
	Bid nextBid;
	Bid prevBid;
    int doubled = 1;   //1, 2, 4


	public Bid(int tvalue, char tsuit, Bid pBid){
        Bid bid;

        if(tvalue<1||tvalue>7) { System.out.println("Invalid Bid Value " + tvalue); return; }

        value = tvalue;
        tsuit = Character.toUpperCase(tsuit);
        cSuit = tsuit;

        switch(tsuit) {
            case 'C': suit = 1; break;
            case 'D': suit = 2; break;
            case 'H': suit = 3; break;
            case 'S': suit = 4; break;
            case 'N': suit = 5; break;
            case 'P': suit = 0; value = 1; break;
            case 'X':
              bid = prevBid;
              if(bid == null) { return; }
              suit = bid.getIntSuit();
              while(suit == 0) {
                bid = bid.getPrevBid();
                if(bid == null) { return; }
                 suit = bid.getIntSuit();
              }
              cSuit = bid.getSuit();
              value = bid.getValue();
              doubled*=2;
              break;
            default:
                suit  =  0;
                cSuit = 'O';
                value =  0;
                System.out.println("Invalid suit specified.");
                break;
        }

 		prevBid = pBid;
 		nextBid = null;
	}

  //this method can be used for pass or double
  public Bid(char tsuit, Bid pBid) {
    Bid bid;

    tsuit = Character.toUpperCase(tsuit);

    if(tsuit == 'P') {
      suit  = 0;
      cSuit = 'P';
      value = 1;
    }
    else if(tsuit == 'D'){
      //set this bid to the last non passing bid
      bid = prevBid;
      if(bid == null) { return; }
      suit = bid.getIntSuit();
      while(suit == 0){
        bid = bid.getPrevBid();
        if(bid == null){ return; }
        suit = bid.getIntSuit();
      }
      cSuit = bid.getSuit();
      value = bid.getValue();
    } else {
      suit = 0;
      cSuit = 'O';
      value = 0;
      return;
    }
 
    prevBid = pBid;
    nextBid = null;
  }
	
	
    void printBid() {
      if(suit == 0) { System.out.print("P"); }
      else { System.out.print(value + "" + cSuit); }
    }

    void setNextBid(Bid tBid){ nextBid = tBid; }
    void setPrevBid(Bid tBid){ prevBid = tBid; }
    void incDoubled()        { if(this.doubled<4) { this.doubled+=2; } }

    Bid  getNextBid() { return nextBid; }
    Bid  getPrevBid() { return prevBid; }
    int  getIntSuit() { return suit;    }
    char getSuit()    { return cSuit;   }
    int  getValue()   { return value;   }
    int  getDoubled() { return doubled; }

    boolean isMajor() { return (suit=='H'||suit=='S'); }
    boolean isMinor() { return (suit=='D'||suit=='C'); }

}
