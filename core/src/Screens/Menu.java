package Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import mygdx.game.RhythmGame;

public class Menu extends AbstractScreen implements InputProcessor {
    public Menu(final RhythmGame context) {
        super(context);
    }

    public void handleInput(float dt){
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)){
            try {
                context.setScreen(ScreenType.SOLO);
            } catch (ReflectionException e){
                e.printStackTrace();
            }
        }
    }

    public void update(float dt){
        handleInput(dt);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1,  0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);
    }


    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
