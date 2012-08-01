/* * * * *
*
* Duplicate scoring overview:
*
* A successful contract yields a score summing the folliwing:
*   - The contract
*   - Overtricks
*   - Part-score / game-score bonus
*   - Slam / grand slam bonus
* The defending side receives a negative score of the same absolute value.
*
* A defeated contract yields a positive score for the defenders:
*   - Number of tricks defeated
* Declaring side receives a negative score of the same absolute value
*
* * * * */


public class Score {

  int NS = 0;
  int EW = 1;

  int[] pContract   = {0,0,0,0};
  int[] pOvertricks = {0,0,0,0};
  int[] pBonus      = {0,0,0,0};
  int[] pSlam       = {0,0,0,0};
  int[] tricksWon   = {0,0,0,0};
  int[] scores      = {0,0,0,0};

  boolean vulnerable = false;

  public Score(Trick[] tricks, Bid contract, int declarer) {

    // tally tricks won by each player
    int numOfTricks = tricks.length;
    for(int n=0;n<numOfTricks;n++) {
      int thisWinner = tricks[n].getWinner();
      tricksWon[thisWinner]++;
    }

    // calculate contract points for each player
    int cpPerTrick  = contract.isMinor()?20:30;
    for(int n=0;n<4;n++) {
      if(tricksWon[n]>6) {
        pContract[n]=(tricksWon[n]-6)*cpPerTrick*contract.getDoubled();
        if(contract.getSuit()=='N') { pContract[n]+=(contract.getDoubled()*10); }
      }
    }

    //calculate overtrick bonus
    int pOBonus = 0;
    if(tricksWon[declarer]>(contract.getValue()+6)) {
      switch(contract.getDoubled()) {
        case 1:
          pOBonus=contract.isMinor()?20:30;
          break;
        case 2:
          pOBonus=vulnerable?200:100;
          break;
        case 4:
          pOBonus=vulnerable?400:200;
          break;
        default:
          break;
      }
      pOvertricks[declarer]=(tricksWon[declarer]-contract.getValue()-6)*pOBonus;
    }

    printScores();

  }

  private void printScores() {

    System.out.print("TR: ");
    for(int n=0;n<4;n++) {
      System.out.print(tricksWon[n] + " ");
    }
    System.out.println();


    System.out.print("CP: ");
    for(int n=0;n<4;n++) {
      System.out.print(pContract[n] + " ");
    }
    System.out.println();

    System.out.print("OT: ");
    for(int n=0;n<4;n++) {
      System.out.print(pOvertricks[n] + " ");
    }
    System.out.println();

  }


}
