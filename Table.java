/***********************************************************************
*
*    Table.java
*    Jason K Leininger
*    2012-05-24
*
*    This class invokes the players, the deck, and game properties.
*
*    S=0  W=1  N=2  E=3
*
***********************************************************************/
import java.util.ArrayList;

public class Table {
  AIPlayer[]      player      = new AIPlayer[4];
  Deck            theDeck     = new Deck(true);
  char            suit        = 'N';
  char            trump       = 'D';
  int             dealer      = -1;
  int             lead        = 0;
  int             trickWinner = -1;
  int[]           score       = {0,0,0,0};
  char[]          swne        = {'S','W','N','E'};
  int             goal        = 0;
  int[]           partner     = {2,3,0,1};
  int             declarer    = -1;
  Bid             contract;
  int             bidWinner   = 2;

  Trick[]         tricks      = new Trick[13];
  Score           theScore;

  public Table() {
    for(int p=0;p<4;p++) { player[p]=new AIPlayer(); }
    dealer=(int)Math.random()*4;  //simulate dealer choice
    deal();

    contract = doBidding().getActualLastBid();

    goal     = contract.getValue();
    trump    = contract.getSuit();
    declarer = bidWinner; // NOT ACCURATE, BidList must contain this property
    lead     = declarer+1;
    System.out.println("D:" + declarer + " G:" + goal + " T:" + trump);
    mainPlay();
  }

  private BidList doBidding() {
    BidList b = new BidList();
    b.addBid(2,'D');
    b.addBid(3,'H');
    return b;
  }

  public void deal() {
    for(int r=0;r<13;r++) {
      for(int p=dealer+1;p<=dealer+4;p++) {
        player[p<4?p:p-4].dealMeOne(theDeck.dealNext());
      }
    }
  }

  public void mainPlay() {
    Card[] thisTrick = new Card[4];
    for(int r=0;r<13;r++) {
      suit='N';
      while(lead>3) { lead-=4; }
      for(int t=lead;t<lead+4;t++) {
        int p=(t<4?t:t-4);
        thisTrick[p]=player[p].selectAndPlay(theDeck.getPlayed(),thisTrick,trump,suit);
        if(t==lead) { suit=thisTrick[t].getSuit(); }
      }
      tricks[r] = new Trick(thisTrick,lead,suit,trump);
      lead=tricks[r].getWinner();
    }
    theScore = new Score(tricks,contract,declarer);
  }

}
