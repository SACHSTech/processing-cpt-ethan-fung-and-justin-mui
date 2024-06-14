import java.util.ArrayList;
import java.util.Arrays;
import processing.core.PApplet;
import processing.core.PImage;

  /*
   * Start game
   * All boxes are drawn onto screen from incorrectGroups
   * Only a max of 4 boxes can be selected, then the user must press enter
   * The program then checks to see if the selected matches a row in the correct groups
   * If not then checks if the user is one away from a correct group
   * If yes, then turn those boxes grey, etc
   * Game is complete when all 4 rows are solved
   */

public class Sketch extends PApplet {
	
  class Button {
    float x, y, w, h;
    String label;
    
    boolean isOver = false;

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

  //Variables For Rest Of The Game
  Button startButton, gameButton, gearButton, backButton;
  int intScreenNumber = 0; // 0 = Intro Screen, 1 = Setting1, 2 = Game1, 3 = Setting2, 4 = Game2, 5 = Setting3, 6 = Game3, 7 = Ending Screen, 8 = Information screen
  boolean showPopup = false; // screen specific pop up info screen
  boolean showWinPopup = false;
  boolean showLosePopup = false;

  int intPlayerX, intPlayerY;
  
  boolean isUpPressed = false;
  boolean isDownPressed = false;
  boolean isLeftPressed = false;
  boolean isRightPressed = false;

  PImage setting1, setting2, setting3, setting4, setting5, setting6, setting7, setting8;
  PImage playerForward, playerBackward, playerLeft, playerRight;
  PImage currentPlayerState;

  PImage exclamationMark;
  PImage woodenPlank;
  int intExclamationY;
  float fltAlpha;
  float fltFadeSpeed = 0.5f;

  boolean isSwitchButtonDisplayed;

  boolean plank1Show = true;
  boolean plank2Show = true;
  boolean plank3Show = true;

  //---------WIN/LOSS VARIABLES---------------
  boolean isGameOver;
  boolean isGameVictory = false;

  //---------VARIABLES FOR WORDLE--------------
  int intGridSizeX = 5;
  int intGridSizeY = 6;
  String[] strWordList = {"STARK", "REESE", "CRACK", "NOSEY", "HITCH", "RURAL", "CRAIC", "ERGOT", "OUIJA"};
  String strTargetWord;
  String[] strGuesses;
  int intCurrentRow;
  boolean isSettingGameChanged = false;

  //----------VARIABLES FOR CONNECTIONS------------
  String[][] incorrectGroups;
  String[][] correctGroups;

  ArrayList<String> selectedWords = new ArrayList<>();
  boolean[][] selectedBoxes = new boolean[4][4];
  boolean[][] solvedGroups = new boolean[4][4];
  String message = "";
  int lives = 4;

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
    
    //Rest of game setup
    textSize(32);
    startButton = new Button(width / 2 - 100, height / 2 - 50, 200, 50, "Start Game");
    gameButton = new Button(width / 2 - 100, height / 2 + 50, 200, 50, "Settings");
    gearButton = new Button(width - 60, 10, 50, 50, "Gear");
    backButton = new Button(width / 2 - 50, height / 2 + 100, 100, 50, "Back");
    playerForward = loadImage("images/NerdFace.png"); 
    playerBackward = loadImage("images/NerdFaceBack.png"); 
    playerLeft = loadImage("images/NerdFaceLeft.png"); 
    playerRight = loadImage("images/NerdFaceRight.png"); 

    setting1 = loadImage("images/BossRoom.png");
    setting2 = loadImage("images/PlankWalk.png");
    setting3 = loadImage("images/TopFloor.png");
    setting4 = loadImage("images/Floor2Closed.png");
    setting5 = loadImage("images/Floor2Open.png");
    setting6 = loadImage("images/Floor1Closed.png");
    setting7 = loadImage("images/Floor1Open.png");
    setting8 = loadImage("images/GroundFloor.png");

