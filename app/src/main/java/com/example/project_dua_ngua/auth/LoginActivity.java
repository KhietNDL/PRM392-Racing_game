package com.example.project_dua_ngua.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_dua_ngua.R;
import com.example.project_dua_ngua.game.GameActivity1;
import com.example.project_dua_ngua.sound.MusicManager;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword;
    private Button buttonLogin;
    private TextView textViewRegister;

    // Hardcoded credentials
    private final String HARDCODED_USERNAME = "admin";
    private final String HARDCODED_PASSWORD = "admin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewRegister = findViewById(R.id.textViewRegister);

        // Start background music using MusicManager
        MusicManager.getInstance().start(this, R.raw.login_sound);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();

                if (username.equals(HARDCODED_USERNAME) && password.equals(HARDCODED_PASSWORD)) {
                    Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();

                    // Stop the music when logging in to the main app
                    MusicManager.getInstance().stop();

                    Intent intent = new Intent(LoginActivity.this, GameActivity1.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Music will continue to play when going to RegisterActivity
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Resume music if it was paused
        MusicManager.getInstance().resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Pause music when the activity is not in the foreground
        // but don't stop it if we are just switching activities.
        if (!isFinishing()) {
            MusicManager.getInstance().pause();
        }
    }
}
