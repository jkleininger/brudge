import java.util.ArrayList;

public class AIPlayer {
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

  Card selectAndPlay(ArrayList<Card> played,Card[] trick,char trump,char suit) {
    int retCard = -1;
    retCard = canFollowSuit(suit);
    if(retCard<0) { retCard=0; }
    return(hand.remove(retCard));
  }

  public int canFollowSuit(char suit) {
    for(int c=0;c<hand.size();c++) {
      if(hand.get(c).getSuit()==suit) { return c; }
    }
    return -1;
  }

}
