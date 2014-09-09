package com.alec.newtonsCannon;

import com.alec.newtonsCannon.models.Assets;
import com.alec.newtonsCannon.views.DirectedGame;
import com.alec.newtonsCannon.views.Play;
import com.badlogic.gdx.assets.AssetManager;

public class newtonsCannon extends DirectedGame {
	
	public final static String TITLE = "Newton's Cannon";
	
	@Override
	public void create() {		
		// no skin yet
		Assets.instance.init(new AssetManager());
		GamePreferences.instance.load();
		//AudioManager.instance.play(Assets.instance.music.background);
		
		setScreen(new Play(this));
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public void render() {		
		super.render();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}
}
