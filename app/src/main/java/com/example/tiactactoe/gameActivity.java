package com.example.tiactactoe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.Random;

public class gameActivity extends AppCompatActivity {

    private boolean myTurn;
    private String[][] board;
    private ImageView winner_image;
    private ImageView looser_image;
    private Button exitGame_btn;
    private Button playAgain_btn;
    private ImageView draw_image;
    private ProgressBar progressBar;
    private String gameLevel;
    private MediaPlayer touchSound;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the content view to the activity_game layout
        setContentView(R.layout.activity_game);

        // Set the background drawable for the window
        getWindow().setBackgroundDrawableResource(R.drawable.ingamebackground);

        // Initialize the touch sound MediaPlayer
        touchSound = MediaPlayer.create(this, R.raw.tapsound);

        // Retrieve the data from the Intent
        Intent intent = getIntent();
        if (intent != null) {
            gameLevel = intent.getStringExtra("gameLevel");
        }

        // Initialize UI elements and set their initial visibility
        winner_image = findViewById(R.id.winner_image);
        looser_image = findViewById(R.id.looser_image);
        winner_image.setVisibility(View.INVISIBLE);
        looser_image.setVisibility(View.INVISIBLE);

        exitGame_btn = findViewById(R.id.exitGame_btn);
        playAgain_btn = findViewById(R.id.playAgain_btn);
        exitGame_btn.setVisibility(View.INVISIBLE);
        playAgain_btn.setVisibility(View.INVISIBLE);

        draw_image = findViewById(R.id.draw_image);
        draw_image.setVisibility(View.INVISIBLE);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        // Initialize game state variables
        myTurn = true;
        board = new String[3][3];

