package com.game.entities;

public class ImmortalModule {
    private long timeStartImmortal;
    private boolean isImmortal;

    public ImmortalModule(){
        timeStartImmortal = 0;
        isImmortal = false;
    }

    public void Start(){
        timeStartImmortal = System.currentTimeMillis();
        isImmortal = true;
    }

    public void Stop(){
        isImmortal = false;
    }

    public boolean IsImmortal(){
        return isImmortal;
    }

    public long getTimeStartImmortal() {
        return timeStartImmortal;
    }
}
