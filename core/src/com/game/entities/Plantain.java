package com.game.entities;


import com.badlogic.gdx.math.Vector2;

public class Plantain extends Bullet {
   private float rotation;
    public Plantain(Vector2 position, Vector2 direction, Vector2 size, int damage) {
        super(position, direction, size, damage);
        rotation = 0;
        health = maxHealth = 0;
    }

    @Override
    public void ProcessLogic(float delta) {
        super.ProcessLogic(delta);
        rotation += 200 * delta;
    }

    @Override
    public float getRotation() {
        return rotation;
    }
}
