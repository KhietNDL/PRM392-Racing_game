package com.example.project_dua_ngua.game;

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

import java.util.List;
import java.util.Random;

public class RaceActivity extends AppCompatActivity {

    private static final int STARTING_MONEY = 1000;
    private static final double PAYOUT_MULTIPLIER = 1.5;

    private TextView textMoney;
    private TextView textStatus;
    private EditText editTopUpAmount;
    private Button buttonTopUp;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race);

        textMoney = findViewById(R.id.textMoney);
        textStatus = findViewById(R.id.textStatus);
        editTopUpAmount = findViewById(R.id.editTopUpAmount);
        buttonTopUp = findViewById(R.id.buttonTopUp);
        editBetHorse1 = findViewById(R.id.editBetHorse1);
        editBetHorse2 = findViewById(R.id.editBetHorse2);
        raceTrack1 = findViewById(R.id.raceTrack1);
        raceTrack2 = findViewById(R.id.raceTrack2);
        horse1 = findViewById(R.id.horse1);
        horse2 = findViewById(R.id.horse2);
        startButton = findViewById(R.id.startButton);
        resetButton = findViewById(R.id.resetButton);

        player = new Player(STARTING_MONEY);
        updateMoneyText();

        buttonTopUp.setOnClickListener(v -> topUpMoney());
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

        player.placeBets(bets, totalBet);
        updateMoneyText();
        setBetInputsEnabled(false);
        resetHorsePositions();

        isRacing = true;
        winnerDeclared = false;
        startButton.setEnabled(false);
        resetButton.setEnabled(false);
        setTopUpEnabled(false);
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
        startButton.setEnabled(true);
        resetButton.setEnabled(true);
        setTopUpEnabled(true);
        setBetInputsEnabled(true);

        BetResult result = betManager.resolveBets(player, winnerIndex, PAYOUT_MULTIPLIER);
        updateMoneyText();

        String winnerName = winnerIndex == 0 ? "Horse 1" : "Horse 2";
        textStatus.setText("Winner: " + winnerName + " | Net: " + result.getNetChange());
        Toast.makeText(this, winnerName + " wins!", Toast.LENGTH_SHORT).show();
        clearBetInputs();
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
        setTopUpEnabled(true);
    }

    private void topUpMoney() {
        if (isRacing) {
            Toast.makeText(this, "Please wait for the race to finish", Toast.LENGTH_SHORT).show();
            return;
        }
        int amount = parseAmount(editTopUpAmount);
        if (amount <= 0) {
            Toast.makeText(this, "Please enter a valid top up amount", Toast.LENGTH_SHORT).show();
            return;
        }
        player.addMoney(amount);
        updateMoneyText();
        editTopUpAmount.setText("");
        Toast.makeText(this, "Added " + amount, Toast.LENGTH_SHORT).show();
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

    private void setTopUpEnabled(boolean enabled) {
        editTopUpAmount.setEnabled(enabled);
        buttonTopUp.setEnabled(enabled);
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
