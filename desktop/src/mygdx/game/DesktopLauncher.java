package mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import mygdx.game.RhythmGame;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Graphics.DisplayMode desktopMode = Lwjgl3ApplicationConfiguration.getDisplayMode();
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowIcon("uiGFX/misc/icon.png");
		config.setWindowedMode(2560, 1440);
		//config.setForegroundFPS(165);
		config.useVsync(true);
		///config.setFullscreenMode(desktopMode);

		new Lwjgl3Application(new RhythmGame(), config);
	}
}
