import java.util.ArrayList;
import java.util.Arrays;
import processing.core.PApplet;

  /*
   * Start game
   * All boxes are drawn onto screen from incorrectGroups
   * Only a max of 4 boxes can be selected, then the user must press enter
   * The program then checks to see if the selected matches a row in the correct groups
   * If not then checks if the user is one away from a correct group
   * If yes, then turn those boxes grey, etc
   * Game is complete when all 4 rows are solved
   */

public class Sketch1 extends PApplet {
	
  String[][] incorrectGroups;
  String[][] correctGroups;

  ArrayList<String> selectedWords = new ArrayList<>();
  boolean[][] selectedBoxes = new boolean[4][4];
  boolean[][] solvedGroups = new boolean[4][4];
  String message = "";
  int lives = 4;
  boolean gameOver = false;
  boolean showRestartButton = false;
  boolean gameWon = false;
  boolean showWinButton = false;

  /**
   * Called once at the beginning of execution, put your size all in this method
   */
  public void settings() {
	// put your size call here
    size(800, 600);
  }

  /** 
   * Called once at the beginning of execution.  Add initial set up
   * values here i.e background, stroke, fill etc.
   */
  public void setup() {
    background(255);
    if (random(1) < 0.5) {
      incorrectGroups = new String[][] {
        {"TAPE", "GOOD", "RIVET", "PETALS"},
        {"PLEATS", "FILM", "ENGROSS", "WOMAN"},
        {"RECORD", "STAPLE", "SHOOT", "PENNY"},
        {"HOLD", "ABSORB", "PLEASE", "PASTEL"}
      };
    
      correctGroups = new String[][] {
        {"ABSORB", "ENGROSS", "HOLD", "RIVET", "Grab one’s attention (EASY)"},
        {"FILM", "RECORD", "SHOOT", "TAPE", "Document with video (MODERATE)"},
        {"PASTEL", "PETALS", "PLEATS", "STAPLE", "Anagrams (HARD)"},
        {"GOOD", "PENNY", "PLEASE", "WOMAN", "Pretty ____ (EXTREMELY HARD)"}
      };
  } else {
      incorrectGroups = new String[][] {
          {"SICK", "KIND", "DRIFT", "TENDER"},
          {"STYLE", "RING", "NICE", "SWEET"},
          {"POINT", "SORT", "COOL", "WING"},
          {"MESSAGE", "TYPE", "STICK", "IDEA"}
      };
      correctGroups = new String[][] {
          {"COOL", "NICE", "SICK", "SWEET", "Awesome! (EASY)"},
          {"KIND", "SORT", "STYLE", "TYPE", "Variety (MODERATE)"},
          {"DRIFT", "IDEA", "MESSAGE", "POINT", "Gist (HARD)"},
          {"RING", "STICK", "TENDER", "WING", "Fried Appetizer: Informally (EXTREMELY HARD)"}
      };
  }
  }

  /**
   * Called repeatedly. Draws the boxes and updates the game state display.
   */
  public void draw() {
    background(255); 
    int intWordColumn = 0;

    // Draws out boxes in a 4x4 grid with text in each of them. Depending on the status of the box, completed, selected, unselected, it will have a different colour
    for (int rectColumn = 40; rectColumn < 600; rectColumn += 140) {
      int intWordRow = 0;
      for (int rectRow = 40; rectRow < 600; rectRow += 140) {
        if (solvedGroups[intWordColumn][intWordRow]) {
          fill(200); 
        } else if (selectedBoxes[intWordColumn][intWordRow]) {
          fill(0, 255, 0); 
        } else {
          fill(255); 
        }
        rect(rectRow, rectColumn, 100, 100);
        textSize(20);
        fill(0);
        text(incorrectGroups[intWordColumn][intWordRow], 10 + rectRow, 50 + rectColumn);
        intWordRow++;
      }
      intWordColumn++;
    }

    //Writes the current message to the top of the screen
    textSize(20);
    fill(0);
    text(message, 50, 30);

    //Display lives
    textSize(20);
    fill(0);
    text("Lives left: " + lives, width - 150, 30);

    if (gameOver) {
      // Show game over screen and restart button
      fill(255);
      rect(0, 0, width, height);
      fill(0);
      textSize(24);
      text("You lose!", width / 2 - textWidth("You lose!") / 2, height / 2 - 40);
      text("Better luck next time!", width / 2 - textWidth("Better luck next time!") / 2, height / 2 - 10);
      fill(100);
      rect(width / 2 - 50, height / 2, 100, 50);
      fill(255);
      textSize(20);
      text("Restart", width / 2 - textWidth("Restart") / 2, height / 2 + 30);
      showRestartButton = true;
    
    } else if (gameWon) {
      // Show you won screen and next game button
      fill(255);
      rect(0, 0, width, height);
      fill(0);
      textSize(32);
      text("You Won!", width / 2 - textWidth("You Won!") / 2, height / 2 - 40);
      fill(100);
      rect(width / 2 - 50, height / 2, 100, 50);
      fill(255);
      textSize(20);
      text("Next", width / 2 - textWidth("Next") / 2, height / 2 + 30);
      showWinButton = true;
    }
  }

