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
     * Handles the click event for the "Easy" button.
     *
     * @param view The View that was clicked (in this case, the "Easy" button).
     */
    public void onClickEasy(View view) {
        // Set the game level to "Easy"
        setGameLevel("Easy");
        openGameActivity();
    }

    /**
     * Handles the click event for the "Hard" button.
     *
     * @param view The View that was clicked (in this case, the "Hard" button).
     */
    public void onClickHard(View view) {
        // Set the game level to "Hard"
        setGameLevel("Hard");
        openGameActivity();
    }

    /**
     * Handles the click event for the "Extreme" button.
     *
     * @param view The View that was clicked (in this case, the "Extreme" button).
     */
    public void onClickExtreme(View view) {
        // Set the game level to "Hard"
        setGameLevel("Extreme");
        openGameActivity();
    }

    /**
     * Sets the game level and prepares for gameplay.
     *
     * @param level The chosen game level ("Easy" or "Hard" or "Extreme").
     */
    public void setGameLevel(String level) {
        // Set the game level
        gameLevel = level;
    }

    /**
     * Opens the game activity when the game level is selected
     */
    public void openGameActivity() {
        // Open the game activity with the selected game level
        Intent intent = new Intent(this, gameActivity.class);
        intent.putExtra("gameLevel", gameLevel);
        startActivity(intent);
    }

}