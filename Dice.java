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
   private int secondTrioNumber;
   private int numTrios;
   private int numPairs;
   private int numSingles;

   private Die[] dice;
   private String[] diceNames;

   // Base constructor
   public Dice() {

      diceNames = new String[] { "A", "B", "C", "D", "E", "F" };

      score = 0;
      tmpScore = 0;
      numRolls = 0;
      selectedDice = 0;
      trioNumber = 0;
      secondTrioNumber = 0;
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
         score = 0;
         tmpScore = 0;
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
      String diceString = getActiveDice(false);
      countEachOccurance(diceString);
      Boolean zilch = true;
      if (numTrios != 0 || numPairs == 3 || numberExistsInString(diceString, 1)
            || numberExistsInString(diceString, 5)
            || (trioNumber > 0 && numberExistsInString(diceString, trioNumber))) {
         zilch = false;
      }
      nextThrow();
      return zilch;
   }

   // Move dice to the points, out of throw, returning true or false if valid to do
   public boolean bankDiceIfValid(String[] diceToKeep) {
      if (readDiceInput(diceToKeep)) {
         if (allSelectedDiceHaveValidScorePoints()) {
            bankSelectedDice();
            return true;
         }
      }
      unselectDice();
      return false;
   }

   // Store the points from these dice
   private void bankSelectedDice() {
      for (Die die : dice) {
         if (die.isSelected()) {
            die.unselectDie();
            die.bankDie();
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
            die.unselectDie();
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
         if (dice[index].isActive()) {
            dice[index].selectDie();
            selectedDice++;
         } else {
            // Attempting to select a non-active die. Cheating!
            return false;
         }
      }
      return true;
   }

   // Confirm the kept dice is allowed
   private boolean allSelectedDiceHaveValidScorePoints() {

      int diceRemaining = selectedDice;
      String diceString = getSelectedDice(false);

      countEachOccurance(diceString);

      // Check for matches on all six dice - straight or three pair
      if (diceRemaining == 6) {
         if (numSingles == 6) {
            tmpScore += 1500;
            diceRemaining -= 6;
         } else if (numPairs == 3) {
            tmpScore += 1000;
            diceRemaining -= 6;
         }
      }

      // Check for matches of triples
      if (diceRemaining >= 3) {
         if (trioNumber != 0) {
            diceString = removeAnyTrioMatch(diceString, trioNumber);
            diceRemaining = diceRemaining - 3;
         }
         if (secondTrioNumber != 0) {
            diceString = removeAnyTrioMatch(diceString, secondTrioNumber);
            diceRemaining = diceRemaining - 3;
         }
      }

      while (diceRemaining > 0) {
         String finalDice = removeAnySingleMatch(diceString);
         if (finalDice.equals(diceString)) {
            // No single dice removed -> it isn't valid to keep!
            tmpScore = 0;
            return false;
         } else {
            // Positive match -> keep going through dice
            diceString = finalDice;
            diceRemaining--;
         }
      }

      return tmpScore != 0;
   }

   // Remove sets of matching threes from the dice
   private String removeAnyTrioMatch(String diceString, int numberToCheck) {
      if (numberToCheck == 1) {
         tmpScore += 1000;
      } else if (trioNumber > 1) {
         tmpScore += numberToCheck * 100;
      }
      for (int i = 0; i < 3; i++) {
         diceString = removeDiceName(diceString, numberToCheck);
      }
      return diceString;
   }

   // Points for single dice.
   private String removeAnySingleMatch(String diceString) {

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
         if (numTrios > 1) {
            secondTrioNumber = numToCount;
         } else {
            trioNumber = numToCount;
         }
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
      secondTrioNumber = 0;
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
         if (die.isBanked()) {
            die.unbankDie();
         }
         if (die.isSelected()) {
            die.unselectDie();
         }
      }
      trioNumber = 0;
      secondTrioNumber = 0;
      bankedDice = 0;
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
      public String getDiceNames(boolean getFormatted, Boolean getActiveOnly) {

         StringBuilder sb = new StringBuilder();
   
         if (getFormatted) {
            sb.append("\t");
         }
   
         for (Die die : dice) {

            if (getActiveOnly && !die.isActive()) {
               continue;
            } 

            if (getFormatted) {
               sb.append("[" + die.getName() + "]");
            } else {
               sb.append(die.getName());
            }
            sb.append(" ");
         }
   
         return sb.toString();
      }
   // Set up the required number of dice.
   public String getOldDiceNames(boolean getFormatted) {

      StringBuilder sb = new StringBuilder();

      if (getFormatted) {
         sb.append("\t");
      }

      for (String name : diceNames) {
         if (getFormatted) {
            sb.append("[" + name + "]");
         } else {
            sb.append(name);
         }
         sb.append(" ");
      }

      return sb.toString();
   }

   // Get a string of the current active dice
   public String getActiveDice(boolean getFormatted) {
      return getDice(true, false, false, getFormatted);
   }

   // Get a string of the current banked dice
   public String getBankedDice(boolean getFormatted) {
      return getDice(false, true, false, getFormatted);
   }

   // Get a string of the current selected dice
   public String getSelectedDice(boolean getFormatted) {
      return getDice(false, false, true, getFormatted);
   }

   // Return a string of the dice
   private String getDice(boolean getActive, boolean getBanked, boolean getSelected, boolean getFormatted) {

      StringBuilder sb = new StringBuilder();

      if (getFormatted) {
         sb.append("\t");
      }

      for (Die die : dice) {
         if (getActive) {
            if (die.isActive()) {
               sb.append(formatNumber(die.getNumber(), getFormatted));
            } else {
               sb.append(formatNumber(0, getFormatted));
            }
         } else if (getBanked) {
            if (die.isBanked()) {
               sb.append(formatNumber(die.getNumber(), getFormatted));
            } else {
               sb.append(formatNumber(0, getFormatted));
            }
         } else if (getSelected) {
            if (die.isSelected()) {
               sb.append(formatNumber(die.getNumber(), getFormatted));
            } else {
               sb.append(formatNumber(0, getFormatted));
            }
         }
         sb.append(" ");
      }

      return sb.toString();
   }

   // return dice number in brackets or not
   private String formatNumber(int number, boolean getFormatted) {
      if (getFormatted && number == 0) {
         return "[ ]";
      } else if (getFormatted) {
         return "[" + number + "]";
      } else if (number == 0) {
         return "";
      } else {
         return Integer.toString(number);
      }
   }

   public String toString() {
      return "\n" + getBankedDice(true) + "\n" + getActiveDice(true) + "\n" + getDiceNames(true, false);
   }
}
