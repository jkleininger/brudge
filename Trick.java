public class Trick {
  Card[] cards = new Card[4];
  int    lead;
  char   suit;
  char   trump;
  int    winner;

  public Trick(Card[] thisTrick, int thisLead, char thisSuit, char thisTrump) {
    cards[0] = thisTrick[0];
    cards[1] = thisTrick[1];
    cards[2] = thisTrick[2];
    cards[3] = thisTrick[3];
    lead     = thisLead;
    suit     = thisSuit;
    trump    = thisTrump;
    winner   = this.getWinner();
    this.printTrick();
  }
  
  private void printTrick() {
    for(int i=0;i<4;i++) {
      System.out.format("%s%s ", cards[i].getCValue(), cards[i].getSuit());
    }
    System.out.print(" <L:" + lead + " S:" + suit + " W:" + winner + ">");
    System.out.println();
  }

  public int getWinner() {
    char suit = cards[lead].getSuit();
    int theWinner = -1;
    int highValue = 0;
    for(int t=0;t<4;t++) {
      int av = getAdjustedValue(cards[t],trump,suit);
      if(av>highValue) { theWinner=t; highValue=av; }
    }
    return theWinner;
  }

  private int getAdjustedValue(Card c, char trump, char suit) {
    if(c.getSuit()==trump) return c.getValue()+39;
    if(c.getSuit()==suit)  return c.getValue()+26;
    return c.getValue();
  }

}
