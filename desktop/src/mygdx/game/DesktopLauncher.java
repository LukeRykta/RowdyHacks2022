package mygdx.game;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import mygdx.game.RhythmGame;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Graphics.DisplayMode desktopMode = Lwjgl3ApplicationConfiguration.getDisplayMode();

		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowedMode(1920, 1080);
		config.setForegroundFPS(165);
		config.useVsync(true);
		//config.setFullscreenMode(desktopMode);

		new Lwjgl3Application(new RhythmGame(), config);
	}
}
