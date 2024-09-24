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
   private int gameScore;
   private int mostRecentScore;
   private int numZilches;

   // Default constructor, auto generated name
   public Player() {
      this("Player_", totalPlayers++);
   }

   // Standard constructor, name passed in
   public Player(String playerName, int playerNumber) {
      name = playerName;
      playerID = playerNumber;
      gameScore = 0;
      mostRecentScore = 0;
      numZilches = 0;
   }

   // Return player name
   public String getName() {
      return name;
   }

   // Return player score
   public int getScore() {
      return gameScore;
   }

   // Return most recent score
   public int getLastScore() {
      return mostRecentScore;
   }

   // Return player ID
   public int getID() {
      return playerID;
   }

   // Return count of Zilches
   public int getZilchCount() {
      return numZilches;
   }

   // Return if the player is in the game score yet
   public boolean isInGame() {
      return inGame;
   }

   // Return if score is enough to win
   public boolean isWinner() {
      return gameScore >= WINNING_GAME_SCORE;
   }

   // Return summary string of player
   public String toString() {
      return getNameString() + "\n" + getScoreString() + "\n" + getRecentScoreString();
   }

   // Return summary string of player
   public String getNameString() {
      return ">>> " + name + ":";
   }

   // Return summary string of player
   public String getScoreString() {
      return "\t [" + gameScore + " points] [" + numZilches + " zilches]";
   }

   // Return summary string of player
   public String getRecentScoreString() {
      return "\t [" + mostRecentScore + " last score]";
   }

   // Update player score, marking zilches as required
   public int updateScore(int newPoints) {

      // Don't record scores if not yet in the game
      if (!inGame) {
         if (newPoints < MIN_START_SCORE) {
            mostRecentScore = 0;
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
      mostRecentScore = newPoints;
      gameScore += newPoints;
      return gameScore;
   }

   // Handle logic for getting zero points (zilch score)
   private void increaseZilchCount() {
      numZilches++;
      if (numZilches >= MAX_NUM_ZILCHES) {
         gameScore -= SCORE_PENALTY;
         numZilches = 0;
      }
   }

}
