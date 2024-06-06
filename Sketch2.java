import processing.core.PApplet;

public class Sketch2 extends PApplet {
  
  int intGridSizeX = 5;
  int intGridSizeY = 6;
  String[] strWordList = {"stark", "reese", "crack", "nosey", "hitch", "rural", "craic", "ergot", "ouija"}; // Sample word list
  String strTargetWord;
  String[] strGuesses;
  int intCurrentRow;
  boolean isGame1Over;
  boolean isGame1Victory = false;
  boolean isGameScreenActive = false; // This controls whether the game screen is active

  /**
   * Called once at the beginning of execution, put your size all in this method
   */
  public void settings() {
    // put your size call here
    size(600, 600);
  }

  /** 
   * Called once at the beginning of execution. Add initial set up values here i.e background, stroke, fill etc.
   */
  public void setup() {
    textSize(32);
    initializeGame();
  }

  /**
   * Called repeatedly, anything drawn to the screen goes here
   */
  public void draw() {
    background(210, 255, 173);  // Clear the background each frame
    
    if (isGameScreenActive) {
      drawWordleGrid();
      if (isGame1Over && !isGame1Victory) {
        fill(0);
        // Change y and set padding later
        text("You lose! The word was " + strTargetWord + "!", 65, 35);
        text(" Press 'R' to restart", 150, 75);
      }
      if (isGame1Over && isGame1Victory) {
        fill(0);
        text("You win! Press 'R' to continue", width / 2 - 150, height - 50);
      }
    } 
    else {
      drawInactiveState();
    }
    // System.out.println(isGameScreenActive);
  }
  
  public void drawWordleGrid() {
    for (int i = 0; i < intGridSizeY; i++) {
      for (int j = 0; j < intGridSizeX; j++) {
        fill(200);
        rect(j * 80 + (width - 380) / 2, i * 80 + 100, 60, 60);
        if (strGuesses[i] != null && j < strGuesses[i].length()) {
          char letter = strGuesses[i].charAt(j);
          fill(0);
          if (strTargetWord.charAt(j) == letter && i != intCurrentRow) {
            fill(0, 255, 0); // Correct letter and position
          } else if (strTargetWord.indexOf(letter) != -1 && i != intCurrentRow) {
            fill(255, 255, 0); // Correct letter but wrong position
          } else if (i != intCurrentRow) {
            fill(255, 0, 0); // Incorrect letter
          } else {
            fill(200);
          }
          rect(j * 80 + (width - 380) / 2, i * 80 + 100, 60, 60);
          fill(0);
          text(letter, j * 80 + (width - 380) / 2 + 20, i * 80 + 140);
        }
      }
    }
  }

  public void drawInactiveState() {
    fill(0);
    text("Press 'S' to start the game", width / 2 - 150, height / 2);
  }

  public void keyPressed() {
    // ONLY FOR DEBUG
    if ((key == 's' || key == 'S') && isGameScreenActive == false) {
      isGameScreenActive = true;
      if (isGameScreenActive) {
        initializeGame();
      }
    } else if (isGameScreenActive && !isGame1Over) {
      if (key >= 'a' && key <= 'z' && (strGuesses[intCurrentRow] == null || strGuesses[intCurrentRow].length() < intGridSizeX)) {
        if (strGuesses[intCurrentRow] == null) {
          strGuesses[intCurrentRow] = "";
        }
        strGuesses[intCurrentRow] += key;
      } else if (key == BACKSPACE && strGuesses[intCurrentRow] != null && strGuesses[intCurrentRow].length() > 0) {
        strGuesses[intCurrentRow] = strGuesses[intCurrentRow].substring(0, strGuesses[intCurrentRow].length() - 1);
      } else if (key == ENTER && strGuesses[intCurrentRow] != null && strGuesses[intCurrentRow].length() == intGridSizeX) {
        checkGuess();
        intCurrentRow++;
      }
    } 
    else if (isGameScreenActive && isGame1Over && (key == 'r' || key == 'R') && !isGame1Victory) {
      initializeGame();
    }
    else if (isGameScreenActive && isGame1Over && (key == 'r' || key == 'R') && isGame1Victory) {
      isGameScreenActive = false;
    }
  }

  public void checkGuess() {
    if (strGuesses[intCurrentRow].equals(strTargetWord)) {
      isGame1Over = true;
      isGame1Victory = true;
    } else if (intCurrentRow == intGridSizeY - 1) {
      isGame1Over = true;
      isGame1Victory = false;
    }
  }

  public void initializeGame() {
    isGame1Over = false;
    strGuesses = new String[intGridSizeY];
    intCurrentRow = 0;
    strTargetWord = strWordList[(int)(random(strWordList.length))]; // Randomly choose a target word
    println("Target Word: " + strTargetWord); // For debugging
  }
}
