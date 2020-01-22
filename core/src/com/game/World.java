package com.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.game.entities.*;
import com.game.helpers.HeavyCloudWithPlantains;
import com.game.helpers.KeyManager;
import com.game.helpers.Troposphere;

import java.util.ArrayList;
import java.util.Random;

public class World {
    public static float SCREEN_FACTOR;
    private Plane plane;
    private ArrayList<Bullet> planesBullets;
    private ArrayList<Bullet> minionsBullets;
    private ArrayList<Bullet> minionsBulletsMiddle;
    private ArrayList<Explosion> explosions;
    private Minion minion;
    private com.game.helpers.Troposphere troposphere;
    private com.game.helpers.HeavyCloudWithPlantains heavyCloudWithPlantains;
    public static int height, width;

    private GameState currState;
    private int balls;


    public World() {
        height = Gdx.graphics.getHeight();
        width = Gdx.graphics.getWidth();
        currState = GameState.READY;
        balls = 0;
        Init();
    }

    private void Init() {
        if (width < height)
            SCREEN_FACTOR = (float) width / 720;
        else
            SCREEN_FACTOR = (float) height / 600;
        planesBullets = new ArrayList<Bullet>();
        minionsBullets = new ArrayList<Bullet>();
        minionsBulletsMiddle = new ArrayList<Bullet>();
        explosions = new ArrayList<Explosion>();
        troposphere = new com.game.helpers.Troposphere();
        Vector2 posPlane = new Vector2(width / 2 - 50, 20);
        plane = new Plane(posPlane, planesBullets);
        heavyCloudWithPlantains = new com.game.helpers.HeavyCloudWithPlantains(height, width, plane);
        Vector2 posMinion = new Vector2((float)width / 2 - 50, height + 200);
        minion = new Minion(posMinion, this);
    }

    public void ProcessLogic(float delta) {
        if (currState == GameState.READY)
            ProcessLogicReady(delta);
        else if (currState == GameState.RUNNING)
            ProcessLogicRunning(delta);
        else if (currState == GameState.GAMEOVER)
            ProcessLogicGameOver(delta);

    }

    private void ProcessLogicReady(float delta) {
        troposphere.ProcessLogic(delta);
    }

    private void ProcessLogicRunning(float delta) {
        plane.ProcessLogic(delta);
        if (KeyManager.isTouch)
            plane.Attack();
        if (Game.isAndroid) {
            float sign = -Math.signum(Gdx.input.getAccelerometerZ());
            plane.setDirection(sign * (Gdx.input.getAccelerometerX() - KeyManager.calibrate.x) * 150 * SCREEN_FACTOR,
                    sign * (Gdx.input.getAccelerometerY() - KeyManager.calibrate.y) * 150 * SCREEN_FACTOR);
        }

        ListProcessLogic(planesBullets, delta);
        ListProcessLogic(minionsBullets, delta);
        ListProcessLogic(minionsBulletsMiddle, delta);
        ListProcessLogic(explosions, delta);

        troposphere.ProcessLogic(delta);
        heavyCloudWithPlantains.ProcessLogic(delta);
        minion.ProcessLogic(delta);

        CheckCollision();
    }

    private void ProcessLogicGameOver(float delta) {
        ListProcessLogic(planesBullets, delta);
        ListProcessLogic(minionsBullets, delta);
        ListProcessLogic(minionsBulletsMiddle, delta);
        ListProcessLogic(explosions, delta);
        troposphere.ProcessLogic(delta);
        minion.ProcessLogic(delta);
    }

    private void SetReadyState() {
        plane.Reset();
        minion.Reset();
        currState = GameState.READY;
        balls = 0;
        planesBullets.clear();
        minionsBullets.clear();
        minionsBulletsMiddle.clear();
    }

    private void SetRunningState() {
        currState = GameState.RUNNING;
        heavyCloudWithPlantains.Reset();
    }

    private void SetGameOverState() {
        ExplodeEntity(plane);
        currState = GameState.GAMEOVER;
        heavyCloudWithPlantains.ClearPlantains();
    }

    private void ExplodeEntity(Entity entity) {
        Random rand = new Random();
        for (int i = 0; i < 10; i++) {
            explosions.add(new Explosion(new Vector2(entity.getPosition().x + rand.nextFloat() * entity.getSize().x,
                    entity.getPosition().y + rand.nextFloat() * entity.getSize().y), entity.getSize().cpy().scl(rand.nextFloat())));
        }
    }



    private void CreateNewMinion() {
        ExplodeEntity(minion);
        Random randX = new Random();
        minion.Upgrade();
        minion.setPosition(randX.nextInt(width), height + 200);
        minion.Raise();
    }


    private <T extends Entity> void ListProcessLogic(ArrayList<T> entities, float delta) {
        for (int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);
            entity.ProcessLogic(delta);
            if (entity.IsDead()) {
                entities.remove(i);
                i--;
            }
        }
    }


    private void CheckCollision() {
        CheckCollisionWithBullets(plane, minionsBullets, true);
        CheckCollisionWithBullets(plane, minionsBulletsMiddle, true);
        CheckCollisionWithBullets(plane, heavyCloudWithPlantains.getPlantains(), false);

        CheckCollisionWithBullets(minion, planesBullets, true);
        if (isCollides(plane, minion)) {
            int damagePlane = plane.getDamage();
            int damageMinion = minion.getDamage();
            plane.CauseDamage(damageMinion);
            minion.CauseDamage(damagePlane);
        }
        if (plane.IsDead())
            SetGameOverState();
        if (minion.IsDead()) {
            CreateNewMinion();
            balls++;
        }
    }

    private boolean CheckCollisionWithBullets(MaterialEntity entity, ArrayList<Bullet> list, boolean bulletExplodes) {
        for (int i = 0; i < list.size(); i++) {
            Bullet bullet = list.get(i);
            if (isCollides(entity, bullet)) {
                //нанести урон существу
                bullet.CauseDamage(entity.getDamage());
                entity.CauseDamage(bullet.getDamage());
                if (bullet.IsDead()) {
                    //удалить пулю из списка
                    list.remove(i);
                    i--;
                    if (bulletExplodes) {
                        //создадим взрыв на ее месте
                        float size = (float)bullet.getDamage() / 2.5f * SCREEN_FACTOR;
                        explosions.add(new Explosion(bullet.getPosition(), new Vector2(size, size)));
                    }
                }
                //если существо погибло
                if (entity.IsDead())
                    return true;
            }
        }
        return false;
    }

    private boolean isCollides(MaterialEntity one, MaterialEntity two) {
        if (Intersector.overlapConvexPolygons(one.getPolygon(), two.getPolygon()))
            return true;
        return false;
    }

    public Plane getPlane() {
        return plane;
    }

    public ArrayList<Bullet> getPlanesBullets() {
        return planesBullets;
    }

    public ArrayList<Bullet> getMinionsBullets() {
        return minionsBullets;
    }

    public ArrayList<Bullet> getMinionsBulletsMiddle() {
        return minionsBulletsMiddle;
    }

    public ArrayList<Explosion> getExplosions() {
        return explosions;
    }

    public Minion getMinion() {
        return minion;
    }

    public Troposphere getTroposphere() {
        return troposphere;
    }

    public HeavyCloudWithPlantains getHeavyCloudWithPlantains() {
        return heavyCloudWithPlantains;
    }

    public GameState getCurrState() {
        return currState;
    }

    public void setCurrState(GameState state) {
        if (state == GameState.READY)
            SetReadyState();
        else if (state == GameState.RUNNING)
            SetRunningState();
        else
            currState = state;
    }

    public int getBalls() {
        return balls;
    }
}
