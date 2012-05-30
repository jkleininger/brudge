/******************************************************************************
*
*    Table.java
*    Jason K Leininger
*    2012-05-24
*
*    This class houses the players, the deck, and game properties.
*
*    S=0  W=1 N=2 E=3
*
******************************************************************************/
import java.util.ArrayList;

public class Table {
  AIPlayer[]      player      = new AIPlayer[4];
  Deck            theDeck     = new Deck(true);
  ArrayList<Card> trick       = new ArrayList<Card>(4);
  char            suit        = 'C';
  char            trump       = 'D';
  Card            dummyCard   = new Card(2,'C');
  int             dealer      = -1;
  int             lead        = 0;
  int             trickWinner = -1;
  int[]           score       = {0,0,0,0};
  char[]          swne        = {'S','W','N','E'};
  int             goal        = 0;
  int             dummyHand   = -1;
  int[]           partner     = {2,3,0,1};
  int             declarer    = -1;
  Bid             winningBid;
  int             bidWinner   = 2;

  public Table() {
    for(int p=0;p<4;p++) { player[p]=new AIPlayer(); }
    dealer=(int)Math.random()*4;  //simulate dealer choice by cutting deck
    deal();

    winningBid = doBidding().getActualLastBid();

    goal=winningBid.getValue();
    trump=winningBid.getSuit();
    declarer=bidWinner; // NOT ACCURATE, BidList must contain this property
    lead=declarer+1;
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
      for(int p=dealer+1;p<dealer+5;p++) {
        player[p<4?p:p-4].dealMeOne(theDeck.dealNext());
      }
    }
  }

  public void mainPlay() {
    System.out.println(" S  W  N  E");
    for(int i=0;i<4;i++) { trick.add(i,dummyCard); }
    for(int r=0;r<13;r++) {
      while(lead>3) { lead-=4; }
      for(int t=lead;t<lead+4;t++) {
        int p=(t<4?t:t-4);
        trick.add(p,player[p].selectAndPlay(theDeck.getPlayed(),trick,trump,suit));
        if(t==lead) { suit=trick.get(t).getSuit(); }
      }
      trickWinner=getTrickWinner(lead,trump);
      score[trickWinner]++;
      printTrick();
      lead=trickWinner;
    }
    printScores();
  }

  protected void printTrick() {
    for(int i=0;i<4;i++) { trick.get(i).printCard(); System.out.print(" "); }
    System.out.print(" <L:" + swne[lead] + " S:" + suit + " W:" + swne[trickWinner] + ">");
    System.out.println();
  }

  protected void printScores() {
    for(int i=0;i<4;i++) { System.out.print(" " + score[i] + " "); }
    System.out.println();
    System.out.println("NS: " + (score[0]+score[2]));
    System.out.println("EW: " + (score[1]+score[3]));
  }

  private int getTrickWinner(int leader, char trump) {
    char suit = trick.get(leader).getSuit();
    int theWinner = -1;
    int highValue = 0;
    for(int t=0;t<4;t++) {
      int av = getAdjustedValue(trick.get(t),trump,suit);
      if(av>highValue) { theWinner=t; highValue=av; }
    }
    return theWinner;
  }

  private int getAdjustedValue(Card c, char trump, char suit) {
    if(c.getSuit()==trump) return c.getValue()+39;
    if(c.getSuit()==suit)  return c.getValue()+13;
    return c.getValue();
  }

}
