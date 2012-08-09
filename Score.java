/* * * * *
*
* Duplicate scoring overview:
*
* A successful contract yields a score summing the following:
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

  int declarers, defenders;

  int[] partner     = {2,3,0,1};

  int[] pContract   = {0,0};
  int[] pOvertricks = {0,0};
  int[] pLevelBonus = {0,0};
  int[] pBonus      = {0,0,0,0};
  int[] pSlam       = {0,0,0,0};
  int[] tricksWon   = {0,0};
  int[] scores      = {0,0,0,0};

  boolean vulnerable = false;

  public Score(Trick[] tricks, Bid contract, int declarer) {

    declarers = declarer<2?declarer:declarer-2;
    defenders = declarer==0?1:0;

    // tally tricks won by each player
    int numOfTricks = tricks.length;
    for(int n=0;n<numOfTricks;n++) {
      int thisWinner = tricks[n].getWinner();
      thisWinner = thisWinner<2?thisWinner:thisWinner-2;
      tricksWon[thisWinner]++;
    }
 
    // calculate contract points for each player
    int cpPerTrick  = contract.isMinor()?20:30;
    if(tricksWon[declarers]>6) {
      pContract[declarers]=(tricksWon[declarers]-6)*contract.getDoubled();
      if(contract.getSuit()=='N') { pContract[declarers]+=(contract.getDoubled()*10); }
    }

    //calculate overtrick bonus
    int pOBonus = 0;
    if((tricksWon[declarers])>(contract.getValue()+6)) {
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
      pOvertricks[declarers]=((tricksWon[declarers])-contract.getValue()-6)*pOBonus;
    }

    //calculate level bonus
    pLevelBonus[declarers]=0;
    if(pContract[declarers]<100) { pLevelBonus[declarers]=50;  } else {
      switch(contract.getValue()) {
        case 6: pLevelBonus[declarers]=vulnerable?750:500;   break;
        case 7: pLevelBonus[declarers]=vulnerable?1500:1000; break;
        default: break;
      }
      if(pContract[declarers]>=100) { pLevelBonus[declarers]+=500; }
    }

    printScores();

  }

  private void printScores() {
    System.out.println("TR: " + tricksWon[0]   + " " + tricksWon[1]);
    System.out.println("CP: " + pContract[0]   + " " + pContract[1]);
    System.out.println("OT: " + pOvertricks[0] + " " + pOvertricks[1]);
    System.out.println("LV: " + pLevelBonus[0] + " " + pLevelBonus[1]);
  }


}
