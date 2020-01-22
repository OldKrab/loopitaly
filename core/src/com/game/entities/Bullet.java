package com.game.entities;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.game.World;

public class Bullet extends MaterialEntity {
    public Bullet(Vector2 position, Vector2 direction, Vector2 size, int damage) {
        super(position);
        this.size.set(size);
        setDirection(direction.x, direction.y);
        health = maxHealth = 1;
        this.damage = damage;
        FormPolygon();
    }

    @Override
    protected void FormPolygon() {
        float[] arr = {
                0, 0,
                getSize().x, 0,
                getSize().x, getSize().y,
                0, getSize().y};
        polygon = new Polygon(arr);
        polygon.setOrigin(size.x / 2, size.y / 2);
        polygon.setPosition(getPosition().x, getPosition().y);
    }

    @Override
    public boolean IsDead() {
        return (super.IsDead() || IsOutOfScreen());
    }

    private boolean IsOutOfScreen() {
        if (position.x + size.x < 0 || position.x > World.width ||
                position.y + size.y < 0 || position.y > World.height)
            return true;
        return false;
    }

    public float getRotation() {
        return polygon.getRotation();
    }
}