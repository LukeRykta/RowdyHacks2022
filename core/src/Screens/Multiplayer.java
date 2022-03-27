package Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import mygdx.game.RhythmGame;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Multiplayer extends AbstractScreen {
    //Global variables
    SpriteBatch batch;
    private Socket socket;
    private final String userName = RhythmGame.username;

    //If we decide to move all lane properties to a separate class that information can be tied to any players ID.
    //HashMap<String, lanes> otherPlayer;

    public Multiplayer(RhythmGame context) {
        super(context);
        create();
        connect();
        configSocketEvents();
    }

    public void create(){

    }

    public void connect(){
        try{
            socket = IO.socket("http://localhost:3000");
            socket.connect();
        } catch (Exception e){
            System.out.println("Error connecting to server!");
        }
    }

    public void configSocketEvents(){
        socket.on(Socket.EVENT_CONNECT, args -> {
            Gdx.app.log("SocketIO", "connected.");
            JSONObject newPlayer = new JSONObject();
            try{
                newPlayer.put("name", userName);
            } catch(JSONException e){
                Gdx.app.log("SocketIO", "Cannot store player data!");
            }
            socket.emit("storePlayer", newPlayer);
            socket.emit("roomCheck", newPlayer);
        }).on("gameStart", args -> {

        });

    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {
        super.resume();
    }

    @Override
    public void hide() {
        super.hide();
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
