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
      for(int r=1;r<=13;r++) {
        card.add(new Card(r,s,-1,extractCardImage(r,s)));
      }
    }
    if(shuf) { this.shuffle(); }
    int n=0;
    for(Card theCard : card) { theCard.setZ(n++); }
  }

  public ArrayList<Card> getDeck()   { return card;   }
  public ArrayList<Card> getPlayed() { return played; }

  public void shuffle() {
    Collections.shuffle(card);
  }

  public void sortByZ() {
    Collections.sort(card);
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

  public BufferedImage extractCardImage(int r, int s) {
    int rOff = ((r-1) * 73)+1;
    int sOff = ((s-1) * 98)+1;
    return cImg.getSubimage(rOff,sOff,71,96);
  }
  
  public Card getClicked(int x, int y) {
    int z=-1;
    for(Card theCard : card) {
	  if(theCard.contains(x,y)) { z=theCard.getZ(); }
	}
    if(z>=0) { return card.get(z); } else { return null; }
  }

  public void bringToFront(Card theCard) {
    int oldZ = theCard.getZ();
    for(Card c : card) {
	  if(c.getZ()>oldZ) { c.decZ(); }
	}
    theCard.setZ(card.size()-1);
    sortByZ();
  }

}
