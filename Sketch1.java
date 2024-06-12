import java.util.ArrayList;
import java.util.Arrays;
import processing.core.PApplet;

public class Sketch1 extends PApplet {
	
	String[][] incorrectGroups = {
    {"bus", "orange", "blue", "bird"},
    {"green", "car", "apple", "dog"},
    {"banana", "train", "cat", "red"},
    {"fish", "bike", "grape", "yellow"}
  };

  String[][] correctGroups = {
    {"apple", "orange", "banana", "grape"},
    {"dog", "cat", "fish", "bird"},
    {"car", "bus", "bike", "train"},
    {"red", "blue", "green", "yellow"}
  };

  ArrayList<String> selectedWords = new ArrayList<>();
  boolean[][] selectedBoxes = new boolean[4][4];

  /*
   * Start game
   * All boxes are drawn onto screen from incorrectGroups
   * 4 boxes can be selected
   * Only a max of 4 boxes can be selected, then the user must press enter
   * The program then checks to see if the selected matches a row in the correct groups
   * If yes, then turn those boxes blank, grey, etc
   * Game is complete when all 4 rows are solved
   */

  /**
   * Called once at the beginning of execution, put your size all in this method
   */
  public void settings() {
	// put your size call here
    size(600, 600);
  }

  /** 
   * Called once at the beginning of execution.  Add initial set up
   * values here i.e background, stroke, fill etc.
   */
  public void setup() {
    background(255);
  }

  /**
   * Called repeatedly, anything drawn to the screen goes here
   */

  //40 + 100 + 40 + 100 + 40 + 100 + 40 + 100 + 40
  
  public void draw() {
    background(255); // Clear the background each frame
    int intWordColumn = 0;
    for (int rectColumn = 40; rectColumn < 600; rectColumn += 140) {
      int intWordRow = 0;
      for (int rectRow = 40; rectRow < 600; rectRow += 140) {
        if (selectedBoxes[intWordColumn][intWordRow]) {
          fill(0, 255, 0); // Green for selected
        } else {
          fill(255); // White for unselected
        }
        rect(rectRow, rectColumn, 100, 100);
        textSize(20);
        fill(0);
        text(incorrectGroups[intWordColumn][intWordRow], 10 + rectRow, 50 + rectColumn);
        intWordRow++;
      }
      intWordColumn++;
    }
  }

  public void mousePressed() {
    int intWordColumn = 0;
    for (int rectColumn = 40; rectColumn < 600; rectColumn += 140) {
      int intWordRow = 0;
      for (int rectRow = 40; rectRow < 600; rectRow += 140) {
        if (mouseX > rectRow && mouseX < rectRow + 100 && mouseY > rectColumn && mouseY < rectColumn + 100) {
          String word = incorrectGroups[intWordColumn][intWordRow];
          if (selectedBoxes[intWordColumn][intWordRow]) {
            // Deselect the box
            selectedBoxes[intWordColumn][intWordRow] = false;
            selectedWords.remove(word);
          } else {
            // Select the box if less than 4 are selected
            if (selectedWords.size() < 4) {
              selectedBoxes[intWordColumn][intWordRow] = true;
              selectedWords.add(word);
            }
          }
          println("Selected words: " + selectedWords);
          return; // Exit once the correct rectangle is found and processed
        }
        intWordRow++;
      }
      intWordColumn++;
    }
  }

}
