/*

AIPlayer.java
Jason K Leininger

Individual AI player strategy based on hand, dummy hand, cards already played, current trick, trump, and suit

*/

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Comparator;
import java.util.Collections;

public class AIPlayer {
  ArrayList<Card> choices;
  Hand            myHand;
  int             points = 0;
  boolean         vulnerable = false;

  public AIPlayer() {
    myHand = new Hand();
  }

  public void dealMeOne(Card c, int owner) {
    c.setOwner(owner);
    myHand.addCard(c);
  }    

  protected void showHand() {
    for(int c=0;c<myHand.getNumberOfCards();c++) {
      myHand.getCard(c).printCard(); System.out.print(" ");
    }
    System.out.println();
  }

  protected void showChoices() {
    System.out.print("c> ");
    for(int c=0;c<choices.size();c++) {
      choices.get(c).printCard(); System.out.print(" ");
    }
    System.out.println();
  }

  // systematically eliminates cards from hand.  returns whatever remains.
  public Card selectAndPlay(ArrayList<Card> played,Card[] trick,char trump,char suit) {
    choices = new ArrayList<Card>(myHand.getHand());
    if(suit=='N') { pickSuit(); }  // player is lead
    if(hasSuit(suit)) { leaveHighest(suit); }
    else if(hasSuit(trump)) { leaveLowest(trump); }
    myHand.removeCard(choices.get(0));
    return(choices.get(0));
  }

  private char pickSuit() {
    char[] suits = {'C','H','D','S'};
    return suits[(int)Math.random()*4];
  }

  private boolean hasSuit(char suit) {
    if(suit=='N') { return true; }
    for(int c=0;c<choices.size();c++) {
      if(choices.get(c).getSuit()==suit) { return true; }
    }
    return false;
  }

  private void leaveHighest(char suit) {
    if(suit!='N') { removeNonSuit(suit); }
    Collections.sort(choices,byValRev);
    choices.removeAll(choices.subList(1,choices.size()));
  }

  private void leaveLowest(char suit) {
    if(suit!='N') { removeNonSuit(suit); }
    Collections.sort(choices,byVal);
    choices.removeAll(choices.subList(1,choices.size()));
  }

  private void removeNonSuit(char suit) {
    Iterator<Card> i = choices.listIterator();
    while(i.hasNext()) {
      Card c = i.next();
      if(c.getSuit()!=suit) { i.remove(); }
    }
  }

  Comparator<Card> byVal = new Comparator<Card>() {
    public int compare(Card c1, Card c2) {
      return c1.getRank()-c2.getRank();
    }
  };
  Comparator<Card> byValRev = new Comparator<Card>() {
    public int compare(Card c1, Card c2) {
      return c2.getRank()-c1.getRank();
    }
  };

  int     getPoints()       { return(points);         }
  void    setPoints(int p)  { this.points = p;        }
  boolean isVulnerable()    { return this.vulnerable; }
  Hand    getHand()         { return myHand;          }

}
