package com.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Cloud extends Entity {


    public Cloud(Vector2 position, Vector2 size) {
        super(position);
        this.size.set(size);
        direction.x = 0;
        direction.y = -1.5f * size.x;
    }


    public void Reset(int startY) {
        position.y = startY;
    }

    @Override
    public boolean IsDead() {
        return (position.y + size.y) < 0;
    }
}
