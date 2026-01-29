package com.example.project_dua_ngua.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project_dua_ngua.R;
import com.example.project_dua_ngua.sound.MusicManager;

public class WinnerActivity extends AppCompatActivity {

	private ImageView ivWinnerIcon;
	private ImageView ivWinnerImage;
	private TextView tvWinnerTitle;
	private Button btnNext;

	private int winnerId;
	private String winnerName;
	private int winnerImageRes;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_winner);

		// Initialize views
		initViews();

		// Get data from intent
		getIntentData();

		// Display winner info
		displayWinner();

		// Play congratulation BGM
		MusicManager.getInstance().startBgm(this, R.raw.congratulation_bgm);

		// Start entrance animation
		startAnimation();

		// Setup button click
		setupButtons();

		// Disable back button using OnBackPressedDispatcher
		getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
			@Override
			public void handleOnBackPressed() {
				// Do nothing - user must click Next button
			}
		});
	}

	private void initViews() {
		ivWinnerIcon = findViewById(R.id.ivWinnerIcon);
		ivWinnerImage = findViewById(R.id.ivWinnerImage);
		tvWinnerTitle = findViewById(R.id.tvWinnerTitle);
		btnNext = findViewById(R.id.btnNext);
	}

	private void getIntentData() {
		Intent intent = getIntent();
		winnerId = intent.getIntExtra("winnerId", 1);
		winnerName = intent.getStringExtra("winnerName");
		winnerImageRes = intent.getIntExtra("winnerImageRes", R.drawable.ic_launcher_foreground);

		if (winnerName == null) {
			winnerName = "Horse " + winnerId;
		}
	}

	private void displayWinner() {
		tvWinnerTitle.setText("Winner: " + winnerName);
		ivWinnerImage.setImageResource(winnerImageRes);
		ivWinnerIcon.setImageResource(R.drawable.ic_trophy);
	}

	private void startAnimation() {
		// Scale animation for winner image
		ScaleAnimation scaleAnimation = new ScaleAnimation(
				0.0f, 1.0f, 0.0f, 1.0f,
				Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f
		);
		scaleAnimation.setDuration(800);
		scaleAnimation.setFillAfter(true);

		// Fade in animation for title
		AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
		fadeIn.setDuration(1000);
		fadeIn.setStartOffset(300);

		ivWinnerImage.startAnimation(scaleAnimation);
		tvWinnerTitle.startAnimation(fadeIn);
	}

	private void setupButtons() {
		btnNext.setOnClickListener(v -> {
			// Pass data to BetResultActivity
			Intent intent = new Intent(WinnerActivity.this, BetResultActivity.class);
			intent.putExtra("winnerId", winnerId);
			intent.putExtra("winnerName", winnerName);

			// Get money and bet data from previous intent
			int moneyBefore = getIntent().getIntExtra("moneyBefore", 1000);
			int moneyAfter = getIntent().getIntExtra("moneyAfter", 1000);
			int betAmount = getIntent().getIntExtra("betAmount", 0);
			int betOnId = getIntent().getIntExtra("betOnId", -1);

			intent.putExtra("moneyBefore", moneyBefore);
			intent.putExtra("moneyAfter", moneyAfter);
			intent.putExtra("betAmount", betAmount);
			intent.putExtra("betOnId", betOnId);

			startActivity(intent);
			finish(); // Can't go back to winner screen
		});
	}

	@Override
	protected void onPause() {
		super.onPause();
		MusicManager.getInstance().pauseBgm();
	}

	@Override
	protected void onResume() {
		super.onResume();
		MusicManager.getInstance().resumeBgm();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// Don't stop BGM here, it will continue to BetResultActivity
	}
}
