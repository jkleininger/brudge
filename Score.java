/*
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
 */


public class Score {
  int   pContract   = 0;
  int   pOvertricks = 0;
  int   pBonus      = 0;
  int   pSlam       = 0;
  int[] scores    = {0,0,0,0};

  public Score(Trick[] tricks, Bid contract, int declarer) {
    int cpPerTrick  = 0;
    int numOfTricks = tricks.length;

    // tally tricks won by each player
    for(int n=0;n<numOfTricks;n++) {
      int thisWinner = tricks[n].getWinner();
      scores[thisWinner]++;
    }

    // calculate contract points for each player
    if(contract.isMinor()) { cpPerTrick=20; } else { cpPerTrick=30; }
    for(int n=0;n<4;n++) {
      if(scores[n]>6) {
        scores[n]=(scores[n]-6)*cpPerTrick*contract.getDoubled();
        if(contract.getSuit()=='N') { scores[n]+=(contract.getDoubled()*10); }
      }
    }

  

  }

}
