import javax.swing.ImageIcon;
import java.awt.image.*;

public class Card {

    int  suit,  rank;

    char[] suits = {'N','C','D','H','S'};
    char[] ranks = {'A','2','3','4','5','6','7','8','9','T','J','Q','K','A'};

    int xPos,yPos,zPos;

    boolean faceup;

    BufferedImage img;

    public Card(int r, int s, BufferedImage cardImage) {
      suit   = s;
      rank   = r==0?14:r;
      faceup = false;
      img    = cardImage;
    }

    public BufferedImage getImage() { return this.img; }

    void printCard()  { System.out.print(ranks[rank] + "" + suits[suit]); }

    int  getRank()    { return rank;         }
    char getCRank()   { return ranks[rank];  }
    int  getIntSuit() { return suit;         }
    char getSuit()    { return suits[suit];  }

    void flip()       { faceup = !faceup;    }

}
