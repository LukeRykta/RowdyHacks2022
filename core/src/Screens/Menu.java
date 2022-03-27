package Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import mygdx.game.RhythmGame;

import javax.swing.event.MenuKeyListener;
import javax.swing.event.MenuListener;

public class Menu extends AbstractScreen implements InputProcessor {

    private Stage stage;
    private Skin skin;

    private TextButton singleButton;
    private TextButton multiButton;
    private TextButton quitButton;
    private TextButton leaderButton;
    private static Dialog dialog;
    private Label title;

    private Table menuTable;

    private Music music;
    private Sound forwardSound;
    private Sound nextSound;
    private Sound backSound;

    public Menu(final RhythmGame context) {
        super(context);
        initSkin();
        initStage();
        initMusic();
    }

    public void initMusic(){
        music = manager.get("music/songs/menu.mp3");
        forwardSound = manager.get("music/sounds/forward.wav");
        backSound = manager.get("music/sounds/back.wav");
        //nextSound = manager.get("music/sounds/next.wav");
    }

    private void initSkin(){
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
    }

    private void initStage(){
        stage  = new Stage(new ScreenViewport());

        final Table titleTable = new Table(skin); // table for header
        menuTable = new Table(skin); // table for menu buttons
        final Table leaderTable = new Table(skin); // table for leaderboard results\

        final Table backgroundTable = new Table(skin); // Table for background
        backgroundTable.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture("uiGFX/backgrounds/bg.png"))));
        backgroundTable.setFillParent(true);

        //initialize menuTable and title table sizes to scale with aspect ration
        menuTable.setWidth(stage.getWidth());
        menuTable.align(Align.center|Align.top);
        menuTable.setPosition(0, Gdx.graphics.getHeight());
        titleTable.setWidth(stage.getWidth());
        titleTable.align(Align.center|Align.top);
        titleTable.setPosition(0, Gdx.graphics.getHeight());

        title = new Label("RHYTHM GAME\n A game by <team name>", skin); // titleTable items

        singleButton = new TextButton("Singleplayer", skin); // menuTable items
        multiButton = new TextButton("Multiplayer", skin);
        leaderButton = new TextButton ("Leaderboard", skin);
        quitButton = new TextButton("Quit Game", skin);

        titleTable.add(title).padTop(stage.getHeight()/4);
        menuTable.row();
        menuTable.padTop(stage.getHeight()/2);
        menuTable.add(singleButton).padBottom(30);
        menuTable.row();
        menuTable.add(multiButton).padBottom(30);
        menuTable.row();
        menuTable.add(leaderButton).padBottom(30);
        menuTable.row();
        menuTable.add(quitButton);

        dialog = new Dialog("Leaderboard", skin, "dialog-modal");
        dialog.background("window");

        dialog.setMovable(false);
        dialog.button("return");
        dialog.getContentTable().add(leaderTable);

        initActors();

        stage.addActor(backgroundTable);
        stage.addActor(titleTable);
        stage.addActor(menuTable);
    }

    private void initActors(){
        singleButton.addListener(new ClickListener(){ // singleplayer
            @Override
            public void clicked(InputEvent event, float x, float y){
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        try{
                            context.setScreen(ScreenType.SSEL);
                        } catch (ReflectionException e){
                            e.printStackTrace();
                        }
                    }
                },0);
            }
        });

        multiButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                Timer.schedule(new Timer.Task() {

                    @Override
                    public void run() {
                        try{
                            context.setScreen(ScreenType.MSEL);
                        } catch (ReflectionException e){
                            e.printStackTrace();
                        }
                    }
                },0);
            }
        });

        leaderButton.addListener(new ClickListener(){ // leaderboard
            @Override
            public void clicked(InputEvent event, float x, float y){
                dialog.show(stage).setY(dialog.getHeight()/2);
                Timer.schedule(new Timer.Task() {

                    @Override
                    public void run() {

                    }
                },0);
            }
        });

        quitButton.addListener(new ClickListener(){ // quit
            @Override
            public void clicked(InputEvent event, float x, float y){
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        backSound.play();
                        Gdx.app.exit();
                    }
                },0.8f);
            }
        });
    }

    public void getFocus() {
        if (Gdx.input.isKeyJustPressed(Keys.DOWN) && singleButton.hasKeyboardFocus()){
            stage.setKeyboardFocus(multiButton);
        } else if (Gdx.input.isKeyJustPressed(Keys.DOWN) && multiButton.hasKeyboardFocus()){
            stage.setKeyboardFocus(leaderButton);
        } else if (Gdx.input.isKeyJustPressed(Keys.DOWN) && leaderButton.hasKeyboardFocus()){
            stage.setKeyboardFocus(quitButton);
        } else if (Gdx.input.isKeyJustPressed(Keys.DOWN) && quitButton.hasKeyboardFocus()){
            stage.setKeyboardFocus(singleButton);
        }

        if (Gdx.input.isKeyJustPressed(Keys.UP) && singleButton.hasKeyboardFocus()){
            stage.setKeyboardFocus(quitButton);
        } else if (Gdx.input.isKeyJustPressed(Keys.UP) && multiButton.hasKeyboardFocus()){
            stage.setKeyboardFocus(singleButton);
        } else if (Gdx.input.isKeyJustPressed(Keys.UP) && leaderButton.hasKeyboardFocus()){
            stage.setKeyboardFocus(multiButton);
        } else if (Gdx.input.isKeyJustPressed(Keys.UP) && quitButton.hasKeyboardFocus()){
            stage.setKeyboardFocus(leaderButton);
        }
    }

    public void handleEnter() throws ReflectionException {
        if (Gdx.input.isKeyJustPressed(Keys.ENTER) && singleButton.hasKeyboardFocus()){
            context.setScreen(ScreenType.SSEL);
        } else if (Gdx.input.isKeyJustPressed(Keys.ENTER) && multiButton.hasKeyboardFocus()){
            context.setScreen(ScreenType.MSEL);
        } else if (Gdx.input.isKeyJustPressed(Keys.ENTER) && leaderButton.hasKeyboardFocus()){
            dialog.show(stage).setY(dialog.getHeight()/2);
        } else if (Gdx.input.isKeyJustPressed(Keys.ENTER) && quitButton.hasKeyboardFocus()){
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    backSound.play();
                    Gdx.app.exit();
                }
            },0.8f);
        }

    }

    public void handleInput(float dt) throws ReflectionException {
        getFocus();
        handleEnter();

        if (Gdx.input.isKeyJustPressed(Keys.NUM_2)){
            try {
                context.setScreen(ScreenType.SOLO);
            } catch (ReflectionException e){
                e.printStackTrace();
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            dialog.hide();
        }
    }

    public void update(float dt) throws ReflectionException {
        handleInput(dt);
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Keys.ENTER)){
            //todo add trigger event for pressing enter on a button
        }
        Gdx.gl.glClearColor(0.25882354f,  0.25882354f, 0.90588236f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        try {
            update(delta);
        } catch (ReflectionException e) {
            e.printStackTrace();
        }
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void show(){
        stage.setKeyboardFocus(singleButton);
        if(!music.isPlaying()){
            music.play();
        }
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void hide(){

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
