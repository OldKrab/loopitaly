package com.game.entities;

import com.badlogic.gdx.math.Vector2;
import com.game.helpers.Resourses;

public class Explosion extends Entity {
    private float currTime;
    private float duration;
    public Explosion(Vector2 position, Vector2 size) {
        super(position);
        this.size.set(size);
        this.position.sub(size.x/2, size.y/2);
        currTime = 0;
        duration = Resourses.explosion.getAnimationDuration();
    }

    @Override
    public void ProcessLogic(float delta) {
        super.ProcessLogic(delta);
        currTime += delta;
    }

    @Override
    public boolean IsDead() {
        if(currTime > duration)
            return true;
        return false;
    }

    public float getCurrTime() {
        return currTime;
    }
}
