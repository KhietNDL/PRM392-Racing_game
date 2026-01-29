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

	private MusicManager() {
		// Private constructor for singleton
		// Initialize SoundPool for sound effects
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

	/**
	 * Start background music (looped)
	 */
	public void startBgm(Context context, int musicId) {
		// If same music is already playing, do nothing
		if (currentBgmId == musicId && bgmPlayer != null && bgmPlayer.isPlaying()) {
			return;
		}

		// Stop previous BGM if exists
		stopBgm();

		// Create and start new BGM
		bgmPlayer = MediaPlayer.create(context.getApplicationContext(), musicId);
		if (bgmPlayer != null) {
			bgmPlayer.setLooping(true);
			bgmPlayer.start();
			currentBgmId = musicId;
		}
	}

	/**
	 * Play a short sound effect (non-looped)
	 */
	public void playEffect(Context context, int soundId) {
		if (soundPool != null) {
			int loadedSoundId = soundPool.load(context, soundId, 1);
			soundPool.setOnLoadCompleteListener((soundPool, sampleId, status) -> {
				if (status == 0) { // Success
					soundPool.play(sampleId, 1.0f, 1.0f, 1, 0, 1.0f);
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
		if (bgmPlayer != null && !bgmPlayer.isPlaying()) {
			bgmPlayer.start();
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

	// Legacy method for backward compatibility
	@Deprecated
	public void start(Context context, int musicId) {
		startBgm(context, musicId);
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