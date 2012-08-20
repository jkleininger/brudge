import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

public class BridgeWindow extends JFrame {

    Deck tmpDeck;

  public BridgeWindow() {
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
    
    for(Card thisCard : tmpDeck.getDeck()) {
      g.drawImage(thisCard.getImage(),thisCard.getRank()*15,thisCard.getRank()*10,this);
    }

  }

  public void processClick(int x, int y) {
    System.out.println("Clicked (" + x + "," + y + ")") ;
  }

}