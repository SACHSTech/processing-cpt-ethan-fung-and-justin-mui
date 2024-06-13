import java.util.Arrays;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

/**
* The program Sketch2.java creates the basic skeleton of the game.
* The program organizes the various screens in the game and establishes
* the proper flow and communication, typically through buttons. This includes
* changing maps based on if the user wins or loses mini-games, and typical in-game
* objects that guide the flow of the game (exclamation marks, open/closed doors, etc.) 
* Furthermore, the program includes the first of 3 mini-games, which is a game very 
* similar to the New York Time's WORDLE game. Player movement between stages is also 
* established in this program, including with collision.
* @author: E. Fung
*
*/

public class Sketch2 extends PApplet {

  // Define button class
  class Button {
    float x, y, w, h;
    String label;
    
    boolean isOver = false;

    /**
     * Computes input x, y, w, h, and label values and localizes in terms of the class
     * Inputs assigned to class local variables for future use
     * 
     * @param x x-coordinate of the button
     * @param y y-coordinate of the button
     * @param w Width of the button
     * @param h Height of the button
     * @param label Text displayed on the button
     */
    Button(float x, float y, float w, float h, String label) {
      this.x = x;
      this.y = y;
      this.w = w;
      this.h = h;
      this.label = label;
      
    }

    void display() {
      // Checks if button is still active
      if (isOver) {
        fill(50, 150, 200); // Change to hover color
      } else {
        fill(0, 102, 153); // Default color
      }
      // Drawing button rectangle
      rect(x, y, w, h);
      fill(255);

      // Writing button text
      textAlign(CENTER, CENTER);
      text(label, x + w / 2, y + h / 2);
    }

    /**
     * Detects if the user has clicked the button
     * @return returns true if the cursor clicks the button. Else, it remains false
     */
    public boolean isOver() {
      return mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + h;
    }
  }

  Button startButton, gameButton, gearButton, backButton;
  int intScreenNumber = 0; // 0 = Intro Screen, 1 = Setting1, 2 = Game1, 3 = Setting2, 4 = Game2, 5 = Setting3, 6 = Game3, 7 = Ending Screen, 8 = Information screen
  boolean showPopup = false; // screen specific pop up info screen
  boolean showWinPopup = false;
  boolean showLosePopup = false;

  int intGridSizeX = 5;
  int intGridSizeY = 6;
  String[] strWordList = {"STARK", "REESE", "CRACK", "NOSEY", "HITCH", "RURAL", "CRAIC", "ERGOT", "OUIJA"};
  String strTargetWord;
  String[] strGuesses;
  int intCurrentRow;
  boolean isGameOver;
  boolean isGameVictory = false;
  // boolean isSettingGameChanged = false;

  int intPlayerX, intPlayerY;
  
  boolean isUpPressed = false;
  boolean isDownPressed = false;
  boolean isLeftPressed = false;
  boolean isRightPressed = false;

  PImage setting1, setting2, setting3;
  PImage playerForward, playerBackward, playerLeft, playerRight;
  PImage currentPlayerState;

  PImage exclamationMark;
  int intExclamationX, intExclamationY;
  int intExclamationW = 30;
  int intExclamationH = 50;
  float fltAlpha;
  float fltFadeSpeed = 1.5f;

  boolean isSwitchButtonDisplayed;

  public static void main(String[] args) {
    PApplet.main("Sketch2");
  }

  public void settings() {
    size(800, 600);
  }

  public void setup() {
    textSize(32);
    startButton = new Button(width / 2 - 100, height / 2 - 50, 200, 50, "Start Game");
    gameButton = new Button(width / 2 - 100, height / 2 + 50, 200, 50, "Settings");
    gearButton = new Button(width - 60, 10, 50, 50, "Gear");
    backButton = new Button(width / 2 - 50, height / 2 + 100, 100, 50, "Back");
    playerForward = loadImage("images/NerdFace.png"); 
    playerBackward = loadImage("images/NerdFaceBack.png"); 
    playerLeft = loadImage("images/NerdFaceLeft.png"); 
    playerRight = loadImage("images/NerdFaceRight.png"); 
    setting1 = loadImage("images/office.png");

    exclamationMark = loadImage("images/exclamation_mark.png"); 
    exclamationMark.resize(55, 55);
    

  }

