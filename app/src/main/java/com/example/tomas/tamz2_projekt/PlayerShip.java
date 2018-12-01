package com.example.tomas.tamz2_projekt;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.support.constraint.solver.widgets.Rectangle;

public class PlayerShip {

    // Hitbox lodě
    RectF shipHitbox;

    private Bitmap ship;

    // délka a výška lodi
    private float length;
    private float height;

    private float x;
    private float y;

    private float shipSpeed;

    public final int STOPPED = 0;
    public final int LEFT = 1;
    public final int RIGHT = 2;

    private int shipMove = STOPPED;

    private int screenX;

    public PlayerShip(Context context, int sizeX, int sizeY){

        shipHitbox = new RectF();

        screenX=sizeX;

        length=sizeX/10;
        height=sizeY/10;

        // Začátek lodě v centru obrazovky
        x=sizeX/2;
        y=20;

        ship = BitmapFactory.decodeResource(context.getResources(), R.drawable.shipskin);
        ship = Bitmap.createScaledBitmap(ship, (int)length, (int)height, false);

        shipSpeed = 5;
    }

    public RectF getHitbox(){
        return shipHitbox;
    }

    public Bitmap getShip(){
        return ship;
    }

    public float getX(){
        return x;
    }

    public float getLength(){
        return length;
    }

    public void setMovementState(int state){
        shipMove = state;
    }

    public void update(/*long fps*/){
        if(shipMove == LEFT){
            if(x-shipSpeed<=0){
                setMovementState(RIGHT);
            }
            x = x - shipSpeed;// / fps;
        }

        if(shipMove == RIGHT){
            if(x+length+shipSpeed>=screenX){
                setMovementState(LEFT);
            }
            x = x + shipSpeed; // fps;
        }
        shipHitbox.top =y;
        shipHitbox.left = x;
        shipHitbox.bottom = y + height;
        shipHitbox.right = x + length;

    }

}
