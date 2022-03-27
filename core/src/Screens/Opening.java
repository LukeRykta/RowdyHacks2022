package Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import mygdx.game.RhythmGame;

import static com.badlogic.gdx.utils.Align.center;

public class Opening extends AbstractScreen implements InputProcessor {
    private OrthographicCamera cam;
    private Stage stage;
    private TextField name;
    private Label presentInfo;
    private Label nameInfo;
    private Skin skin;
    private Music music;

    protected Sound forwardSound;

    public Opening(RhythmGame context) {
        super(context);
        initMusic();
        initSkin();
        initStage();
    }

    public void initMusic(){
        //todo add sounds and start music here
        music = manager.get("music/songs/menu.mp3");
        forwardSound = manager.get("music/sounds/forward.wav");
        music.setVolume(.5f);
    }

    public void initSkin(){
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        cam = new OrthographicCamera();
        cam.setToOrtho(false, RhythmGame.V_WIDTH, RhythmGame.V_HEIGHT);
    }

    public void initStage(){
        stage = new Stage (new ScreenViewport(cam));

        final Table backgroundTable = new Table(skin); // Table for background
        backgroundTable.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture("arrows/bg.png"))));
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

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        //stage.act(delta);
        stage.draw();

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
