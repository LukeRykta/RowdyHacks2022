package Screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.viewport.FitViewport;
import mygdx.game.RhythmGame;

public class AbstractScreen implements Screen {
    protected final RhythmGame context;
    protected final FitViewport viewport;
    protected final Music music;

    public AbstractScreen(final RhythmGame context) {
        this.context = context;
        viewport = context.getScreenViewport();
        music = context.getMusic();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
