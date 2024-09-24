/*
 * Class just for the dice
 */
public class Dice {

   private final int NUM_DICE = 6;

   private int score;
   private int tmpScore;
   private int selectedDice;
   private int bankedDice;

   private int trioNumber;
   private int numTrios;
   private int numPairs;

   private Die[] dice;
   private String[] diceNames;
   private String allDiceNames;

   // Base constructor
   public Dice() {

      diceNames = new String[] { "A", "B", "C", "D", "E", "F" };

      allDiceNames = getDiceNames();

      score = 0;
      tmpScore = 0;
      selectedDice = 0;
      trioNumber = 0;
      numPairs = 0;
      numTrios = 0;

      dice = new Die[NUM_DICE];
      for (int j = 0; j < NUM_DICE; j++) {
         dice[j] = new Die(diceNames[j]);
      }
   }

   // Start a new dice throw
   public void startPlayerThrow() {
      newThrow();
      rollActiveDice();
   }

   // Main dice method to loop a player through all throws until no longer can
   public Boolean canContinue() {
      if (autoZilch()) {
         return false;
      } else {
         // Something score here?
         return true;
      }
   }

   // Keep the throws going
   public void continueThrow() {
      if (bankedDice >= NUM_DICE - 1) {
         refillThrow();
      }
      rollActiveDice();
   }

   // WHen all dice are banked, but can continue, reset them into active
   private void refillThrow() {
      for (Die die : dice) {
         if (die.isBanked()) {
            die.unselectDice();
         }
      }
      trioNumber = 0;
      bankedDice = 0;
   }

   // Start all dice active and roll
   public void rollAllDice() {
      for (Die die : dice) {
         die.unselectDice();
         die.roll();
      }
   }

   // Roll the active dice
   private void rollActiveDice() {
      for (Die die : dice) {
         if (die.isActive()) {
            die.roll();
         }
      }
   }

   // Scan dice throw for auto zilch
   private boolean autoZilch() {
      String diceString = getActiveDice();
      if (countOccurences(diceString, 1) >= 1 || countOccurences(diceString, 5) >= 1) {
         return false;
      } else if (countOccurences(diceString, 2) >= 3 || countOccurences(diceString, 3) >= 3 ||
            countOccurences(diceString, 4) >= 3 || countOccurences(diceString, 6) >= 3) {
         return false;
      } else {
         // TODO check for three pairs.
      }
      return true;
   }

   // Move dice to the points, out of throw, returning true or false if valid to do
   public boolean bankDiceIfValid(String[] diceToKeep) {   
      if (!readDiceInput(diceToKeep)) {
         return false;
      }
      if (checkThrowScore()) {
         bankSelectedDice();
         return true;
      } else {
         unselectDice();
         return false;
      }
   }

   // Store the points from these dice
   private void bankSelectedDice() {
      for (Die die : dice) {
         if (die.isSelected()) {
            die.bankDice();
            bankedDice++;
         }
      }
      score += tmpScore;
      nextThrow();
   }

   // Store the points from these dice
   private void unselectDice() {
      for (Die die : dice) {
         if (die.isSelected()) {
            die.unselectDice();
         }
      }
      nextThrow();
   }

   // Take the letter name of a dice, get the array index
   private boolean readDiceInput(String[] diceToKeep) {
      for (String dieName : diceToKeep) {
         int index = getDiceIndex(dieName);
         if (index < 0) {
            return false;
         }
         dice[index].selectDice();
         selectedDice++;
      }
      return true;
   }

