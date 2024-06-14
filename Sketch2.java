import processing.core.PApplet;
import processing.core.PImage;

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

    float fltX, fltY, fltW, fltH;
    String label;
    
    boolean isOver = false;

    /**
     * Computes input x, y, w, h, and label values and localizes in terms of the class
     * Inputs assigned to class local variables for future use
     * 
     * @param fltX x-coordinate of the button
     * @param fltY y-coordinate of the button
     * @param fltW Width of the button
     * @param fltH Height of the button
     * @param label Text displayed on the button
     */
    Button(float fltX, float fltY, float fltW, float fltH, String label) {
      this.fltX = fltX;
      this.fltY = fltY;
      this.fltW = fltW;
      this.fltH = fltH;
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
      rect(fltX, fltY, fltW, fltH);
      fill(255);

      // Writing button text
      textAlign(CENTER, CENTER);
      text(label, fltX + fltW / 2, fltY + fltH / 2);
    }

    /**
     * Detects if the user has clicked the button
     * @return returns true if the cursor clicks the button. Else, it remains false
     */
    public boolean isOver() {
      return mouseX >= fltX && mouseX <= fltX + fltW && mouseY >= fltY && mouseY <= fltY + fltH;
    }
  }

  Button startButton, gameButton, infoButton, backButton;
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

  
  PImage playerForward, playerBackward, playerLeft, playerRight;
  PImage currentPlayerState;

  PImage exclamationMark;
  int intExclamationX, intExclamationY;
  int intExclamationW = 30;
  int intExclamationH = 50;
  float fltExclamAlpha;
  float fltFadeSpeed = 1.5f;

  float fltElevatorAlpha = 0f;
  boolean isElevatorOpen;

  boolean isSwitchButtonDisplayed;
  PImage setting1, setting2, setting3, setting4_1, setting4_2, setting5_1, setting5_2, setting6;
  boolean isScreenFaded;
  
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
    infoButton = new Button(width - 100, 10, 80, 50, "Info");
    backButton = new Button(width / 2 - 50, height / 2 + 100, 100, 50, "Back");
    playerForward = loadImage("images/NerdFace.png"); 
    playerBackward = loadImage("images/NerdFaceBack.png"); 
    playerLeft = loadImage("images/NerdFaceLeft.png"); 
    playerRight = loadImage("images/NerdFaceRight.png"); 
    
    setting1 = loadImage("images/BossRoom.png");
    setting2 = loadImage("images/PlankWalk.png");
    setting3 = loadImage("images/TopFloor.png");
    setting4_1 = loadImage("images/Floor2Closed.png");
    setting4_2 = loadImage("images/Floor2Open.png");
    setting5_1 = loadImage("images/Floor1Closed.png");
    setting5_2 = loadImage("images/Floor1Open.png");
    setting6 = loadImage("images/GroundFloor.png");

    exclamationMark = loadImage("images/exclamation_mark.png"); 
    exclamationMark.resize(55, 55);


    

  }

  public void draw() {
    // print(isCollidingMarker(100, 100));
    background(50);
    
    // HOME SCREEN
    if (intScreenNumber == 0) {
      introScreen();
    } 
    // SETTING 1 SCREEN
    else if (intScreenNumber == 1) {
      settingScreen1();
    } 
    // GAME 1 SCENE
    else if (intScreenNumber == 2) {
      gameScreen1();
    } 
    // TRANSFER 1 SCREEN
    else if (intScreenNumber == 3) {
      transferScreen1();
    } 
    // SETTING 2 SCREEN
    else if (intScreenNumber == 4) {
      settingScreen2();
    } 
    // GAME 2 SCREEN
    else if (intScreenNumber == 5) {
      gameScreen2();
    } 
    // SETTING 3 SCREEN
    else if (intScreenNumber == 6) {
      settingScreen3();
    } 
    // GAME 3 SCREEN
    else if (intScreenNumber == 7) {
      gameScreen3();
    } 
    // TRANSFER 2 SCREEN
    else if (intScreenNumber == 8) {
      transferScreen2();
    } 
    // END SCREEN
    else if (intScreenNumber == 9) {
      endingScreen();
    } 
    // INFO SCREEN
    else if (intScreenNumber == 10) {
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

  public void gameScreen1() {
    background(173, 210, 255);
    //drawWordleGrid();
    infoButton.isOver = infoButton.isOver();
    infoButton.display();
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
    
    background(210, 255, 173);
    drawWordleGrid();
    infoButton.isOver = infoButton.isOver();
    infoButton.display();
    if (isGameOver && !isGameVictory) {
      showLosePopup = true;
    }
    if (isGameOver && isGameVictory) {
      showWinPopup = true;
    }
    if (showPopup && !showWinPopup && !showLosePopup) {
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
    infoButton.isOver = infoButton.isOver();
    infoButton.display();
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
   * Displays the first BOSS office screen
   */

  public void settingScreen1() {
    background(200, 100, 100);
    // Setting1 background generation
    image(setting1, 0, 0);
    fill(255);
    textAlign(CENTER);
    textSize(32);
    text("Settings Screen 1", width / 2, height / 2);

    playerMovement();
    
    if (isCollidingElevator()){
      intScreenNumber = 2;
    }

    infoButton.isOver = infoButton.isOver();
    infoButton.display();
    if (showPopup) {
      drawPopup();
    }
  }

  /**
   * Displays the second environment screen (FLOOR 2)
   */

  public void settingScreen2() {
    background(200, 100, 100);
    // Setting1 background generation
    intExclamationX = 100;
    intExclamationY = 100;
    if (isGameVictory){
      image(setting4_2, 0, 0);
      isElevatorOpen = true;
    }
    if (!isGameVictory){
      image(setting4_1, 0, 0);
      displayExclamMark(100, 100);
    }
    
    playerMovement();
    
    
    if (isCollidingMarker(intExclamationX, intExclamationY) && !isGameVictory){
      isSwitchButtonDisplayed = true;
      drawGameInfoPopup();
    }

    if (isCollidingElevator() && isGameVictory){
      fadeOutElevator();
      
    }
    if (isScreenFaded){
      
      intScreenNumber = 6;
      resetSetting();
    }
    infoButton.isOver = infoButton.isOver();
    infoButton.display();
    if (showPopup) {
      drawPopup();
    }
  }

  /**
   * Displays the third environment screen (FLOOR 1)
   */
  public void settingScreen3() {
    intExclamationX = 100;
    intExclamationY = 100;
    if (isGameVictory){
      image(setting5_2, 0, 0);
    }
    if (!isGameVictory){
      image(setting5_1, 0, 0);
      displayExclamMark(100, 100);
    }
    playerMovement();
    
    
    infoButton.isOver = infoButton.isOver();
    infoButton.display();
    if (showPopup) {
      drawPopup();
    }
  }

  /**
   * Displays the fourth environment screen (FLOOR 0)
   */
  public void transferScreen1() {
    isElevatorOpen = true;
    image(setting3, 0, 0);
    
    playerMovement();
    infoButton.isOver = infoButton.isOver();
    infoButton.display();

    if (showPopup) {
      drawPopup();
    }
    if (isCollidingElevator()){
      fadeOutElevator();
      
    }
    if (isScreenFaded){
      
      intScreenNumber = 4;
      resetSetting();
    }
  }
  public void transferScreen2() {
    
    background(100, 100, 200);
    fill(255);
    textAlign(CENTER);
    textSize(32);
    text("Settings Screen 3", width / 2, height / 2);
    
    infoButton.isOver = infoButton.isOver();
    infoButton.display();
    if (showPopup) {
      drawPopup();
    }
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
          textSize(32);
          text(Character.toUpperCase(letter), j * 80 + (width - 380) / 2 + 20, i * 80 + 140);
        }
      }
    }
  }
  /**
   * Handles mouse pressed events.
   */
  public void mousePressed() {


    // Intro home button
    if (intScreenNumber == 0) {
      if (startButton.isOver()) {
        
        intScreenNumber = 3; // Change to Setting1 (Debug to transfer1)
        resetSetting();
      } 
    } 

    // Setting 2 Buttons
    if (intScreenNumber == 4 && isSwitchButtonDisplayed) {
      if (gameButton.isOver()) {
        initializeGame2();
        intScreenNumber = 5; // Change to Setting1
        isSwitchButtonDisplayed = false;
      } 
      
    } 

    // Game 2 buttons
    if (intScreenNumber == 5) {
      if (backButton.isOver() && showWinPopup) {
        // Upon game1 win, the user is directed back to the setting 1 screen
        intScreenNumber = 4; // Change to Game1
        showWinPopup = false;
      } 
      else if (backButton.isOver() && showLosePopup) {
        // Upon game1 loss, the user is directed to play the game again!
        initializeGame2();
        showLosePopup = false;
      } 
    }
    // if (intScreenNumber == 1) {
    //   if (gameButton.isOver()){
    //     // initializeGame();
    //     intScreenNumber = 2; // Change to Setting1
    //   } 
    // } 
    if (intScreenNumber > 0 && intScreenNumber < 9) {
      if (infoButton.isOver()) {
        showPopup = true;
      } 
      else if (showPopup && backButton.isOver()) {
        showPopup = false;
      }
    } 
    
  }

  /**
   * Handles key release events.
   */

  public void keyReleased(){
    if (keyCode == UP) {
      isUpPressed = false;
    }

    if (keyCode == DOWN) {
      isDownPressed = false;
    }

    if (keyCode == LEFT) {
      isLeftPressed = false;
    }

    if (keyCode == RIGHT) {
      isRightPressed = false;
    }
  }
  /**
   * Handles key pressed events.
   */
  public void keyPressed() {
    if (intScreenNumber >= 2 && intScreenNumber <= 6 && !isGameOver){
      if (key >= 'a' && key <= 'z' && (strGuesses[intCurrentRow] == null || strGuesses[intCurrentRow].length() < intGridSizeX) && !showPopup) {
        if (strGuesses[intCurrentRow] == null) {
          strGuesses[intCurrentRow] = "";
        }
        strGuesses[intCurrentRow] += Character.toUpperCase(key);
      } 
      else if (key == BACKSPACE && strGuesses[intCurrentRow] != null && strGuesses[intCurrentRow].length() > 0) {
        strGuesses[intCurrentRow] = strGuesses[intCurrentRow].substring(0, strGuesses[intCurrentRow].length() - 1);
      }

      else if (key == ENTER && strGuesses[intCurrentRow] != null && strGuesses[intCurrentRow].length() == intGridSizeX) {
        checkGuess();
        intCurrentRow++;
      }
    } 

    if (keyCode == UP) {
      isUpPressed = true;
    }
    if (keyCode == DOWN) {
      isDownPressed = true;
    }
    if (keyCode == LEFT) {
      isLeftPressed = true;
    }
    if (keyCode == RIGHT) {
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
  public void initializeGame2() {
    isGameOver = false;
    isGameVictory = false;
    strGuesses = new String[intGridSizeY];
    intCurrentRow = 0;
    strTargetWord = strWordList[(int) (random(strWordList.length))]; // Randomly choose a target word
    println("Target Word: " + strTargetWord); // For debugging
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
    if (isUpPressed && intPlayerY >= 0 + 40 && !isSwitchButtonDisplayed && !showPopup) {
      if (isCollidingElevator() && isElevatorOpen) {
        
      } 
      else {
        intPlayerY -= 3;
        currentPlayerState = playerBackward;
      }
    }
    
    if (isDownPressed && intPlayerY <= height - 10 - 80 && !isSwitchButtonDisplayed && !showPopup) {
      if (isCollidingElevator() && isElevatorOpen) {
        // Additional logic for the elevator if needed
      } 
      else {
        intPlayerY += 3;
        currentPlayerState = playerForward;
      }
    }
    
    if (isLeftPressed && intPlayerX >= 0 + 10 && !isSwitchButtonDisplayed && !showPopup) {
      if (isCollidingElevator() && isElevatorOpen) {
        // Additional logic for the elevator if needed
      } else {
        intPlayerX -= 4;
        currentPlayerState = playerLeft;
      }
    }
    
    if (isRightPressed && intPlayerX <= width - 10 - 50 && !isSwitchButtonDisplayed && !showPopup) {
      if (isCollidingElevator() && isElevatorOpen) {
        // Additional logic for the elevator if needed
      } else {
        intPlayerX += 4;
        currentPlayerState = playerRight;
      }
    }
    
    image(currentPlayerState, intPlayerX, intPlayerY);
    // fill(0, 255, 0);
    // ellipse(intPlayerX, intPlayerY, 50, 50);
  }
  /**
   * Resets player to initial position on the setting screen upon switching of setting screens
   */
  public void resetSetting(){
    if (intScreenNumber == 3){
      intPlayerX = 400;
      intPlayerY = 520;
    }
    if (intScreenNumber == 4){
      intPlayerX = 400;
      intPlayerY = 50;
    }
    if (intScreenNumber == 6){
      intPlayerX = 400;
      intPlayerY = 50;
    }
    // if (intScreenNumber == 4){
    //   intPlayerX = 400;
    //   intPlayerY = 50;
    // }
    
    currentPlayerState = playerForward;
    fltExclamAlpha = 0;
    fltElevatorAlpha = 0;
    isSwitchButtonDisplayed = false;
    isScreenFaded = false;
    isGameVictory = false;
    isGameOver = false;
    isElevatorOpen = false;
  }

  public void displayExclamMark(float intX, float initialY){
    

    if (fltExclamAlpha < 255.0) {
      fltExclamAlpha += fltFadeSpeed;
    }
  
    // Constrain alpha to not exceed 255
    fltExclamAlpha = constrain((int) fltExclamAlpha, 0, 255);
  
    // Apply the tint with the current alpha value
    tint(255, fltExclamAlpha);
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
  public boolean isCollidingElevator(){
    if (intPlayerY <= 50 && intPlayerX < (width / 2) + 40 && intPlayerX > (width / 2) - 40) {
      return true;
    } 
    else {
      return false;
    }
  }
  public void fadeOutElevator(){
    if (fltElevatorAlpha < 255.0) {
      fltElevatorAlpha += 1.5f;
      fill(0, fltElevatorAlpha);
      rect(0, 0, width, height);
      
    }
    else{
      background(0); // Ensure the screen is fully black after 3 seconds
      isScreenFaded = true;
    }
    
  }
}


