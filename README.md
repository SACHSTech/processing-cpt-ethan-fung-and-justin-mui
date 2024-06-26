[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-24ddc0f5d75046c5622901739e7c5dd533143b0c8e959d652212380cedb1ea36.svg)](https://classroom.github.com/a/B2OnycBl)
[![Open in Visual Studio Code](https://classroom.github.com/assets/open-in-vscode-718a45dd9cf7e7f842a935f5ebbe5719a5e09af4491e668f4dbf3b35d5cca122.svg)](https://classroom.github.com/online_ide?assignment_repo_id=15143760&assignment_repo_type=AssignmentRepo)
# Escape The New York Times 
### Created by Ethan Fung & Justin Mui

## Objective Of The Game
**Escape the New York Times** is a timed adventure/puzzle game where you, the biggest New York Times (NYT) nerd, have to escape the NYT office building after making the claim to your boss, the CEO, that the puzzles are so easy "they could be solved by a child". Enraged by your claims, the CEO has prepared 3 of the hardest puzzles known to man. Fail even once and you **WILL** be forced to work for the New York Times  **FOREVER**. Do you think you have what it takes to solve these puzzles and defeat the CEO once and for all? 

## Gameplay Mechanics & User Interactions
Within the game, the user will have to interact with the map to access puzzles and move through the game. This includes walking to the elevators and interacting with the desk to play certain puzzles. Buttons are used to switch between screens and provide
the user with guidance. The user will also need to use key inputs to solve the puzzles. The user can also toggle the 
difficulty of the puzzles at the start of the game. 

### Broken Bridge
The first puzzle is a broken bridge game. There are 3 pairs of wooden planks placed next to each other. In each pair of wooden planks, one is safe to step on, and the other plunges you to your death, sending you back to the start.

### Wordle
The second puzzle is the classic New York Times game, Wordle. Here you will have six attempts to guess a five-letter word. Feedback will be given for each guess, with the colour of the tile indicating when letters match or occupy the correct position. A green tile 🟩 indicates that the letter is correct and in the correct position, yellow 🟨 means it is in the answer but not in the right position, and red 🟥 indicates that the letter is not in the word at all.

### Connections
The third and final game is the classic New York Times game, Connections. The goal of the game is to divide a grid of sixteeen words into four groups of four such that the words in each group belong to a specific category (Eg, green, red, blue, yellow, for the category "Colours"). When a group is found, the category is revealed and the words will no longer be selectable. The categories range from easy to extremely hard. The game is won once all four groups are found.

## Speedrun Timer (Scoring)
The game features a timer system for speedrunning/scoring purposes. While it doesn't impact any gameplay, it allows for a more competitive environment to see who can finish the quickest and rank the highest in the highscore ranking. 

## Limitations 

### Broken Bridge Limitations
- Planks have no animation, they just dissapear upon collision

### Connections Limitations 
- Tiles do not change colour once solved
- Tiles do not move once solved
- Will not remember previous guesses
- Can keep losing lives on the same 4 selected words
- Word set will not change on restart 

### Wordle Limitations
- Does not display what letters have been used
- Guesses from the user do not have to be actual five character words; guesses could be a 5 character word that doesn't exit

## Screenshots Of The Game

### Broken Bridge Game
![](screenshots/BrokenBridge.png)

### Wordle Game
![](screenshots/Wordle.png)

### Connections Game
![](screenshots/Connections.png)

### Environment/Setting
![](screenshots/GeneralEnvironment.png)