package com.game.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.game.Game;
import com.game.World;
import com.game.entities.Cloud;

import java.util.Random;

public class Troposphere {
    private Cloud[] clouds;
    private final int nClouds = 30;

    public Troposphere() {
        clouds = new Cloud[nClouds];
        for (int i = 0; i < nClouds; i++) {
            Random randInt = new Random();
            float size = (float) randInt.nextInt((int)(200f* World.SCREEN_FACTOR)) + 50*World.SCREEN_FACTOR;
            float x = (float) randInt.nextInt(World.width) - size / 2;
            clouds[i] = new Cloud(new Vector2(x, i * 50), new Vector2(size, size));
        }
        //сортировка по размеру для корректной отрисовки
        for(int i = 0; i < nClouds - 1; i++)
            for(int j = i + 1; j < nClouds; j++)
                if(clouds[i].getSize().x > clouds[j].getSize().x){
                    Cloud temp = clouds[i];
                    clouds[i] = clouds[j];
                    clouds[j] = temp;
                }
    }

    public void ProcessLogic(float delta) {
        for (Cloud cloud : clouds) {
            cloud.ProcessLogic(delta);
            if (cloud.IsDead())
                cloud.Reset(World.height);
        }
    }
    public Cloud[] getClouds(){
        return clouds;
    }
}