  /**
   * Called when the mouse is pressed. Checks words selected and button clicks.
   */
  public void mousePressed() {

    // Check if restart button is clicked
    if (showRestartButton) {
      if (mouseX > width / 2 - 50 && mouseX < width / 2 + 50 && mouseY > height / 2 && mouseY < height / 2 + 50) {
        resetGame();
      }
      return;

    }

    // Check if next game button is clicked
    if (showWinButton) {
      if (mouseX > width / 2 - 50 && mouseX < width / 2 + 50 && mouseY > height / 2 && mouseY < height / 2 + 50) {
        println("Next game placeholder");
      }
      return;
    }

    //Selects and deselects boxes
    int intWordColumn = 0;
    for (int rectColumn = 40; rectColumn < 600; rectColumn += 140) {
      int intWordRow = 0;
      for (int rectRow = 40; rectRow < 600; rectRow += 140) {
        if (mouseX > rectRow && mouseX < rectRow + 100 && mouseY > rectColumn && mouseY < rectColumn + 100) {
          String word = incorrectGroups[intWordColumn][intWordRow];
          if (solvedGroups[intWordColumn][intWordRow]) {
            // Do nothing if the group is solved
          } else if (selectedBoxes[intWordColumn][intWordRow]) {
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
          return; 
        }
        intWordRow++;
      }
      intWordColumn++;
    }
  }

  /**
   * Checks if a key is pressed. Checks if the Enter key is pressed and then checks the selected words if they are correct.
   */
  public void keyPressed() {
    if (key == ENTER) {
      checkSelectedWords();
    }
  }

  /**
   * Checks if the selected words form a correct group. If not, it checks for one away and removes a life. If wrong then removes a life.
   */
  public void checkSelectedWords() {
    //Checks that 4 words are selected
    if (selectedWords.size() != 4) {
      message = "You must select exactly 4 words.";
      return;
    }

    for (int i = 0; i < correctGroups.length; i++) {
      String[] correctGroup = Arrays.copyOfRange(correctGroups[i], 0, 4);
      if (selectedWords.containsAll(Arrays.asList(correctGroup))) {
        // Mark the solved group
        for (int j = 0; j < 4; j++) {
          int index = findWordIndex(correctGroups[i][j]);
          if (index != -1) {
            int row = index / 4;
            int col = index % 4;
            solvedGroups[row][col] = true;
          }
        }
        message = "You solved the group: " + correctGroups[i][4];
        selectedWords.clear();
        if (checkIfGameWon()) {
          gameWon = true;
        }
        return;
      }
    }

    //If the correctGroup was not found, checks for one word away, and if not then just removes a life
    boolean correctGroupFound = false;
    if (!correctGroupFound) {
      if (checkIfOneWordAway()) {
        message = "One word away...";
        lives--;
        if (lives <= 0) {
          gameOver = true;
        }
      } else {
        message = "Selected words are incorrect.";
        lives--;
        if (lives <= 0) {
          gameOver = true;
        }
      }
    }
  }

  /**
   * Checks if all groups are solved and will make the game end if it has
   *
   * @return true if the game is won, false otherwise
   */
  public boolean checkIfGameWon() {
    for (int i = 0; i < solvedGroups.length; i++) {
      for (int j = 0; j < solvedGroups[i].length; j++) {
        if (!solvedGroups[i][j]) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * Finds the index of a given word in the incorrectGroups array.
   *
   * @param word the word to find
   * @return the index of the word, or -1 if not found
   */
  public int findWordIndex(String word) {
    for (int i = 0; i < incorrectGroups.length; i++) {
      for (int j = 0; j < incorrectGroups[i].length; j++) {
        if (incorrectGroups[i][j].equals(word)) {
          return i * 4 + j;
        }
      }
    }
    return -1;
  }

  /**
   * Checks if the selected words are one word away from forming a correct group.
   *
   * @return true if three out of four words are correct and belong to the same category, false otherwise
   */
  public boolean checkIfOneWordAway() {
    for (int i = 0; i < correctGroups.length; i++) {
      int matchCount = 0;
      for (int j = 0; j < 4; j++) {
        if (selectedWords.contains(correctGroups[i][j])) {
          matchCount++;
        }
      }
      if (matchCount == 3) {
        return true;
      }
    }
    return false;
  }

  /**
   * Resets the game by resetting all variables
   */
  public void resetGame() {
    selectedWords.clear();
    selectedBoxes = new boolean[4][4];
    solvedGroups = new boolean[4][4];
    message = "";
    lives = 4;
    gameOver = false;
    gameWon = false;
    showRestartButton = false;
    showWinButton = false;
  }
}