  public void draw() {
    // print(isCollidingMarker(100, 100));
    background(50);
    
    if (intScreenNumber == 0) {
      introScreen();
    } else if (intScreenNumber == 1) {
      settingsScreen1();
    } else if (intScreenNumber == 2) {
      gameScreen1();
    } else if (intScreenNumber == 3) {
      settingsScreen2();
    } else if (intScreenNumber == 4) {
      gameScreen2();
    } else if (intScreenNumber == 5) {
      settingsScreen3();
    } else if (intScreenNumber == 6) {
      gameScreen3();
    } else if (intScreenNumber == 7) {
      endingScreen();
    } else if (intScreenNumber == 8) {
      informationScreen();
    }
  }
  
  /**
   * Displays the start home screen
   */
  public void introScreen() {
    textAlign(CENTER);
    fill(255);
    textSize(32);
    text("ESCAPE THE NEW YORK TIMES", width / 2, height / 3);
    startButton.isOver = startButton.isOver();
    startButton.display();
    
  }

  /**
   * Displays the first game screen (ORIGINAL WORDLE)
   */

  public void gameScreen1() {
    background(210, 255, 173);
    drawWordleGrid();
    gearButton.isOver = gearButton.isOver();
    gearButton.display();
    if (isGameOver && !isGameVictory) {
      showLosePopup = true;
    }
    if (isGameOver && isGameVictory) {
      showWinPopup = true;
    }
    if (showPopup) {
      drawPopup();
    }
    if (showWinPopup) {
      drawWinPopup();
    }
    if (showLosePopup) {
      drawLosePopup();
    }
  }
  /**
   * Displays the second game screen (WORDLE CONNECTIONS)
   */
  public void gameScreen2() {
    background(173, 210, 255);
    //drawWordleGrid();
    gearButton.isOver = gearButton.isOver();
    gearButton.display();
    if (isGameOver && !isGameVictory) {
      showLosePopup = true;
    }
    if (isGameOver && isGameVictory) {
      showWinPopup = true;
    }
    if (showPopup) {
      drawPopup();
    }
    if (showWinPopup) {
      drawWinPopup();
    }
    if (showLosePopup) {
      drawLosePopup();
    }
  }
  /**
   * Displays the third game screen
   */
  public void gameScreen3() {
    background(255, 210, 173);
    //drawWordleGrid();
    gearButton.isOver = gearButton.isOver();
    gearButton.display();
    if (isGameOver && !isGameVictory) {
      showLosePopup = true;
    }
    if (isGameOver && isGameVictory) {
      showWinPopup = true;
    }
    if (showPopup) {
      drawPopup();
    }
    if (showWinPopup) {
      drawWinPopup();
    }
    if (showLosePopup) {
      drawLosePopup();
    }
  }

  /**
   * Draws a popup window with information.
   */

  public void drawPopup() {
    fill(0, 0, 0, 150);
    rect(50, 50, width - 100, height - 100);
    fill(255);
    textAlign(CENTER);
    textSize(24);
    text("Information Popup", width / 2, height / 2 - 50);
    text("This is some information for the user.", width / 2, height / 2);
    backButton.isOver = backButton.isOver();
    backButton.display();
  }
  /**
   * Draws a popup window when the player wins.
   */
  public void drawWinPopup() {
    fill(0, 0, 0, 150);
    rect(50, 50, width - 100, height - 100);
    fill(255);
    textAlign(CENTER);
    textSize(24);
    text("You Win!", width / 2, height / 2 - 50);
    text("Congratulations!", width / 2, height / 2);
    backButton.isOver = backButton.isOver();
    backButton.display();
  }
  /**
   * Draws a popup window when the player loses.
   */
  public void drawLosePopup() {
    fill(0, 0, 0, 150);
    rect(50, 50, width - 100, height - 100);
    fill(255);
    textAlign(CENTER);
    textSize(24);
    text("You Lose!", width / 2, height / 2 - 50);
    text("Better luck next time!", width / 2, height / 2);
    backButton.isOver = backButton.isOver();
    backButton.display();
  }

  public void drawGameInfoPopup() {
    fill(0, 0, 0, 150);
    rect(50, 50, width - 100, height - 100);
    fill(255);
    textAlign(CENTER);
    textSize(24);
    text("Complete the Wordle to Unlock the Elevator", width / 2, height / 2 - 50);
    text("It takes one of extreme intellect to join the New York Times", width / 2, height / 2);
    text("It takes one of extreme optimism to leave the New York Times", width / 2, height / 2 + 50);
    gameButton.isOver = gameButton.isOver();
    gameButton.display();
  }

