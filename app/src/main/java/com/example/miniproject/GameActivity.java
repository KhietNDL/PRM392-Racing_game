package com.example.miniproject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {

    private ImageView horse1, horse2;
    private Button startButton, resetButton;
    private RaceEngine raceEngine;
    private View raceTrack1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        horse1 = findViewById(R.id.horse1);
        horse2 = findViewById(R.id.horse2);
        startButton = findViewById(R.id.startButton);
        resetButton = findViewById(R.id.resetButton);
        raceTrack1 = findViewById(R.id.raceTrack1);

        Handler handler = new Handler(Looper.getMainLooper());

        raceEngine = new RaceEngine(handler, new RaceEngine.RaceListener() {
            @Override
            public void onProgress(float horse1Progress, float horse2Progress) {
                float maxTranslation = raceTrack1.getWidth() - horse1.getWidth();
                horse1.setTranslationX(maxTranslation * horse1Progress);
                horse2.setTranslationX(maxTranslation * horse2Progress);
            }

            @Override
            public void onWinner(int winner) {
                Toast.makeText(GameActivity.this, "Horse " + winner + " wins!", Toast.LENGTH_SHORT).show();
                startButton.setEnabled(false);
            }
        });

        startButton.setOnClickListener(v -> {
            raceEngine.startRace();
            startButton.setEnabled(false);
            resetButton.setEnabled(true);
        });

        resetButton.setOnClickListener(v -> {
            raceEngine.resetRace();
            horse1.setTranslationX(0);
            horse2.setTranslationX(0);
            startButton.setEnabled(true);
            resetButton.setEnabled(false);
        });
    }
}