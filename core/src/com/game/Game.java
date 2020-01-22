package com.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.game.entities.*;
import com.game.helpers.KeyManager;
import com.game.helpers.Resourses;

import java.util.ArrayList;
import java.util.ResourceBundle;

public class Game extends ApplicationAdapter {
    World world;
    Renderer renderer;
    public static boolean isAndroid;

    public Game(boolean isAndroid){
        Game.isAndroid = isAndroid;
    }

    @Override
    public void create() {
        Resourses.Load();
        world = new World();
        renderer = new Renderer(world);
        Gdx.input.setInputProcessor(new KeyManager(world.getPlane(), world));
    }

    @Override
    public void render() {
        world.ProcessLogic(Gdx.graphics.getDeltaTime());
        renderer.Render();
    }

    public void pause() {

    }

    public void resume() {

    }


    @Override
    public void dispose() {

    }
}
