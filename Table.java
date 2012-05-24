public class Table {
  AIPlayer[] player = new AIPlayer[4];
  Deck theDeck = new Deck();

  public Table() {
    player[0]=new AIPlayer();
    player[1]=new AIPlayer();
    player[2]=new AIPlayer();
    player[3]=new AIPlayer();
    System.out.println("Created new Table.");
  }

}
