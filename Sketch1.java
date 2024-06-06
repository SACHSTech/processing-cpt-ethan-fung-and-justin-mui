import processing.core.PApplet;

public class Sketch1 extends PApplet {
	
	String[][] incorrectGroups = {
    {"apple", "orange", "banana", "grape"},
    {"dog", "cat", "fish", "bird"},
    {"car", "bus", "bike", "train"},
    {"red", "blue", "green", "yellow"}
  };

  String[][] correctGroups = {
    {"apple", "orange", "banana", "grape"},
    {"dog", "cat", "fish", "bird"},
    {"car", "bus", "bike", "train"},
    {"red", "blue", "green", "yellow"}
  };

  String[] selectedWords = new String [4];
  int intNumSelected = 0;

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
    System.out.println(correctGroups);
    System.out.println(incorrectGroups);
  }

  /**
   * Called repeatedly, anything drawn to the screen goes here
   */
  public void draw() {
	  
	// sample code, delete this stuff
    
  }
  
  // define other methods down here.
}
