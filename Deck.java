import java.util.ArrayList;
import java.util.Collections;
import java.awt.image.*;
import javax.imageio.ImageIO;
import java.io.*;

public class Deck {
  char[] suit = {'C','D','H','S'};
  ArrayList<Card> card   = new ArrayList<Card>(0);
  ArrayList<Card> played = new ArrayList<Card>(0);
  BufferedImage       cImg;

  public Deck(boolean shuf) {
    try { cImg = ImageIO.read(new File("assets/cards.png")); } catch (IOException e) { System.exit(1); }
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

  public BufferedImage getCardImage(int v, char s) {
    s = Character.toUpperCase(s);
    int vOff = v * 58;
    return cImg.getSubimage(vOff,0,54,80);
  }

}
