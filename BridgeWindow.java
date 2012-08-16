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

  }

  public void paint(Graphics g) {
    super.paint(g);
    for(int d=0;d<10;d++) {
      g.drawImage(tmpDeck.getCardImage(d,'c'), d*30, d*50, this);
    }
  }

}
