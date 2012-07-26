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
  int pConract = 0;
  int pOvertricks = 0;
  int pBonus = 0;
  int pSlam = 0;
  
  public Score(Trick[] tricks) {
    System.out.println("temporary");
  }

}