  /**
   * Displays the first environment screen (FLOOR 3)
   */
  public void settingsScreen0() {
    background(200, 100, 100);
    // Setting1 background generation
    image(setting1, 0, 0);
    fill(255);
    textAlign(CENTER);
    textSize(32);
    text("Settings Screen 1", width / 2, height / 2);

    playerMovement();
    gameButton.isOver = gameButton.isOver();
    gameButton.display();
  }

  /**
   * Displays the second environment screen (FLOOR 2)
   */
  public void settingsScreen1() {
    background(200, 100, 100);
    // Setting1 background generation
    intExclamationX = 100;
    intExclamationY = 100;
    if (isGameVictory){
      image(setting1, 0, 0);
    }
    if (!isGameVictory){
      image(setting1, 0, 0);
      displayExclamMark(100, 100);
    }
    
    

    playerMovement();
    if (isCollidingMarker(intExclamationX, intExclamationY)){
      isSwitchButtonDisplayed = true;
    }
    //if player hits bush or smt
    if (isSwitchButtonDisplayed){
      drawGameInfoPopup();
    }
    
  }

  /**
   * Displays the third environment screen (FLOOR 1)
   */
  public void settingsScreen2() {
    background(100, 200, 100);
    fill(255);
    textAlign(CENTER);
    textSize(32);
    text("Settings Screen 2", width / 2, height / 2);
    backButton.isOver = backButton.isOver();
    backButton.display();
  }

  /**
   * Displays the fourth environment screen (FLOOR 0)
   */
  public void settingsScreen3() {
    background(100, 100, 200);
    fill(255);
    textAlign(CENTER);
    textSize(32);
    text("Settings Screen 3", width / 2, height / 2);
    backButton.isOver = backButton.isOver();
    backButton.display();
  }

  /**
   * Displays the end screen of the game upon completion
   */
  public void endingScreen() {
    background(50);
    fill(255);
    textAlign(CENTER);
    textSize(32);
    text("Ending Screen", width / 2, height / 2);
  }

  /**
   * Displays the screen specific info screen to help player throughout gameplay (FLOOR 2)
   */
  public void informationScreen() {
    fill(0, 0, 0, 150);
    rect(50, 50, width - 100, height - 100);
    fill(255);
    textAlign(CENTER);
    textSize(24);
    text("Information Screen", width / 2, height / 2 - 50);
    text("This is the information screen.", width / 2, height / 2);
    backButton.isOver = backButton.isOver();
    backButton.display();
  }
  /**
   * Draws the Wordle-like grid and handles Game1 logic.
   */

