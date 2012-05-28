public class Table {
  AIPlayer[] player = new AIPlayer[4];
  Deck       theDeck = new Deck(true);
  Card[]     trick   = new Card[4];
  char       suit    = 'H';
  char       trump   = 'N';

  public Table() {
    for(int p=0;p<4;p++) { player[p]=new AIPlayer(); }
    this.deal(0);
    mainPlay();
  }

  public void deal(int dealer) {
    for(int r=0;r<13;r++) {
      for(int p=dealer+1;p<dealer+5;p++) {
        player[p<4?p:p-4].dealMeOne(theDeck.dealNext());
      }
    }
  }

  public void mainPlay() {
    for(int r=0;r<13;r++) {
      for(int t=0;t<4;t++) {
        trick[t] = player[t].selectAndPlay(theDeck.getPlayed(),trick,trump,suit);
      }
      for(int z=0;z<4;z++) { trick[z].printCard(); System.out.print(" "); }
      System.out.println();
    }
  }

}
