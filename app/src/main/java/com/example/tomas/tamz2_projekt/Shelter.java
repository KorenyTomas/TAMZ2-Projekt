package com.example.tomas.tamz2_projekt;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

public class Shelter {
    private RectF hitbox;

    private int damage;

    private Bitmap shieldSkin;
    private Bitmap shieldSkin1;
    private Bitmap shieldSkin2;
    private Bitmap shieldSkin3;
    private Bitmap shieldSkin4;
    private Bitmap shieldSkinEmpty;

    public Shelter(Context context, int column, int sizeX, int sizeY){
        int width=sizeX/5;
        int height=sizeY/12;

        this.damage=0;

        shieldSkin = BitmapFactory.decodeResource(context.getResources(), R.drawable.shield);
        shieldSkin = Bitmap.createScaledBitmap(shieldSkin, width, height, false);

        shieldSkin1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.shield1);
        shieldSkin1 = Bitmap.createScaledBitmap(shieldSkin1, width, height, false);

        shieldSkin2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.shield2);
        shieldSkin2 = Bitmap.createScaledBitmap(shieldSkin2, width, height, false);

        shieldSkin3 = BitmapFactory.decodeResource(context.getResources(), R.drawable.shield3);
        shieldSkin3 = Bitmap.createScaledBitmap(shieldSkin3, width, height, false);

        shieldSkin4 = BitmapFactory.decodeResource(context.getResources(), R.drawable.shield4);
        shieldSkin4 = Bitmap.createScaledBitmap(shieldSkin4, width, height, false);

        shieldSkinEmpty = BitmapFactory.decodeResource(context.getResources(), R.drawable.shieldempty);
        shieldSkinEmpty = Bitmap.createScaledBitmap(shieldSkinEmpty, width, height, false);


        this.hitbox = new RectF(sizeX/10 + (sizeX/3)*column, sizeY-100-height , (sizeX/10 + (sizeX/3)*column)+width, sizeY-100);

    }

    public RectF getHitbox(){
        return this.hitbox;
    }

    public void setDamage(int damage){
        this.damage+=damage;
    }

    public int getDamage(){
        return this.damage;
    }

    public Bitmap getShieldSkin(){
        if(this.damage < 20){
            return shieldSkin;
        }else if((this.damage >= 20) && (this.damage<40)){
            return shieldSkin1;
        }else if((this.damage >= 40) && (this.damage<60)){
            return shieldSkin2;
        }else if((this.damage >= 60) && (this.damage<80)){
            return shieldSkin3;
        }else if((this.damage >= 80) && (this.damage<100)){
            return shieldSkin4;
        }else {
            return shieldSkinEmpty;
        }
    }






}
