package Screens;

import Scenes.Hud;
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
    private Hud hud;
    private final SpriteBatch batch;
    private OrthographicCamera gamecam;

    private Texture downArrow;
    private Texture downTrigger;
    private Rectangle triggerDown;

    private final Array<Rectangle> testNotes;

    public Solo(RhythmGame context) {
        super(context); // receive cache context
        batch = new SpriteBatch(); // create batch to store all our sprite objects
        hud = new Hud(batch);

        createCamera(); // create Orthographic Camera and set it to our vwidth vheight that we declared in driver
        createTextures(); // load our image files into local variables
        createTriggers();

        testNotes = new Array<Rectangle>(); // array of rectangle objects that will store each individual note
        Rectangle testNote = new Rectangle(); // Rectangle object that will contain size info and hitboxes
        testNote.x = gamecam.viewportWidth / 2-16; // position on x axis for each note
        testNote.y = gamecam.viewportHeight + 12; // set to max y + a lil extra so it appears the notes spawn about the screen
        testNote.width = 32; // define our note HITBOX width
        testNote.height = 32; // define our note HITBOX height
        testNotes.add(testNote); // put our note object in set of notes

        music = Gdx.audio.newMusic(Gdx.files.internal("music/songs/star.mp3"));
    }

    public void createCamera(){
        gamecam = new OrthographicCamera();
        gamecam.setToOrtho(false, RhythmGame.V_WIDTH, RhythmGame.V_HEIGHT);
    }

    public void createTextures(){
        downArrow = new Texture(Gdx.files.internal("gameGFX/arrows/downArrow.png"));
        downTrigger = new Texture(Gdx.files.internal("gameGFX/triggers/downTriggerP.png"));
    }

    public void createTriggers() {
        triggerDown = new Rectangle();
        triggerDown.x = gamecam.viewportWidth / 2-16;
        triggerDown.y = gamecam.viewportHeight / 16;
        triggerDown.width = 32; // set the trigger HITBOX to be the real width of the trigger
        triggerDown.height = 16; // set the trigger HITBOX to be half the size of the trigger so we cant hit notes that are far outside of the range we want
    }

    public void handleInput(float dt){
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) // exit game
            Gdx.app.exit();

        if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) //left pressed
            Hud.removeScore(10);

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)){
            try {
                context.setScreen(ScreenType.MENU);
            } catch (ReflectionException e){
                e.printStackTrace();
            }
        }
    }

    public void update(float dt){
        iterHandle(dt);
        handleInput(dt);
        hud.update(dt);
    }

    public void iterHandle(float dt){
        for (Iterator<Rectangle> iter = testNotes.iterator(); iter.hasNext();) {
            Rectangle note = iter.next(); // create note for each existing object in notes
            note.y -= 100 * dt;

            if (note.y + 64 < 0){ // if note goes below screen view, remove
                iter.remove();
                Hud.removeScore(10);
            }
            if(note.overlaps(triggerDown)) {
                if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
                    Hud.addScore(20);
                    //System.out.println("EVENT: downArrow triggered");
                    iter.remove();
                }
            }
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.5f, .5f, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(gamecam.combined);

        batch.begin();

        batch.draw(downTrigger, gamecam.viewportWidth/2-16, 0); // down
        for(Rectangle testNote: testNotes)
            batch.draw(downArrow, testNote.x, testNote.y);

        //batch.draw(testImg, 100, 100);
        batch.end();

        batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.update(delta);
        hud.stage.draw();
        update(delta);
    }

    @Override
    public void resize(int width, int height) {
        gamecam.update();
    }

    @Override
    public void dispose(){
        downArrow.dispose();
        downTrigger.dispose();
        batch.dispose();
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
