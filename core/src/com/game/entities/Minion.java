package com.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.game.World;

import java.util.ArrayList;

public class Minion extends MaterialEntity implements AttackBehavior {
    long lastTimeAttack = 0;
    Vector2 startPosition;
    boolean isThere = false;
    float there;
    ArrayList<Bullet> bullets;
    ArrayList<Bullet> bulletsMiddle;
    Plane plane;
    float speed = 200 * World.SCREEN_FACTOR;
    float speedBullet = 300;
    float speedReloadBullet = 2000;
    float angleBullet;
    ImmortalModule immortalModule;

    public Minion(Vector2 position, World world) {
        super(position);
        angleBullet = (float) (Math.atan(0.2 * World.width / World.height) * 180f / Math.PI);
        startPosition = position;
        size.set(150 * World.SCREEN_FACTOR, 150 * World.SCREEN_FACTOR);
        direction.set(0, -speed);
        health = maxHealth = 200;
        damage = 500;
        FormPolygon();
        there = Gdx.graphics.getHeight() - getSize().y - 10;
        bullets = world.getMinionsBullets();
        bulletsMiddle = world.getMinionsBulletsMiddle();
        plane = world.getPlane();
        immortalModule = new ImmortalModule();
        immortalModule.Start();
    }

    @Override
    protected void FormPolygon() {
        float[] arr = {
                0, 0,
                getSize().x, 0,
                0, getSize().y,
                getSize().x, getSize().y};
        polygon = new Polygon(arr);
        polygon.setPosition(getPosition().x, getPosition().y);
    }

    @Override
    public void ProcessLogic(float delta) {
        super.ProcessLogic(delta);
        damage = health;
        if (!isThere) {
            direction.set(0, -speed);
            if (position.y <= there) {
                position.y = there;
                direction.y = 0;
                direction.x = speed;
                isThere = true;
                immortalModule.Stop();
            }
        } else {
            //проверка на столкновение с границами экрана
            if (getPosition().x < 10)
                direction.x = speed;
            else if (getPosition().x + getSize().x > Gdx.graphics.getWidth() - 10)
                direction.x = -speed;
            //атака
            if (!plane.IsDead()) {
                long currTime = System.currentTimeMillis();
                if (currTime - lastTimeAttack > speedReloadBullet) {
                    Attack();
                    lastTimeAttack = currTime;
                }
            }
        }
    }

    @Override
    public void Attack() {
        Vector2 dir = new Vector2(plane.getPosition().x - getPosition().x,
                plane.getPosition().y + plane.getSize().y / 2 - getPosition().y);
        float len = dir.len() / speedBullet;

        dir.x /= len;
        dir.y /= len;
        Vector2 size = new Vector2(16*World.SCREEN_FACTOR, 64*World.SCREEN_FACTOR);
        Bullet bullet = new Bullet(new Vector2(getPosition().x + getSize().x / 2, getPosition().y - size.y/2), dir, size, 200);
        bullet.getPolygon().setRotation(dir.angle() + 90);
        bulletsMiddle.add(bullet);
        dir.setAngle(dir.angle() + angleBullet);

        size = new Vector2(20*World.SCREEN_FACTOR, 20*World.SCREEN_FACTOR);


        bullet = new Bullet(new Vector2(getPosition().x + getSize().x / 2, getPosition().y - size.y), dir, size, 100);
        bullets.add(bullet);
        dir.setAngle(dir.angle() - 2 * angleBullet);

        bullet = new Bullet(new Vector2(getPosition().x + getSize().x / 2, getPosition().y - size.y), dir, size, 100);
        bullets.add(bullet);
    }

    public void Reset() {
        isThere = false;
        health = maxHealth = 200;
        position = startPosition.cpy();
        speed = 200;
        speedBullet = 300;
    }

    public void Raise() {
        health = maxHealth;
    }

    public void Upgrade() {
        if (maxHealth < 1500)
            maxHealth += 100;
        if (speedBullet < 1000)
            speedBullet += 50;
        if (speed < 1000 * World.SCREEN_FACTOR)
            speed += 10 * World.SCREEN_FACTOR;
        if (speedReloadBullet > 300)
            speedReloadBullet -= 100;
        lastTimeCauseDamage = 0;
     }

    @Override
    public void CauseDamage(int damage) {
        if (isThere)
            super.CauseDamage(damage);
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        isThere = false;
    }

    public ImmortalModule getImmortalModule() {
        return immortalModule;
    }
}
