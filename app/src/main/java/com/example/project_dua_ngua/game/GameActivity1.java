package com.example.project_dua_ngua.game;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_dua_ngua.R;

public class GameActivity1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Button playGameButton = findViewById(R.id.playGameButton);

        playGameButton.setOnClickListener(v -> {
            // Start RaceActivity when the button is clicked
            Intent intent = new Intent(GameActivity1.this, RaceActivity.class);
            startActivity(intent);
        });
    }
}
