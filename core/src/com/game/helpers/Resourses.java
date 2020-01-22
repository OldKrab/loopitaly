package com.game.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class Resourses {
    static public Texture cloud, bulletPlane, meatBall, redHealth, greenHealth, scoreWindow;
    static public TextureRegion plane, minion, macarone, plantain;
    static public Animation explosion;
    static public void Load() {
        try {
           Texture planeTexture = new Texture("plane.png");
            plane = new TextureRegion(planeTexture);
            Texture minionTexture = new Texture("minion.png");
            minion = new TextureRegion(minionTexture);
            cloud = new Texture("cloud.png");
            bulletPlane = new Texture("bullet.png");
            redHealth = new Texture("red.png");
            greenHealth = new Texture("green.png");
            meatBall = new Texture("meatball.png");
            Texture macaroneTexture = new Texture("macarone.png");
            macarone = new TextureRegion(macaroneTexture);
            scoreWindow = new Texture("scoreWindow.png");
            Texture plantainTexture = new Texture("plantain.png");
            plantain = new TextureRegion(plantainTexture);

            Texture expTexture = new Texture("explosion.png");
            TextureRegion[][] temp = TextureRegion.split(expTexture, expTexture.getWidth() / 9, expTexture.getHeight());
            TextureRegion[] frames = new TextureRegion[9];
            for(int i = 0; i < frames.length; i++)
                frames[i] = temp[0][i];
            explosion = new Animation(0.07f, frames);

        } catch (GdxRuntimeException e) {
            Gdx.app.debug("Error", e.getMessage());
            System.exit(1);
        }
    }

}
