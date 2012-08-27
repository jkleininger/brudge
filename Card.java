import java.awt.image.BufferedImage;
import java.awt.Rectangle;

public class Card implements Comparable<Card> {

    int suit, rank;
    int owner=-1;

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

    BufferedImage getImage()             { return img;                }
    Rectangle     getRect()              { return rect;               }
    int           getX()                 { return (int)rect.getX();   }
    int           getY()                 { return (int)rect.getY();   }
    int           getZ()                 { return zPos;               }
    void          setZ(int z)            { zPos = z;                  }
    void          decZ()                 { zPos--;                    }
    int           getOwner()             { return owner;              }
    void          setOwner(int o)        { owner = o;                 }
    int           getRank()              { return rank;               }
    char          getCRank()             { return ranks[rank];        }
    int           getIntSuit()           { return suit;               }
    char          getSuit()              { return suits[suit];        }
    boolean       isFaceUp()             { return faceup;             }
    void          flip()                 { faceup = !faceup;          }
    void          moveTo(int x, int y)   { rect.setLocation(x,y);     }
    boolean       contains(int x, int y) { return rect.contains(x,y); }

    void printCard()  { System.out.println(ranks[rank] + "" + suits[suit] + "(" + zPos + ")"); }

    @Override
    public int compareTo(Card c) { return(this.zPos - c.getZ()); }

}
