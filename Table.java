public class Table {
  AIPlayer playerSouth = new AIPlayer();
  AIPlayer playerWest  = new AIPlayer();
  AIPlayer playerNorth = new AIPlayer();
  AIPlayer playerEast  = new AIPlayer();
  Deck     theDeck     = new Deck(true);
  Card[]   trick       = new Card[4];
  char    suit        = 'H';
  char    trump       = 'N';

  public Table() {
    this.deal(0);
    playerSouth.showHand();
    playerWest.showHand();
    playerNorth.showHand();
    playerEast.showHand();
    mainPlay();
  }

  public void deal(int dealer) {
    for(int r=0;r<13;r++) {
      playerSouth.dealMeOne(theDeck.dealNext());
      playerWest.dealMeOne(theDeck.dealNext());
      playerNorth.dealMeOne(theDeck.dealNext());
      playerEast.dealMeOne(theDeck.dealNext());
    }
  }

  public void mainPlay() {
    for(int r=0;r<13;r++) {
      trick[0] = playerSouth.selectAndPlay(theDeck.getPlayed(),trick,trump,suit);
      trick[1] = playerWest.selectAndPlay(theDeck.getPlayed(), trick,trump,suit);
      trick[2] = playerNorth.selectAndPlay(theDeck.getPlayed(),trick,trump,suit);
      trick[3] = playerEast.selectAndPlay(theDeck.getPlayed(), trick,trump,suit);
      for(int z=0;z<4;z++) { trick[z].printCard(); System.out.print(" "); }
      System.out.println();
    }
  }

}
