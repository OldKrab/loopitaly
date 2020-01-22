package com.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

abstract public class Entity {
    protected Vector2 size;
    protected Vector2 position;
    protected Vector2 direction;

    public Entity(Vector2 position){
        this.size = new Vector2();
        this.position = new Vector2(position);
        direction = new Vector2();
    }

    public void ProcessLogic(float delta) {
        position.x += direction.x * delta;
        position.y += direction.y * delta;
    }

    public abstract boolean IsDead();

    public void setDirection(float x, float y) {
        direction.x = x;
        direction.y = y;
    }

    public void setPosition(float x, float y) {
        position.x = x;
        position.y = y;
    }

    @Override
    protected void finalize() {
        try {
            super.finalize();
        } catch (Throwable ignored) {
        }
    }

    public Vector2 getSize() {
        return size;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getDirection() {
        return direction;
    }
}
