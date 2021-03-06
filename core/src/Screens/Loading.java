package Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import mygdx.game.RhythmGame;

import javax.sound.midi.InvalidMidiDataException;
import java.io.IOException;

public class Loading extends AbstractScreen{
    private Stage stage;
    private Skin skin;
    private Label info;
    private Label error;
    private Button back;
    private Table loadingTable;
    private Table buttonTable;
    private boolean isGameInit = false;
    private boolean errorHasOccured = false;
    private Sound backSound;

    public Loading(final RhythmGame context){
        super(context);
        initMusic();
        initSkin();
        initStage();
    }

    public void initMusic(){
        backSound = manager.get("music/sounds/back.wav");
    }

    public void initSkin(){
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
    }

    public void initStage(){
        stage = new Stage (new ScreenViewport());

        final Table backgroundTable = new Table(skin); // Table for background
        backgroundTable.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture("uiGFX/backgrounds/bg.png"))));
        backgroundTable.setFillParent(true);

        loadingTable = new Table(skin);
        buttonTable = new Table(skin);

        loadingTable.setWidth(stage.getWidth());
        loadingTable.align(Align.center|Align.top);
        loadingTable.setPosition(0, Gdx.graphics.getHeight());

        buttonTable.setWidth(stage.getWidth());
        buttonTable.align(Align.center|Align.top);
        buttonTable.setPosition(0, Gdx.graphics.getHeight());

        back = new TextButton("return", skin); // menuTable items

        info = new Label("Loading...", skin); // titleTable items
        error = new Label("An error has occurred.", skin);

        loadingTable.add(info).padTop(stage.getHeight()/2);
        stage.setKeyboardFocus(back);

        stage.addActor(backgroundTable);
        stage.addActor(loadingTable);
        stage.addActor(buttonTable);

        Timer.schedule(new Timer.Task() {

            @Override
            public void run() {
                try {
                    context.setScreen(new Solo(context));
                } catch (InvalidMidiDataException | IOException e) {
                    errorHasOccured = true;
                    initActors();
                    e.printStackTrace();
                }
                isGameInit = true;
            }
        },2);
    }

    public void initActors(){
        loadingTable.align(Align.center|Align.top);
        loadingTable.debug();
        loadingTable.removeActor(info);
        loadingTable.add(error).padTop(stage.getHeight()/2).padBottom(30);
        loadingTable.row();
        buttonTable.add(back).padTop(stage.getHeight()/2 + stage.getHeight()/4);
        //loadingTable.add(back);

        back.addListener(new ClickListener(){ // leaderboard
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    backSound.play();
                    context.setScreen(ScreenType.MENU);
                } catch (ReflectionException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        if (isGameInit){
            RunGame();
        }
    }

    public void handleInput(float dt){
        if ((errorHasOccured) && Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
            backSound.play();
            try {
                context.setScreen(ScreenType.MENU);
            } catch (ReflectionException e) {
                e.printStackTrace();
            }
        }
    }

    public void update (float dt){
        handleInput(dt);
    }

    public void RunGame() {
        Timer.schedule(new Timer.Task() {

            @Override
            public void run() {
                try {
                    context.setScreen(new Solo(context));
                } catch (InvalidMidiDataException | IOException e) {
                    e.printStackTrace();
                }
            }
        },0);
    }

    @Override
    public void render(float delta) {
        //Gdx.gl.glClearColor(0.25882354f,  0.25882354f, 0.90588236f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //stage.act(delta);
        stage.draw();
        update(delta);
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
        stage.dispose();
        context.dispose();
    }
}