    exclamationMark = loadImage("images/exclamation_mark.png"); 
    exclamationMark.resize(55, 55);
    woodenPlank = loadImage("images/WoodenPlank.png");

    //Connections Setup
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
    
    background(50);
    if (intScreenNumber == 0) {
      introScreen();
    } else if (intScreenNumber == 1) {
      BossRoomScreen();
    } else if (intScreenNumber == 2) {
      PlankWalkScreen();
    } else if (intScreenNumber == 3) {
      TopFloorScreen();
    } else if (intScreenNumber == 4) {
      Floor2ClosedScreen();
    } else if (intScreenNumber == 5) {
      WordleGameScreen();
    } else if (intScreenNumber == 6) {
      Floor2OpenScreen();
    } else if (intScreenNumber == 7) {
      Floor1ClosedScreen();
    } else if (intScreenNumber == 8) {
      ConnectionsGameScreen();
    } else if (intScreenNumber == 9) {
      Floor1OpenScreen();
    } else if (intScreenNumber == 10) {
      GroundFloorScreen();
    } else if (intScreenNumber == 11) {
      EndingScreen();
    }

  }

  //---------------------MOUSE PRESSED METHOD------------------------
  /**
   * Called when the mouse is pressed. Checks words selected and button clicks.
   */
  public void mousePressed() {

    // Toggling Intro screen 
    if (intScreenNumber == 0) {
      if (startButton.isOver()) {
        resetSettingBottom();
        intScreenNumber = 1; // Change to BossFloor
      } 
    } 

    // Mouse press Win/Loss for connections
    if (intScreenNumber == 8) {
      if (backButton.isOver() && showWinPopup) {
        // Upon game1 win, the user is directed back to the setting 1 screen
        intScreenNumber = 9; // Change to Transfer 1
        resetSetting();
        showWinPopup = false;
      } 
      else if (backButton.isOver() && showLosePopup) {
        // Upon game1 loss, the user is directed to play the game again!
        initializeConnections();
        showLosePopup = false;
      } 
    }
    //Mouse press check for connections
    if (intScreenNumber == 8){

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
  }

  //----------------KEY PRESSED/RELEASED METHODS------------------------
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
   * Checks if a key is pressed. Checks if the Enter key is pressed and then checks the selected words if they are correct.
   */
  public void keyPressed() {

    //Key press check for movement
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
  
    //Key press check for connections
    if (intScreenNumber == 8){
      if (key == ENTER) {
        checkSelectedWords();
      }
    }
  }


//-------------------SCREEN METHODS---------------
  public void introScreen(){
    textAlign(CENTER);
    fill(255);
    textSize(32);
    text("ESCAPE THE NEW YORK TIMES", width / 2, height / 3);
    startButton.isOver = startButton.isOver();
    startButton.display();
  }

  public void BossRoomScreen(){
    // Setting1 background generation
    image(setting1, 0, 0);
    playerMovementBossRoom();
  }

  public void PlankWalkScreen(){
    // Setting1 background generation
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
  }

  public void TopFloorScreen(){
    image(setting3, 0, 0);
    playerMovement();


  }

  public void Floor2ClosedScreen(){
    image(setting4, 0, 0);
  }

  public void WordleGameScreen(){

  }

  public void Floor2OpenScreen(){
    image(setting5, 0, 0);
  }

  public void Floor1ClosedScreen(){
    image(setting6, 0, 0);
  }

  public void ConnectionsGameScreen(){
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
        text(incorrectGroups[intWordColumn][intWordRow], 50 + rectRow, 50 + rectColumn);
        intWordRow++;
      }
      intWordColumn++;
    }

    //Writes the current message to the top of the screen
    textSize(20);
    fill(0);
    text(message, 100, 30);

    //Display lives
    textSize(20);
    fill(0);
    text("Lives left: " + lives, width - 150, 30);