        // Initialize the board with empty values
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                board[i][j] = " ";
            }
        }
    }

    /**
     * Handles the click event when a player tries to place a mark on the board.
     *
     * @param view The View that was clicked (in this case, the place on the board).
     */
    @SuppressLint("ResourceAsColor")
    public void onClickPlace(View view) {
        // Get the ID and button associated with the clicked place
        int placeClickedId = view.getId();
        Button placeClickedBtn = findViewById(placeClickedId);

        // Check if it's the player's turn
        if (myTurn) {
            // Check if the clicked place is empty
            if (!placeClickedBtn.getText().equals(" ")) {
                // Display an error toast if the place is not empty
                //Toast.makeText(this, "Not an empty place", Toast.LENGTH_SHORT).show();
            } else {
                // Play the touch sound and update the turn
                touchSound.start();
                myTurn = false;

                // Draw "X" on the clicked place
                drawXO(placeClickedBtn, "X");

                // Check for win or draw conditions
                if (checkForWin("X") == 0)
                    computerTurnMovement();
                else if (checkForWin("X") == 1) {
                    // Player wins
                    Log.d("GAME", "Player wins");
                    GameEnded(1);
                } else if (checkForWin("O") == 2) {
                    // Draw
                    Log.d("GAME", "Draw");
                    GameEnded(2);
                }
            }
        }
    }

    /**
     * Handles the click event when the user wants to exit the game.
     *
     * @param view The View that was clicked (in this case, the exit game button).
     */
    public void onClickExitGame(View view) {
        // Finish the current activity to exit the game
        finish();
    }

    /**
     * Handles the click event when the user wants to play the game again.
     *
     * @param view The View that was clicked (in this case, the play again button).
     */
    public void onClickPlayAgain(View view) {
        // Reset the current activity for a new game
        resetActivity();
    }

    /**
     * Resets the current activity to start a new game.
     */
    public void resetActivity() {
        // Create a new intent for the current activity
        Intent intent = getIntent();

        // Finish the current activity
        finish();

        // Start the current activity again with the new intent
        startActivity(intent);
    }

    /**
     * Handles the end of the game and displays appropriate UI elements based on the reason.
     *
     * @param reason The reason for the game ending (0: computer wins, 1: player wins, 2: draw).
     */
    public void GameEnded(int reason) {
        // Disable further turns
        myTurn = false;

        // Show appropriate UI elements based on the reason
        if (reason == 0) {
            // Computer wins
            looser_image.setVisibility(View.VISIBLE);
        } else if (reason == 1) {
            // Player wins
            winner_image.setVisibility(View.VISIBLE);
        } else if (reason == 2) {
            // Draw
            draw_image.setVisibility(View.VISIBLE);
        }

        // Show exit game and play again buttons
        exitGame_btn.setVisibility(View.VISIBLE);
        playAgain_btn.setVisibility(View.VISIBLE);
    }

    /**
     * Handles the computer's turn movement, including updating the progress bar and making a move.
     */
    public void computerTurnMovement() {
        // Show the progress bar
        progressBar.setVisibility(View.VISIBLE);

        // Start a countdown timer for the computer's turn
        new CountDownTimer(1000 / 3, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Update the progress bar based on the remaining time
                int progress = (int) ((1000 / 3 - millisUntilFinished) * 100 / (1000 / 3));
                progressBar.setProgress(progress);
            }

            @Override
            public void onFinish() {
                // Timer is complete
                // You can perform any actions you want here

                Button randBtn = null;

                // For Easy mode, get a random button from the specified level
                if (gameLevel.equals("Easy")) {
                    randBtn = getPlace("Easy", 0);
                }

                // For Hard mode, try to win, then try to block the player, otherwise, choose a random move
                if (gameLevel.equals("Hard")) {
                    randBtn = tryToWin();
                    if (randBtn == null) {
                        randBtn = tryToBlockThePlayer(); // return random move (button) if cannot block the player
                    }
                }

                // Draw "O" on the chosen button
                drawXO(randBtn, "O");

                // Check for win or draw conditions
                if (checkForWin("O") == 0) {
                    // Computer wins
                    myTurn = true;
                } else if (checkForWin("O") == 1) {
                    // Computer loses
                    Log.d("GAME", "Computer loses");
                    GameEnded(0);
                } else if (checkForWin("O") == 2) {
                    // Draw
                    Log.d("GAME", "Draw");
                    GameEnded(2);
                }

                // Hide the progress bar
                progressBar.setVisibility(ProgressBar.INVISIBLE);
            }
        }.start();
    }

    /**
     * Tries to find a winning move for the computer.
     *
     * @return The Button representing the winning move, or null if no winning move is found.
     */
    public Button tryToWin() {
        boolean winFound = false;
        int i = 0, j = 0;

        // Iterate through the board to find a winning move
        for (i = 0; i < 3; i++) {
            for (j = 0; j < 3; j++) {
                // Check if the current position is empty
                if (board[i][j].equals(" ")) {
                    // Place "O" in the current position
                    board[i][j] = "O";

                    // Check if the computer wins with this move
                    if (checkForWin("O") == 1) {
                        // Winning move found, return the corresponding button
                        board[i][j] = " "; // Remove the temporary "O"
                        winFound = true;
                        break;
                    } else {
                        // No win, remove the temporary "O"
                        board[i][j] = " ";
                    }
                }
            }

            // If a winning move is found, return the corresponding button
            if (winFound) {
                return getPlace("Hard", i * 3 + j);
            }
        }

        // No winning move found
        return null;
    }

    /**
     * Tries to find a move to block the player from winning.
     *
     * @return The Button representing the blocking move, or a random move if no blocking move is found.
     */
    public Button tryToBlockThePlayer() {
        int i = 0, j = 0;
        boolean blockFound = false;

        // Iterate through the board to find a move to block the player
        for (i = 0; i < 3; i++) {
            for (j = 0; j < 3; j++) {
                // Check if the current position is empty
                if (board[i][j].equals(" ")) {
                    // Place "X" in the current position (simulate player's turn)
                    board[i][j] = "X";

                    // Check if the player wins with this move
                    if (checkForWin("X") == 1) {
                        // Blocking move found, remove the temporary "X"
                        board[i][j] = " ";
                        blockFound = true;
                        break;
                    } else {
                        // No win, remove the temporary "X"
                        board[i][j] = " ";
                    }
                }
            }

            // If a blocking move is found, break from the loop
            if (blockFound) {
                break;
            }
        }

        // If a blocking move is found, return the corresponding button
        if (!blockFound) {
            // If no blocking move is found, return a random move
            return getPlace("Easy", 0);
        } else {
            // If a blocking move is found, return the corresponding button (convert x y to 0-8, button number)
            return getPlace("Hard", i * 3 + j);
        }
    }

    /**
     * Draws the given X or O on the specified button and updates the game board.
     *
     * @param placeToDraw The Button on which to draw X or O.
     * @param xo          The string representing X or O.
     */
    public void drawXO(Button placeToDraw, String xo) {
        // Set the text, gravity, and text color of the specified button
        placeToDraw.setText(xo);
        placeToDraw.setGravity(Gravity.CENTER);
        placeToDraw.setTextColor(ContextCompat.getColor(this, android.R.color.black));

        // Get the x and y coordinates of the button on the game board
        int x = getXYByPlace(placeToDraw)[0]; // x
        int y = getXYByPlace(placeToDraw)[1]; // y

        // Update the game board with the specified X or O
        board[x][y] = xo;
    }

    /**
     * Gets the x and y coordinates on the game board based on the specified button.
     *
     * @param place The Button for which to determine the x and y coordinates.
     * @return An array containing the x and y coordinates [x, y].
     */
    public int[] getXYByPlace(Button place) {
        int x = 0;
        int y = 0;

        // Determine the x and y coordinates based on the specified button
        switch (place.getId()) {
            case R.id.place0_btn:
                y = 0;
                x = 0;
                break;
            case R.id.place1_btn:
                y = 1;
                x = 0;
                break;
            case R.id.place2_btn:
                y = 2;
                x = 0;
                break;
            case R.id.place3_btn:
                y = 0;
                x = 1;
                break;
            case R.id.place4_btn:
                y = 1;
                x = 1;
                break;
            case R.id.place5_btn:
                y = 2;
                x = 1;
                break;
            case R.id.place6_btn:
                y = 0;
                x = 2;
                break;
            case R.id.place7_btn:
                y = 1;
                x = 2;
                break;
            case R.id.place8_btn:
                y = 2;
                x = 2;
                break;
        }

        // Create and return an array containing the x and y coordinates
        int[] xy_arr = {x, y};
        return xy_arr;
    }

    /**
     * Gets a Button representing a place on the game board based on the game level.
     *
     * @param gameLevel   The level of the game ("Easy" or "Hard").
     * @param buttonHard  The button number for Hard level (0-8).
     * @return A Button representing an available place on the game board.
     */
    public Button getPlace(String gameLevel, int buttonHard) {
        Button button = null;
        Random random = new Random();
        int number = 0;

        // Loop until an available button is found
        while ((button == null || !button.getText().equals(" "))) {
            // Generate a random number for Easy level or use the specified button for Hard level
            if (gameLevel.equals("Easy")) {
                number = random.nextInt(9);
            } else if (gameLevel.equals("Hard")) {
                number = buttonHard;
            }

            // Find the corresponding button based on the generated or specified number
            switch (number) {
                case 0:
                    button = findViewById(R.id.place0_btn);
                    break;
                case 1:
                    button = findViewById(R.id.place1_btn);
                    break;
                case 2:
                    button = findViewById(R.id.place2_btn);
                    break;
                case 3:
                    button = findViewById(R.id.place3_btn);
                    break;
                case 4:
                    button = findViewById(R.id.place4_btn);
                    break;
                case 5:
                    button = findViewById(R.id.place5_btn);
                    break;
                case 6:
                    button = findViewById(R.id.place6_btn);
                    break;
                case 7:
                    button = findViewById(R.id.place7_btn);
                    break;
                case 8:
                    button = findViewById(R.id.place8_btn);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + number);
            }
        }
        return button;
    }

    /**
     * Checks for a win or draw based on the current state of the game board.
     *
     * @param xo The string representing X or O.
     * @return 0 if no win or draw, 1 if a win, 2 if a draw.
     */
    public int checkForWin(String xo) {
        int[] row = new int[3];
        int[] col = new int[3];
        int[] diagonal = new int[2];
        int draw = 0;

        // Iterate through the game board to check for wins and draws
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                // Check for matches in rows
                if (board[i][j].equals(xo)) {
                    row[i]++;
                }

                // Check for matches in columns
                if (board[j][i].equals(xo)) {
                    col[i]++;
                }

                // Check for matches in the main diagonal
                if (i == j && board[i][j].equals(xo)) {
                    diagonal[0]++;
                }

                // Check for matches in the other diagonal
                if (((i == 0 && j == 2) || (i == 1 && j == 1) || (i == 2 && j == 0)) && board[i][j].equals(xo)) {
                    diagonal[1]++;
                }

                // Check for a win in rows, columns, and diagonals
                if (row[i] == 3 || col[j] == 3 || diagonal[0] == 3 || diagonal[1] == 3) {
                    // Win
                    return 1;
                }

                // Check for a draw
                // getting here only if nothing return before (no win or lose)
                if (!board[i][j].equals(" ")) {
                    draw++;
                    if (draw == 9) {
                        // Draw
                        return 2;
                    }
                }
            }
        }

        // No win or draw
        return 0;
    }

}