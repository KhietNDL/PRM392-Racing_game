package com.example.project_dua_ngua.game;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_dua_ngua.R;

import java.util.Random;

public class RaceActivity extends AppCompatActivity {

    private ImageView horse1, horse2;
    private Button startButton, resetButton;

    private Handler handler = new Handler();
    private Runnable raceRunnable;
    private boolean isRacing = false;
    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race);

        horse1 = findViewById(R.id.horse1);
        horse2 = findViewById(R.id.horse2);
        startButton = findViewById(R.id.startButton);
        resetButton = findViewById(R.id.resetButton);

        startButton.setOnClickListener(v -> startRace());
        resetButton.setOnClickListener(v -> resetRace());
    }

    private void startRace() {
        if (isRacing) return;
        isRacing = true;
        startButton.setEnabled(false);
        resetButton.setEnabled(false);

        raceRunnable = new Runnable() {
            @Override
            public void run() {
                if (!isRacing) return;

                // Move horses
                horse1.setTranslationX(horse1.getTranslationX() + random.nextInt(20) + 10);
                horse2.setTranslationX(horse2.getTranslationX() + random.nextInt(20) + 10);

                // Check for winner
                float raceTrackWidth = findViewById(R.id.raceTrack1).getWidth();
                if (horse1.getTranslationX() + horse1.getWidth() > raceTrackWidth) {
                    endRace("Horse 1 Wins!");
                    return;
                }
                if (horse2.getTranslationX() + horse2.getWidth() > raceTrackWidth) {
                    endRace("Horse 2 Wins!");
                    return;
                }

                handler.postDelayed(this, 100);
            }
        };

        handler.post(raceRunnable);
    }

    private void endRace(String winnerMessage) {
        isRacing = false;
        handler.removeCallbacks(raceRunnable);
        Toast.makeText(this, winnerMessage, Toast.LENGTH_SHORT).show();
        startButton.setEnabled(true);
        resetButton.setEnabled(true);
    }

    private void resetRace() {
        isRacing = false;
        handler.removeCallbacks(raceRunnable);
        horse1.setTranslationX(0);
        horse2.setTranslationX(0);
        startButton.setEnabled(true);
        resetButton.setEnabled(true);
    }
}
