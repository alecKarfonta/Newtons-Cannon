package com.alec.solarsystem;

import com.badlogic.gdx.tools.imagepacker.TexturePacker2;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2.Settings;

public class TexturePack {

	private static boolean drawDebugOutline = false;
	
	public static void main(String[] args) {
		Settings settings = new Settings();
		settings.maxWidth = 4096;
		settings.maxHeight = 2048;
		settings.debug = drawDebugOutline;
		TexturePacker2.process(settings, "assets-raw/", "../solarSystem-android/assets/images", "solarSystem.pack");
	}
}
