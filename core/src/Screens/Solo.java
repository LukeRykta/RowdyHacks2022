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
    private Hud hud; // new hud overlay
    private final SpriteBatch batch;
    private OrthographicCamera gamecam;
    private final float[] worldCenterXY = new float [2]; // stores x and y values for world center for easy referencing
    private final float[] trigPos = {1, 1.33f, 2, 4}; // relative positions for each lane (triggers / arrows)

    private Texture leftArrow; // arrow imgs
    private Texture upArrow;
    private Texture downArrow;
    private Texture rightArrow;

    private Texture leftTrigger; // trigger imgs
    private Texture upTrigger;
    private Texture downTrigger;
    private Texture rightTrigger;

    private final Rectangle[] triggerLR = new Rectangle[4]; // each element represents a trigger

    private Array<Rectangle> leftNotes;
    private Array<Rectangle> upNotes;
    private Array<Rectangle> downNotes;
    private Array<Rectangle> rightNotes;

    public Solo(RhythmGame context) {
        super(context); // receive cache context
        batch = new SpriteBatch(); // create batch to store all our sprite objects
        hud = new Hud(batch); // add the batch to our hud so it can draw on our screen

        createCamera(); // create Orthographic Camera and set it to our vwidth vheight that we declared in driver

        worldCenterXY[0] = gamecam.viewportWidth / 2; // find the x center of our cam
        worldCenterXY[1] = gamecam.viewportHeight / 2; // find the y center of our cam

        createTextures(); // load our image files into local variables
        createTriggers(); // create triggers (hitboxes, position on screen, size)
        createNoteArrays();
    }

    public void createCamera(){
        gamecam = new OrthographicCamera();
        gamecam.setToOrtho(false, RhythmGame.V_WIDTH, RhythmGame.V_HEIGHT);
    }

    public void createTextures(){
        leftArrow = new Texture(Gdx.files.internal("gameGFX/arrows/downArrow.png"));
        upArrow = new Texture(Gdx.files.internal("gameGFX/arrows/downArrow.png"));
        downArrow = new Texture(Gdx.files.internal("gameGFX/arrows/downArrow.png"));
        rightArrow = new Texture(Gdx.files.internal("gameGFX/arrows/downArrow.png"));

        leftTrigger = new Texture(Gdx.files.internal("gameGFX/triggers/downTriggerP.png"));
        upTrigger = new Texture(Gdx.files.internal("gameGFX/triggers/downTriggerP.png"));
        downTrigger = new Texture(Gdx.files.internal("gameGFX/triggers/downTriggerP.png"));
        rightTrigger = new Texture(Gdx.files.internal("gameGFX/triggers/downTriggerP.png"));
    }

    public void createTriggers() {
        for (int i = 0; i < triggerLR.length; i++){
            triggerLR[i] = new Rectangle();
            triggerLR[i].x = worldCenterXY[0] / trigPos[i] - 16;
            triggerLR[i].y = gamecam.viewportHeight / 16;
            triggerLR[i].width = 32; // set the trigger HITBOX to be the real width of the trigger
            triggerLR[i].height = 16; // set the trigger HITBOX to be half the size of the trigger so we cant hit notes that are far outside of the range we want
        }
    }

    public void spawnLeftNote() {
        Rectangle leftNote= new Rectangle(); // Rectangle object that will contain size info and hitboxes
        leftNote.x = worldCenterXY[0] / 4 - 16; // position on x axis for each note
        leftNote.y = gamecam.viewportHeight + 12; // set to max y + a lil extra so it appears the notes spawn about the screen
        leftNote.width = 32; // define our note HITBOX width
        leftNote.height = 32; // define our note HITBOX height
        leftNotes.add(leftNote); // put our note object in set of notes
    }

    public void spawnUpNote() {
        Rectangle upNote = new Rectangle();
        upNote.x = worldCenterXY[0] / 2- 16;
        upNote.y = gamecam.viewportHeight + 12;
        upNote.width = 32;
        upNote.height = 32;
        upNotes.add(upNote);

    }

    public void spawnDownNote() {
        Rectangle downNote= new Rectangle();
        downNote.x = worldCenterXY[0] / 1.33f - 16;
        downNote.y = gamecam.viewportHeight + 12;
        downNote.width = 32;
        downNote.height = 32;
        downNotes.add(downNote);
    }

    public void spawnRightNote() {
        Rectangle rightNote= new Rectangle();
        rightNote.x = worldCenterXY[0] * 1 - 16;
        rightNote.y = gamecam.viewportHeight + 12;
        rightNote.width = 32;
        rightNote.height = 32;
        rightNotes.add(rightNote);
    }

    public void createNoteArrays(){
        downNotes = new Array<Rectangle>(); // array of rectangle objects that will store each individual note
        upNotes = new Array<Rectangle>();
        leftNotes = new Array<Rectangle>();
        rightNotes = new Array<Rectangle>();
    }

    public void handleInput(float dt){
        if(Gdx.input.isKeyJustPressed(Input.Keys.A) )
            spawnLeftNote();
        if(Gdx.input.isKeyJustPressed(Input.Keys.W))
            spawnUpNote();
        if(Gdx.input.isKeyJustPressed(Input.Keys.S) )
            spawnDownNote();
        if(Gdx.input.isKeyJustPressed(Input.Keys.D) )
            spawnRightNote();

        if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) //left pressed
            Hud.removeScore(10);

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)){ // switch back to menu
            try {
                context.setScreen(ScreenType.MENU);
            } catch (ReflectionException e){
                e.printStackTrace();
            }
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) // exit game
            Gdx.app.exit();
    }

    public void update(float dt){
        handleInput(dt); // handles other inputs
        iterHandle(dt); // checks to see if player pressed buttons at the right time
        hud.update(dt);
    }

    public void iterHandle(float dt){
        for (Iterator<Rectangle> iter = leftNotes.iterator(); iter.hasNext();) {
            Rectangle note = iter.next(); // create note for each existing object in notes
            note.y -= 100 * dt;

            if (note.y + 64 < 0){ // if note goes below screen view, remove
                iter.remove();
                Hud.removeScore(10);
            }
            if(note.overlaps(triggerLR[3])) { // left trigger
                if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
                    Hud.addScore(20);
                    //System.out.println("EVENT: downArrow triggered");
                    iter.remove();
                }
            }
        }

        for (Iterator<Rectangle> iter = upNotes.iterator(); iter.hasNext();) {
            Rectangle note = iter.next(); // create note for each existing object in notes
            note.y -= 100 * dt;

            if (note.y + 64 < 0){ // if note goes below screen view, remove
                iter.remove();
                Hud.removeScore(10);
            }
            if(note.overlaps(triggerLR[2])) {
                if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
                    Hud.addScore(20);
                    //System.out.println("EVENT: downArrow triggered");
                    iter.remove();
                }
            }
        }

        for (Iterator<Rectangle> iter = downNotes.iterator(); iter.hasNext();) {
            Rectangle note = iter.next(); // create note for each existing object in notes
            note.y -= 100 * dt;

            if (note.y + 64 < 0){ // if note goes below screen view, remove
                iter.remove();
                Hud.removeScore(10);
            }
            if(note.overlaps(triggerLR[1])) {
                if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
                    Hud.addScore(20);
                    //System.out.println("EVENT: downArrow triggered");
                    iter.remove();
                }
            }
        }

        for (Iterator<Rectangle> iter = rightNotes.iterator(); iter.hasNext();) {
            Rectangle note = iter.next(); // create note for each existing object in notes
            note.y -= 100 * dt;

            if (note.y + 64 < 0){ // if note goes below screen view, remove
                iter.remove();
                Hud.removeScore(10);
            }
            if(note.overlaps(triggerLR[0])) {
                if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
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

        batch.draw(leftTrigger, triggerLR[3].x, triggerLR[3].y); // left
        batch.draw(upTrigger, triggerLR[2].x, triggerLR[2].y); // up
        batch.draw(downTrigger, triggerLR[1].x, triggerLR[1].y); // down
        batch.draw(rightTrigger, triggerLR[0].x, triggerLR[0].y); // right

        for(Rectangle leftNote: leftNotes)
            batch.draw(leftArrow, leftNote.x, leftNote.y);
        for(Rectangle upNote: upNotes)
            batch.draw(upArrow, upNote.x, upNote.y);
        for(Rectangle downNote: downNotes)
            batch.draw(downArrow, downNote.x, downNote.y);
        for(Rectangle rightNote: rightNotes)
            batch.draw(rightArrow, rightNote.x, rightNote.y);

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
        //music.play();
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
