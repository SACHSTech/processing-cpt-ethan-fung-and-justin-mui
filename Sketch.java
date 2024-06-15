import java.util.ArrayList;
import java.util.Arrays;

import processing.core.PApplet;
import processing.core.PImage;

/**
* The program Sketch.java runs the "ESCAPE THE NEW YORK TIMES" game
* 
* @author: E. Fung
* @author: J. Mui
*/

public class Sketch extends PApplet {

  // This class creates similar buttons for future use
  class Button {
    // Initializing class specific variables
    float fltX, fltY, fltW, fltH;
    String strLabel;
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
    Button(float fltX, float fltY, float fltW, float fltH, String strLabel) {
      this.fltX = fltX;
      this.fltY = fltY;
      this.fltW = fltW;
      this.fltH = fltH;
      this.strLabel = strLabel;
    }
    /**
     * Displays button and changes colour based on player input
     */
    void display() {
      // Checks if button is still active
      if (isOver) {
        fill(50, 150, 200); // Change to hover color
      } else {
        fill(0, 102, 153); // Default color
      }
      if (strLabel == "Change Difficulty"){
        if (intWordleDifficulty == 0){
          fill(0, 255, 0);
        }
        if (intWordleDifficulty == 1){
          fill(255, 255, 0);
        }
        if (intWordleDifficulty == 2){
          fill(255, 0, 0);
        }
      }
      // Drawing button rectangle
      rect(fltX, fltY, fltW, fltH);
      if (strLabel == "Change Difficulty"){
        fill(0);
      }
      else{
        fill(255);
      }
      // Writing button text
      textAlign(CENTER, CENTER);
      text(strLabel, fltX + fltW / 2, fltY + fltH / 2);
    }
    /**
     * Detects if the user has clicked the button
     * @return returns true if the cursor clicks the button. Else, it remains false
     */
    public boolean isOver() {
      return mouseX >= fltX && mouseX <= fltX + fltW && mouseY >= fltY && mouseY <= fltY + fltH;
    }
  }
  
  // INITIALIZING BUTTON AND POP UP VARIABLES
  Button highscoreButton, difficultyButton, playAgainButton, startButton, gameButton, infoButton, backButton;
  int intScreenNumber = 0; // 0 = Intro Screen, 1 = Setting1, 2 = Game1, 3 = Setting2, 4 = Game2, 5 = Setting3, 6 = Game3, 7 = Ending Screen, 8 = Information screen
  boolean showPopup = false; // screen specific pop up info screen
  boolean showWinPopup = false;
  boolean showLosePopup = false;

  // INITIALIZING BACKGROUND IMAGE VARIABLES
  boolean isSwitchButtonDisplayed;
  PImage setting0, setting1, setting2, setting3, setting4_1, setting4_2, setting5_1, setting5_2, setting6, setting7;
  boolean isScreenFaded;

  // INITIALIZING BROKEN BRIDGE GAME VARIABLES
  boolean plank1Show = true;
  boolean plank2Show = true;
  boolean plank3Show = true;
  PImage woodenPlank;

  // INITIALIZING WORDLE GAME VARIABLES
  int intGridSizeX = 5;
  int intGridSizeY = 6;
  String[][] strWordList = {
    {"HITCH", "RURAL", "CRAIC", "ERGOT", "OUIJA"},
    {"CREST", "SCHWA", "GLITZ", "ZEBEC", "PSYCH"},
    {"LYMPH", "WALTZ", "XYLEM", "GLYPH", "NYMPH"}
  };
  String strTargetWord;
  String[] strGuesses;
  int intCurrentRow;
  boolean isGameOver;
  boolean isGameVictory = false;
  int intWordleDifficulty = 0;

  // INITIALIZING CONNECTIONS GAME VARIABLES
  String[][] incorrectGroups;
  String[][] correctGroups;

  ArrayList<String> selectedWords = new ArrayList<>();
  boolean[][] selectedBoxes = new boolean[4][4];
  boolean[][] solvedGroups = new boolean[4][4];
  String message = "";
  int lives = 4;

  // INITIALIZING PLAYER CHARACTER GENERATION VARIABLES
  int intPlayerX, intPlayerY;
  
  boolean isUpPressed = false;
  boolean isDownPressed = false;
  boolean isLeftPressed = false;
  boolean isRightPressed = false;
  
  PImage playerForward, playerBackward, playerLeft, playerRight;
  PImage currentPlayerState;

  // INITIALIZING EXCLAMATION MARK VARIABLES
  PImage exclamationMark;
  int intExclamationX, intExclamationY;
  int intExclamationW = 30;
  int intExclamationH = 50;
  float fltExclamAlpha;
  float fltFadeSpeed = 5f;

  // INITIALIZING ELAVATOR MARK VARIABLES
  float fltElevatorAlpha = 0f;
  boolean isElevatorOpen;

  // INITIALIZING DESK VARIABLES
  int intDeskX = 315; // X position of the hitbox
  int intDeskY = 230; // Y position of the hitbox
  int intDeskWidth = 175; // Width of the hitbox
  int intDeskHeight = 135; // Height of the hitbox
  boolean isCollidingDesk;

  // INITIALIZING IN-GAME CLOCK VARIABLES
  int intStartTime;
  String strTime = "";

  // HIGHSCORE VARIABLES
  ArrayList<String> strHighScores = new ArrayList<>();
  ArrayList<Integer> intHighScoreDifficulty = new ArrayList<>();
  boolean[] isBestInCategory = new boolean[5];

  public void settings() {
    // size of screen
    size(800, 600);
  }

