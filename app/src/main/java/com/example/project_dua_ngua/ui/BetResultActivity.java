package com.example.project_dua_ngua.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.project_dua_ngua.R;
import com.example.project_dua_ngua.game.MainActivity;
import com.example.project_dua_ngua.sound.MusicManager;

public class BetResultActivity extends AppCompatActivity {

	private TextView tvResultTitle;
	private TextView tvMoneyBefore;
	private TextView tvMoneyChange;
	private TextView tvMoneyAfter;
	private ImageView ivResultIcon;
	private Button btnHome;
	private Button btnPlayAgain;

	private int winnerId;
	private int betOnId;
	private int moneyBefore;
	private int moneyAfter;
	private boolean isWin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bet_result);

		// Initialize views
		initViews();

		// Get data from intent
		getIntentData();

		// Calculate win/loss
		calculateResult();

		// Display result
		displayResult();

		// Setup buttons
		setupButtons();

		// Disable back button using OnBackPressedDispatcher
		getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
			@Override
			public void handleOnBackPressed() {
				// Do nothing - user must use Home or Play Again buttons
			}
		});
	}

	private void initViews() {
		tvResultTitle = findViewById(R.id.tvResultTitle);
		tvMoneyBefore = findViewById(R.id.tvMoneyBefore);
		tvMoneyChange = findViewById(R.id.tvMoneyChange);
		tvMoneyAfter = findViewById(R.id.tvMoneyAfter);
		ivResultIcon = findViewById(R.id.ivResultIcon);
		btnHome = findViewById(R.id.btnHome);
		btnPlayAgain = findViewById(R.id.btnPlayAgain);
	}

	private void getIntentData() {
		Intent intent = getIntent();
		winnerId = intent.getIntExtra("winnerId", 1);
		betOnId = intent.getIntExtra("betOnId", -1);
		moneyBefore = intent.getIntExtra("moneyBefore", 1000);
		moneyAfter = intent.getIntExtra("moneyAfter", 1000);
	}

	private void calculateResult() {
		isWin = (betOnId == winnerId);
	}

	private void displayResult() {
		int moneyChange = moneyAfter - moneyBefore;

		if (isWin) {
			// Player won
			tvResultTitle.setText("Congratulations! You Won!");
			tvResultTitle.setTextColor(ContextCompat.getColor(this, R.color.win_green));
			ivResultIcon.setImageResource(R.drawable.ic_win);
		} else {
			// Player lost
			tvResultTitle.setText("Better Luck Next Time!");
			tvResultTitle.setTextColor(ContextCompat.getColor(this, R.color.lose_red));
			ivResultIcon.setImageResource(R.drawable.ic_lose);
		}

		// Display money before (white color)
		tvMoneyBefore.setText("Previous: $" + moneyBefore);
		tvMoneyBefore.setTextColor(ContextCompat.getColor(this, R.color.white));

		// Display money change (green for profit, red for loss)
		if (moneyChange > 0) {
			tvMoneyChange.setText("+" + moneyChange);
			tvMoneyChange.setTextColor(ContextCompat.getColor(this, R.color.win_green));
		} else if (moneyChange < 0) {
			tvMoneyChange.setText(String.valueOf(moneyChange));
			tvMoneyChange.setTextColor(ContextCompat.getColor(this, R.color.lose_red));
		} else {
			tvMoneyChange.setText("±0");
			tvMoneyChange.setTextColor(ContextCompat.getColor(this, R.color.white));
		}

		// Display money after (green if higher, red if lower, white if same)
		tvMoneyAfter.setText("Current: $" + moneyAfter);
		if (moneyAfter > moneyBefore) {
			tvMoneyAfter.setTextColor(ContextCompat.getColor(this, R.color.win_green));
		} else if (moneyAfter < moneyBefore) {
			tvMoneyAfter.setTextColor(ContextCompat.getColor(this, R.color.lose_red));
		} else {
			tvMoneyAfter.setTextColor(ContextCompat.getColor(this, R.color.white));
		}
	}

	private void setupButtons() {
		// Home button - go to MainActivity
		btnHome.setOnClickListener(v -> {
			// Stop congratulation BGM before going home
			MusicManager.getInstance().stopBgm();

			Intent intent = new Intent(BetResultActivity.this, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			finish();
		});

		// Play Again button - go back to betting/game screen
		btnPlayAgain.setOnClickListener(v -> {
			// Stop congratulation BGM
			MusicManager.getInstance().stopBgm();

			// Go to betting screen or game screen
			// Assuming there's a BettingActivity or go back to MainActivity
			Intent intent = new Intent(BetResultActivity.this, MainActivity.class);
			intent.putExtra("currentMoney", moneyAfter); // Pass updated money
			startActivity(intent);
			finish();
		});
	}

	@Override
	protected void onPause() {
		super.onPause();
		// Don't pause BGM here, let it continue playing
	}

	@Override
	protected void onResume() {
		super.onResume();
		// BGM should still be playing from WinnerActivity
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// BGM will be stopped when user clicks home or play again
	}
}
