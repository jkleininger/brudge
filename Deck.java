public class Deck {
  char[] suit = {'C','D','H','S'};
  Card[] card = new Card[52];

  public Deck() {
    for(int s=1;s<=4;s++) {
      for(int v=1;v<=13;v++) {
        card[v+(13*(s-1))-1]=new Card(v,suit[s-1]);
      }
    }
    this.shuffle();
  }

  public void shuffle() {
    Card tmpCard;
    
    for(int i=0;i<52;i++) {
      int j = (int)(Math.random()*52);
      tmpCard=card[i];
      card[i]=card[j];
      card[j]=tmpCard;
    }
  }

  public void printDeck() {
    for(int c=0;c<card.length;c++) {
      card[c].printCard();
      System.out.print(" ");
    }
  }

}
