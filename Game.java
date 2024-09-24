import java.lang.StringBuilder;
import java.util.Scanner;

/*
 * Zilch, the game of 6 dice.
 */
public class Game {

   private final int MIN_BASE_SCORE = 350;
   private final int MIN_START_SCORE = 1000;
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
   public Game(int numberOfPlayers, String[] playerNames) {

      dice = new Dice();
      numPlayers = numberOfPlayers;
      players = new Player[numPlayers];

      for (int i = 0; i < numPlayers; i++) {
         players[i] = new Player(playerNames[i], i);
      }

      activePlayer = -1;
      winner = "";
   }

   // Reset everything in the game.
   public void resetGame() {

   }

   // Start the very first turn of play in the game
   public void playGame() {

      Scanner sc = new Scanner(System.in);

      while (winner.isEmpty()) {

         // Change player
         nextPlayer();
         printActivePlayer();

         // Start the dice roll
         dice.startPlayerThrow();
         printDice();

         // Play through until player cannot
         String input;
         while (dice.canContinue()) {

            System.out.println("Enter the letters for the dice to keep.");
            if (readDiceInput(sc)) {

               printDicePoints();

               if ((!players[activePlayer].isInGame() && dice.getScore() > MIN_START_SCORE)
                     || (players[activePlayer].isInGame() && dice.getScore() > MIN_BASE_SCORE)) {
                  System.out.println("Press (X) to finish or any other key to roll again.");
                  if (readActionInput(sc)) {
                     break;
                  }
               }
               dice.continueThrow();
               printDice();

            } else {
               System.out.println("Not a valid input. Please try again.");
            }
         }

         // Finish current turn
         players[activePlayer].updateScore(dice.getScore());
         winner = getWinner();
      }
   }

   // Standard switch case to read keyboard next action input
   private boolean readActionInput(Scanner sc) {
      String input = sc.nextLine();
      if (input == "X") {
         return true;
      }
      return false;
   }

   // Standard switch case to read keyboard dice names input
   private boolean readDiceInput(Scanner sc) {

      String input = sc.nextLine();
      // Multiple input characters is dice, handle those here
      if (input.length() >= 1) {
         String[] split = input.trim().split("\\s+");
         if (dice.bankDiceIfValid(split)) {
            return true;
         }
      }
      return false;
   }

   // Swap players to the next
   public void nextPlayer() {
      activePlayer++;
      if (activePlayer > numPlayers) {
         activePlayer = 0;
      }
   }

   // Return name of player who is winner, if any
   public String getWinner() {
      if (players[activePlayer].isWinner()) {
         return players[activePlayer].getName();
      } else {
         return "";
      }
   }

   // Quick print the dice points only
   public void printDicePoints() {
      System.out.println("POINTS:\t" + dice.getPointsDice() + " totalling " + dice.getScore());
   }

   // Quick print the dice throw
   public void printDice() {
      System.out.println("---------------------------------");
      System.out.println("DICE: \t" + dice.getDiceNames());
      System.out.println("THROW: \t" + dice.getActiveDice());
      System.out.println("---------------------------------");
   }

   // Print current active player
   public void printActivePlayer() {
      System.out.println("* You're up, " + players[activePlayer].getName() +
            "!\n\tYou have " + players[activePlayer].getScore() + " points and " +
            players[activePlayer].getZilchCount() + " zilches! ****");
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
