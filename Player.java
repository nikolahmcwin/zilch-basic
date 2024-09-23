import java.util.Random;

/*
 * Basic class to hold a player character
 */
public class Player {

   private final int MAX_NUM_ZILCHES = 3;
   private final int SCORE_PENALTY = 1000;
   private final int MIN_START_SCORE = 1000;
   private final int WINNING_GAME_SCORE = 10000;

   private static int totalPlayers = 0;

   private int playerID;
   private String name;
   private boolean inGame;
   private int score;
   private int numZilches;
   private boolean isWinner;

   // Default constructor, auto generated name
   public Player() {   
      this("Player_", totalPlayers++);
   }

   // Standard constructor, name passed in
   public Player(String playerName, int playerNumber) {   
      name = playerName;
      playerID = playerNumber;
      score = 0;
      numZilches = 0;
      isWinner = false;
   }

   // Return player name
   public String getName() {
      return name;
   }

   // Return player score
   public int getScore() {
      return score;
   }

   // Return player ID
   public int getID(){
      return playerID;
   }

   // Return count of Zilches 
   public int getZilchCount() {
      return numZilches;
   }

   // Return summary string of player
   public String toString() {
      return "PLAYER: [" + name +
               "] SCORE: [" + Integer.toString(score) + 
               "] #ZILCH: [" + Integer.toString(numZilches) +
               "] IN: [" + Boolean.toString(inGame) + "]";
   }

      // Quick print the active player
      public void printPlayer(int i) {
         System.out.println("PLAYER: \t" + name);
         System.out.println("SCORE:  \t" + score);
         System.out.println("#ZILCH: \t" + numZilches);
      }

   // Update player score, marking zilches as required
   public int updateScore(int newPoints) {

      // Don't record scores if not yet in the game
      if (!inGame) {
         if (newPoints < MIN_START_SCORE) {
            return 0;
         } else {
            inGame = true;
         }
      }

      // Handle zero points separately
      if (inGame && newPoints == 0) {
         increaseZilchCount();  
      } 

      // Increment score by new points
      score += newPoints;     
      if(score >= WINNING_GAME_SCORE) {
         isWinner = true;
      }
      return score;
   }

   // Handle logic for getting zero points (zilch score)
   private void increaseZilchCount() {
      numZilches++;
      if (numZilches >= MAX_NUM_ZILCHES) {
         score -= SCORE_PENALTY; 
         numZilches = 0;
      }
   }

}