  public void setup() {
    // initializing UI buttons
    textSize(26);
    startButton = new Button(width / 2 - 80, height / 2, 200, 50, "Start Game");
    difficultyButton = new Button(width / 2 - 102, 360, 240, 50, "Change Difficulty");
    highscoreButton = new Button(width / 2 - 102, 370, 240, 50, "Highscores");
    gameButton = new Button(width / 2 - 100, height / 2 + 90, 200, 50, "Play");
    infoButton = new Button(width - 100, 10, 80, 50, "INFO");
    backButton = new Button(630, height / 2 + 184, 100, 50, "Back");
    playAgainButton = new Button(width / 2 - 120, height / 2 + 130, 200, 60, "Play Again!");

    // initializing player images
    playerForward = loadImage("images/NerdFace.png"); 
    playerBackward = loadImage("images/NerdFaceBack.png"); 
    playerLeft = loadImage("images/NerdFaceLeft.png"); 
    playerRight = loadImage("images/NerdFaceRight.png"); 
    
    // initializing background images
    setting0 = loadImage("images/IntroScreen.png");
    setting1 = loadImage("images/BossRoom.png");
    setting2 = loadImage("images/PlankWalk.png");
    setting3 = loadImage("images/TopFloor.png");
    setting4_1 = loadImage("images/Floor2Closed.png");
    setting4_2 = loadImage("images/Floor2Open.png");
    setting5_1 = loadImage("images/Floor1Closed.png");
    setting5_2 = loadImage("images/Floor1Open.png");
    setting6 = loadImage("images/GroundFloor.png");
    setting7 = loadImage("images/OutroScreen.png");

    // initializing exclamation mark image
    exclamationMark = loadImage("images/exclamation_mark.png"); 
    exclamationMark.resize(55, 55);

    // Initializes and randomizes connection game answers
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
    // initializing wooden plank image
    woodenPlank = loadImage("images/WoodenPlank.png");
    
    // Initializing filler values for highscore
    strHighScores.add("00h:00m:20s");
    strHighScores.add("00h:00m:50s");
    strHighScores.add("00h:00m:50s");
    strHighScores.add("00h:01m:20s");
    strHighScores.add("00h:02m:15s");
    intHighScoreDifficulty.add(0);
    intHighScoreDifficulty.add(0);
    intHighScoreDifficulty.add(1);
    intHighScoreDifficulty.add(2);
    intHighScoreDifficulty.add(2);

    for (int i = 0; i < 5; i++){
      intHighScoreDifficulty.add(-1);
      isBestInCategory[i] = false;
    }
  }
  
