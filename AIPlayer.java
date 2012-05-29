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

  public void showHand() {
    for(int c=0;c<hand.size();c++) {
      hand.get(c).printCard(); System.out.print(" ");
    }
    System.out.println();
  }

  public void showChoices() {
    System.out.print("c> ");
    for(int c=0;c<choices.size();c++) {
      choices.get(c).printCard(); System.out.print(" ");
    }
    System.out.println();
  }

  Card selectAndPlay(ArrayList<Card> played,Card[] trick,char trump,char suit) {
    choices = new ArrayList<Card>(hand);
    //if(hasSuit(suit)) { removeNonSuit(suit); }
    if(hasSuit(suit)) { leaveHighest(suit); }
    hand.remove(choices.get(0));
    return(choices.get(0));
  }

  public boolean hasSuit(char suit) {
    for(int c=0;c<choices.size();c++) {
      if(choices.get(c).getSuit()==suit) { return true; }
    }
    return false;
  }

  public void leaveHighest(char suit) {
    removeNonSuit(suit);
    Collections.sort(choices,byValRev);
    Card h = choices.get(0);
    for(int n=1;n<choices.size();n++) {
      if(choices.get(n).getValue()>h.getValue()) { 
        h=choices.get(n);
      }
    }
  }

  public void removeNonSuit(char suit) {
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
