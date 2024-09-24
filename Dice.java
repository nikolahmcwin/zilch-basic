/*
 * Class just for the dice
 */
public class Dice {

   private final int NUM_DICE = 6;

   private int score;
   private int tmpScore;
   private int selectedDice;
   private int bankedDice;
   private int numRolls;

   private int trioNumber;
   private int numTrios;
   private int numPairs;
   private int numSingles;

   private Die[] dice;
   private String[] diceNames;
   private String allDiceNames;

   // Base constructor
   public Dice() {

      diceNames = new String[] { "A", "B", "C", "D", "E", "F" };

      allDiceNames = getDiceNames();

      score = 0;
      tmpScore = 0;
      numRolls = 0;
      selectedDice = 0;
      trioNumber = 0;
      numSingles = 0;
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
      refillThrow();
      rollActiveDice();
   }

   // Main dice method to loop a player through all throws until no longer can
   public Boolean canContinue() {
      if (autoZilch()) {
         updateScore();
         return false;
      } else {
         return true;
      }
   }

   // Keep the throws going
   public void continueThrow() {
      if (bankedDice >= NUM_DICE) {
         refillThrow();
      }
      rollActiveDice();
   }

   // Roll the active dice
   private void rollActiveDice() {
      for (Die die : dice) {
         if (die.isActive()) {
            die.roll();
         }
      }
      numRolls++;
   }

   // Scan dice throw for auto zilch
   private boolean autoZilch() {
      String diceString = getActiveDice();
      countEachOccurance(diceString);
      if (numTrios != 0 || numPairs == 3 || numberExistsInString(diceString, 1)
            || numberExistsInString(diceString, 5)) {
         return false;
      }
      tmpScore = 0;
      updateScore();
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
      updateScore();
      nextThrow();
   }

   // Store the points from these dice
   private void unselectDice() {
      for (Die die : dice) {
         if (die.isSelected()) {
            die.putIntoActiveThrow();
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
      int diceRemaining = selectedDice;
      int triosRemaining = numTrios;

      if (diceRemaining == 6) {
         if (numSingles == 6) {
            tmpScore += 1500;
            diceRemaining -= 6;
         } else if (numPairs == 3) {
            tmpScore += 1000;
            diceRemaining -= 6;
         }
      }

      while (diceRemaining >= 3 && triosRemaining > 0) {
         diceString = checkForTrioMatch(diceString);
         triosRemaining = triosRemaining - 1;
         diceRemaining = diceRemaining - 3;
      }

      while (diceRemaining > 0) {
         diceString = checkForSingleMatch(diceString);
         diceRemaining--;
      }

      return tmpScore != 0;
   }

   // Remove sets of matching threes from the dice
   private String checkForTrioMatch(String diceString) {
      if (trioNumber <= 0) {
         return diceString;
      }

      if (trioNumber == 1) {
         tmpScore += trioNumber * 1000;
      } else {
         tmpScore += trioNumber * 100;
      }

      for (int i = 0; i < 3; i++) {
         diceString = removeDiceName(diceString, trioNumber);
      }
      return diceString;
   }

   // Points for single dice.
   private String checkForSingleMatch(String diceString) {

      if (trioNumber > 0 && numberExistsInString(diceString, trioNumber)) {
         diceString = removeDiceName(diceString, trioNumber);
         tmpScore += 100;
      } else if (numberExistsInString(diceString, 1)) {
         diceString = removeDiceName(diceString, 1);
         tmpScore += 100;
      } else if (numberExistsInString(diceString, 5)) {
         diceString = removeDiceName(diceString, 5);
         tmpScore += 50;
      }

      return diceString;
   }

   // Basic manipulation to remove a dice name from the string
   private String removeDiceName(String diceNameString, int diceNumber) {
      return diceNameString.replaceFirst(Integer.toString(diceNumber), "");
   }

   // Count if specific number exists within string
   private boolean numberExistsInString(String string, int numToCount) {
      return string.indexOf(Integer.toString(numToCount)) >= 0;
   }

   // Fill out global dice number counts
   private void countEachOccurance(String diceString) {
      int[] numbers = new int[] { 1, 2, 3, 4, 5, 6 };
      for (int num : numbers) {
         countOccurences(diceString, num);
      }

   }

   // Count the number of times the integer appears in the string
   private void countOccurences(String string, int numToCount) {
      String match = Integer.toString(numToCount);
      int count = string.length() - string.replace(match, "").length();

      if (count >= 3) {
         numTrios++;
         trioNumber = numToCount;
      } else if (count == 2) {
         numPairs++;
      } else if (count == 1) {
         numSingles++;
      }
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

   // Return the number of rolls this dice set is on.
   public int getRollNumber() {
      return numRolls;
   }

   // Clear all stored parameters on the dice throw.
   private void newThrow() {
      score = 0;
      trioNumber = 0;
      bankedDice = 0;
      numRolls = 0;
      nextThrow();
   }

   // Clear single throw stored parameters
   private void nextThrow() {
      tmpScore = 0;
      numSingles = 0;
      numPairs = 0;
      numTrios = 0;
      selectedDice = 0;
   }

   // WHen all dice are banked, but can continue, reset them into active
   private void refillThrow() {
      for (Die die : dice) {
         if (die.isBanked() || die.isSelected()) {
            die.putIntoActiveThrow();
         }
      }
      trioNumber = 0;
      bankedDice = 0;
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
         sb.append("\t");
         for (String name : diceNames) {
            sb.append("[" + name + "] ");
         }
         allDiceNames = sb.toString();
      }
      return allDiceNames;
   }

   // Get a string of the current active dice
   public String getActiveDice() {
      return getDice(true, false, false);
   }

   // Get a string of the current banked dice
   public String getBankedDice() {
      return getDice(false, true, false);
   }

   // Get a string of the current selected dice
   public String getSelectedDice() {
      return getDice(false, false, true);
   }

   // Return a string of the dice
   private String getDice(boolean getActive, boolean getBanked, boolean getSelected) {

      StringBuilder sb = new StringBuilder();
      sb.append("\t");
      for (Die die : dice) {
         if ((getActive && die.isActive()) || (getBanked && die.isBanked()) || (getSelected && die.isSelected())) {
            sb.append("[" + die.getNumber() + "] ");
         } else {
            sb.append("[ ] ");
         }
      }
      return sb.toString();
   }

   public String toString() {
      return "\n" + getBankedDice() + "\n" + getActiveDice() + "\n" + getDiceNames();
   }
}
