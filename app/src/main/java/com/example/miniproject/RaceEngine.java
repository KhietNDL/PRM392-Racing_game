package com.example.miniproject;

import android.os.Handler;

import java.util.Random;

public class RaceEngine {

    public interface RaceListener {
        void onProgress(float horse1Progress, float horse2Progress);
        void onWinner(int winner);
    }

    private static final int MAX_PROGRESS = 1000;

    private Handler handler;
    private RaceListener listener;
    private Random random = new Random();
    private int horse1Progress = 0;
    private int horse2Progress = 0;
    private boolean raceRunning = false;

    private Runnable raceRunnable = new Runnable() {
        @Override
        public void run() {
            if (!raceRunning) {
                return;
            }

            horse1Progress += random.nextInt(20);
            horse2Progress += random.nextInt(20);

            if (listener != null) {
                listener.onProgress((float) horse1Progress / MAX_PROGRESS, (float) horse2Progress / MAX_PROGRESS);
            }

            if (horse1Progress >= MAX_PROGRESS) {
                if (listener != null) {
                    listener.onWinner(1);
                }
                raceRunning = false;
            } else if (horse2Progress >= MAX_PROGRESS) {
                if (listener != null) {
                    listener.onWinner(2);
                }
                raceRunning = false;
            }

            if (raceRunning) {
                handler.postDelayed(this, 100);
            }
        }
    };

    public RaceEngine(Handler handler, RaceListener listener) {
        this.handler = handler;
        this.listener = listener;
    }

    public void startRace() {
        if (!raceRunning) {
            raceRunning = true;
            handler.post(raceRunnable);
        }
    }

    public void resetRace() {
        raceRunning = false;
        handler.removeCallbacks(raceRunnable);
        horse1Progress = 0;
        horse2Progress = 0;
    }
}