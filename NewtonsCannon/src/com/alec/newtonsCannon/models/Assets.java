package com.alec.newtonsCannon.models;

import com.alec.newtonsCannon.Constants;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Disposable;

public class Assets implements Disposable, AssetErrorListener {

	public static final String TAG = Assets.class.getName();
	public static final Assets instance = new Assets(); // singleton
	private AssetManager assetManager;

	public Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"), new TextureAtlas("ui/uiskin.atlas"));
	
	private Assets() {
	}

	public AssetLevel level;
	public AssetEarth earth;
	public AssetCannon cannon;
	public AssetUI ui;
	public AssetSounds sounds;
	public AssetMusic music;
	public AssetFonts fonts;

	public void init(AssetManager assetManager) {

		// establish the asset manager
		this.assetManager = assetManager;
		assetManager.setErrorListener(this);
		assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);
		assetManager.load("sounds/cannon.wav", Sound.class);
		assetManager.load("music/intro.ogg", Music.class);
		assetManager.finishLoading();

		// log all the assets there were loaded
		Gdx.app.debug(TAG,
				"# of assets loaded: " + assetManager.getAssetNames().size);
		for (String asset : assetManager.getAssetNames()) {
			Gdx.app.debug(TAG, "asset: " + asset);
		}

		// load the texture atlas
		TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS_OBJECTS);

		// enable texture filtering
		for (Texture texture : atlas.getTextures()) {
			texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}

		// create the game resources (inner Asset~ classes)
		earth = new AssetEarth(atlas);
		level = new AssetLevel(atlas);
		cannon = new AssetCannon(atlas);
		ui = new AssetUI(atlas);
		sounds = new AssetSounds(assetManager);
		music = new AssetMusic(assetManager);
		fonts = new AssetFonts();
	}

	public class AssetCannon {
		public final AtlasRegion cannon, cannonBall;

		public AssetCannon(TextureAtlas atlas) {
			cannon = atlas.findRegion("cannon");
			cannonBall = atlas.findRegion("cannonBall");

		}
	}

	public class AssetLevel {
		public final AtlasRegion background,
								moon;

		public AssetLevel(TextureAtlas atlas) {
			background = atlas.findRegion("itsFullOfStars");
			moon = atlas.findRegion("moon");
		}
	}

	public class AssetEarth {
		public final AtlasRegion earth;

		public AssetEarth(TextureAtlas atlas) {
			earth = atlas.findRegion("earth");
		}
	}

	public class AssetUI {
		public final AtlasRegion slider, sliderBar, sliderVert, sliderBarVert;

		public AssetUI(TextureAtlas atlas) {
			slider = atlas.findRegion("slider");
			sliderBar = atlas.findRegion("sliderBar");
			sliderVert = atlas.findRegion("sliderVert");
			sliderBarVert = atlas.findRegion("sliderBarVert");
		}
	}

	public class AssetSounds {
		public final Sound cannon;
		
		public AssetSounds (AssetManager am) {
			cannon = am.get("sounds/cannon.wav");
		}
	}
	
	public class AssetMusic {
		public final Music background;
		
		public AssetMusic (AssetManager am) {
			background = am.get("music/intro.ogg");
		}
	}
	
	public class AssetFonts {
		public final BitmapFont defaultSmall;
		public final BitmapFont defaultNormal;
		public final BitmapFont defaultBig;

		public AssetFonts() {
			defaultSmall = new BitmapFont(
					Gdx.files.internal("fonts/white16.fnt"), true);
			defaultNormal = new BitmapFont(
					Gdx.files.internal("fonts/white32.fnt"), true);
			defaultBig = new BitmapFont(
					Gdx.files.internal("fonts/white64.fnt"), true);

			defaultSmall.setScale(.75f);
			defaultNormal.setScale(1.0f);
			defaultBig.setScale(2.0f);

			defaultSmall.getRegion().getTexture()
					.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			defaultNormal.getRegion().getTexture()
					.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			defaultBig.getRegion().getTexture()
					.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}

	}

	@Override
	public void error(AssetDescriptor asset, Throwable throwable) {
		Gdx.app.error(TAG, "Couldn't load asset: '" + asset.fileName + "' "
				+ (Exception) throwable);
	}

	@Override
	public void dispose() {
		assetManager.dispose();
	}
}
