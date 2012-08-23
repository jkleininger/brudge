import java.awt.image.BufferedImage;
import java.awt.Rectangle;

public class Card implements Comparable<Card> {

    int  suit,  rank;

    char[] suits = {'N','C','D','H','S'};
    char[] ranks = {'N','A','2','3','4','5','6','7','8','9','T','J','Q','K','A'};

    Rectangle rect;
    int zPos;

    boolean faceup;

    BufferedImage img;

    public Card(int r, int s, int z, BufferedImage cardImage) {
      suit   = s;
      rank   = r==1?14:r;
      faceup = true;
      img    = cardImage;
      rect   = new Rectangle((int)(Math.random()*300),((int)(Math.random()*200))+100,
                             cardImage.getWidth(),
                             cardImage.getHeight());
      zPos   = z;
    }

    public BufferedImage getImage()  { return img;              }
    public Rectangle     getRect()   { return rect;             }
    public int           getX()      { return (int)rect.getX(); }
    public int           getY()      { return (int)rect.getY(); }
    public int           getZ()      { return zPos;             }
    public void          setZ(int z) { zPos = z;                }
    public void          decZ()      { zPos--;                  }

    void printCard()  { System.out.println(ranks[rank] + "" + suits[suit] + "(" + zPos + ")"); }

    int  getRank()    { return rank;         }
    char getCRank()   { return ranks[rank];  }
    int  getIntSuit() { return suit;         }
    char getSuit()    { return suits[suit];  }

    void flip()       { faceup = !faceup;    }

    boolean contains(int x, int y) { return rect.contains(x,y); }

    @Override
    public int compareTo(Card c) { return(this.zPos - c.getZ()); }

}
