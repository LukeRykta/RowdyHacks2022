package Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
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

public class SoloSelect extends AbstractScreen implements InputProcessor {
    private Stage stage;
    private Skin skin;

    private TextButton song1Button;
    private TextButton song2Button;
    private TextButton song3Button;
    private TextButton returnButton;
    private static Dialog dialog;

    private Table menuTable;

    private Music music;
    private Sound forwardSound;
    private Sound nextSound;
    private Sound backSound;
    private Sound popSound;

    public SoloSelect(RhythmGame context) {
        super(context);
        initSkin();
        initStage();
        initMusic();
    }

    public void initMusic(){
        music = manager.get("music/songs/menu.mp3");
        forwardSound = manager.get("music/sounds/forward.wav");
        backSound = manager.get("music/sounds/back.wav");
        popSound = manager.get("music/sounds/fx10.mp3");
        //nextSound = manager.get("music/sounds/next.wav");
    }

    private void initSkin(){
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
    }

    private void initStage(){
        stage  = new Stage(new ScreenViewport());

        menuTable = new Table(skin); // table for menu buttons
        final Table leaderTable = new Table(skin); // table for leaderboard results\

        final Table backgroundTable = new Table(skin); // Table for background
        backgroundTable.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture("uiGFX/backgrounds/bg.png"))));
        backgroundTable.setFillParent(true);

        //initialize menuTable and title table sizes to scale with aspect ration
        menuTable.setWidth(stage.getWidth());
        menuTable.align(Align.center|Align.top);
        menuTable.setPosition(0, Gdx.graphics.getHeight());

        song1Button = new TextButton("Rowdy", skin); // menuTable items
        song2Button = new TextButton("Star", skin);
        song3Button = new TextButton ("Jump", skin);
        returnButton = new TextButton("Back", skin);

        menuTable.row();
        menuTable.padTop(stage.getHeight()/2);
        menuTable.add(song1Button).padBottom(30);
        menuTable.row();
        menuTable.add(song2Button).padBottom(30);
        menuTable.row();
        menuTable.add(song3Button).padBottom(60);
        menuTable.row();
        menuTable.add(returnButton).padBottom(30);

        initActors();

        stage.addActor(backgroundTable);
        stage.addActor(menuTable);
    }

    private void initActors(){
        song1Button.addListener(new ClickListener(){ // singleplayer
            @Override
            public void clicked(InputEvent event, float x, float y) {
                RhythmGame.songname = "rowdy";
                try {
                    forwardSound.play();
                    music.stop();
                    context.setScreen(ScreenType.LOADING);
                } catch (ReflectionException e) {
                    e.printStackTrace();
                }
            }
        });


        song2Button.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                RhythmGame.songname = "star";
                try{
                    forwardSound.play();
                    music.stop();
                    context.setScreen(ScreenType.LOADING);
                } catch (ReflectionException e){
                    e.printStackTrace();
                }
            }
        });

        song3Button.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        try{
                            forwardSound.play();
                            music.stop();
                            context.setScreen(ScreenType.LOADING);
                        } catch (ReflectionException e){
                            e.printStackTrace();
                        }
                    }
                },0);
            }
        });

        returnButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                Timer.schedule(new Timer.Task() {

                    @Override
                    public void run() {
                        backSound.play();
                        try{
                            context.setScreen(ScreenType.MENU);
                        } catch (ReflectionException e){
                            e.printStackTrace();
                        }
                    }
                },0);
            }
        });
    }

    public void getFocus() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) && song1Button.hasKeyboardFocus()){
            popSound.play();
            stage.setKeyboardFocus(song2Button);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) && song2Button.hasKeyboardFocus()){
            popSound.play();
            stage.setKeyboardFocus(song3Button);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) && song3Button.hasKeyboardFocus()) {
            popSound.play();
            stage.setKeyboardFocus(returnButton);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) && returnButton.hasKeyboardFocus()){
            popSound.play();
            stage.setKeyboardFocus(song1Button);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && song1Button.hasKeyboardFocus()){
            popSound.play();
            stage.setKeyboardFocus(returnButton);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && song2Button.hasKeyboardFocus()){
            popSound.play();
            stage.setKeyboardFocus(song1Button);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && song3Button.hasKeyboardFocus()) {
            popSound.play();
            stage.setKeyboardFocus(song2Button);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && returnButton.hasKeyboardFocus()) {
            popSound.play();
            stage.setKeyboardFocus(song3Button);
        }
    }

    public void handleEnter() throws ReflectionException {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) && song1Button.hasKeyboardFocus()){
            RhythmGame.songname = "rowdy";
            forwardSound.play();
            music.stop();
            context.setScreen(ScreenType.LOADING);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) && song2Button.hasKeyboardFocus()){
            RhythmGame.songname = "star";
            forwardSound.play();
            music.stop();
            context.setScreen(ScreenType.LOADING);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) && song3Button.hasKeyboardFocus()) {
            forwardSound.play();
            music.stop();
            context.setScreen(ScreenType.LOADING);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) && returnButton.hasKeyboardFocus()) {
            backSound.play();
            context.setScreen(ScreenType.MENU);
        }
    }

    public void handleInput(float dt) throws ReflectionException {
        getFocus();
        handleEnter();

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)){
            try {
                context.setScreen(ScreenType.SOLO);
            } catch (ReflectionException e){
                e.printStackTrace();
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            backSound.play();
            context.setScreen(ScreenType.MENU);
        }
    }

    public void update(float dt) throws ReflectionException {
        handleInput(dt);
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
            //todo add trigger event for pressing enter on a button
        }
        Gdx.gl.glClearColor(0,  0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        try {
            update(delta);
        } catch (ReflectionException e) {
            e.printStackTrace();
        }
        stage.act(delta);
        stage.draw();
        //FIXME Menu button navigation
        //if(Gdx.input.isKeyPressed(Keys.UP))
    }

    @Override
    public void show(){
        stage.setKeyboardFocus(song1Button);
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
