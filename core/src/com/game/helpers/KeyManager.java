package com.game.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.game.GameState;
import com.game.World;
import com.game.entities.Plane;

public class KeyManager implements InputProcessor {
    private Plane plane;
    private World world;
    public static boolean isTouch = false;
    public static Vector2 calibrate;

    public KeyManager(Plane plane, World world) {
        this.plane = plane;
        this.world = world;
        calibrate = new Vector2(0 , 6);
    }

    @Override
    public boolean keyDown(int keycode) {
        if (world.getCurrState() == GameState.READY) {
            world.setCurrState(GameState.RUNNING);
        }
        else if(world.getCurrState() == GameState.GAMEOVER && keycode == Input.Keys.R)
            world.setCurrState(GameState.READY);

        float x = 0;
        float y = 0;
        float speed = 600 * World.SCREEN_FACTOR;
        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.DPAD_UP))
            y += speed;
        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DPAD_DOWN))
            y -= speed;
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT))
            x -= speed;
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT))
            x += speed;
        plane.setDirection(x, y);
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE))
            isTouch = true;
        return true;
    }

    private void Calibrate(){
        calibrate.set(Gdx.input.getAccelerometerX(), Gdx.input.getAccelerometerY());
    }

    @Override
    public boolean keyUp(int keycode) {
        if (world.getCurrState() == GameState.RUNNING)
            keyDown(keycode);
        isTouch = false;
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (world.getCurrState() == GameState.READY) {
            world.setCurrState(GameState.RUNNING);
            Calibrate();
        }
         else if(world.getCurrState() == GameState.GAMEOVER)
                world.setCurrState(GameState.READY);
        isTouch = true;
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        isTouch = false;
        return true;
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
    public boolean scrolled(int amount) {
        return false;
    }
}
