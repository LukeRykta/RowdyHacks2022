package mygdx.game;

import Screens.AbstractScreen;
import Screens.ScreenType;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.EnumMap;

public class RhythmGame extends Game {
	private static final String TAG = RhythmGame.class.getSimpleName(); // get each screen name for logging when moving between screens
	public static String username = "";
	private EnumMap<ScreenType, AbstractScreen> screenCache; // defining our screenCache which will allow us to change screens without losing data
	private FitViewport screenViewport; // define the ratio of our screen
	SpriteBatch batch;
	Texture img;

	public static int V_WIDTH = 400; // use vwidth and vheight for scaling sprites and screens
	public static int V_HEIGHT = 208;

	@Override
	public void create () {
		batch = new SpriteBatch();

		screenCache = new EnumMap<ScreenType, AbstractScreen>(ScreenType.class); // create our cache
		screenViewport = new FitViewport(RhythmGame.V_WIDTH, RhythmGame.V_HEIGHT); // create our viewport using the scales

		try {
			setScreen(ScreenType.OPENING); // attempt to set screen to our menu (this will create a new screen if it's null)
		} catch (ReflectionException e) {
			e.printStackTrace();
		}

		Gdx.app.setLogLevel(Application.LOG_ERROR);
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}

	public FitViewport getScreenViewport() {
		return screenViewport;
	}

	public void setScreen(final ScreenType screenType) throws ReflectionException {
		final Screen screen = screenCache.get(screenType);
		if(screen==null) {
			try {
				Gdx.app.log(TAG, "Creating new screen: " + screenType);
				final AbstractScreen newScreen = (AbstractScreen) ClassReflection.getConstructor(screenType.getScreenClass(), RhythmGame.class).newInstance(this);
				screenCache.put(screenType, newScreen);
				setScreen(newScreen);
			} catch (ReflectionException e){
				throw new GdxRuntimeException("Screen " + screenType + " could not be created", e);
			}
		} else {
			Gdx.app.log(TAG, "Switching to screen: " + screenType);
			setScreen(screen);
		}
	}
}
