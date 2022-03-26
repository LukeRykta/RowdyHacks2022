package Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import mygdx.game.RhythmGame;

public class Hud implements Disposable {
    public Stage stage;
    private Viewport viewport;
    private Skin skin;

    public static Integer score;
    private static Label scoreLabel;
    private static Label exclaimLabel;
    public static String exclaim = "start";

    Table table = new Table();

    public Hud (SpriteBatch sb){
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        score = 0;

        viewport = new StretchViewport(RhythmGame.V_WIDTH, RhythmGame.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        table.top();
        table.setFillParent(true);

        scoreLabel = new Label(String.format("%06d", score), skin);
        exclaimLabel = new Label(String.format("%s", exclaim), skin);

        table.add(scoreLabel).expandX();
        table.row();
        table.add(exclaimLabel).expand();
        stage.addActor(table);
    }

    public static void addScore(int value){
        score += value;
        scoreLabel.setText(String.format("%06d", score));
    }

    public static void removeScore(int value){
        score -= value;
        if (score < 0){
            score = 0;
        }
        scoreLabel.setText(String.format("%06d", score));
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public void update(float dt) {

    }
}
