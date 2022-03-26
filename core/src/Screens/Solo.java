package Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import mygdx.game.RhythmGame;

import java.util.Iterator;

public class Solo extends AbstractScreen implements InputProcessor {
    private final SpriteBatch batch;
    private final OrthographicCamera gamecam;

    private final Texture testImg;

    private final Array<Rectangle> testNotes;


    public Solo(RhythmGame context) {
        super(context);
        batch = new SpriteBatch();

        testImg = new Texture(Gdx.files.internal("gameGFX/triggers/downTrigger.png"));

        gamecam = new OrthographicCamera();
        gamecam.setToOrtho(false, RhythmGame.V_WIDTH, RhythmGame.V_HEIGHT);

        testNotes = new Array<Rectangle>();
        Rectangle testNote = new Rectangle();
        testNote.x = 200;
        testNote.y = gamecam.viewportHeight + 12;
        testNote.width = 32;
        testNote.height = 32;
        testNotes.add(testNote);

        music = Gdx.audio.newMusic(Gdx.files.internal("music/songs/star.mp3"));
    }

    public void handleInput(float dt){
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)){
            try {
                context.setScreen(ScreenType.MENU);
            } catch (ReflectionException e){
                e.printStackTrace();
            }
        }
    }

    public void update(float dt){
        for (Iterator<Rectangle> iter = testNotes.iterator(); iter.hasNext();) {
            Rectangle note = iter.next(); // create note for each existing object in notes
            note.y -= 100 * dt;

            if (note.y + 64 < 0){ // if note goes below screen view, remove
                iter.remove();
            }
        }

        handleInput(dt);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 1, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);

        batch.setProjectionMatrix(gamecam.combined);

        batch.begin();

        for(Rectangle testNote: testNotes)
            batch.draw(testImg, testNote.x, testNote.y);

        //batch.draw(testImg, 100, 100);
        batch.end();
    }
    @Override
    public void show(){
        music.play();
    }

    @Override
    public void hide(){
        music.stop();
    }

    @Override
    public void resize(int width, int height) {
        gamecam.update();
    }

    @Override
    public void dispose(){
        testImg.dispose();
        batch.dispose();
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
