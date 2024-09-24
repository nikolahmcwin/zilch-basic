import java.util.Scanner;

/*
 * Base console app to run the game in
 */
public class Main {

   public static void main(String[] args) {
      
      System.out.println("\nSTARTING NEW GAME OF ZILCH...\n");

      boolean skipInput = true;
      Game myFirstGame;
       
      if (skipInput) {
         myFirstGame = new Game();
      } else {
         Scanner inputScanner = new Scanner(System.in);
         int numPlayers = getNumPlayers(inputScanner);
         String[] playerNames = getPlayerNames(inputScanner, numPlayers);
         inputScanner.close();
         myFirstGame = new Game(numPlayers, playerNames);
      }

      myFirstGame.playGame();
      myFirstGame.printGameStatus();
   }

   // Use console input to get number of playeres
   private static int getNumPlayers(Scanner input) {
      int inNum = 0;
      System.out.println("Enter number of players:");
      while (inNum < 2 || inNum > 5) {
         if (inNum != 0) {
            System.out.println("Enter a number between 2 and 5.");
         }
         try {
            inNum = Integer.parseInt(input.nextLine());
         } catch (Exception e) {
            System.out.println("Enter a valid number.");
         }
      }
      return inNum;
   }

   // Use console input to get number of playeres
   private static String[] getPlayerNames(Scanner input, int numPlayers) {
      String[] playerNames = new String[numPlayers];
      for (int i = 0; i < numPlayers; i++) {
         System.out.println("Enter name for player " + Integer.toString(i + 1) + ":");
         playerNames[i] = input.nextLine();
      }
      return playerNames;
   }
}
