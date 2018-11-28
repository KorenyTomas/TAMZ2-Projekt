package com.example.tomas.tamz2_projekt;

import android.graphics.RectF;
import android.support.constraint.solver.widgets.Rectangle;

public class Bullet {
    private float x;
    private float y;

    private RectF hitbox;

    public final int UP = 0;
    public final int DOWN = 1;

    int heading = -1;
    float speed = 10;

    private int width;
    private int height;

    private boolean isActive;

    public Bullet(int sizeY){
        height = sizeY / 20;
        isActive = false;

        hitbox = new RectF();
    }

    public RectF getHitbox(){
        return hitbox;
    }

    public boolean getStatus(){
        return isActive;
    }

    public void shoot(float startX, float startY, int direction) {
        if (!isActive) {
            x = startX;
            y = startY;
            heading = direction;
            isActive = true;

        }
    }

    public void update(/*long fps*/){

        if(heading == UP){
            y = y - speed; // / fps;
        }else{
            y = y + speed; // / fps;
        }

        hitbox.left = x;
        hitbox.top = y;
        hitbox.right = x + width;
        hitbox.bottom = y + height;

    }
}
