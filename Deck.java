import java.util.ArrayList;
import java.util.Collections;
import java.awt.image.*;
import javax.imageio.ImageIO;
import java.io.*;

public class Deck {
  char[] suit = {'N','C','D','H','S'};
  ArrayList<Card> card   = new ArrayList<Card>(0);
  ArrayList<Card> played = new ArrayList<Card>(0);
  BufferedImage   cImg;

  public Deck(boolean shuf) {
    try { cImg = ImageIO.read(new File("assets/cards.png")); } catch (IOException e) { System.exit(1); }
    for(int s=1;s<=4;s++) {
      for(int v=1;v<=13;v++) {
        card.add(new Card(v,s,extractCardImage(v,s)));
      }
    }
    if(shuf) { this.shuffle(); }
  }

  public ArrayList<Card> getDeck()   { return card;   }
  public ArrayList<Card> getPlayed() { return played; }

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

  public BufferedImage extractCardImage(int v, int s) {
    int vOff = (v-1) * 73;
    int sOff = (s-1) * 98;
    return cImg.getSubimage(vOff,sOff,72,97);
  }

}
