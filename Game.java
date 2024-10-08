import java.util.Scanner;

/*
 * Zilch, the game of 6 dice.
 */
public class Game {

   private final int MIN_BASE_SCORE = 350;
   private final int MIN_START_SCORE = 1000;

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

   // Start the very first turn of play in the game
   public void playGame(Scanner sc) {

      // Scanner sc = new Scanner(System.in);
      while (winner.isEmpty()) {

         // Change player
         nextPlayer();
         printActivePlayer();

         // Start the dice roll
         dice.startPlayerThrow();
         printDiceRoll();

         // Play through until player cannot
         boolean autoEnded = true;
         while (dice.canContinue()) {

            System.out.println("Enter the letters for the dice to keep or press (X) for all.");
            if (readDiceInput(sc)) {

               printDicePoints();

               if ((!players[activePlayer].isInGame() && dice.getScore() >= MIN_START_SCORE)
                     || (players[activePlayer].isInGame() && dice.getScore() >= MIN_BASE_SCORE)) {

                  System.out.println("\nPress (X) to finish or any other key to roll again.");
                  if (readActionInput(sc)) {
                     autoEnded = false;
                     break;
                  }
               }
               dice.continueThrow();
               printDiceRoll();

            } else {
               System.out.println("\nNot valid. Please try again.");
            }
         }

         // Finish current turn
         players[activePlayer].updateScore(dice.getScore());

         //
         if (autoEnded) {
            printDramaticHeadingOut("YOU GOT ZILCH...");
         } else {
            printDramaticHeadingOut("BANKING POINTS...");
         }

         printPlayerScores();
         winner = getWinner();

         if (winner.isEmpty()) {
            printDramaticHeadingIn("CHANGING PLAYERS");
         }
      }

      printDramaticHeadingIn("CONGRATULATIONS!");
      printBorderAndHeading(players[activePlayer].getName() + " IS THE WINNER!!!");
      printBorderAndHeading("");

   }

   // Standard switch case to read keyboard next action input
   private boolean readActionInput(Scanner sc) {
      String input = sc.nextLine();
      input = input.toUpperCase();

      if (input.equals("X")) {
         return true;
      }
      return false;
   }

   // Standard switch case to read keyboard dice names input
   private boolean readDiceInput(Scanner sc) {

      String input = sc.nextLine();
      input = input.toUpperCase().trim();

      // Check if (X) was entered to shortcut to all dice
      if (input.equals("X")) {
         input = dice.getDiceNames(false, true);
      }

      // Count how many dice were entered
      input = input.replaceAll("\\s+", "");
      int numDiceEntered = input.length();
      String[] diceChars = new String[numDiceEntered];
      for (int i = 0; i < numDiceEntered; i++) {
         diceChars[i] = input.substring(i, i + 1);
      }

      // Make sure the input dice can actually be kept
      if (dice.bankDiceIfValid(diceChars)) {
         return true;
      }
      return false;
   }

   // Swap players to the next
   private void nextPlayer() {
      activePlayer++;
      if (activePlayer >= numPlayers) {
         activePlayer = 0;
      }
   }

   // Return name of player who is winner, if any
   private String getWinner() {
      if (players[activePlayer].isWinner()) {
         return players[activePlayer].getName();
      } else {
         return "";
      }
   }

   // Quick print the dice points only
   private void printDicePoints() {
      printBorderHeading("" + dice.getScore() + " POINTS");
      System.out.println(dice.getBankedDice(true));
   }

   // Quick print the dice roll
   public void printDiceRoll() {
      printBorderHeading("ROLL # " + dice.getRollNumber());
      System.out.println(dice.getActiveDice(true));
      System.out.println(dice.getDiceNames(true, false) + "\n");
   }

   // Quick print the dice throw
   public void printDice() {
      System.out.println(dice.toString());
   }

   // Print current active player
   public void printActivePlayer() {
      printBorderAndHeading("YOU'RE UP " + players[activePlayer].getNameString() + ":");
   }

   // Print full details all players
   public void printPlayerScores() {
      printBorderAndHeading("CURRENT SCORES:\n");
      for (Player player : players) {
         System.out.println("" + player.toString());
      }
   }

   // Print a single line border followed by the heading
   public void printBorderAndHeading(String headingToPrint) {
      System.out.println("\n>>> * ------------------------------------ * <<<");
      System.out.println("\n\t" + headingToPrint + "");
   }

   // Print a single line border followed by the heading
   public void printDramaticHeadingOut(String headingToPrint) {
      System.out.println("\n   ------------------------------------   ");
      System.out.println("\n\t. \n\t . \n\t  . \n\t   " + headingToPrint + "...");
      // System.out.println("\n\t >>> ___ " + headingToPrint + "___ <<<");
   }

   // Print a single line border followed by the heading
   public void printDramaticHeadingIn(String headingToPrint) {
      System.out.println("\n   ------------------------------------   ");
      System.out.println("\n\t   . \n\t  . \n\t . \n\t" + headingToPrint + "...");
      // System.out.println("\n\t >>> ___ " + headingToPrint + "___ <<<");
   }

   // Print a single line border followed by the heading
   public void printBorderHeading(String headingToPrint) {
      System.out.println("\n   ------------- " + headingToPrint + " -------------   ");
   }

   // Quick print for entire game status
   public void printGameStatus() {
      printPlayerScores();
      printDice();
   }

   // Standard to string method
   public String toString() {
      printGameStatus();
      return "CHECK CONSOLE FOR GAME STATUS";
   }

}
