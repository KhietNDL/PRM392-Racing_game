package com.example.project_dua_ngua.bet;

public class BetResult {
    private final int winnerIndex;
    private final int totalBet;
    private final int payout;
    private final int netChange;

    public BetResult(int winnerIndex, int totalBet, int payout) {
        this.winnerIndex = winnerIndex;
        this.totalBet = totalBet;
        this.payout = payout;
        this.netChange = payout - totalBet;
    }

    public int getWinnerIndex() {
        return winnerIndex;
    }

    public int getTotalBet() {
        return totalBet;
    }

    public int getPayout() {
        return payout;
    }

    public int getNetChange() {
        return netChange;
    }
}
