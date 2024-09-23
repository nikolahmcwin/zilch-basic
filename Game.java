import java.lang.StringBuilder;
import java.util.Scanner;

/*
 * Zilch, the game of 6 dice.
 */
public class Game {

   private final int MIN_BASE_SCORE = 350;
   private final int WINNING_GAME_SCORE = 10000;

   private int numPlayers;
   private int activePlayer;
   private Player[] players;
   private Dice dice;
   private String winner;

   // Default constructor, 2 player game
   public Game() {
      this(2, new String[] { "Player_1", "Player_2" });
   }

   // Set up X player game, setting names, etc
   public Game(int numPlayers, String[] playerNames) {

      dice = new Dice();
      players = new Player[numPlayers];

      for (int i = 0; i < numPlayers; i++) {
         players[i] = new Player(playerNames[i], i);
      }

      activePlayer = players[0].getID();
      winner = "";
   }

   // Reset everything in the game.
   public void resetGame() {

   }

   // Start the very first turn of play in the game
   public void playGame() {

      while (winner.isEmpty()) {

         // Change player
         nextPlayer();
         printActivePlayer();

         // Start the dice roll
         dice.rollAllDice();
         printDice();
         askForInput();

      }

   }

   // Swap players to the next
   public void nextPlayer() {
      activePlayer++;
      if (activePlayer > numPlayers) {
         activePlayer = 0;
      }
      dice.rollAllDice();
   }

   public void checkForWinner() {

   }

   public void askForInput() {
      Scanner inputScanner = new Scanner(System.in);
      String input = "";
      while (!readInput(input)) {
         input = inputScanner.nextLine();
      }

   }

   // Standard switch case to read keyboard input
   private boolean readInput(String input){

      if (input.length() > 1 ){
         return false;
      }

      switch (input) {
         case "A","B","C","D","E","F":
            // A DICE TO SELECT
            dice.keepDice(input);
            return true;
         case "R":
            // Roll dice
            dice.rollActiveDice();
            return true;
         case "Z":
            // Zilch score
            players[activePlayer].updateScore(0);
            return true;
         case "S":
            // Score - bank the points
            players[activePlayer].updateScore(dice.getScore());
            return true;
         case "N":
            // Next player turn
            return true;
         default:
            return false;
      } 
   }


   // Quick print the dice throw
   public void printDice() {
      System.out.println("-----------------------");
      System.out.println("NAMES:  \t" + dice.getDiceNames());
      System.out.println("ACTIVE: \t" + dice.getActiveDice());
      System.out.println("POINTS: \t" + dice.getPointsDice());
      System.out.println("-----------------------");
   }

   // Print current active player
   public void printActivePlayer() {
      System.out.println("**** You're up: " + players[activePlayer].getName() + " ****");
   }

   // Print full details all players
   public void printPlayers() {
      for (Player player : players) {
         System.out.println(player.toString());
      }
   }

   // Quick print for entire game status
   public void printGameStatus() {
      printPlayers();
      printDice();
   }

   // Standard to string method
   public String toString() {
      printGameStatus();
      return "CHECK CONSOLE FOR GAME STATUS";
   }

}
