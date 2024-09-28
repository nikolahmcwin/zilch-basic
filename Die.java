import java.util.Random;

/*
 * Basic class to hold a single 6 sided die
 */
public class Die {

   private final int NUM_SIDES = 6;

   private String name;
   private int currentNumber;
   private boolean selected;
   private boolean banked;
   private Random rand;

   // Default Constructor
   public Die() {
      this("?");
   }

   // Constructor to assign name
   public Die(String dieName) {
      name = dieName;
      selected = false;
      banked = false;
      rand = new Random();
      currentNumber = 0;
      // currentNumber = roll();
   }

   // Returns true if the dice is in play, not in points
   public boolean isActive() {
      return !banked;
   }

   // Returns true if the dice is in play, not in points
   public boolean isSelected() {
      return selected;
   }

   // Returns true if the dice is now points, no longer in play
   public boolean isBanked() {
      return banked;
   }

   // Move from active dice out to points bay
   public void selectDie() {
      selected = true;
   }

   // Move from active dice out to points bay
   public void bankDie() {
      banked = true;
   }

   // Move from points bay back into active
   public void unselectDie() {
      selected = false;
   }

   // Move from points bay back into active
   public void unbankDie() {
      banked = false;
   }

   // Roll the dice to get a number, if active
   public int roll() {
      if (!banked) {
         currentNumber = rand.nextInt(NUM_SIDES) + 1;
      }
      return currentNumber;
   }

   // Return the current number on the die
   public int getNumber() {
      return currentNumber;
   }

   // Return the die name
   public String getName() {
      return name;
   }

   // Return summary string of the die
   public String toString() {
      return "Dice Name: " + name +
            "\t Number: " + currentNumber +
            "\t Banked: " + banked +
            "\t Selected: " + selected;
   }

}
