package Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import mygdx.game.RhythmGame;

import static com.badlogic.gdx.utils.Align.center;

public class Opening extends AbstractScreen implements InputProcessor {
    private OrthographicCamera cam;
    private Stage stage;

    private Animation runAnimation; // animation key frames
    private Texture blueDinoSheet; // loaded image sheet (png)

    private TextField name;
    private SpriteBatch batch;
    private Label presentInfo;
    private Label nameInfo;
    private Skin skin;
    private Music music;
    private int b;

    float stateTime;

    private Texture blueDino;

    protected Sound forwardSound;

    public Opening(RhythmGame context) {
        super(context);
        createCamera();
        initMusic();
        initTextures();
        initSkin();
        initStage();
    }

    public void createCamera(){
        cam = new OrthographicCamera();
        cam.setToOrtho(false, RhythmGame.V_WIDTH, RhythmGame.V_HEIGHT);
        //gamePort = new StretchViewport(RhythmGame.V_WIDTH , RhythmGame.V_HEIGHT , gamecam);
    }

    public void initMusic(){
        music = manager.get("music/songs/menu.mp3");
        forwardSound = manager.get("music/sounds/forward.wav");
        music.setVolume(.5f);
    }

    public void initTextures(){
        blueDinoSheet = new Texture("uiGFX/texturepacks/blueDino.png"); // set file path for png
        TextureRegion[][] tmp = TextureRegion.split(blueDinoSheet, blueDinoSheet.getWidth() / 24, blueDinoSheet.getHeight()); // declare region size

        TextureRegion[] runFrames = new TextureRegion[11];
        int index = 0;
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 11; j++) {
                runFrames[index++] = tmp[i][j];
            }
        }

        runAnimation = new Animation<TextureRegion>(0.1f, runFrames);
    }

    public void initSkin(){
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        cam = new OrthographicCamera();
        cam.setToOrtho(false, RhythmGame.V_WIDTH, RhythmGame.V_HEIGHT);
    }

    public void initStage(){
        stage = new Stage (new ScreenViewport(cam));
        batch = new SpriteBatch();

        final Table backgroundTable = new Table(skin); // Table for background
        backgroundTable.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture("uiGFX/backgrounds/bg.png"))));
        backgroundTable.setFillParent(true);

        final Table openingTable = new Table(skin);
        openingTable.setWidth(stage.getWidth());
        openingTable.align(center| Align.top);
        openingTable.setPosition(0, Gdx.graphics.getHeight());

        name = new TextField("", skin);
        name.setMaxLength(10);
        name.setWidth(openingTable.getWidth()/8);
        name.setAlignment(center|Align.top);
        name.setPosition(stage.getWidth()/2 - name.getWidth()/2, stage.getHeight()/2 - name.getWidth()/3);

        presentInfo = new Label("L&B present...", skin); // titleTable items
        nameInfo = new Label("- Enter Your Name -", skin);

        openingTable.add(presentInfo).padTop(stage.getHeight()/2);
        stage.addActor(backgroundTable);
        stage.addActor(openingTable);

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                forwardSound.play();
                openingTable.removeActor(presentInfo);
                openingTable.add(nameInfo).padTop(stage.getHeight()/2);
                stage.addActor(name);
                stage.setKeyboardFocus(name);
                name.getOnscreenKeyboard().show(true);
            }
        },3.5f);

    }

    public void handleInput(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
            RhythmGame.username = name.getText();
            try{
                context.setScreen(ScreenType.MENU);
                forwardSound.play();
            } catch (ReflectionException e){
                e.printStackTrace();
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.B)){
            b++;
        }
    }

    public void update (float dt) throws ReflectionException {
        handleInput(dt);
    }

    @Override
    public void render(float delta) {
        //Gdx.gl.glClearColor(0xEB,  0x32, 0x23, 1); //red
        //Gdx.gl.glClearColor(0xC7,  0xFB, 0x50, 1/4); //green
        //Gdx.gl.glClearColor(0x38,  0x95, 0xF7, 1/4); //blue
        //Gdx.gl.glClearColor(0xF5,  0xB0, 0x3E, 1/4); //orange
        stateTime += delta;
        TextureRegion currentFrame = (TextureRegion) runAnimation.getKeyFrame(stateTime, true); // get the key frame based on the state time

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //stage.act(delta);
        stage.draw();

        batch.setProjectionMatrix(cam.combined);

        batch.begin();
        batch.draw(currentFrame, stage.getWidth() / 4 + b, stage.getHeight()/2 - 128, 256, 256); // draw x and y position and scale size
        batch.end();

        try {
            update(delta);
        } catch (ReflectionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void show(){
        Gdx.input.setInputProcessor(stage);
        music.play();
    }

    @Override
    public void hide() {
        stage.dispose();
    }

    @Override
    public void resize(int width, int height) {
        cam.update();
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
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