  public void draw() {
    background(50);
    // HOME SCREEN
    if (intScreenNumber == 0) {
      textSize(26);
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
    // ELAPSED TIME CLOCK
    if (intScreenNumber != 0 && intScreenNumber != 9){
      displayElapsedTime();
    }
  }
  // ----------------INTRO SCREEN------------------------
  /**
   * Displays the start home screen
   */
  public void introScreen(){
    // Displays intro screen
    image(setting0, 0, 0);
    
    // start game button
    startButton.isOver = startButton.isOver();
    startButton.display();
    
    // difficulty toggle button
    difficultyButton.isOver = difficultyButton.isOver();
    difficultyButton.display();
    // highscore button
    highscoreButton.isOver = highscoreButton.isOver();
    highscoreButton.display();
    // shows highscore screen
    if (showPopup) {
      drawPopup();
    }
  }
  // ----------------GAME SCREENS------------------------
  /**
   * Displays Game 1 Screen (Broken Bridge)
   */
  public void gameScreen1() {
    image(setting2, 0, 0);

    if (plank1Show == true){
      image(woodenPlank, 455, 401);
    }
    if (plank2Show == true){
      image(woodenPlank,455, 221);
    }
    if (plank3Show == true){
      image(woodenPlank, 278, 43);
    }
    playerMovementPlankWalk();
    // extra info button
    infoButton.isOver = infoButton.isOver();
    infoButton.display();
    if (showPopup) {
      drawPopup();
    }
    // Plays scene change animation when player is in contact with skybridge door
    if (intPlayerY <= 50) {
      intScreenNumber = 3; // Transfer 1
      resetSetting();
    } 
  }
  /**
   * Displays Game 2 Screen (WORDLE)
   */
  public void gameScreen2() {
    // background colour
    background(210, 255, 173);

    // draws wordle guesses, boxes, colours
    drawWordleGrid();

    // extra info button
    infoButton.isOver = infoButton.isOver();
    infoButton.display();

    // Shows win or lose pop-up after game finishes
    if (isGameOver && !isGameVictory) {
      showLosePopup = true;
    }
    if (isGameOver && isGameVictory) {
      showWinPopup = true;
    }
    if (showWinPopup) {
      drawWinPopup();
    }
    if (showLosePopup) {
      drawLosePopup();
      textSize(24);
      fill(255);
      textAlign(CENTER);
      text("The word was: " + strTargetWord, width / 2, height / 2 + 50);
    }
    // Extra info pop-up
    if (showPopup && !showWinPopup && !showLosePopup) {
      drawPopup();
    }
    // Title of Mini-game
    textSize(70);
    fill(0);
    textAlign(CENTER);
    text("WORDLE", width / 2, 70);
  }

  /**
   * Displays Game 3 Screen (Connections)
   */

  public void gameScreen3() {
    background(255, 210, 173);
    ConnectionsGameScreen();
    
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
  // ----------------SETTING SCREENS------------------------
  /**
   * Displays setting 1 screen (BOSS office)
   */
  public void settingScreen1() {
    // Setting1 background generation
    image(setting1, 0, 0);
    // player movement is called here
    playerMovement();

    // extra info button
    infoButton.isOver = infoButton.isOver();
    infoButton.display();
    // extra info pop-up
    if (showPopup) {
      drawPopup();
    }
    // Plays scene change animation when player is in contact with skybridge door
    if (intPlayerY <= 50 && intPlayerX < (width / 2) + 60 && intPlayerX > (width / 2) - 60) {
      isElevatorOpen = true;
      fadeOutElevator();
    } 
    if (isScreenFaded){
      intScreenNumber = 2; // GAME 1
    }
  }
  /**
   * Displays setting 2 screen (Floor 2 office)
   */
  public void settingScreen2() {
    // Setting 2 background generation + exclamation mark generation
    intExclamationX = 440;
    intExclamationY = 280;
    // Open elevator after player wins game, close elevator before player wins game
    // player movement is called here
    if (isGameVictory){
      image(setting4_2, 0, 0);
      playerMovement();
      isElevatorOpen = true;
    }
    if (!isGameVictory){
      image(setting4_1, 0, 0);
      playerMovement();
      displayExclamMark(intExclamationX, intExclamationY);
    }
    // extra info button
    infoButton.isOver = infoButton.isOver();
    infoButton.display();
    // extra info pop-up
    if (showPopup && !isSwitchButtonDisplayed) {
      drawPopup();
    }
    // Takes player to game on collision with desk
    if (isCollidingDesk && !isGameVictory){
      isSwitchButtonDisplayed = true;
      drawGameInfoPopup();
    }
    // Scene animation to next scene upon colliding with open elevator
    if (isCollidingElevator() && isGameVictory){
      fadeOutElevator();
    }
    if (isScreenFaded){
      intScreenNumber = 6;
      resetSetting();
    }
    
  }

  /**
   * Displays setting 3 screen (floor 1 office)
   */
  public void settingScreen3() {
    
    // Setting 3 background generation + exclamation mark generation
    intExclamationX = 440;
    intExclamationY = 280;
    // Open elevator after player wins game, close elevator before player wins game
    // player movement is called here
    if (isGameVictory){
      image(setting5_2, 0, 0);
      playerMovement();
      isElevatorOpen = true;
    }
    if (!isGameVictory){
      image(setting5_1, 0, 0);
      playerMovement();
      displayExclamMark(intExclamationX, intExclamationY);
    }
    // extra info button
    infoButton.isOver = infoButton.isOver();
    infoButton.display();
    // extra info pop-up
    if (showPopup && !isSwitchButtonDisplayed) {
      drawPopup();
    }
    // Takes player to game on collision with desk
    if (isCollidingDesk && !isGameVictory){
      isSwitchButtonDisplayed = true;
      drawGameInfoPopup();
    }
    // Scene animation to next scene upon colliding with open elevator
    if (isCollidingElevator() && isGameVictory){
      fadeOutElevator();
    }
    if (isScreenFaded){
      intScreenNumber = 8;
      resetSetting();
    }
    
  }
  /**
   * Resets player to initial position on the setting screen upon switching of setting screens
   */
  public void resetSetting(){
    // Initializing starting player character position for specific scenes
    if (intScreenNumber == 1 || intScreenNumber == 2 || intScreenNumber == 3){
      intPlayerX = 380;
      intPlayerY = 520;
    }
    if (intScreenNumber == 4 || intScreenNumber == 6 || intScreenNumber == 8){
      intPlayerX = 400;
      intPlayerY = 50;
    }
    // initializing variables
    currentPlayerState = playerForward;
    fltExclamAlpha = 0;
    fltElevatorAlpha = 0;
    isSwitchButtonDisplayed = false;
    isScreenFaded = false;
    isGameVictory = false;
    isGameOver = false;
    isElevatorOpen = false;
  }
  // ----------------TRANSFER SCREENS------------------------
  /**
   * Displays transfer 1 screen (top floor office)
   */
  public void transferScreen1() {
    // Opens elevator as there is no game
    isElevatorOpen = true;
    // Transfer 1 background generation
    image(setting3, 0, 0);
    // player movement is called here
    playerMovement();
    // extra info button
    infoButton.isOver = infoButton.isOver();
    infoButton.display();
    // extra info pop-up
    if (showPopup) {
      drawPopup();
    }
    // Scene animation to next scene upon colliding with open elevator
    if (isCollidingElevator()){
      fadeOutElevator();
      
    }
    if (isScreenFaded){
      intScreenNumber = 4;
      resetSetting();
    }
  }
  /**
   * Displays transfer 2 screen (ground floor office)
   */
  public void transferScreen2() {
    // Transfer 2 background generation
    image(setting6, 0, 0);
    // extra info button
    infoButton.isOver = infoButton.isOver();
    infoButton.display();
    // extra info pop-up
    if (showPopup) {
      drawPopup();
    }
    // Scene animation to next scene upon colliding with open elevator; player will also disappear 
    if (intPlayerY >= 500 && intPlayerX < (width / 2) + 40 && intPlayerX > (width / 2) - 40) {
      isElevatorOpen = true;
      fadeOutElevator();
    } 
    else{
      // player movement is called here
      playerMovement();
    }
    // Changes scene when animation is finished playing
    if (isScreenFaded){
      intScreenNumber = 9;
      resetSetting();      
    }
  }
  // --------------------------ENDING SCREEN----------------------------
  /**
   * Displays the end screen of the game 
   */
  public void endingScreen() {
    // Writing ending text
    image(setting7, 0, 0);
    fill(0);
    textAlign(CENTER);
    textSize(32);
    text(strTime, 450, 110);
    // Play Again button
    playAgainButton.isOver = playAgainButton.isOver();
    playAgainButton.display();
  }
  // ----------------POP-UP SCREENS------------------------

  /**
   * Draws a popup window with scene-specific information to help guide the player
   */
   public void drawPopup() {
    if (intScreenNumber > 0){
      // draws padding rectangle
      fill(0, 0, 0, 150);
      rect(50, 50, width - 100, height - 100);
      // displays title of pop-up screen
      fill(255);
      textAlign(CENTER);
      textSize(24);
      text("Information Popup", width / 2, 100);
    }
    
    // Intro Screen
    if (intScreenNumber == 0){
      // draws padding rectangle
      fill(0, 0, 0, 220);
      rect(50, 50, width - 100, height - 100);
      fill(255);
      textAlign(CENTER);
      textSize(24);
      text("TOP 5 ALL-TIME HIGH SCORES", width / 2, 100);
      text("TIMES", 150, 200);
      text("DIFFICULTY", 400, 200);
      text("BEST IN", 650, 175);
      text("CATEGORY?", 650, 200);
      for (int i = 0; i < strHighScores.size(); i++){
        checkIfBestInCategory();
        text(strHighScores.get(i), 150, 260 + i * 50);
        if (intHighScoreDifficulty.get(i) == -1){
          text("FILLER", 400, 260 + i * 50);
        }
        else if (intHighScoreDifficulty.get(i) == 0){
          text("EASY", 400, 260 + i * 50);
        }
        else if (intHighScoreDifficulty.get(i) == 1){
          text("MEDIUM", 400, 260 + i * 50);
        }
        else if (intHighScoreDifficulty.get(i) == 2){
          text("HARD", 400, 260 + i * 50);
        }
        if (isBestInCategory[i]){
          text("YES!", 650, 260 + i * 50);
        }
        else if (!isBestInCategory[i]){
          text("NAH :(", 650, 260 + i * 50);
        }
      }
    }
    // Setting 1
    if (intScreenNumber == 1){
      text("Make your way to the shattered windows to ", width / 2, height / 2);
      text("escape the boss's office", width / 2, height / 2 + 25);
    }
    // Game 1
    if (intScreenNumber == 2){
      text("Make your away across the skybridge", width / 2, height / 2);
    }
    // Transfer 1
    if (intScreenNumber == 3){
      text("GO TO THE ELEVATOR TO ESCAPE THE BUILDING", width / 2, height / 2);
    }
    // Setting 2
    if (intScreenNumber == 4 && !isGameVictory){
      text("The desk seems to have something on it...", width / 2, height / 2);
    }
    if (intScreenNumber == 4 && isGameVictory){
      text("GO TO THE ELEVATOR TO ESCAPE THE BUILDING", width / 2, height / 2);
    }
    // Game 2
    if (intScreenNumber == 5){
      text("This is the ORIGINAL Wordle game by the ", width / 2, 160);
      text("New York Times.", width / 2, 200);
      text("To play, enter 5 letter combinations and ", width / 2, 240);
      text("press enter to check if your guess is the ", width / 2, 280);
      text("target 5 letter word. ", width / 2, 320);
      text("Red means the letter is not in the correct word.", width / 2, 360);
      text("Yellow means the letter is in the correct word, ", width / 2, 400);
      text("but not in the correct position.", width / 2, 440);
      text("Green means the letter is in the correct word ", width / 2, 480);
      text("and is in the correct position.", width / 2, 520);
    }
    // Setting 3
    if (intScreenNumber == 6 && !isGameVictory){
      text("The desk seems to have something on it...", width / 2, height / 2);
    }
    if (intScreenNumber == 6 && isGameVictory){
      text("GO TO THE ELEVATOR TO ESCAPE THE BUILDING", width / 2, height / 2);
    }
    // Game 3
    if (intScreenNumber == 7){
      text("This is Connections, the classic puzzle game by the", width / 2, 160);
      text("New York Times.", width / 2, 200);
      text("To play, you will need to sort all 16 tiles into ", width / 2, 240);
      text("four groups of four such that the words in each group", width / 2, 280);
      text("belong to a specific category.", width / 2, 320);
      text("To guess a group press ENTER", width / 2, 360);
      text("When a group is found, the category is revealed", width / 2, 400);
      text("and the words will no longer be selectable.", width / 2, 440);
      text("The categories range from easy to extremely hard.", width / 2, 480);
      text("The game is won once all four groups are found.", width / 2 - 50, 520);
    }
    // Transfer 2
    if (intScreenNumber == 8){
      text("GO TO THE EXIT!!!", width / 2, height / 2);
    }
    // displays back button to exit information pop-up
    backButton.isOver = backButton.isOver();
    backButton.display();
  }
  /**
   * Draws a popup window when the player wins.
   */
  public void drawWinPopup() {
    // draws padding rectangle
    fill(0, 0, 0, 150);
    rect(50, 50, width - 100, height - 100);
    // displays win text
    fill(255);
    textAlign(CENTER);
    textSize(24);
    text("You Win!", width / 2, height / 2 - 50);
    text("Congratulations!", width / 2, height / 2);
    // displays back button to escape pop-up and continue to next scene
    backButton.isOver = backButton.isOver();
    backButton.display();
  }
  /**
   * Draws a popup window when the player loses.
   */
  public void drawLosePopup() {
    // draws padding rectangle
    fill(0, 0, 0, 150);
    rect(50, 50, width - 100, height - 100);
    // displays lose text
    fill(255);
    textAlign(CENTER);
    textSize(24);
    text("You Lose!", width / 2, height / 2 - 50);
    text("Better luck next time!", width / 2, height / 2);
    // displays back button to escape pop-up and play the game again
    backButton.isOver = backButton.isOver();
    backButton.display();
  }
  /** 
   * Before entering this game, a brief preamble and a button will be displayed through this pop-up
   * Only after clicking the button will the player be able to play the game
   */
  public void drawGameInfoPopup() {
    // draws padding rectangle
    fill(0, 0, 0, 150);
    rect(50, 50, width - 100, height - 100);
    // displays game preamble text
    fill(255);
    textAlign(CENTER);
    textSize(24);
    if (intScreenNumber == 4){
      text("Complete the Wordle to Unlock the Elevator", width / 2, height / 2 - 50);
      text("Intelligence is needed to join the New York Times", width / 2, height / 2);
      text("Genius is needed to leave the New York Times", width / 2, height / 2 + 50);
    }
    else if (intScreenNumber == 6){
      text("Solve the Connections puzzle to Unlock the Elevator", width / 2, height / 2 - 50);
      text("'Know the enemy, know yourself;", width / 2, height / 2);
      text("victory will never be endangered' - Sun Tzu", width / 2, height / 2 + 50);
    }
    // displays start mini-game button to escape pop-up and go to the "game" screen
    gameButton.isOver = gameButton.isOver();
    gameButton.display();
  }
  // <-----------------------PLANK PUZZLE METHODS ------------------------------->
  /**
   * Considers hitboxes of when the player falls off the building to regular player movement
   * Resets the player to the initial starting position
   */
  public void playerMovementPlankWalk(){
    // Death barriers (Left block, right block, top death, middle death, bottom death)
    if ((intPlayerX <= 268 && intPlayerY <= 459)|| (intPlayerX >= width - 254 - 50 && intPlayerY <= 459) || (intPlayerX >= 374 - 50 && intPlayerX <= 374 + 66 && intPlayerY >= 50  - 80 && intPlayerY <= 50 + 73) || (intPlayerX >= 374 - 50 && intPlayerX <= 374 + 66 && intPlayerY >= 243 - 80 && intPlayerY <= 243 + 66) || (intPlayerX >= 374 - 50 && intPlayerX <= 374 + 66 && intPlayerY >= 425 - 80 && intPlayerY <= 425 + 63)) {
      resetSetting();
      return; // Exit the method to prevent further movement
    }
    // First Dissapearing Plank
    if (intPlayerX >= 455 - 20 && intPlayerX <= 455 + 80 + 20 && intPlayerY >= 425  && intPlayerY <= 425 + 29){
      plank1Show = false;
      resetSetting();
      return;
    }
    // Second Dissapearing Plank
    if (intPlayerX >= 455 - 20 && intPlayerX <= 455 + 80 + 20 && intPlayerY >= 245  && intPlayerY <= 245 + 29){
      plank2Show = false;
      resetSetting();
      return;
    }
    // Third Dissapearing Plank
    if (intPlayerX >= 277 - 20 && intPlayerX <= 277 + 80 + 20 && intPlayerY >= 67  && intPlayerY <= 67 + 29){
      plank3Show = false;
      resetSetting();
      return;
    }
    // player movement is called here
    playerMovement();
  }
  // ----------------WORDLE METHODS------------------------
  /**
   * Draws the Wordle-like grid and handles Game2 logic.
   */
  public void drawWordleGrid() {
    // draws grid based on intGridSizeY and intGridSizeX
    for (int i = 0; i < intGridSizeY; i++) {
      for (int j = 0; j < intGridSizeX; j++) {
        // draws grid of boxes for letters to be displayed in
        fill(200);
        rect(j * 80 + (width - 380) /2, i * 80 + 100, 60, 60);
        // Checks if the game is properly initialized
        if (strGuesses[i] != null && j < strGuesses[i].length()) {
          // Assigns colour to each inputted letter's box based on correctness to the target word
          char letter = strGuesses[i].charAt(j);
          if (strTargetWord.charAt(j) == letter && i != intCurrentRow) {
            fill(0, 255, 0); // Correct letter and position
          } else if (strTargetWord.indexOf(letter) != -1 && i != intCurrentRow) {
            fill(255, 255, 0); // Correct letter but wrong position
          } else if (i != intCurrentRow) {
            fill(255, 0, 0); // Incorrect letter
          } else {
            fill(200); // empty box
          }
          // Draws the specific letter in the box (in CAPITAL letters)
          rect(j * 80 + (width - 380) / 2, i * 80 + 100, 60, 60);
          fill(0);
          textSize(32);
          text(Character.toUpperCase(letter), j * 80 + (width - 380) / 2 + 20, i * 80 + 120);
        }
      }
    }
  }
  /**
   * Checks if the guesses for Game 2 (WORDLE) are correct answers
   */
  public void checkWordleGuess() {
    // checks if working row letters match the target word
    if (strGuesses[intCurrentRow].equals(strTargetWord)) {
      isGameOver = true;
      isGameVictory = true;
    } 
    // Checks if all rows have been used
    else if (intCurrentRow == intGridSizeY - 1) {
      isGameOver = true;
      isGameVictory = false;
    }
  }
  // ----------------CONNECTIONS METHODS------------------------
  /**
   * Draws the Connections Game grid and handles Game 3 logic.
   */
  public void ConnectionsGameScreen(){
    background(255); 
    // Initializing method variable
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
        text(incorrectGroups[intWordColumn][intWordRow], 8 + rectRow, 31 + rectColumn);
        intWordRow++;
      }
      intWordColumn++;
    }
    // Writes the current message to the top of the screen
    textSize(20);
    fill(0);
    text(message, 30, 10);

    // Display lives
    textSize(20);
    fill(0);
    text("Lives left: " + lives, width - 130, 90);
    // Check if game is won
    if (checkIfGameWon()) {
      isGameVictory = true;
      isGameOver = true;
    }
  }
  /**
   * Checks if the selected words form a correct group. If not, it checks for one away and removes a life. If wrong then removes a life.
   */
  public void checkSelectedWords() {
    // Checks that 4 words are selected
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
        
        return;
      }
    }
    // If the correctGroup was not found, checks for one word away, and if not then just removes a life
    boolean correctGroupFound = false;
    if (!correctGroupFound) {
      if (checkIfOneWordAway()) {
        message = "One word away...";
        lives--;
        if (lives <= 0) {
          isGameOver = true;
        }
      } 
      else {
        message = "Selected words are incorrect.";
        lives--;
        if (lives <= 0) {
          isGameOver = true;
        }
      }
    }
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
   * Finds the index of a given word in the incorrectGroups array.
   * 
   * @param word is the string word to find
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
  // -----------------------------MOUSE EVENT METHODS----------------------------
  /**
   * Handles mouse pressed events
   */
  public void mousePressed() {
    // Start game button (Intro screen)
    if (intScreenNumber == 0) {
      // brings player from intro screen to setting 1
      if (startButton.isOver() && !showPopup) {
        intStartTime = millis();
        intScreenNumber = 1; // Change to Setting1 
        resetSetting();
      } 
      // Toggling between three different wordle difficulties (0 = easy, 1 = medium, 2 = hard)
      if (difficultyButton.isOver() && !showPopup){
        if (intWordleDifficulty == 2){
          intWordleDifficulty = 0;
        }
        else{
          intWordleDifficulty++;
        }
      }

      // Upon clicking, will show screen-specific guiding information
      if (highscoreButton.isOver()) {
        showPopup = true;
      } 
      // Upon clicking back button of the pop-up, the extra info pop-up will stop displaying
      else if (showPopup && backButton.isOver()) {
        showPopup = false;
      }
    } 
    // Game 1
    if (intScreenNumber == 2) {
      // Upon game1 win, the user is directed back to the setting 1 screen
      if (backButton.isOver() && showWinPopup) {
        intScreenNumber = 3; // Change to Transfer 1
        resetSetting();
        showWinPopup = false;
      } 
      // Upon game1 loss, the user is directed to play the game again!
      else if (backButton.isOver() && showLosePopup) {
        initializeGame2();
        showLosePopup = false;
      } 
    }

    // Setting 2 Buttons
    if (intScreenNumber == 4 && isSwitchButtonDisplayed) {
      // Takes user from Setting 2 to Game 2
      if (gameButton.isOver()) {
        initializeGame2();
        intScreenNumber = 5; // Change to Game 2
        isSwitchButtonDisplayed = false;
      } 
    } 

    // Game 2 buttons
    if (intScreenNumber == 5) {
      // Upon game 2 win, the user is directed back to the setting 2 screen
      if (backButton.isOver() && showWinPopup) {
        intScreenNumber = 4; // Change to Game1
        showWinPopup = false;
      } 
      // Upon game 2 loss, the user is directed to play the game again!
      else if (backButton.isOver() && showLosePopup) {
        initializeGame2();
        showLosePopup = false;
      } 
    }

    // Setting 3 Button
    if (intScreenNumber == 6 && isSwitchButtonDisplayed) {
      // Takes user from Setting 3 to Game 3
      if (gameButton.isOver()) {
        initializeGame3();
        intScreenNumber = 7; // Change to Game 3
        isSwitchButtonDisplayed = false;
      } 
    } 
    // Mouse press check for connections
    if (intScreenNumber == 7){
      // Selects and deselects boxes
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
    // Game 3 Buttons
    if (intScreenNumber == 7) {
      // Upon game 3 win, the user is directed back to the setting 3 screen
      if (backButton.isOver() && showWinPopup) {
        intScreenNumber = 6; // Change to Setting 3
        showWinPopup = false;
      } 
      // Upon game 3 loss, the user is directed to play the game again!
      else if (backButton.isOver() && showLosePopup) {
        initializeGame3();
        showLosePopup = false;
      } 
    }

    // Extra info button for screens except intro and ending
    if (intScreenNumber > 0 && intScreenNumber < 9) {
      // Upon clicking, will show screen-specific guiding information
      if (infoButton.isOver()) {
        showPopup = true;
      } 
      // Upon clicking back button of the pop-up, the extra info pop-up will stop displaying
      else if (showPopup && backButton.isOver()) {
        showPopup = false;
      }
    } 
    // Ending screen play again button
    if (intScreenNumber == 9){
      if (playAgainButton.isOver){
        // go back to the intro screen
        intScreenNumber = 0;
        addTime();
        restartGame();
      }
    }
  }
  // -------------------------------------KEY RELEASED / PRESSED METHODS------------------------------
  /**
   * Handles key release events.
   */
  public void keyReleased(){
    // Player movement stops if key is released
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
    // Game 2 key inputs
    if (intScreenNumber == 5 && !isGameOver){
      // Checks if input is a letter
      if (key >= 'a' && key <= 'z' && (strGuesses[intCurrentRow] == null || strGuesses[intCurrentRow].length() < intGridSizeX) && !showPopup) {
        // replaces null values with empty string
        if (strGuesses[intCurrentRow] == null) {
          strGuesses[intCurrentRow] = "";
        }
        // changes all inputs to capital letters
        strGuesses[intCurrentRow] += Character.toUpperCase(key);
      } 
      // Removes letters from the working row that has yet to be submitted to check for correctness
      else if (key == BACKSPACE && strGuesses[intCurrentRow] != null && strGuesses[intCurrentRow].length() > 0) {
        strGuesses[intCurrentRow] = strGuesses[intCurrentRow].substring(0, strGuesses[intCurrentRow].length() - 1);
      }
      // Checks the working row for correctness
      else if (key == ENTER && strGuesses[intCurrentRow] != null && strGuesses[intCurrentRow].length() == intGridSizeX) {
        checkWordleGuess();
        // moves onto next row
        intCurrentRow++;
      }
    } 
    // Game 3 Inputs
    // Key press check for correctness for connections (Game 3)
    if (intScreenNumber == 7){
      if (key == ENTER) {
        checkSelectedWords();
      }
    }
    // Player movement begins if key is held down
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
  // -------------------------- GAME INITIALIZATION METHODS-------------------------------
  /**
   * Initializes Game 2 (WORDLE)
   */
  public void initializeGame2() {
    // initializing variables
    isGameOver = false;
    isGameVictory = false;
    strGuesses = new String[intGridSizeY];
    intCurrentRow = 0;
    // Selects target answer randomly from string list
    strTargetWord = strWordList[intWordleDifficulty][(int) (random(strWordList[0].length))]; // Randomly choose a target word
    // println("Target Word: " + strTargetWord); // For debugging
    // resetting all pop-ups
    showLosePopup = false;
    showWinPopup = false;
    showPopup = false;
    // initializing text size
    textSize(32);
  }
  /**
   * Initializes Game 3 (CONNECTIONS)
   */
  public void initializeGame3() {
    // initializing variables
    isGameOver = false;
    isGameVictory = false;
    selectedWords.clear();
    selectedBoxes = new boolean[4][4];
    solvedGroups = new boolean[4][4];
    message = "";
    lives = 4;
    // resetting all pop-ups
    showLosePopup = false;
    showWinPopup = false;
    showPopup = false;
    // initializing text size
    textSize(32);
  }

  public void restartGame(){
    initializeGame2();
    initializeGame3();
    resetSetting();
    plank1Show = true;
    plank2Show = true;
    plank3Show = true;
  }
  
  // ------------------------------PLAYER MOVEMENT & COLLISION METHODS-----------------------------
  /**
   * Player movement and sprite display
   */
  public void playerMovement(){
    // initializng next player position variables
    int intNextX, intNextY;
    // player is moving up
    if (isUpPressed && intPlayerY >= 0 + 40 && !isSwitchButtonDisplayed && !showPopup) {
      intNextY = intPlayerY - 4;
      // checks if player is colliding with an open elevator
      if (!isCollidingElevator() || !isElevatorOpen) {
        // restricts player from overlapping the desk hitbox
        if (!isPlayerCollidingDesk(intPlayerX, intNextY)) {
          // changes player character image and position
          intPlayerY = intNextY;
          currentPlayerState = playerBackward;
        }
      }
    }
    // player is moving down
    if (isDownPressed && intPlayerY <= height - 10 - 80 && !isSwitchButtonDisplayed && !showPopup) {
      intNextY = intPlayerY + 4;
      // checks if player is colliding with an open elevator
      if (!isCollidingElevator() || !isElevatorOpen) {
        // restricts player from overlapping the desk hitbox
        if (!isPlayerCollidingDesk(intPlayerX, intNextY)) {
          // changes player character image and position
          intPlayerY = intNextY;
          currentPlayerState = playerForward;
        }
      }
    }
    // player is moving left
    if (isLeftPressed && intPlayerX >= 0 + 10 && !isSwitchButtonDisplayed && !showPopup) {
      intNextX = intPlayerX - 4;
      // checks if player is colliding with an open elevator
      if (!isCollidingElevator() || !isElevatorOpen) {
        // restricts player from overlapping the desk hitbox
        if (!isPlayerCollidingDesk(intNextX, intPlayerY)) {
          // changes player character image and position
          intPlayerX = intNextX;
          currentPlayerState = playerLeft;
        }
      }
    }
    // player is moving right 
    if (isRightPressed && intPlayerX <= width - 10 - 50 && !isSwitchButtonDisplayed && !showPopup) {
      intNextX = intPlayerX + 4;
      // checks if player is colliding with an open elevator
      if (!isCollidingElevator() || !isElevatorOpen) {
        // restricts player from overlapping the desk hitbox
        if (!isPlayerCollidingDesk(intNextX, intPlayerY)) {
          // changes player character image and position
          intPlayerX = intNextX;
          currentPlayerState = playerRight;
        }
      }
    }
    // displays player onto screen
    image(currentPlayerState, intPlayerX, intPlayerY);
  }
  /**
   * Checks if the player is colliding with the elevator in the image
   * 
   * @return true if colliding, false if not colliding
   */
  public boolean isCollidingElevator(){
    // Checks if player is colliding with elevator hitbox
    if (intPlayerY <= 50 && intPlayerX < (width / 2) + 60 && intPlayerX > (width / 2) - 60) {
      return true;
    } 
    else {
      return false;
    }
  }
  /**
   * Checks if the player will collide with the desk
   * 
   * @param intX The next X-coordinate of the player
   * @param intY The next Y-coordinate of the player
   * @return true if the player will collide with the desk, false if the player isn't colliding with desk
   */
  public boolean isPlayerCollidingDesk(int intX, int intY){
    // compares the player position with the hitbox of the desk
    if (intX < intDeskX + intDeskWidth && intX + 55 > intDeskX && intY < intDeskY + intDeskHeight && intY + 55 > intDeskY && (intScreenNumber == 1 || intScreenNumber == 4 || intScreenNumber == 6 || intScreenNumber == 8)){
      isCollidingDesk = true;
      return true;
    }
    else{
      isCollidingDesk = false;
      return false;
    }
  }
  
  // ------------------------------FADING OBJECT METHODS----------------------------------
  /**
   * Displays the exclamation marker at a given x and y coordinate and bobs up and down
   * The exclamation mark also slowly fades into view from an initial transparent state
   * 
   * @param intX X-coordinate of the exclamation mark
   * @param initialY Initial Y-coordinate of the exclamation mark
   */
  public void displayExclamMark(float intX, float initialY){
    // Slowly increases the opacity of the exclamation mark image
    if (fltExclamAlpha < 255.0) {
      fltExclamAlpha += fltFadeSpeed;
    }
    // Constrain alpha to not exceed 255
    fltExclamAlpha = constrain((int) fltExclamAlpha, 0, 255);

    // Apply the tint with the current alpha value
    tint(255, fltExclamAlpha);
    float bobbingY = initialY + 20 * sin((float)(TWO_PI * 0.4 * millis() / 1000.0));
    // draws exclamation mark
    image(exclamationMark, intX, bobbingY);
    // disables tint after use
    noTint();
  }
  
  /**
   * Upon player contact with an open elevator, 
   * the whole screen will slowly fade to dark
   * to transition to the next scene
   */
  public void fadeOutElevator(){
    // Increases opacity of the black screen over time until it is completely opaque
    if (fltElevatorAlpha < 255.0) {
      fltElevatorAlpha += 3.2f;
      fill(0, fltElevatorAlpha);
      rect(0, 0, width, height);
    }
    else{
      // Ensure the screen is fully black after 3 seconds
      background(0); 
      isScreenFaded = true;
    }
  }
  // -------------------------------IN GAME STOPWATCH--------------------------------------
  /**
   * Displays a clock of the time that has passed since the start of the game
   */
  public void displayElapsedTime() {
    // Compares the current time passed in the game with the time of the game's start
    int intCurrentTime = millis();
    int intElapsedTime = intCurrentTime - intStartTime;
  
    // Calculates seconds, minutes and hours
    int intSeconds = (intElapsedTime / 1000) % 60;
    int intMinutes = (intElapsedTime / (1000 * 60)) % 60;
    int intHours = (intElapsedTime / (1000 * 60 * 60)) % 24;
  
    // formats the time in digital clock display
    strTime = nf(intHours, 2) + "h:" + nf(intMinutes, 2) + "m:" + nf(intSeconds, 2) + "s";
  
    // initializes formatting of the string display
    if (intScreenNumber == 5 || intScreenNumber == 1 || intScreenNumber == 7){
      fill(0); // Set the text color to black
    }
    else{
      fill(255); // Set the text color to white
    }
    textSize(20);
    textAlign(LEFT, TOP);
    // displays the time
    if (intScreenNumber == 7){
      text("Elapsed Time: ", 630, 380);
      text(strTime, 630, 410);
    }
    else{
      text("Elapsed Time: ", 10, 10);
      text(strTime, 10, 30);
    }
  }
  /**
   * Adds the time that the player took to finish the game to the highscore list; will not show if it is not better than pre-existing scores
   */
  public void addTime(){
    // Initializing variables
    int intNewTime = parseTimeToSeconds(strTime);
    int intNewPos = -1;

    // Check every value in the list to see if the new time is smaller than the rest
    for(int i = 0; i < 5; i++){
      if (intNewTime < parseTimeToSeconds(strHighScores.get(i))){
        // Adds to the ranking of where the new time will be on the leaderboard
        intNewPos++;
      }
    }
    // inputs the new time into the highscore list if the new time is smaller than the ones already existing
    if(intNewPos > -1){
      intNewPos = 4 - intNewPos;
      strHighScores.add(intNewPos, strTime);
      strHighScores.remove(5);
      intHighScoreDifficulty.add(intNewPos, intWordleDifficulty);
      intHighScoreDifficulty.remove(5);
    }
  }
  /**
   * Takes the string of time in the format 00h:00m:00s to an integer amount of seconds
   */
  public int parseTimeToSeconds(String strTimer){
    // Initialization
    String[] strCurrentTime = strTimer.split(":");

    // Removing the "h", "m", or "s"
    for (int i = 0; i < strCurrentTime.length; i++){
      strCurrentTime[i] = strCurrentTime[i].substring(0, 2);
    }

    // Parse the split strings into integers
    int intcurrentHours = Integer.parseInt(strCurrentTime[0]);
    int intcurrentMinutes = Integer.parseInt(strCurrentTime[1]);
    int intcurrentSeconds = Integer.parseInt(strCurrentTime[2]);

    // Return total number of seconds
    return intcurrentHours * 3600 + intcurrentMinutes * 60 + intcurrentSeconds;
  }

  /**
   * Check which values of the high score list are the best in the respective difficulty and changes boolean to be displayed
   */
  public void checkIfBestInCategory(){
    // Initialization
    ArrayList<Integer> intIndexEasy = new ArrayList<>();
    ArrayList<Integer> intIndexMed = new ArrayList<>();
    ArrayList<Integer> intIndexHard = new ArrayList<>();
    int intLowestEasy = 999999999;
    int intLowestMed = 999999999;
    int intLowestHard = 999999999;
    int intBestEasy = -1;
    int intBestMed = -1;
    int intBestHard = -1;
    // Sorting the times by difficulty
    for(int i = 0; i < 5; i++){
      if (intHighScoreDifficulty.get(i) == 0){
        intIndexEasy.add(i);
      }
      else if (intHighScoreDifficulty.get(i) == 1){
        intIndexMed.add(i);
      }
      else if (intHighScoreDifficulty.get(i) == 2){
        intIndexHard.add(i);
      }
    }
    // Finding the smallest time in the easy difficulty
    for (int i = 0; i < intIndexEasy.size(); i++){
      if(intLowestEasy > parseTimeToSeconds(strHighScores.get(intIndexEasy.get(i)))){
        intLowestEasy = parseTimeToSeconds(strHighScores.get(intIndexEasy.get(i)));
        intBestEasy = intIndexEasy.get(i);
      }
    }
// Finding the smallest time in the medium difficulty
    for (int i = 0; i < intIndexMed.size(); i++){
      if(intLowestMed > parseTimeToSeconds(strHighScores.get(intIndexMed.get(i)))){
        intLowestMed = parseTimeToSeconds(strHighScores.get(intIndexMed.get(i)));
        intBestMed = intIndexMed.get(i);
      }
    }
    // Finding the smallest time in the hard difficulty
    for (int i = 0; i < intIndexHard.size(); i++){
      if(intLowestHard > parseTimeToSeconds(strHighScores.get(intIndexHard.get(i)))){
        intLowestHard = parseTimeToSeconds(strHighScores.get(intIndexHard.get(i)));
        intBestHard = intIndexHard.get(i);
      }
    }
    // resetting the old data
    for (int i = 0; i < 5; i++){
      isBestInCategory[i] = false;
    }
    // assigning true to the shortest values of each respective difficulty
    if (intBestEasy != -1){
      isBestInCategory[intBestEasy] = true;
    }
    if (intBestMed != -1){
      isBestInCategory[intBestMed] = true;
    }
    if (intBestHard != -1){
      isBestInCategory[intBestHard] = true;
    }
  }
}