package com.example.project_dua_ngua.statistic;

import android.content.Context;

public class GameData {
    private static final int INITIAL_MONEY = 1000;

    private static GameData instance;
    private int totalRaces = 0;
    private int totalWins = 0;
    private int currentMoney = INITIAL_MONEY;

    private GameData() {
    }

    public static synchronized GameData getInstance(Context context) {
        if (instance == null) {
            instance = new GameData();
        }
        return instance;
    }

    public int getTotalRaces() {
        return totalRaces;
    }

    public int getTotalWins() {
        return totalWins;
    }

    public int getTotalMoney() {
        return currentMoney;
    }

    public void recordRace(boolean isWin, int money) {
        totalRaces++;
        if (isWin) {
            totalWins++;
        }
        currentMoney = money;
    }

    public void updateMoney(int money) {
        currentMoney = money;
    }
}
