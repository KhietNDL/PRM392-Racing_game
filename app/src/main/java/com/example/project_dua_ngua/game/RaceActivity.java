package com.example.project_dua_ngua.game;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_dua_ngua.R;
import com.example.project_dua_ngua.bet.Bet;
import com.example.project_dua_ngua.bet.BetManager;
import com.example.project_dua_ngua.bet.BetResult;
import com.example.project_dua_ngua.bet.Player;
import com.example.project_dua_ngua.ui.WinnerActivity;
import com.example.project_dua_ngua.sound.MusicManager;

import java.util.List;
import java.util.Random;

public class RaceActivity extends AppCompatActivity {

    private static final int STARTING_MONEY = 1000;
    private static final int PAYOUT_MULTIPLIER = 2;

    private TextView textMoney;
    private TextView textStatus;
    private EditText editBetHorse1;
    private EditText editBetHorse2;
    private View raceTrack1;
    private View raceTrack2;
    private ImageView horse1;
    private ImageView horse2;
    private Button startButton;
    private Button resetButton;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable raceRunnable;
    private boolean isRacing = false;
    private boolean winnerDeclared = false;
    private final Random random = new Random();
    private final BetManager betManager = new BetManager();
    private Player player;

    // Variables to track bet information for WinnerActivity
    private int moneyBeforeRace;
    private int totalBetAmount;
    private int betOnHorseId = -1; // -1 means bet on multiple horses

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race);

        textMoney = findViewById(R.id.textMoney);
        textStatus = findViewById(R.id.textStatus);
        editBetHorse1 = findViewById(R.id.editBetHorse1);
        editBetHorse2 = findViewById(R.id.editBetHorse2);
        raceTrack1 = findViewById(R.id.raceTrack1);
        raceTrack2 = findViewById(R.id.raceTrack2);
        horse1 = findViewById(R.id.horse1);
        horse2 = findViewById(R.id.horse2);
        startButton = findViewById(R.id.startButton);
        resetButton = findViewById(R.id.resetButton);

        // Check if money is passed from previous race
        int currentMoney = getIntent().getIntExtra("currentMoney", STARTING_MONEY);
        player = new Player(currentMoney);
        updateMoneyText();

        startButton.setOnClickListener(v -> startRace());
        resetButton.setOnClickListener(v -> resetRace());
    }

    private void startRace() {
        if (isRacing) {
            Toast.makeText(this, "Race is running", Toast.LENGTH_SHORT).show();
            return;
        }

        int betHorse1 = parseAmount(editBetHorse1);
        int betHorse2 = parseAmount(editBetHorse2);
        List<Bet> bets = betManager.buildBets(new int[]{betHorse1, betHorse2});
        int totalBet = betManager.sumBets(bets);
        if (totalBet <= 0) {
            Toast.makeText(this, "Please place at least one bet", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!player.canAfford(totalBet)) {
            Toast.makeText(this, "Not enough money", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save money before race and bet info
        moneyBeforeRace = player.getMoney();
        totalBetAmount = totalBet;

        // Determine which horse was bet on
        if (betHorse1 > 0 && betHorse2 == 0) {
            betOnHorseId = 0; // Bet on Horse 1 only
        } else if (betHorse2 > 0 && betHorse1 == 0) {
            betOnHorseId = 1; // Bet on Horse 2 only
        } else {
            betOnHorseId = -1; // Bet on both horses
        }

        player.placeBets(bets, totalBet);
        updateMoneyText();
        setBetInputsEnabled(false);
        resetHorsePositions();

        // Play racing music
        MusicManager.getInstance().startBgm(this, R.raw.race_sound);

        isRacing = true;
        winnerDeclared = false;
        startButton.setEnabled(false);
        resetButton.setEnabled(false);
        textStatus.setText("Race started...");

        raceRunnable = new Runnable() {
            @Override
            public void run() {
                if (!isRacing) return;

                // Move horses
                horse1.setTranslationX(horse1.getTranslationX() + random.nextInt(20) + 10);
                horse2.setTranslationX(horse2.getTranslationX() + random.nextInt(20) + 10);

                // Check for winner
                float finishLine1 = Math.max(0, raceTrack1.getWidth() - horse1.getWidth());
                float finishLine2 = Math.max(0, raceTrack2.getWidth() - horse2.getWidth());
                if (!winnerDeclared && horse1.getTranslationX() >= finishLine1) {
                    winnerDeclared = true;
                    endRace(0);
                    return;
                }
                if (!winnerDeclared && horse2.getTranslationX() >= finishLine2) {
                    winnerDeclared = true;
                    endRace(1);
                    return;
                }

                handler.postDelayed(this, 100);
            }
        };

        handler.post(raceRunnable);
    }

    private void endRace(int winnerIndex) {
        isRacing = false;
        handler.removeCallbacks(raceRunnable);

        // Stop racing music
        MusicManager.getInstance().stopBgm();

        // Calculate payout and update money
        BetResult result = betManager.resolveBets(player, winnerIndex, PAYOUT_MULTIPLIER);
        int moneyAfterRace = player.getMoney();

        // Prepare winner data
        String winnerName = winnerIndex == 0 ? "Horse 1" : "Horse 2";
        int winnerImageRes = R.drawable.ic_horse; // Use horse drawable

        // Determine the main bet horse ID for result display
        int mainBetHorseId;
        if (betOnHorseId != -1) {
            mainBetHorseId = betOnHorseId + 1; // Convert to 1-based (Horse 1 = 1, Horse 2 = 2)
        } else {
            // If bet on multiple, use the winner as main bet for display
            mainBetHorseId = winnerIndex + 1;
        }

        // Navigate to WinnerActivity immediately after race ends
        Intent intent = new Intent(RaceActivity.this, WinnerActivity.class);
        intent.putExtra("winnerId", winnerIndex + 1); // Horse 1 = id 1, Horse 2 = id 2
        intent.putExtra("winnerName", winnerName);
        intent.putExtra("winnerImageRes", winnerImageRes);
        intent.putExtra("moneyBefore", moneyBeforeRace);
        intent.putExtra("moneyAfter", moneyAfterRace);
        intent.putExtra("betAmount", totalBetAmount);
        intent.putExtra("betOnId", mainBetHorseId);

        startActivity(intent);
        finish(); // Close RaceActivity so user can't go back with back button
    }

    private void resetRace() {
        if (isRacing) {
            Toast.makeText(this, "Please wait for the race to finish", Toast.LENGTH_SHORT).show();
            return;
        }
        isRacing = false;
        winnerDeclared = false;
        handler.removeCallbacks(raceRunnable);
        resetHorsePositions();
        clearBetInputs();
        setBetInputsEnabled(true);
        textStatus.setText("");
        startButton.setEnabled(true);
        resetButton.setEnabled(true);
    }

    private void updateMoneyText() {
        textMoney.setText("Money: " + player.getMoney());
    }

    private int parseAmount(EditText editText) {
        String text = editText.getText().toString().trim();
        if (text.isEmpty()) {
            return 0;
        }
        try {
            int value = Integer.parseInt(text);
            return Math.max(value, 0);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void clearBetInputs() {
        editBetHorse1.setText("");
        editBetHorse2.setText("");
    }

    private void setBetInputsEnabled(boolean enabled) {
        editBetHorse1.setEnabled(enabled);
        editBetHorse2.setEnabled(enabled);
    }

    private void resetHorsePositions() {
        horse1.setTranslationX(0f);
        horse2.setTranslationX(0f);
    }
}