   // Confirm the kept dice is allowed
   private boolean checkThrowScore() {

      String diceString = getSelectedDice();

      if (selectedDice == 1) {
         tmpScore = checkSingleDie(diceString);
         return tmpScore != 0;
      }

      // TODO more than single dice score;
      int ones, twos, threes, fours, fives, sixes;

      ones = countOccurences(diceString, 1);
      twos = countOccurences(diceString, 2);
      threes = countOccurences(diceString, 3);
      fours = countOccurences(diceString, 4);
      fives = countOccurences(diceString, 5);
      sixes = countOccurences(diceString, 6);

      if (selectedDice == 6) {
         if (ones == 1 && twos == 1 && threes == 1 && fours == 1 && fives == 1 && sixes == 1) {
            tmpScore = 1500;
         } else if (numPairs == 3) { 
            tmpScore = 1000;
         }
      }

      if (selectedDice == 3 && numTrios > 0) {
         if (trioNumber == 1) {
            tmpScore = trioNumber * 1000;
         } else { 
            tmpScore = trioNumber * 100;
         }
      }

      //TODO four dice , five dice, two, dice etc

      return tmpScore != 0;
   }

   // Points for single dice.
   private int checkSingleDie(String diceString) {
      int number = numberFromString(diceString);
      if (number == 1) {
         return 100;
      } else if (number == trioNumber) {
         return 100;
      } else if (number == 5) {
         return 50;
      }
      return 0;
   }

   // Count the number of times the integer appears in the string
   private int countOccurences(String string, int numToCount) {
      String match = Integer.toString(numToCount);
      int count = string.length() - string.replace(match, "").length();
      if (count >= 3) {
         numTrios++;
         trioNumber = numToCount;
      }
      if (count == 2) {
         numPairs++;
      }
      return count;
   }

   // Return dice number based on it existing in String or not
   private int numberFromString(String string) {
      if (string.indexOf("1") >= 0) {
         return 1;
      } else if (string.indexOf("2") >= 0) {
         return 2;
      } else if (string.indexOf("3") >= 0) {
         return 3;
      } else if (string.indexOf("4") >= 0) {
         return 4;
      } else if (string.indexOf("5") >= 0) {
         return 5;
      } else if (string.indexOf("6") >= 0) {
         return 6;
      }
      return 0;
   }

   // Take the score and lock it in
   private void updateScore() {
      score += tmpScore;
      tmpScore = 0;
   }

   // Return the score for this dice throw
   public int getScore() {
      return score;
   }

   // Clear all stored parameters on the dice throw.
   private void newThrow() {
      score = 0;
      trioNumber = 0;
      bankedDice = 0;
      nextThrow();
   }

   // Clear single throw stored parameters
   private void nextThrow(){
      tmpScore = 0;
      numPairs = 0;
      numTrios = 0;
      selectedDice = 0;
   }
   
   // Take the letter name of a dice, get the array index
   private int getDiceIndex(String dieName) {
      switch (dieName) {
         case "A":
            return 0;
         case "B":
            return 1;
         case "C":
            return 2;
         case "D":
            return 3;
         case "E":
            return 4;
         case "F":
            return 5;
         default:
            return -1;
      }
   }

   // Set up the required number of dice.
   public String getDiceNames() {
      if (allDiceNames == null) {
         StringBuilder sb = new StringBuilder();
         for (String name : diceNames) {
            sb.append("-" + name + "- ");
         }
         allDiceNames = sb.toString();
      }
      return allDiceNames;
   }

   // Get a string of the current active dice
   public String getActiveDice() {
      return getDice(true, false);
   }

   // Get a string of the current selected dice
   public String getSelectedDice() {
      return getDice(false, true);
   }

   // Get a string of the current banked dice
   public String getPointsDice() {
      return getDice(false, false);
   }

   // Return a string of the dice
   private String getDice(boolean getActive, boolean getSelected) {

      StringBuilder sb = new StringBuilder();
      for (Die die : dice) {
         if ((getActive && die.isActive()) || (!getActive && die.isBanked()) || (getSelected && die.isSelected())) {
            sb.append("[" + die.getNumber() + "] ");
         } else {
            sb.append("[ ] ");
         }
      }
      return sb.toString();
   }

   // Detailed print of all dice details
   public void printDiceDetails() {
      System.out.println("\n\n  **** Dice Details **** ");
      for (Die die : dice) {
         System.out.println(die.toString());
      }
   }

}
