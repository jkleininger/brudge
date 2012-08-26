import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

public class BridgeGUI extends JFrame {

  //char[] zoneChars = {'S','W','N','E'};

   Deck tmpDeck;

  Rectangle[] zone = new Rectangle[] {
    new Rectangle(50,450,700,100),
    new Rectangle(50,240,80,190),
    new Rectangle(50,120,700,100),
    new Rectangle(670,240,80,190),
  };

  public static void main(String[] args) {
    BridgeGUI theWindow = new BridgeGUI();
    theWindow.setVisible(true);
  }

  public BridgeGUI() {
    setTitle("Bridge");
    setSize(800,600);
	
    tmpDeck = new Deck(true);

    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) { System.exit(0); }
    });

    addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) { processClick(e.getX(),e.getY()); }
    });

  }

  public void paint(Graphics g) {
    super.paint(g);

    for(int z=0;z<4;z++) {
      floodRect(zone[z],g);
    }
    
    for(Card thisCard : tmpDeck.getDeck()) {
      g.drawImage(thisCard.getImage(),thisCard.getX(),thisCard.getY(),this);
    }

  }

  private void floodRect(Rectangle r, Graphics g) {
    g.fillRect((int)r.getX(),(int)r.getY(),(int)r.getWidth(),(int)r.getHeight());
  }
    
	public void processClick(int x, int y) {
    Card myCard = tmpDeck.getClicked(x,y);
    if(myCard!=null) {
      tmpDeck.bringToFront(myCard);
      repaint();
    }
  }

}
