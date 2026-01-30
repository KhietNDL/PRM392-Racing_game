package com.example.project_dua_ngua.bet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Player {
    private int money;
    private int totalBetAmount;
    private int totalPayout;
    private final List<Bet> bets = new ArrayList<>();

    public Player(int startingMoney) {
        this.money = startingMoney;
    }

    public int getMoney() {
        return money;
    }

    public int getTotalBetAmount() {
        return totalBetAmount;
    }

    public int getTotalPayout() {
        return totalPayout;
    }

    public List<Bet> getBets() {
        return Collections.unmodifiableList(bets);
    }

    public void clearBets() {
        bets.clear();
    }

    public boolean canAfford(int totalBet) {
        return totalBet <= money;
    }

    public void placeBets(List<Bet> newBets, int totalBet) {
        bets.clear();
        bets.addAll(newBets);
        money -= totalBet;
        totalBetAmount += totalBet;
    }

    public void addMoney(int amount) {
        money += amount;
        totalPayout += amount;
    }
}
