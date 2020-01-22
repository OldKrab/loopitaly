package com.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

abstract public class MaterialEntity extends Entity  {
    protected int health;
    protected int maxHealth;
    protected int damage;
    protected Polygon polygon;
    protected long lastTimeCauseDamage = 0;

    public MaterialEntity(Vector2 position) {
        super(position);
    }


    abstract protected void FormPolygon();

    @Override
    public void ProcessLogic(float delta){
        super.ProcessLogic(delta);
        polygon.setPosition(getPosition().x, getPosition().y);
    }

    public void CauseDamage(int damage) {
        health = getHealth() - damage;
        if(health > maxHealth)      //дамаг не всегда плохо
            health = maxHealth;
        lastTimeCauseDamage = System.currentTimeMillis();
    }

    public boolean IsDead(){
        return getHealth() <= 0;
    }

    public int getDamage() {
        return damage;
    }

    public Polygon getPolygon() {
        return polygon;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public long getLastTimeCauseDamage() {
        return lastTimeCauseDamage;
    }
}
