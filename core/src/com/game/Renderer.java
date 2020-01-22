package com.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.game.entities.*;
import com.game.entities.ImmortalModule;
import com.game.helpers.Resourses;
import com.game.helpers.ScoreWindow;
import com.game.helpers.TextManager;

import java.util.ArrayList;
import java.util.List;

public class Renderer {
    SpriteBatch batch;

    private Plane plane;
    private ArrayList<Bullet> planesBullets, minionsBullets, minionsBulletsMiddle;
    private ArrayList<Explosion> explosions;
    private Minion minion;
    private Cloud[] clouds;
    private ArrayList<Plantain> plantains;
    private TextManager startMessage;
    private TextManager ballsMessage;
    private com.game.helpers.ScoreWindow scoreWindow;
    private TextManager reloadMessage;

    private World world;

    public Renderer(World world) {
        batch = new SpriteBatch();

        plane = world.getPlane();
        planesBullets = world.getPlanesBullets();
        minionsBullets = world.getMinionsBullets();
        minionsBulletsMiddle = world.getMinionsBulletsMiddle();
        minion = world.getMinion();
        clouds = world.getTroposphere().getClouds();
        plantains = world.getHeavyCloudWithPlantains().getPlantains();
        explosions = world.getExplosions();
        this.world = world;
        String textStart;
        if (Game.isAndroid)
            textStart = "Нажмите,\nчтобы начать";
        else
            textStart = "Нажмите любую клавишу,\nчтобы начать";
        startMessage = new TextManager(textStart,
                new Vector2((float) World.width / 2, (float)World.height / 2), (int) (35 * World.SCREEN_FACTOR));
        ballsMessage = new TextManager(
                "" + world.getBalls(),
                new Vector2(10, World.height - 10), (int) (60 * World.SCREEN_FACTOR), Align.left);
        scoreWindow = new ScoreWindow(world);
        String textReload;
        if (Game.isAndroid)
            textReload = "Нажмите,\nчтобы начать заново";
        else
            textReload = "Нажмите клавишу R,\nчтобы начать заново";
        reloadMessage = new TextManager(textReload,  new Vector2((float)World.width/2, scoreWindow.getPosition().y - 50),
                (int) (35 * World.SCREEN_FACTOR));
    }

    /**
     * Отрисовка игровых обьектов
     */
    public void Render() {
        //закрасить экран синим
        Gdx.gl.glClearColor(0.5f, 0.9f, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        //облака
        for (Cloud cloud : clouds)
            Draw(Resourses.cloud, cloud);
        //подорожники
        DrawPlantains();
        //пули
        for (Bullet bullet : planesBullets)
            Draw(Resourses.bulletPlane, bullet);
        for (Bullet bullet : minionsBullets)
            Draw(Resourses.meatBall, bullet);
        DrawMacarones();
        //самолет
        ImmortalModule immortal = plane.getImmortalModule();
        if (!plane.IsDead() && (!immortal.IsImmortal()
                || (int) (System.currentTimeMillis() - immortal.getTimeStartImmortal()) / 150 % 2 == 1))
            //если самолет не мертв и (он не бессмертен или с момента начала бессмертия прошло нечетное количество интервалов)
            DrawPlane();
        if (world.getCurrState() == GameState.RUNNING)
            DrawHealthPlane();
        //миньон
        immortal = minion.getImmortalModule();
        if (!immortal.IsImmortal() || (int) (System.currentTimeMillis() - immortal.getTimeStartImmortal()) / 100 % 2 == 1)
        DrawMinion();
        if (System.currentTimeMillis() - minion.getLastTimeCauseDamage() < 2000)
            DrawHealthUnder(minion);
        //текст
        if (world.getCurrState() == GameState.READY) {
            startMessage.DisplayMessage(batch);
        } else if (world.getCurrState() == GameState.RUNNING) {
            ballsMessage.SetText("" + world.getBalls());
            ballsMessage.DisplayMessage(batch);
        }
        if (world.getCurrState() == GameState.GAMEOVER) {
            scoreWindow.Render(batch);
            reloadMessage.DisplayMessage(batch);
        }
        //взрывы
        DrawExplosions(batch);
        batch.end();
    }

    private void DrawExplosions(SpriteBatch batch) {
        TextureRegion currFrame;
        Vector2 pos;
        Vector2 size;
        for (int i = 0; i < explosions.size(); i++) {
            currFrame = (TextureRegion)Resourses.explosion.getKeyFrame(explosions.get(i).getCurrTime());
            pos = explosions.get(i).getPosition();
            size = explosions.get(i).getSize();
            batch.draw(currFrame, pos.x, pos.y, size.x, size.y);
        }
    }

    private void Draw(Texture texture, Entity entity) {
        batch.draw(texture, entity.getPosition().x, entity.getPosition().y, entity.getSize().x, entity.getSize().y);
    }

    private void DrawPlantains() {
        for (Bullet plantain : plantains)
            batch.draw(Resourses.plantain, plantain.getPosition().x, plantain.getPosition().y,
                    plantain.getSize().x / 2, plantain.getSize().y / 2,
                    plantain.getSize().x, plantain.getSize().y,
                    1, 1, plantain.getRotation());
    }

    private void DrawMacarones() {
        for (Bullet macarone : minionsBulletsMiddle)
            batch.draw(Resourses.macarone, macarone.getPosition().x, macarone.getPosition().y,
                    macarone.getSize().x / 2, macarone.getSize().y / 2,
                    macarone.getSize().x, macarone.getSize().y,
                    1, 1, macarone.getPolygon().getRotation());

    }

    private void DrawPlane() {
        batch.draw(Resourses.plane, plane.getPosition().x, plane.getPosition().y,
                plane.getSize().x / 2, plane.getSize().y / 2,
                plane.getSize().x, plane.getSize().y,
                1, 1, plane.getPolygon().getRotation());

    }

    private void DrawMinion() {
        if (minion.getDirection().x >= 0)
            batch.draw(Resourses.minion, minion.getPosition().x, minion.getPosition().y,
                    minion.getSize().x / 2, minion.getSize().y / 2,
                    minion.getSize().x, minion.getSize().y,
                    -1, 1, 0);
        else
            batch.draw(Resourses.minion, minion.getPosition().x, minion.getPosition().y,
                    minion.getSize().x / 2, minion.getSize().y / 2,
                    minion.getSize().x, minion.getSize().y,
                    1, 1, 0);

    }

    private void DrawHealthPlane() {

        batch.draw(Resourses.redHealth, 2, 5, World.width * 0.4f, World.height * 0.02f);
        batch.draw(Resourses.greenHealth, 2, 5,
                World.width * (float) plane.getHealth() / plane.getMaxHealth() * 0.4f, World.height * 0.02f);
    }


    private void DrawHealthUnder(MaterialEntity entity) {
        batch.draw(Resourses.redHealth, entity.getPosition().x, entity.getPosition().y - 10, entity.getSize().x, 5);
        batch.draw(Resourses.greenHealth, entity.getPosition().x, entity.getPosition().y - 10,
                entity.getSize().x * (float) entity.getHealth() / entity.getMaxHealth(), 5);
    }
}
