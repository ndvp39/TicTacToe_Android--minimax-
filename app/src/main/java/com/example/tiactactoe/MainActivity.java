package com.example.tiactactoe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private String gameLevel;
    private boolean canPlay;
    private ImageView arrow_image;
    private MediaPlayer mediaPlayer;
    private ImageView mute_image;
    private boolean isMuted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the content view to the activity_main layout
        setContentView(R.layout.activity_main);

        // Set the background drawable for the window
        getWindow().setBackgroundDrawableResource(R.drawable.background);

        // Initialize UI elements
        arrow_image = findViewById(R.id.arrow_image);
        mute_image = findViewById(R.id.mute_image);

        // Initialize mute state
        isMuted = false;

        // Create MediaPlayer and start playing background music
        mediaPlayer = MediaPlayer.create(this, R.raw.backgroundmusic);
        mediaPlayer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release the MediaPlayer resources when the activity is destroyed
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    /**
     * Handles the click event for the mute button.
     *
     * @param view The View that was clicked (in this case, the mute button).
     */
    public void onClickMute(View view) {
        // Check the current mute state
        if (isMuted) {
            // If already muted, set the unmuted image and adjust volume
            mute_image.setImageResource(R.drawable.unmuted);
            mediaPlayer.setVolume(0.5f, 0.5f);
        } else {
            // If not muted, set the muted image and mute the volume
            mute_image.setImageResource(R.drawable.muted);
            mediaPlayer.setVolume(0.0f, 0.0f);
        }

        // Toggle the mute state
        isMuted = !isMuted;
    }

    /**
     * Handles the click event for the "Start Game" button.
     *
     * @param view The View that was clicked (in this case, the "Start Game" button).
     */
    public void onClickStartGame(View view) {
        // Open the game activity
        openGameActivity();
    }

    /**
     * Handles the click event for the "Easy" button.
     *
     * @param view The View that was clicked (in this case, the "Easy" button).
     */
    public void onClickEasy(View view) {
        // Set the game level to "Easy"
        setGameLevel("Easy");
    }

    /**
     * Handles the click event for the "Hard" button.
     *
     * @param view The View that was clicked (in this case, the "Hard" button).
     */
    public void onClickHard(View view) {
        // Set the game level to "Hard"
        setGameLevel("Hard");
    }

    /**
     * Sets the game level and prepares for gameplay.
     *
     * @param level The chosen game level ("Easy" or "Hard").
     */
    public void setGameLevel(String level) {
        // Enable gameplay
        canPlay = true;

        // Set the game level
        gameLevel = level;

        // Change the image position based on the selected level
        changeImagePosition(level);

        // Make the arrow image visible
        arrow_image.setVisibility(View.VISIBLE);
    }

    /**
     * Opens the game activity if the game level is selected; otherwise, displays a toast message.
     */
    public void openGameActivity() {
        if (!canPlay) {
            // Display a toast message if the game level is not selected
            Toast.makeText(this, "Choose a game level, please", Toast.LENGTH_SHORT).show();
        } else {
            // Open the game activity with the selected game level
            Intent intent = new Intent(this, gameActivity.class);
            intent.putExtra("gameLevel", gameLevel);
            startActivity(intent);
        }
    }

    /**
     * Changes the position of the arrow image based on the selected game level.
     *
     * @param level The chosen game level ("Easy" or "Hard").
     */
    private void changeImagePosition(String level) {
        // Get the current layout parameters of the ImageView
        ViewGroup.LayoutParams params = arrow_image.getLayoutParams();

        // Check the type of the parent layout
        if (params instanceof RelativeLayout.LayoutParams) {
            // If the parent is a RelativeLayout, use RelativeLayout.LayoutParams
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) params;

            // Set new position values
            int newLeftMargin = 0;
            int newTopMargin = 0;

            if (level.equals("Easy")) {
                newLeftMargin = 600; // Change this to your desired left margin
                newTopMargin = 60; // Change this to your desired top margin
            }
            if (level.equals("Hard")) {
                newLeftMargin = 120; // Change this to your desired left margin
                newTopMargin = 60; // Change this to your desired top margin
            }

            // Update the layout parameters with the new position values
            layoutParams.leftMargin = newLeftMargin;
            layoutParams.topMargin = newTopMargin;

            // Apply the updated layout parameters to the ImageView
            arrow_image.setLayoutParams(layoutParams);
        } else if (params instanceof ConstraintLayout.LayoutParams) {
            // If the parent is a ConstraintLayout, use ConstraintLayout.LayoutParams
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) params;

            // Set new position values
            int newStartMargin = 0;
            int newTopMargin = 0;

            if (level.equals("Easy")) {
                newStartMargin = 600; // Change this to your desired left margin
                newTopMargin = 60; // Change this to your desired top margin
            }
            if (level.equals("Hard")) {
                newStartMargin = 120; // Change this to your desired left margin
                newTopMargin = 60; // Change this to your desired top margin
            }

            // Update the layout parameters with the new position values
            layoutParams.startToStart = ConstraintSet.PARENT_ID;
            layoutParams.topToTop = ConstraintSet.PARENT_ID;
            layoutParams.setMarginStart(newStartMargin);
            layoutParams.topMargin = newTopMargin;

            // Apply the updated layout parameters to the ImageView
            arrow_image.setLayoutParams(layoutParams);
        }
    }



}