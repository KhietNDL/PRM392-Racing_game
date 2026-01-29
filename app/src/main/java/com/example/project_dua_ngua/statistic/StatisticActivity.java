package com.example.project_dua_ngua.statistic;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_dua_ngua.R;
import com.example.project_dua_ngua.game.RaceActivity;

public class StatisticActivity extends AppCompatActivity {

    private TextView tvTotalRaces;
    private TextView tvTotalWins;
    private TextView tvTotalMoney;
    private Button btnBack;
    private GameData gameData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        gameData = GameData.getInstance(this);

        initViews();
        displayStats();
        setupListeners();
    }

    private void initViews() {
        tvTotalRaces = findViewById(R.id.tvTotalRaces);
        tvTotalWins = findViewById(R.id.tvTotalWins);
        tvTotalMoney = findViewById(R.id.tvTotalMoney);
        btnBack = findViewById(R.id.btnBack);
    }

    private void displayStats() {
        tvTotalRaces.setText(String.valueOf(gameData.getTotalRaces()));
        tvTotalWins.setText(String.valueOf(gameData.getTotalWins()));
        tvTotalMoney.setText("$" + gameData.getTotalMoney());
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> {
            // Quay trở lại RaceActivity và xóa các activity trung gian (BetResult, Winner)
            Intent intent = new Intent(StatisticActivity.this, RaceActivity.class);
            intent.putExtra("currentMoney", gameData.getTotalMoney());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }
}