    if (isGameOver) {
      // Show game over screen and restart button
      drawLosePopup();
    
    } else if (isGameVictory) {
      // Show you won screen and next game button
      drawWinPopup();
    }
  }

  public void Floor1OpenScreen(){
    image(setting7, 0, 0);
    playerMovement();
  }

  public void GroundFloorScreen(){
    image(setting8, 0, 0);
  }

  public void EndingScreen(){

  }

//----------------MOVEMENT METHODS---------------------------
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

public void playerMovementBossRoom(){
  if (isUpPressed && (intPlayerY > 10 || (intPlayerX >= 278 && intPlayerX <= 534 && intPlayerY > 0))){
    intPlayerY -=3;      
    currentPlayerState = playerBackward;

    if (intPlayerY <= 0 && intPlayerX >= 278 && intPlayerX <= 534) {
      intScreenNumber = 2; // Change to PlankWalk screen
      resetSettingBottom();; // Reset player position to the bottom of the new screen
    }
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

public void playerMovementPlankWalk(){
  
  //Death barriers (Left block, right block, top death, middle death, bottom death)
  if ((intPlayerX <= 268 && intPlayerY <= 459)|| (intPlayerX >= width - 254 - 50 && intPlayerY <= 459) || (intPlayerX >= 374 - 50 && intPlayerX <= 374 + 66 && intPlayerY >= 50  - 80 && intPlayerY <= 50 + 73) || (intPlayerX >= 374 - 50 && intPlayerX <= 374 + 66 && intPlayerY >= 243 - 80 && intPlayerY <= 243 + 66) || (intPlayerX >= 374 - 50 && intPlayerX <= 374 + 66 && intPlayerY >= 425 - 80 && intPlayerY <= 425 + 63)) {
    resetSettingBottom();
    return; // Exit the method to prevent further movement
  }

  //First Dissapearing Plank
  if (intPlayerX >= 455 - 20 && intPlayerX <= 455 + 80 + 20 && intPlayerY >= 425  && intPlayerY <= 425 + 29){
    plank1Show = false;
    resetSettingBottom();
    return;
  
  }
  //Second Dissapearing Plank
  if (intPlayerX >= 455 - 20 && intPlayerX <= 455 + 80 + 20 && intPlayerY >= 245  && intPlayerY <= 245 + 29){
    plank2Show = false;
    resetSettingBottom();
    return;
  }

  //Third Dissapearing Plank
  if (intPlayerX >= 277 - 20 && intPlayerX <= 277 + 80 + 20 && intPlayerY >= 67  && intPlayerY <= 67 + 29){
    plank3Show = false;
    resetSettingBottom();
    return;
  }
  
  if (isUpPressed && (intPlayerY > 10 )){
    intPlayerY -=3;      
    currentPlayerState = playerBackward;

    if (intPlayerY <= 20 && intPlayerX >= 278 && intPlayerX <= 535) {
      intScreenNumber = 8; // Change to Top Floor screen
      resetSettingBottom();; // Reset player position to the bottom of the new screen
    }
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

}

public void resetSetting(){
    
  intPlayerX = 300;
  intPlayerY = 300;
  currentPlayerState = playerForward;
  fltAlpha = 0;
  isSwitchButtonDisplayed = false;
}

public void resetSettingBottom(){
  
  intPlayerX = 400 - 25;
  intPlayerY = 520;
  currentPlayerState = playerForward;
  fltAlpha = 0;
  isSwitchButtonDisplayed = false;
}

//---------------WIN/LOSS SCREEN METHODS--------------------
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

//----------------CONNECTIONS METHODS------------------------
    
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
          isGameVictory = true;
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
          isGameOver = true;
        }
      } else {
        message = "Selected words are incorrect.";
        lives--;
        if (lives <= 0) {
          isGameOver = true;
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
  public void initializeConnections() {
    selectedWords.clear();
    selectedBoxes = new boolean[4][4];
    solvedGroups = new boolean[4][4];
    message = "";
    lives = 4;
    isGameOver = false;
    isGameVictory = false;
  }
}
