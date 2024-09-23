/*
 * Class just for the dice
 */
public class Dice {

   private final int NUM_DICE = 6;

   private boolean trioBanked;
   private int trioNumber;
   private int totalScore;
   private Die[] dice;
   private String[] diceNames;
   private String allDiceNames;

   // Base constructor
   public Dice() {

      diceNames = new String[] { "A", "B", "C", "D", "E", "F" };

      allDiceNames = getDiceNames();

      trioBanked = false;
      trioNumber = 0;

      dice = new Die[NUM_DICE];
      for (int j = 0; j < NUM_DICE; j++) {
         dice[j] = new Die(diceNames[j]);
      }
   }

   /* Start all dice active and roll */
   public void rollAllDice() {
      for (Die die : dice) {
         die.unselectDice();
         die.roll();
      }
   }

   // Roll the active dice
   public void rollActiveDice() {
      for (Die die : dice) {
         if (die.isActive()) {
            die.roll();
         }
      }
   }

   
   // Move dice to the points, out of throw
   public void keepDice(String diceName) {
      int dieIndex = translateDiceInput(diceName);
      int dieNumber = dice[dieIndex].getNumber();

      if (dice[dieIndex].isBanked())
      if (validToKeep(dice[dieIndex])){
         dice[dieIndex].bankDice();
         updateScore();
      }

   }

   // Confirm the kept dice is allowed
   public boolean validToKeep(Die selectedDie) {

      if (selectedDie.isBanked()) {
         return false;
      }

      switch (selectedDie.getNumber()) {
         case 1, 5:
            return true;
         case 2, 3, 4, 6:
            if (trioBanked && trioNumber == selectedDie.getNumber()) {
               return true;
            }
      }

      return false;
   }

   // Take the score
   private void updateScore(){

      switch (selectedDie.getNumber()) {
         case 1, 5:
            return true;
         case 2, 3, 4, 6:
            if (trioBanked && trioNumber == selectedDie.getNumber()) {
               return true;
            }
      }
   }

   // Return the score for this dice throw
   public int getScore(){
      return totalScore;
   }
   // Take the letter name of a dice, get the array index
   private int translateDiceInput(String diceName) {
      switch (diceName) {
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
      return getDice(true);
   }

   // Get a string of the current active dice
   public String getPointsDice() {
      return getDice(false);
   }

   // Return a string of the dice
   private String getDice(boolean getActive) {

      StringBuilder sb = new StringBuilder();
      for (Die die : dice) {
         if ((getActive && die.isPlaying()) || (!getActive && !die.isPlaying())) {
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
