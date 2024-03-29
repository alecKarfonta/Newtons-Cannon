package com.alec.newtonsCannon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.MathUtils;

public class GamePreferences {
	public static final String TAG = GamePreferences.class.getName();
	public static final GamePreferences instance = new GamePreferences();
	public boolean sound;
	public boolean music;
	public float volSound;
	public float volMusic;
	public boolean showFpsCounter;
	public boolean useAccelerometer;
	
	private Preferences prefs;
	public boolean useMonochromeShader;

	// singleton: prevent instantiation from other classes
	private GamePreferences() {
		prefs = Gdx.app.getPreferences(Constants.PREFERENCES);
	}

	public void load() {
		sound = prefs.getBoolean("sound", true);
		music = prefs.getBoolean("music", true);
		useAccelerometer = prefs.getBoolean("useAccelerometer", true);
		volSound = MathUtils
				.clamp(prefs.getFloat("volSound", 0.5f), 0.0f, 1.0f);
		volMusic = MathUtils
				.clamp(prefs.getFloat("volMusic", 0.5f), 0.0f, 1.0f);
		showFpsCounter = prefs.getBoolean("showFpsCounter", false);
		useMonochromeShader = prefs.getBoolean("useMonochromeShader", false);
	}

	public void save() {
		prefs.putBoolean("sound", sound);
		prefs.putBoolean("music", music);
		prefs.putFloat("volSound", volSound);
		prefs.putFloat("volMusic", volMusic);
		prefs.putBoolean("showFpsCounter", showFpsCounter);
		prefs.putBoolean("useAccelerometer", useAccelerometer);
		prefs.putBoolean("useMonochromeShader", useMonochromeShader);
		prefs.flush();
	}
}