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

public class LocalHud implements Disposable {
    public Stage stage;

    public static Integer leftscore;
    public static Integer rightscore;
    private static Label leftScoreLabel;
    private static Label rightScoreLabel;

    Table table = new Table();

    public LocalHud(SpriteBatch sb){
        Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        leftscore = 0;
        rightscore = 0;

        Viewport viewport = new StretchViewport(RhythmGame.V_WIDTH, RhythmGame.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        table.top();
        table.setFillParent(true);

        leftScoreLabel = new Label(String.format("%06d", leftscore), skin);
        rightScoreLabel = new Label(String.format("%06d", rightscore), skin);

        table.add(leftScoreLabel).expandX();
        table.columnDefaults(2);
        table.add(rightScoreLabel).expandX();
        stage.addActor(table);
    }

    public static void LaddScore(int value){
        leftscore += value;
        leftScoreLabel.setText(String.format("%06d", leftscore));
    }

    public static void LremoveScore(int value){
        leftscore -= value;
        if (leftscore < 0){
            leftscore = 0;
        }
        leftScoreLabel.setText(String.format("%06d", leftscore));
    }
    public static void RaddScore(int value){
        rightscore += value;
        rightScoreLabel.setText(String.format("%06d", rightscore));
    }

    public static void RremoveScore(int value){
        rightscore -= value;
        if (rightscore < 0){
            rightscore = 0;
        }
        rightScoreLabel.setText(String.format("%06d", rightscore));
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public void update(float dt) {

    }
}
