package Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
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

    protected Sound forwardSound;

    public Opening(RhythmGame context) {
        super(context);
        initMusic();
        initSkin();
        initStage();
    }

    public void initMusic(){
        //todo add sounds and start music here
        forwardSound = Gdx.audio.newSound(Gdx.files.internal("music/sounds/fx3.wav"));
    }

    public void initSkin(){
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        cam = new OrthographicCamera();
        cam.setToOrtho(false, RhythmGame.V_WIDTH, RhythmGame.V_HEIGHT);
    }

    public void initStage(){
        stage = new Stage (new ScreenViewport(cam));

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
        stage.addActor(openingTable);

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
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
        Gdx.gl.glClearColor(0.25882354f,  0.25882354f, 0.90588236f, 1);
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
