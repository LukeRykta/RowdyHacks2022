package Screens;

import Scenes.LocalHud;
import Scenes.SoloHud;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import mygdx.game.RhythmGame;

import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class LocalMulti extends AbstractScreen implements InputProcessor {
    private LocalHud hud; // new hud overlay

    private final SpriteBatch batch; // for drawing sprites
    private OrthographicCamera gamecam;
    private Stage stage;

    private final float[] worldCenterXY = new float [2]; // stores x and y values for world center for easy referencing
    private final float[] LtrigPos = {1.33f, 1.85f, 3, 8}; // relative positions for each lane (triggers / arrows)
    private final float[] RtrigPos = {1.26f, 1.47f, 1.66f, 1.87f}; // relative positions for each lane (triggers / arrows)

    public static final int NOTE_ON = 0x90; // midi stuff
    public static final int NOTE_OFF = 0x80;
    public static final String[] NOTE_NAMES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
    static String[] noteName = new String[1000];
    static int[] lane = new int[1000];
    static int[] octave = new int[1000];
    static int[] velocity = new int[1000];
    static int[] note = new int[1000];
    static long[] tick = new long[1000];
    static int[] multiplier = {1,2,3,4};
    static int threshold = 0;
    static int index = 0;
    static float timeScale = 88f / 60 *.9585f; //88.00002346667293f
    private final long startT;

    private Music song;

    private Texture leftArrow; // arrow imgs
    private Texture upArrow;
    private Texture downArrow;
    private Texture rightArrow;

    private Texture leftTrigger; // trigger imgs
    private Texture upTrigger;
    private Texture downTrigger;
    private Texture rightTrigger;

    private Texture leftTriggerP; // trigger imgs
    private Texture upTriggerP;
    private Texture downTriggerP;
    private Texture rightTriggerP;

    private Animation runAnimation; // animation key frames
    private Texture blueDinoSheet; // loaded image sheet (png)
    private float stateTime=0;

    private final Rectangle[] LtriggerLR = new Rectangle[4]; // each element represents a trigger
    private final Rectangle[] RtriggerLR = new Rectangle[4]; // each element represents a trigger

    private Array<Rectangle> leftNotes;
    private Array<Rectangle> upNotes;
    private Array<Rectangle> downNotes;
    private Array<Rectangle> rightNotes;

    private int i = 0;

    public LocalMulti(RhythmGame context) throws InvalidMidiDataException, IOException {
        super(context); // receive cache context
        batch = new SpriteBatch(); // create batch to store all our sprite objects
        hud = new LocalHud(batch); // add the batch to our hud so it can draw on our screen

        createCamera(); // create Orthographic Camera and set it to our vwidth vheight that we declared in driver

        stage = new Stage (new ScreenViewport());

        worldCenterXY[0] = gamecam.viewportWidth / 2; // find the x center of our cam
        worldCenterXY[1] = gamecam.viewportHeight / 2; // find the y center of our cam

        createTextures(); // load our image files into local variables
        createTriggers(); // create triggers (hitboxes, position on screen, size)
        createNoteArrays();

        parseMidi();
        initMusic();
        startT = System.currentTimeMillis();
    }

    public void initMusic(){
        song = manager.get("music/songs/song1.mp3");
    }

    public void parseMidi() throws InvalidMidiDataException, IOException {
        Sequence sequence = MidiSystem.getSequence(new File("assets/midi/rowdy.mid"));
        int trackNumber = 0;
        for (Track track : sequence.getTracks()) {
            trackNumber++;
            //System.out.println("Track " + trackNumber + ": size = " + track.size());
            //System.out.println();
            for (int i = 0; i < track.size(); i++) {
                MidiEvent event = track.get(i);
                //System.out.print("@" + event.getTick() + " ");
                MidiMessage message = event.getMessage();
                tick[i] = event.getTick();
                System.out.println(tick[i]);
                if (message instanceof ShortMessage) {
                    ShortMessage sm = (ShortMessage) message;
                    //System.out.print("Channel: " + sm.getChannel() + " ");
                    if (sm.getCommand() == NOTE_ON) {
                        lane[i] = sm.getData1();
                        octave[i] = (lane[i] / 12) - 1;
                        note[i] = lane[i] % 12;
                        noteName[i] = NOTE_NAMES[note[i]];
                        velocity[i] = sm.getData2();
                        //System.out.println("time=" + (tick[i]) + " lane=" + (lane[i] + 1));
                        //System.out.println("tick="+ tick[i] + " Note on, " + noteName[i] + octave[i] + " lane=" + lane[i] + " velocity: " + velocity[i]);
                    } else if (sm.getCommand() == NOTE_OFF) {
                        //lane[i] = sm.getData1();
                        octave[i] = (lane[i] / 12) - 1;
                        //note[i] = lane[i] % 12;
                        noteName[i] = NOTE_NAMES[note[i]];
                        velocity[i] = sm.getData2();
                        //System.out.println("tick="+ tick[i] + " Note off, " + noteName[i] + octave[i] + " lane=" + lane[i] + " velocity: " + velocity[i]);
                    } else {
                        //System.out.println("Command:" + sm.getCommand());
                    }
                } else {
                    //System.out.println("Other message: " + message.getClass());
                }
                //System.out.println(lane[i]);
            }
            System.out.println();
        }
    }

    public void createCamera(){
        gamecam = new OrthographicCamera();
        gamecam.setToOrtho(false, RhythmGame.V_WIDTH, RhythmGame.V_HEIGHT);
    }

    public void createTextures(){
        leftArrow = new Texture(Gdx.files.internal("gameGFX/arrows/BlueArrow.png"));
        upArrow = new Texture(Gdx.files.internal("gameGFX/arrows/GreenArrow.png"));
        downArrow = new Texture(Gdx.files.internal("gameGFX/arrows/YellowArrow.png"));
        rightArrow = new Texture(Gdx.files.internal("gameGFX/arrows/RedArrow.png"));

        leftTrigger = new Texture(Gdx.files.internal("gameGFX/triggers/left.png"));
        upTrigger = new Texture(Gdx.files.internal("gameGFX/triggers/up.png"));
        downTrigger = new Texture(Gdx.files.internal("gameGFX/triggers/down.png"));
        rightTrigger = new Texture(Gdx.files.internal("gameGFX/triggers/right.png"));

        leftTriggerP = new Texture(Gdx.files.internal("gameGFX/triggersP/leftP.png"));
        upTriggerP = new Texture(Gdx.files.internal("gameGFX/triggersP/upP.png"));
        downTriggerP = new Texture(Gdx.files.internal("gameGFX/triggersP/downP.png"));
        rightTriggerP = new Texture(Gdx.files.internal("gameGFX/triggersP/rightP.png"));
    }

    public void createTriggers() {
        for (int i = 0; i < LtriggerLR.length; i++){
            LtriggerLR[i] = new Rectangle();
            LtriggerLR[i].x = worldCenterXY[0] / LtrigPos[i] - 16;
            LtriggerLR[i].y = gamecam.viewportHeight / 16;
            LtriggerLR[i].width = 32; // set the trigger HITBOX to be the real width of the trigger
            LtriggerLR[i].height = 16; // set the trigger HITBOX to be half the size of the trigger so we cant hit notes that are far outside of the range we want
        }

        for (i = 0; i < RtriggerLR.length; i++){
            RtriggerLR[i] = new Rectangle();
            RtriggerLR[i].x = worldCenterXY[0] * RtrigPos[i] - 16;
            RtriggerLR[i].y = gamecam.viewportHeight / 16;
            RtriggerLR[i].width = 32; // set the trigger HITBOX to be the real width of the trigger
            RtriggerLR[i].height = 16; // set the trigger HITBOX to be half the size of the trigger so we cant hit notes that are far outside of the range we want
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

        if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) { //left pressed
            LocalHud.LremoveScore(100);
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.UP)) { //up pressed
            LocalHud.LremoveScore(100);
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) { //down pressed
            LocalHud.LremoveScore(100);
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) { //right pressed
            LocalHud.LremoveScore(100);

        }

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
            try {
                context.setScreen(ScreenType.MENU);
            } catch (ReflectionException e) {
                e.printStackTrace();
            }
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) // exit game
            Gdx.app.exit();
    }

    public void update(float dt){
        handleInput(dt); // handles other inputs

        long updateT = System.currentTimeMillis(); // gets time at this cycle

        //System.out.println((double)updateT - (double)startT + " " + tick[i] * 10);
        if (((((double)updateT - (double)startT)) * timeScale >= (double)(tick[i]*10)) && i < tick.length-1){ // check to see if we need to spawn another note yet and also makes sure we arent at the end of the ticks
            if ((lane[i] == 60)){ // if key=60 (C), lane=1 (left arrow)
                spawnLeftNote();
            }
            if (lane[i] == 61){ // if key=61 (C#), lane=2 (up arrow)
                spawnUpNote();
            }
            if (lane[i] == 62){ // if key=62 (D), lane=3 (down arrow)
                spawnDownNote();
            }
            if (lane[i] == 63){ // if key=63 (D#), lane=4 (right arrow)
                spawnRightNote();
            }
            i++;
        }

        if (i == tick.length-2){ // return to menu after song over tick.length-1
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    i++;
                    try {
                        context.setScreen(ScreenType.MENU);
                    } catch (ReflectionException e) {
                        e.printStackTrace();
                    }
                }
            }, 1f);
        }

        iterHandle(dt); // checks to see if player pressed buttons at the right time
        hud.update(dt);
    }

    public void iterHandle(float dt){
        System.out.println(threshold);
        if(threshold >= 10 && threshold <= 29)
            index = 1;
        else if(threshold >= 30 && threshold <= 44)
            index = 2;
        else if(threshold >= 45)
            index = 3;
        for (Iterator<Rectangle> iter = leftNotes.iterator(); iter.hasNext();) {
            Rectangle note = iter.next(); // create note for each existing object in notes
            note.y -= 200 * dt;

            if (note.y + 64 < 0){ // if note goes below screen view, remove
                iter.remove();
                LocalHud.LremoveScore(100);
                threshold = 0;
            }
            if(note.overlaps(LtriggerLR[3])) { // left trigger
                if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
                    LocalHud.LaddScore(200 * multiplier[index]);
                    threshold += 1;
                    //System.out.println("EVENT: downArrow triggered");
                    iter.remove();
                }
            }
        }

        for (Iterator<Rectangle> iter = upNotes.iterator(); iter.hasNext();) {
            Rectangle note = iter.next(); // create note for each existing object in notes
            note.y -= 200* dt;

            if (note.y + 64 < 0){ // if note goes below screen view, remove
                iter.remove();
                LocalHud.LremoveScore(100);
                threshold = 0;
            }
            if(note.overlaps(LtriggerLR[2])) {
                if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
                    LocalHud.LaddScore(200 * multiplier[index]);
                    threshold += 1;
                    //System.out.println("EVENT: downArrow triggered");
                    iter.remove();
                }
            }
        }

        for (Iterator<Rectangle> iter = downNotes.iterator(); iter.hasNext();) {
            Rectangle note = iter.next(); // create note for each existing object in notes
            note.y -= 200 * dt;

            if (note.y + 64 < 0){ // if note goes below screen view, remove
                iter.remove();
                LocalHud.LremoveScore(100);
                threshold = 0;
            }
            if(note.overlaps(LtriggerLR[1])) {
                if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
                    LocalHud.LaddScore(200 * multiplier[index]);
                    threshold += 1;
                    //System.out.println("EVENT: downArrow triggered");
                    iter.remove();
                }
            }
        }

        for (Iterator<Rectangle> iter = rightNotes.iterator(); iter.hasNext();) {
            Rectangle note = iter.next(); // create note for each existing object in notes
            note.y -= 200 * dt;

            if (note.y + 64 < 0){ // if note goes below screen view, remove
                iter.remove();
                LocalHud.LremoveScore(100);
                threshold = 0;
            }
            if(note.overlaps(LtriggerLR[0])) {
                if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
                    LocalHud.LaddScore(200 * multiplier[index]);
                    threshold += 1;
                    //System.out.println("EVENT: downArrow triggered");
                    iter.remove();
                }
            }
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.25882354f,  0.25882354f, 0.90588236f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        hud.update(delta);
        hud.stage.draw();

        batch.setProjectionMatrix(gamecam.combined);

        batch.begin();

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            batch.draw(leftTriggerP, LtriggerLR[3].x, LtriggerLR[3].y);
        else
            batch.draw(leftTrigger, LtriggerLR[3].x, LtriggerLR[3].y); // left

        if (Gdx.input.isKeyPressed(Input.Keys.UP))
            batch.draw(upTriggerP, LtriggerLR[2].x, LtriggerLR[2].y);
        else
            batch.draw(upTrigger, LtriggerLR[2].x, LtriggerLR[2].y); // up

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
            batch.draw(downTriggerP, LtriggerLR[1].x, LtriggerLR[1].y);
        else
            batch.draw(downTrigger, LtriggerLR[1].x, LtriggerLR[1].y); // down

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            batch.draw(rightTriggerP, LtriggerLR[0].x, LtriggerLR[0].y);
        else
            batch.draw(rightTrigger, LtriggerLR[0].x, LtriggerLR[0].y); // right

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            batch.draw(leftTriggerP, RtriggerLR[3].x, RtriggerLR[3].y);
        else
            batch.draw(leftTrigger, RtriggerLR[3].x, RtriggerLR[3].y); // left

        if (Gdx.input.isKeyPressed(Input.Keys.UP))
            batch.draw(upTriggerP, RtriggerLR[2].x, RtriggerLR[2].y);
        else
            batch.draw(upTrigger, RtriggerLR[2].x, RtriggerLR[2].y); // up

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
            batch.draw(downTriggerP, RtriggerLR[1].x, RtriggerLR[1].y);
        else
            batch.draw(downTrigger, RtriggerLR[1].x, RtriggerLR[1].y); // down

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            batch.draw(rightTriggerP, RtriggerLR[0].x, RtriggerLR[0].y);
        else
            batch.draw(rightTrigger, RtriggerLR[0].x, RtriggerLR[0].y); // right

        batch.draw(leftTrigger, LtriggerLR[3].x, LtriggerLR[3].y); // left
        batch.draw(upTrigger, LtriggerLR[2].x, LtriggerLR[2].y); // up
        batch.draw(downTrigger, LtriggerLR[1].x, LtriggerLR[1].y); // down
        batch.draw(rightTrigger, LtriggerLR[0].x, LtriggerLR[0].y); // right

        batch.draw(leftTrigger, RtriggerLR[3].x, RtriggerLR[3].y); // left
        batch.draw(upTrigger, RtriggerLR[2].x, RtriggerLR[2].y); // up
        batch.draw(downTrigger, RtriggerLR[1].x, RtriggerLR[1].y); // down
        batch.draw(rightTrigger, RtriggerLR[0].x, RtriggerLR[0].y); // right

        for(Rectangle leftNote: leftNotes)
            batch.draw(leftArrow, leftNote.x, leftNote.y);
        for(Rectangle upNote: upNotes)
            batch.draw(upArrow, upNote.x, upNote.y);
        for(Rectangle downNote: downNotes)
            batch.draw(downArrow, downNote.x, downNote.y);
        for(Rectangle rightNote: rightNotes)
            batch.draw(rightArrow, rightNote.x, rightNote.y);
        batch.end();

        batch.setProjectionMatrix(hud.stage.getCamera().combined);

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
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                song.play();
            }
        },.9f);
    }

    @Override
    public void hide(){
        song.stop();
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
