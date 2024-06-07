import java.util.Arrays;
import processing.core.PApplet;

public class Sketch2 extends PApplet {

  // Define button class
  class Button {
    float x, y, w, h;
    String label;
    int baseColor, highlightColor;
    boolean over = false;

    Button(float x, float y, float w, float h, String label) {
      this.x = x;
      this.y = y;
      this.w = w;
      this.h = h;
      this.label = label;
      baseColor = color(0, 102, 153); // Default color
      highlightColor = color(50, 150, 200); // Hover color
    }

    void display() {
      if (over) {
        fill(highlightColor); // Change to hover color
      } else {
        fill(baseColor); // Default color
      }
      rect(x, y, w, h);
      fill(255);
      textAlign(CENTER, CENTER);
      text(label, x + w / 2, y + h / 2);
    }

    boolean isOver() {
      return mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + h;
    }
  }

  Button startButton, settingsButton, gearButton, backButton;
  String screen = "home";
  boolean showPopup = false;
  boolean showWinPopup = false;
  boolean showLosePopup = false;

  int intGridSizeX = 5;
  int intGridSizeY = 6;
  String[] strWordList = {"stark", "reese", "crack", "nosey", "hitch", "rural", "craic", "ergot", "ouija"};
  String strTargetWord;
  String[] strGuesses;
  int intCurrentRow;
  boolean isGame1Over;
  boolean isGame1Victory = false;

  public static void main(String[] args) {
    PApplet.main("IntegratedGame");
  }

  public void settings() {
    size(800, 600);
  }

  public void setup() {
    textSize(32);
    startButton = new Button(width / 2 - 100, height / 2 - 50, 200, 50, "Start Game");
    settingsButton = new Button(width / 2 - 100, height / 2 + 50, 200, 50, "Settings");

    gearButton = new Button(width - 60, 10, 50, 50, "Gear");

    backButton = new Button(width / 2 - 50, height / 2 + 100, 100, 50, "Back");
  }

  public void draw() {
    background(50);
    if (screen.equals("home")) {
      homeScreen();
    } else if (screen.equals("game")) {
      gameScreen();
      if (showPopup) {
        drawPopup();
      }
      if (showWinPopup) {
        drawWinPopup();
      }
      if (showLosePopup) {
        drawLosePopup();
      }
    } else if (screen.equals("settings")) {
      settingsScreen();
    }
  }

  public void homeScreen() {
    textAlign(CENTER);
    fill(255);
    textSize(32);
    text("Game Title", width / 2, height / 3);
    startButton.over = startButton.isOver();
    startButton.display();
    settingsButton.over = settingsButton.isOver();
    settingsButton.display();
  }

  public void gameScreen() {
    background(210, 255, 173);
    drawWordleGrid();
    gearButton.over = gearButton.isOver();
    gearButton.display();
    if (isGame1Over && !isGame1Victory) {
      showLosePopup = true;
    }
    if (isGame1Over && isGame1Victory) {
      showWinPopup = true;
    }
  }

  public void drawPopup() {
    fill(0, 0, 0, 150);
    rect(50, 50, width - 100, height - 100);
    fill(255);
    textAlign(CENTER);
    textSize(24);
    text("Information Popup", width / 2, height / 2 - 50);
    text("This is some information for the user.", width / 2, height / 2);
    backButton.over = backButton.isOver();
    backButton.display();
  }

  public void drawWinPopup() {
    fill(0, 0, 0, 150);
    rect(50, 50, width - 100, height - 100);
    fill(255);
    textAlign(CENTER);
    textSize(24);
    text("You Win!", width / 2, height / 2 - 50);
    text("Congratulations!", width / 2, height / 2);
    backButton.over = backButton.isOver();
    backButton.display();
  }

  public void drawLosePopup() {
    fill(0, 0, 0, 150);
    rect(50, 50, width - 100, height - 100);
    fill(255);
    textAlign(CENTER);
    textSize(24);
    text("You Lose!", width / 2, height / 2 - 50);
    text("Better luck next time!", width / 2, height / 2);
    backButton.over = backButton.isOver();
    backButton.display();
  }

  public void settingsScreen() {
    background(200, 100, 100);
    fill(255);
    textAlign(CENTER);
    textSize(32);
    text("Settings", width / 2, height / 2);
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
          text(Character.toUpperCase(letter), j * 80 + (width - 380) / 2 + 20, i * 80 + 140);
        }
      }
    }
  }
  public void mousePressed() {
    if (screen.equals("home")) {
      if (startButton.isOver()) {
        screen = "game";
        initializeGame();
      } else if (settingsButton.isOver()) {
        screen = "settings";
      }
    } else if (screen.equals("game")) {
      if (gearButton.isOver()) {
        showPopup = true;
      } else if (showPopup && backButton.isOver()) {
        showPopup = false;
      }
      if (showWinPopup && backButton.isOver()) {
        // Change screen to another screen
        screen = "home";
        showWinPopup = false;
      }
      if (showLosePopup && backButton.isOver()) {
        // Restart the game
        initializeGame();
        showLosePopup = false;
      }
    }
  }

  public void keyPressed() {
    if (screen.equals("game") && !isGame1Over) {
      if (key >= 'a' && key <= 'z' && (strGuesses[intCurrentRow] == null || strGuesses[intCurrentRow].length() < intGridSizeX)) {
        if (strGuesses[intCurrentRow] == null) {
          strGuesses[intCurrentRow] = "";
        }
        strGuesses[intCurrentRow] += Character.toUpperCase(key);
      } else if (key == BACKSPACE && strGuesses[intCurrentRow] != null && strGuesses[intCurrentRow].length() > 0) {
        strGuesses[intCurrentRow] = strGuesses[intCurrentRow].substring(0, strGuesses[intCurrentRow].length() - 1);
      } else if (key == ENTER && strGuesses[intCurrentRow] != null && strGuesses[intCurrentRow].length() == intGridSizeX) {
        checkGuess();
        intCurrentRow++;
      }
    } else if (screen.equals("game") && isGame1Over && (key == 'r' || key == 'R')) {
      initializeGame();
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
    isGame1Victory = false;
    strGuesses = new String[intGridSizeY];
    intCurrentRow = 0;
    strTargetWord = strWordList[(int) (random(strWordList.length))]; // Randomly choose a target word
    println("Target Word: " + strTargetWord); // For debugging
    println("Current Guesses: " + Arrays.deepToString(strGuesses)); // Print the current guesses as a readable string
  }
}

