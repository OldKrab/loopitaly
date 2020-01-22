package com.game.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.game.World;
import com.game.helpers.Resourses;
import com.game.helpers.TextManager;

import java.util.Scanner;

public class ScoreWindow {
    Vector2 position, size;
    TextManager currScore, recordScore;
    TextManager currLabel, recordLabel;
    World world;
    Preferences prefs;
    int record, curr;
    public ScoreWindow(World world){
        prefs = Gdx.app.getPreferences("Loop");
        record = prefs.getInteger("highScore");
        this.world = world;
        float sizeY = 400 * World.SCREEN_FACTOR;
        size = new Vector2(sizeY * 0.7f, sizeY);
        position = new Vector2((float)World.width/2 - size.x/2, (float)World.height/2 - size.y/2 + 100);
        currLabel = new TextManager("SCORE", new Vector2(position.x+ size.x/2, position.y + size.y*0.9f), (int)(60 * World.SCREEN_FACTOR));
        currScore = new TextManager("0", new Vector2(position.x+ size.x/2, position.y + size.y*0.75f), (int)(100*World.SCREEN_FACTOR));
        recordLabel = new TextManager("BEST", new Vector2(position.x+ size.x/2, position.y + size.y*0.45f), (int)(60*World.SCREEN_FACTOR));
        recordScore = new TextManager(""+ record, new Vector2(position.x+ size.x/2, position.y + size.y*0.3f), (int)(100*World.SCREEN_FACTOR));
    }
    public void Render(SpriteBatch batch){
        batch.draw(Resourses.scoreWindow, position.x, position.y, size.x, size.y);
        UpdateScores();
        currLabel.DisplayMessage(batch);
        currScore.DisplayMessage(batch);
        recordLabel.DisplayMessage(batch);
        recordScore.DisplayMessage(batch);
    }
    private void UpdateScores(){
        if(curr != world.getBalls()) {
            curr = world.getBalls();
            currScore.SetText("" + curr);
        }
        if(curr > record){
            record = curr;
            recordScore.SetText("" + record);
            prefs.putInteger("highScore", record);
            prefs.flush();
        }
    }

    public Vector2 getPosition() {
        return position;
    }
}