  public void drawWordleGrid() {
    for (int i = 0; i < intGridSizeY; i++) {
      for (int j = 0; j < intGridSizeX; j++) {
        fill(200);
        rect(j * 80 + (width - 380) /2, i * 80 + 100, 60, 60);
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
  /**
   * Handles mouse pressed events.
   */
  public void mousePressed() {

    // Toggling Intro, Setting1, Game1 screens
    if (intScreenNumber == 0) {
      if (startButton.isOver()) {
        resetSetting();
        intScreenNumber = 1; // Change to Setting1
      } 
    } 
    if (intScreenNumber == 1 && isSwitchButtonDisplayed) {
      if (gameButton.isOver()) {
        initializeGame1();
        intScreenNumber = 2; // Change to Setting1
      } 
    } 
    if (intScreenNumber == 2) {
      if (backButton.isOver() && showWinPopup) {
        // Upon game1 win, the user is directed back to the setting 1 screen
        intScreenNumber = 1; // Change to Game1
        showWinPopup = false;
      } 
      else if (backButton.isOver() && showLosePopup) {
        // Upon game1 loss, the user is directed to play the game again!
        initializeGame1();
        showLosePopup = false;
      } 
    }
    // if (intScreenNumber == 1) {
    //   if (gameButton.isOver()) {
    //     // initializeGame();
    //     intScreenNumber = 2; // Change to Setting1
    //   } 
    // } 
    // else if (intScreenNumber >= 2 && intScreenNumber <= 6) {
    //   if (gearButton.isOver()) {
    //     showPopup = true;
    //   } else if (showPopup && backButton.isOver()) {
    //     showPopup = false;
    //   }
    //   if ((showWinPopup || showLosePopup) && backButton.isOver()) {
    //     intScreenNumber = 0; // Change to Intro Screen
    //     initializeGame();
    //     showWinPopup = false;
    //     showLosePopup = false;
    //   }
    // } 
    // else if (intScreenNumber == 1 || intScreenNumber == 7 || intScreenNumber == 8) {
    //   if (backButton.isOver()) {
    //     intScreenNumber = 0; // Change to Intro Screen
    //   }
    // }
  }

  /**
   * Handles key release events.
   */

  public void keyReleased(){
    if (keyCode == UP) {
      isUpPressed = false;
    }
    else if (keyCode == DOWN) {
      isDownPressed = false;
    }
    else if (keyCode == LEFT) {
      isLeftPressed = false;
    }
    else if (keyCode == RIGHT) {
      isRightPressed = false;
    }
  }
  /**
   * Handles key pressed events.
   */
  public void keyPressed() {
    if (intScreenNumber >= 2 && intScreenNumber <= 6 && !isGameOver) {
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
    } else if ((intScreenNumber >= 2 && intScreenNumber <= 6) && isGameOver && (key == 'r' || key == 'R')) {
      initializeGame1();
    }

    if (keyCode == UP) {
      isUpPressed = true;
    }
    else if (keyCode == DOWN) {
      isDownPressed = true;
    }
    else if (keyCode == LEFT) {
      isLeftPressed = true;
    }
    else if (keyCode == RIGHT) {
      isRightPressed = true;
    }
  }
  /**
   * Checks if the guesses for Game1 are correct answers
   */
  public void checkGuess() {
    if (strGuesses[intCurrentRow].equals(strTargetWord)) {
      isGameOver = true;
      isGameVictory = true;
    } else if (intCurrentRow == intGridSizeY - 1) {
      isGameOver = true;
      isGameVictory = false;
    }
  }

  /**
   * Initializes Game 1 (Original Wordle)
   */
  public void initializeGame1() {
    isGameOver = false;
    isGameVictory = false;
    strGuesses = new String[intGridSizeY];
    intCurrentRow = 0;
    strTargetWord = strWordList[(int) (random(strWordList.length))]; // Randomly choose a target word
    // println("Target Word: " + strTargetWord); // For debugging
    // println("Current Guesses: " + Arrays.deepToString(strGuesses)); // Print the current guesses as a readable string
    showLosePopup = false;
    showWinPopup = false;
    showPopup = false;
    textSize(32);
  }

  /**
   * Player movement and sprite display
   */
  public void playerMovement(){
    if (isUpPressed && intPlayerY >= 0 + 10){
      intPlayerY -=3;      
      currentPlayerState = playerBackward;
    }
    if (isDownPressed && intPlayerY <= height - 10 - 80){
      intPlayerY += 3;    
      currentPlayerState = playerForward;
    }
    if (isLeftPressed && intPlayerX >= 0 + 10){
      intPlayerX -= 4;
      currentPlayerState = playerLeft;
    }
    if (isRightPressed && intPlayerX <= width - 10 - 50){
      intPlayerX += 4;
      currentPlayerState = playerRight;
    }
    image(currentPlayerState, intPlayerX, intPlayerY);
    // fill(0, 255, 0);
    // ellipse(intPlayerX, intPlayerY, 50, 50);
  }
  /**
   * Resets player to initial position on the setting screen upon switching of setting screens
   */
  public void resetSetting(){
    
    intPlayerX = 300;
    intPlayerY = 300;
    currentPlayerState = playerForward;
    fltAlpha = 0;
    isSwitchButtonDisplayed = false;
  }

  public void displayExclamMark(float intX, float initialY){
    

    if (fltAlpha < 255.0) {
      fltAlpha += fltFadeSpeed;
    }
  
    // Constrain alpha to not exceed 255
    fltAlpha = constrain((int) fltAlpha, 0, 255);
  
    // Apply the tint with the current alpha value
    tint(255, fltAlpha);
    float bobbingY = initialY + 20 * sin((float)(TWO_PI * 0.4 * millis() / 1000.0));
    image(exclamationMark, intX, bobbingY);
    noTint();
  }
  public boolean isCollidingMarker(int intMarkerX, int intMarkerY){
    
    if (intPlayerX < intMarkerX + 55 && intPlayerX + intExclamationW > intMarkerX && 
        intPlayerY < intMarkerY + 55 && intPlayerY + intExclamationH > intMarkerY) {
      return true;
    } 
    else {
      return false;
    }
  }
}

