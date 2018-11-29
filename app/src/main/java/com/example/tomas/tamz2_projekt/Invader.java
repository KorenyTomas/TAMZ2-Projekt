package com.example.tomas.tamz2_projekt;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

import java.util.Random;

public class Invader {

    RectF hitbox;

    private Bitmap bitmap1;
    private Bitmap bitmap2;
    private Bitmap bitmap3;

    private float length;
    private float height;

    private float x;
    private float y;

    private float shipSpeed;

    public final int LEFT=1;
    public final int RIGHT = 2;

    private int shipMove = RIGHT;

    boolean isDead;

    Random random = new Random();

    public Invader(Context context, int row, int column, int sizeX, int sizeY){
        hitbox=new RectF();

        length=sizeX/20;
        height=sizeY/20;

        isDead=false;

        int padding = sizeX/20;

        this.x = column * (length+padding);
        this.y = row * (length + padding / 4);

        bitmap1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.alien1skin);
        bitmap2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.alien2skin);
        bitmap3 = BitmapFactory.decodeResource(context.getResources(), R.drawable.alien3skin);

        bitmap1 = Bitmap.createScaledBitmap(bitmap1, (int)length, (int)height, false);
        bitmap2 = Bitmap.createScaledBitmap(bitmap2, (int)length, (int)height, false);
        bitmap3 = Bitmap.createScaledBitmap(bitmap3, (int)length, (int)height, false);

        shipSpeed=2;

    }

    public void setDead(){
        isDead = true;
    }

    public boolean getDead(){
        return isDead;
    }

    public RectF getHitbox(){
        return hitbox;
    }

    public Bitmap getBitmap(int number){
        if (number==1){
            return bitmap1;
        }else if (number==2){
            return bitmap2;
        }else{
            return bitmap3;
        }

    }

    public float getX(){
        return x;
    }

    public float getY(){
        return y;
    }

    public float getLength(){
        return length;
    }

    public void update(){
        if(shipMove == LEFT){
            x -= shipSpeed;
        }else{
            x += shipSpeed;
        }

        hitbox.top=y;
        hitbox.bottom=y+height;
        hitbox.left=x;
        hitbox.right=x+length;
    }

    public void dropDownAndReverse(){
        if(shipMove == LEFT){
            shipMove = RIGHT;
        }else{
            shipMove = LEFT;
        }

        y += height;
    }


}
