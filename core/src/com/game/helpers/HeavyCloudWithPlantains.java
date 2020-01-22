package com.game.helpers;

import com.badlogic.gdx.math.Vector2;
import com.game.World;
import com.game.entities.Bullet;
import com.game.entities.Plantain;
import com.game.entities.Plane;

import java.util.*;

public class HeavyCloudWithPlantains {
    private ArrayList<Plantain> plantains;
    private long lastTimePlantain;
    private final int timeReloadMillis = 7000;
    private Random rand;
    private Plane plane;

    public HeavyCloudWithPlantains(Plane plane) {
        plantains = new ArrayList<Plantain>();
        lastTimePlantain = System.currentTimeMillis();
        rand = new Random();
        this.plane = plane;
    }

    public void ProcessLogic(float delta) {
        for (int i = 0; i < plantains.size(); i++) {
            plantains.get(i).ProcessLogic(delta);
            if (plantains.get(i).getPosition().y + plantains.get(i).getSize().y < 0) {
                plantains.remove(i);
                i--;
            }
        }
        if (System.currentTimeMillis() - lastTimePlantain > timeReloadMillis && (float)plane.getHealth() / plane.getMaxHealth() < 0.6f) {
            Vector2 size = new Vector2(50* World.SCREEN_FACTOR, 50*World.SCREEN_FACTOR);
            Vector2 pos = new Vector2(rand.nextInt(World.width - (int)size.x), World.height);
            Vector2 dir = new Vector2(0, -200);
            plantains.add(new Plantain(pos, dir, size, -250));
            lastTimePlantain = System.currentTimeMillis();
        }
    }

    public void ClearPlantains() {
        plantains.clear();
    }

    public void Reset(){
        lastTimePlantain = System.currentTimeMillis();
    }

    public ArrayList<Plantain> getPlantains() {
        return plantains;
    }
}