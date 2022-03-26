package Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import mygdx.game.RhythmGame;

import javax.swing.event.MenuKeyListener;
import javax.swing.event.MenuListener;

public class Menu extends AbstractScreen implements InputProcessor {

    private Skin skin;
    private Stage stage;
    private Button testButton;

    protected Sound forwardSound;

    public Menu(final RhythmGame context) {
        super(context);
        initSkin();
        initStage();
        initMusic();
    }

    public void initMusic(){
        music = Gdx.audio.newMusic(Gdx.files.internal("music/songs/beat1.mp3"));
        forwardSound = Gdx.audio.newSound(Gdx.files.internal("music/sounds/fx3.wav"));
    }

    private void initSkin(){
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
    }

    private void initStage(){
        stage  = new Stage(new ScreenViewport());

        final Table testTable = new Table(skin);
        testTable.setWidth(stage.getWidth());
        testTable.align(Align.center|Align.top);
        testTable.setPosition(0, Gdx.graphics.getHeight());

        testButton = new TextButton("TEST", skin);

        initActors();
        testTable.add(testButton);
        stage.addActor(testTable);
    }

    private void initActors(){
        testButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                forwardSound.play();
                System.out.println("Button clicked");

            }
        });
    }

    public void handleInput(float dt){
        if (Gdx.input.isKeyJustPressed(Keys.NUM_2)){
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
        stage.act(delta);
        stage.draw();
        //FIXME Menu button navigation
        //if(Gdx.input.isKeyPressed(Keys.UP))

    }

    @Override
    public void show(){
        music.play();
        Gdx.input.setInputProcessor(stage);
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
