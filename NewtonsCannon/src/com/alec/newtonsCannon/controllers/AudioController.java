package com.alec.newtonsCannon.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class AudioController {
	private Sound falling, score, startUp, p1FlipperHit, p2FlipperHit;
	private boolean isEnabled;
	
	public AudioController(boolean isEnabled) {
		this.isEnabled = isEnabled;
		//public static Music song = Gdx.audio.newMusic(Gdx.files.internal("data/determination.mp3"));
		falling = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/falling.wav"));
		score = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/score.mp3"));
		p1FlipperHit = Gdx.audio.newSound((Gdx.files.internal(("data/Sounds/p1FlipperHit.mp3"))));
		p2FlipperHit = Gdx.audio.newSound((Gdx.files.internal(("data/Sounds/p2FlipperHit.mp3"))));
		startUp = Gdx.audio.newSound((Gdx.files.internal(("data/Sounds/startUp.mp3"))));
		
	}

	public void p1FlipperHit() {
		if (isEnabled)
			p1FlipperHit.play();
	}
	public void p2FlipperHit() {
		if (isEnabled)
			p2FlipperHit.play();
	}
		
	public void startUp() {
		if (isEnabled)
			startUp.play();
	}
	
	
	public void falling() {
		if (isEnabled)
			falling.play();
	}
	public void score() {
		if (isEnabled)
			score.play();
	}
	
	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	/** /
	public static void playMusic(boolean looping) {
		song.setLooping(looping);
		song.play();
	}
	
	public static void stopMusic() {
		song.stop();
	}
	
	public static void pauseMusic() {
		song.pause();
	}
	/**/
	
	public void dispose() {
		//song.dispose();
		falling.dispose();
		score.dispose();
	}
}
