package com.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.game.World;

import java.util.ArrayList;

public class Plane extends MaterialEntity implements AttackBehavior {
    long lastTimeAttack = 0;
    Vector2 startPosition;
    ArrayList<Bullet> bullets;
    ImmortalModule immortalModule;

    public Plane(Vector2 position, ArrayList<Bullet> bulletsList) {
        super(position);
        startPosition = new Vector2(position);
        direction.set(0, 0);
        size.set(100* World.SCREEN_FACTOR, 100 * World.SCREEN_FACTOR);
        health = maxHealth = 666;
        damage = 500;
        FormPolygon();
        bullets = bulletsList;
        immortalModule = new ImmortalModule();
    }

    @Override
    protected void FormPolygon() {
        float[] arr = {
                0, getSize().y * 0.3f,
                getSize().x / 2, 0,
                getSize().x, getSize().x * 0.3f,
                getSize().x / 2, getSize().y};
        polygon = new Polygon(arr);
        polygon.setOrigin(size.x / 2, size.y / 2);
        polygon.setPosition(getPosition().x, getPosition().y);
    }

    @Override
    public void ProcessLogic(float delta) {
        super.ProcessLogic(delta);
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE))
            Attack();
        //поворот полигона
            float angle = polygon.getRotation();
            if (Math.abs(direction.x) > 200)
                angle += -direction.x * delta * 0.3f;
            else if (angle > 0)
                angle -= 300 * delta;
            else if (angle < 0)
                angle += 300 * delta;

            if (Math.abs(angle) < 5 && direction.x * angle >= 0)
                angle = 0;
            int maxAngle = 30;
            if (angle < -maxAngle)
                angle = -maxAngle;
            else if (angle > maxAngle)
                angle = maxAngle;
            polygon.setRotation(angle);


        //проверка на столкновение с границами экрана
        if (getPosition().x < 0)
            getPosition().x = 0;
        else if (getPosition().x + getSize().x > Gdx.graphics.getWidth())
            getPosition().x = Gdx.graphics.getWidth() - getSize().x;
        if (getPosition().y < 10)
            getPosition().y = 10;
        else if (getPosition().y + getSize().y > Gdx.graphics.getHeight())
            getPosition().y = Gdx.graphics.getHeight() - getSize().y;

        //проверка модуля бессмертия
        if(immortalModule.IsImmortal() && System.currentTimeMillis() - immortalModule.getTimeStartImmortal() > 1000)
            immortalModule.Stop();
    }

    @Override
    public void Attack() {
        long currTime = System.currentTimeMillis();
        if (currTime - lastTimeAttack > 300) {
            Vector2 dir = new Vector2(0, 700);
            dir.setAngle(polygon.getRotation() + 90);
            Vector2 pos = new Vector2(polygon.getTransformedVertices()[6], polygon.getTransformedVertices()[7]);
            Bullet bullet = new Bullet(pos, dir, new Vector2(10*World.SCREEN_FACTOR, 20 * World.SCREEN_FACTOR), 100);
            bullets.add(bullet);
            lastTimeAttack = currTime;
        }
    }

    @Override
    public void CauseDamage(int damage) {
        if(!immortalModule.IsImmortal() || damage < 0) {
            super.CauseDamage(damage);
            if(damage > 0)
            immortalModule.Start();
        }
    }

    public void Reset() {
        health = maxHealth;
        this.position.set(startPosition);
        lastTimeCauseDamage = 0;
        polygon.setRotation(0);
    }

    @Override
    public int getDamage() {
        if(immortalModule.IsImmortal())
            return 0;
        return super.getDamage();
    }

    public ImmortalModule getImmortalModule() {
        return immortalModule;
    }
}
