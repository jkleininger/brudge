/*
    S = 0
    W = 1
    N = 2
    E = 3

*/
import java.util.ArrayList;

public class Table {
  AIPlayer[] player = new AIPlayer[4];
  Deck       theDeck = new Deck(true);
  ArrayList<Card> trick = new ArrayList<Card>(4);
  char       suit    = 'C';
  char       trump   = 'D';

  public Table() {
    for(int p=0;p<4;p++) { player[p]=new AIPlayer(); }
    this.deal(0);
    mainPlay();
  }

  public void deal(int dealer) {
    for(int r=0;r<13;r++) {
      for(int p=dealer+1;p<dealer+5;p++) {
        player[p<4?p:p-4].dealMeOne(theDeck.dealNext());
      }
    }
  }

  public void mainPlay() {
    for(int r=0;r<13;r++) {
      for(int t=0;t<4;t++) {
        trick.add(t,player[t].selectAndPlay(theDeck.getPlayed(),trick,trump,suit));
      }
      for(int z=0;z<4;z++) { trick.get(z).printCard(); System.out.print("(" + getAdjustedValue(trick.get(z),trump,suit) + ") "); }
      System.out.print(" <" + getWinner(0,trump) + ">");
      System.out.println();
    }
  }

  private int getWinner(int leader, char trump) {
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
