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
import com.example.project_dua_ngua.auth.LoginActivity;
import com.example.project_dua_ngua.sound.MusicManager;

public class WinnerActivity extends AppCompatActivity {

	private ImageView ivWinnerIcon;
	private ImageView ivWinnerImage;
	private TextView tvWinnerTitle;
	private Button btnNext;
	private Button btnBackToLogin;

	private int winnerId;
	private String winnerName;
	private int winnerImageRes;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_winner);

		initViews();
		getIntentData();
		displayWinner();

		int betOnId = getIntent().getIntExtra("betOnId", -1);
		boolean isWin = (betOnId == winnerId);

		MusicManager.getInstance().startBgm(this, isWin ? R.raw.win_sound : R.raw.lose_sound, false);

		startAnimation();
		setupButtons();

		getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
			@Override
			public void handleOnBackPressed() {
				// Do nothing
			}
		});
	}

	private void initViews() {
		ivWinnerIcon = findViewById(R.id.ivWinnerIcon);
		ivWinnerImage = findViewById(R.id.ivWinnerImage);
		tvWinnerTitle = findViewById(R.id.tvWinnerTitle);
		btnNext = findViewById(R.id.btnNext);
		btnBackToLogin = findViewById(R.id.btnBackToLogin);
	}

	private void getIntentData() {
		Intent intent = getIntent();
		winnerId = intent.getIntExtra("winnerId", 1);
		winnerName = intent.getStringExtra("winnerName");
		winnerImageRes = intent.getIntExtra("winnerImageRes", R.drawable.ic_launcher_foreground);

		if (winnerName == null) {
		    switch(winnerId) {
		        case 1:
		            winnerName = "Gold Ship";
		            break;
		        case 2:
		            winnerName = "Maruzensky";
		            break;
		        case 3:
		            winnerName = "Oguri Cap";
		            break;
		        default:
		            winnerName = "Horse " + winnerId;
		    }
		}
	}

	private void displayWinner() {
		tvWinnerTitle.setText("Winner: " + winnerName);
		ivWinnerImage.setImageResource(winnerImageRes);
		ivWinnerIcon.setImageResource(R.drawable.ic_trophy);
	}

	private void startAnimation() {
		ScaleAnimation scaleAnimation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		scaleAnimation.setDuration(800);
		scaleAnimation.setFillAfter(true);

		AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
		fadeIn.setDuration(1000);
		fadeIn.setStartOffset(300);

		ivWinnerImage.startAnimation(scaleAnimation);
		tvWinnerTitle.startAnimation(fadeIn);
	}

	private void setupButtons() {
		btnNext.setOnClickListener(v -> {
            MusicManager.getInstance().restoreMainBgm(this);

			Intent intent = new Intent(WinnerActivity.this, BetResultActivity.class);
			intent.putExtra("winnerId", winnerId);
			intent.putExtra("winnerName", winnerName);
			intent.putExtra("moneyBefore", getIntent().getIntExtra("moneyBefore", 1000));
			intent.putExtra("moneyAfter", getIntent().getIntExtra("moneyAfter", 1000));
			intent.putExtra("betAmount", getIntent().getIntExtra("betAmount", 0));
			intent.putExtra("betOnId", getIntent().getIntExtra("betOnId", -1));

			startActivity(intent);
			finish();
		});

		btnBackToLogin.setOnClickListener(v -> {
            MusicManager.getInstance().restoreMainBgm(this);

			Intent intent = new Intent(WinnerActivity.this, LoginActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			finish();
		});
	}
}
