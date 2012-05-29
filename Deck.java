import java.util.ArrayList;
import java.util.Collections;

public class Deck {
  char[] suit = {'C','D','H','S'};
  ArrayList<Card> card   = new ArrayList<Card>(0);
  ArrayList<Card> played = new ArrayList<Card>(0);

  public Deck(boolean shuf) {
    for(int s=0;s<=3;s++) {
      for(int v=1;v<=13;v++) {
        card.add(new Card(v,suit[s]));
      }
    }
    if(shuf) { this.shuffle(); }
  }

  public ArrayList<Card> getPlayed() {
    return played;
  }

  public void shuffle() {
    Collections.shuffle(card);
  }

  public void playCard(Card c) {
    played.add(c);
  }

  public void printDeck() {
    for(int c=0;c<card.size();c++) {
      card.get(c).printCard(); System.out.print(" ");
    }
  }

  public Card dealNext() {
    return card.remove(card.size()-1);
  }

  public Card randomCard() {
    return card.get((int)(Math.random()*card.size()));
  }

}
