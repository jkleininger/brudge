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
  ArrayList<Card> hand;

  public AIPlayer() {
    hand = new ArrayList<Card>(0);
  }

  public void dealMeOne(Card c) {
    hand.add(c);
  }    

  protected void showHand() {
    for(int c=0;c<hand.size();c++) {
      hand.get(c).printCard(); System.out.print(" ");
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

  public Card selectAndPlay(ArrayList<Card> played,Card[] trick,char trump,char suit) {
    choices = new ArrayList<Card>(hand);
    if(suit=='N') { pickSuit(); }  // player is lead
    if(hasSuit(suit)) { leaveHighest(suit); }
    else if(hasSuit(trump)) { leaveLowest(trump); }
    hand.remove(choices.get(0));
    return(choices.get(0));
  }

  private char pickSuit() {
    char[] suits = {'C','H','D','S'};
    return suits[(int)Math.random()*4];
  }

  private boolean hasSuit(char suit) {
    if(suit=='N') { return true; }
    //System.out.print("Checking hand for " + suit + "... ");
    for(int c=0;c<choices.size();c++) {
      //System.out.print(choices.get(c).getSuit());
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
      return c1.getValue()-c2.getValue();
    }
  };
  Comparator<Card> byValRev = new Comparator<Card>() {
    public int compare(Card c1, Card c2) {
      return c2.getValue()-c1.getValue();
    }
  };

}
