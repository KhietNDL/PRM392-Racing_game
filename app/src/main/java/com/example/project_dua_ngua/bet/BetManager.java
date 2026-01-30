package com.example.project_dua_ngua.bet;

import java.util.ArrayList;
import java.util.List;

public class BetManager {
    public List<Bet> buildBets(int[] amounts) {
        List<Bet> bets = new ArrayList<>();
        for (int i = 0; i < amounts.length; i++) {
            int amount = amounts[i];
            if (amount > 0) {
                bets.add(new Bet(i, amount));
            }
        }
        return bets;
    }

    public int sumBets(List<Bet> bets) {
        int total = 0;
        for (Bet bet : bets) {
            total += bet.getAmount();
        }
        return total;
    }

    public BetResult resolveBets(Player player, int winnerIndex, double payoutMultiplier) {
        List<Bet> bets = player.getBets();
        int totalBet = sumBets(bets);
        int winningAmount = 0;
        for (Bet bet : bets) {
            if (bet.getHorseIndex() == winnerIndex) {
                winningAmount += bet.getAmount();
            }
        }
        int payout = (int) Math.round(winningAmount * payoutMultiplier);
        player.addMoney(payout);
        player.clearBets();
        return new BetResult(winnerIndex, totalBet, payout);
    }
}
