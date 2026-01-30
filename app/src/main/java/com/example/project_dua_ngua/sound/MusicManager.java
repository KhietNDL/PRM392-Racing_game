package com.example.project_dua_ngua.sound;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;

public class MusicManager {
	private static MusicManager instance;
	private MediaPlayer bgmPlayer;
	private SoundPool soundPool;
	private int currentBgmId = -1;
    private int mainBgmId = -1; // To keep track of the main background music
    private boolean isBgmPaused = false; // To track if BGM was paused by an effect

	private MusicManager() {
		// Private constructor for singleton
		AudioAttributes audioAttributes = new AudioAttributes.Builder()
				.setUsage(AudioAttributes.USAGE_GAME)
				.setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
				.build();
		soundPool = new SoundPool.Builder()
				.setMaxStreams(3)
				.setAudioAttributes(audioAttributes)
				.build();
	}

	public static synchronized MusicManager getInstance() {
		if (instance == null) {
			instance = new MusicManager();
		}
		return instance;
	}

	public void startBgm(Context context, int musicId) {
        // By default, consider it a main BGM if no main BGM is set
        startBgm(context, musicId, mainBgmId == -1);
    }

	public void startBgm(Context context, int musicId, boolean isMainBgm) {
		if (currentBgmId == musicId && bgmPlayer != null && bgmPlayer.isPlaying()) {
			return;
		}

		stopBgm();

		bgmPlayer = MediaPlayer.create(context.getApplicationContext(), musicId);
		if (bgmPlayer != null) {
			bgmPlayer.setLooping(true); // Ensure all background music loops
			bgmPlayer.start();
			currentBgmId = musicId;
            if (isMainBgm) {
                mainBgmId = musicId;
            }
		}
	}

	public void playEffect(Context context, int soundId) {
		if (soundPool != null) {
            if (bgmPlayer != null && bgmPlayer.isPlaying()) {
                pauseBgm();
                isBgmPaused = true; // Mark that BGM was paused for an effect
            }

			int loadedSoundId = soundPool.load(context, soundId, 1);
			soundPool.setOnLoadCompleteListener((soundPool, sampleId, status) -> {
				if (status == 0) { // Success
					soundPool.play(sampleId, 1.0f, 1.0f, 1, 0, 1.0f);
                    soundPool.setOnLoadCompleteListener(null); // Avoid multiple triggers

                    // This is tricky. We don't know when the sound effect ends.
                    // A better approach would be to manage this from the calling Activity
                    // if precise resume timing is needed. For now, we don't auto-resume
                    // to avoid cutting off the sound effect.
				}
			});
		}
	}

	public void pauseBgm() {
		if (bgmPlayer != null && bgmPlayer.isPlaying()) {
			bgmPlayer.pause();
		}
	}

	public void resumeBgm() {
        // Resume only if it was paused and not manually stopped
		if (bgmPlayer != null && !bgmPlayer.isPlaying()) {
			bgmPlayer.start();
            isBgmPaused = false;
		}
	}

	public void stopBgm() {
		if (bgmPlayer != null) {
			bgmPlayer.release();
			bgmPlayer = null;
			currentBgmId = -1;
		}
	}

	public void release() {
		stopBgm();
		if (soundPool != null) {
			soundPool.release();
			soundPool = null;
		}
	}

    // Restore the main BGM, for example after a temporary BGM (like win/lose) has finished.
    public void restoreMainBgm(Context context) {
        if (mainBgmId != -1) {
            startBgm(context, mainBgmId, true);
        }
    }

	@Deprecated
	public void start(Context context, int musicId) {
		startBgm(context, musicId, false);
	}

	@Deprecated
	public void pause() {
		pauseBgm();
	}

	@Deprecated
	public void resume() {
		resumeBgm();
	}

	@Deprecated
	public void stop() {
		stopBgm();
	}
}
