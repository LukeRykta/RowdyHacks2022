package Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import mygdx.game.RhythmGame;

import javax.sound.midi.InvalidMidiDataException;
import java.io.IOException;

public class Loading extends AbstractScreen{
    private Stage stage;
    private Skin skin;
    private Label info;
    private boolean isGameInit = false;

    public Loading(final RhythmGame context){
        super(context);
        initSkin();
        initStage();
    }

    public void initSkin(){
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
    }

    public void initStage(){
        stage = new Stage (new ScreenViewport());

        final Table loadingTable = new Table(skin);

        loadingTable.setWidth(stage.getWidth());
        loadingTable.align(Align.center|Align.top);
        loadingTable.setPosition(0, Gdx.graphics.getHeight());

        info = new Label("Loading...", skin); // titleTable items

        loadingTable.add(info).padTop(stage.getHeight()/2);

        stage.addActor(loadingTable);

        Timer.schedule(new Timer.Task() {

            @Override
            public void run() {
                context.setScreen(new Solo(context));
                isGameInit = true;
            }
        },2);
    }
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        if (isGameInit){
            RunGame();
        }
    }

    public void handleInput(float dt){

    }

    public void update (float dt){
        handleInput(dt);
    }

    public void RunGame() {
        Timer.schedule(new Timer.Task() {

            @Override
            public void run() {
                context.setScreen(new Solo(context));
            }
        },0);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.25882354f,  0.25882354f, 0.90588236f, 1);
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
