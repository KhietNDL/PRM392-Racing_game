package com.example.project_dua_ngua.bet;

public class Bet {
    private final int horseIndex;
    private final int amount;

    public Bet(int horseIndex, int amount) {
        this.horseIndex = horseIndex;
        this.amount = amount;
    }

    public int getHorseIndex() {
        return horseIndex;
    }

    public int getAmount() {
        return amount;
    }
}
