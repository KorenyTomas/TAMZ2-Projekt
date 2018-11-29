package com.example.tomas.tamz2_projekt;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class SpaceInvadersView extends SurfaceView implements Runnable{

    Context context;

    private Thread threadGame = null;

    // Podrží surface dokud nevykreslime
    private SurfaceHolder surfaceHolder;

    private boolean playing;

    // Hra je na začátku pauznuta
    private boolean paused = true;

    private int score = 0;

    private int lives = 3;

    private Canvas canvas;
    private Paint paint;

    // Velikost obrazovky v pixelech
    private int sizeX;
    private int sizeY;

    // Lod hrace
    private PlayerShip playerShip;

    // Strela hrace, může byt jen jedna
    private Bullet playerBullet;

    // #TODO podpora podle nastavení
    Invader[] invaders = new Invader[30];
    int numInvaders = 0;

    // Strela vetrelcu (nepředpokladame vice než 100 najednou)
    private Bullet[] invadersBullets = new Bullet[100];

    public SpaceInvadersView(Context context, int x, int y) {
        super(context);

        this.context = context;

        surfaceHolder = getHolder();
        paint = new Paint();

        sizeX = x;
        sizeY = y;

        prepareLevel();

    }

    private void prepareLevel() {
        // #TODO
        playerShip = new PlayerShip(context, sizeX, sizeY);

        // Střela hráče
        playerBullet = new Bullet(sizeY);

        // Střela vetrelcu
        for(int i = 0; i < invadersBullets.length; i++) {
            invadersBullets[i] = new Bullet(sizeY);
        }

        // Vetrelci
        numInvaders = 0;
        for(int column = 0; column < 6; column ++ ){
            for(int row = 0; row < 5; row ++ ){
                invaders[numInvaders] = new Invader(context, row, column, sizeX, sizeY);
                numInvaders ++;
            }
        }
    }

    @Override
    public void run() {
        while (playing){

            // Aktualizuj pozice
            if(!paused){
                update();
            }

            // Nakresli
            draw();

        }
    }

    private void draw() {
        if(surfaceHolder.getSurface().isValid()){
            canvas = surfaceHolder.lockCanvas();

            // Vykresli pozadí
            canvas.drawBitmap(
                    BitmapFactory.decodeResource(getResources(), R.drawable.backgroundskin),
                    0,
                    0,
                    paint
                    );

            //paint.setColor(Color.argb(255,  255, 255, 255));

            paint.setColor(Color.WHITE);

            // #todo vykreslení pro všechny rozlišení
            //Log.d("posY", ""+sizeY);
            canvas.drawBitmap(playerShip.getShip(), playerShip.getX(), sizeY - 182, paint);

            // Vykreslení vetřelců
            for(int i = 0; i < numInvaders; i++){
                if(!invaders[i].getDead()) {
                    canvas.drawBitmap(invaders[i].getBitmap(i%3), invaders[i].getX(), invaders[i].getY(), paint);
                }
            }

            // Vykreslení hráčovi střely
            if(playerBullet.getStatus()){
                canvas.drawRect(playerBullet.getHitbox(), paint);
            }


            for(int i = 0; i < invadersBullets.length; i++){
                if(invadersBullets[i].getStatus()) {
                    canvas.drawRect(invadersBullets[i].getHitbox(), paint);
                }
            }

            // Změna barvy štětce pro vypsaní skore
            paint.setColor(Color.WHITE);
            paint.setTextSize(20);
            canvas.drawText("Score: " + score + " Lives: " + lives, 10, 50, paint);

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void update() {

        // Náraz vetřelců do kraje obrazovky
        boolean bumped=false;

        boolean loss = false;

        // Aktualizace hráče
        playerShip.update();

        // Aktualizace vetřelců
        for(int i = 0; i < numInvaders; i++){

            if(!invaders[i].getDead()) {
                // Aktualizace vetřelce
                invaders[i].update();

                // Střel
                // TODO podmínit nějakou logikou
                invadersBullets[i].shoot(invaders[i].getX() + invaders[i].getLength() / 2, invaders[i].getY(), playerBullet.DOWN);


                // Naraz do zdi?
                if (invaders[i].getX() > sizeX - invaders[i].getLength() || invaders[i].getX() < 0){
                    bumped = true;
                }
            }

        }

        // Pokud vetřelci narazili, posunu je dolu a změním směr
        if(bumped){

            for(int i = 0; i < numInvaders; i++){
                invaders[i].dropDownAndReverse();
                // Pokud jsou už vetřelci dole, končím
                if(invaders[i].getY() > sizeY - sizeY / 10){
                    loss = true;
                }
            }
        }

        if (loss){
            prepareLevel();
        }

        // Update the players bullet
        if(playerBullet.getStatus()){
            Log.d("ViewUpdate", "PlayerBulletActive");
            playerBullet.update();
        }

        for(int i = 0; i < invadersBullets.length; i++){
            if(invadersBullets[i].getStatus()) {
                invadersBullets[i].update();
            }
        }

    }

    public void pause(){
        playing = false;
        try{
            threadGame.join();
        }catch (InterruptedException e){
            Log.e("Error", "připojení vlakna");
        }
    }

    public void resume(){
        playing = true;
        threadGame = new Thread(this);
        threadGame.start();

    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent){
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_DOWN:
                Log.d("onTouchEv", "ActionDown");
                paused = false;

                if(motionEvent.getY() > sizeY - sizeY / 4){
                    //
                    Log.d("onTouchEv", "ActionDownFirst");
                    if(motionEvent.getX() > sizeX / 2){
                        playerShip.setMovementState(playerShip.RIGHT);
                    }else{
                        playerShip.setMovementState(playerShip.LEFT);
                    }
                }

                if(motionEvent.getY() < sizeY - sizeY / 4){
                    Log.d("onTouchEv", "ActionDownSecond");
                    // Střela
                    playerBullet.shoot(playerShip.getX()+playerShip.getLength()/2, sizeY, playerBullet.UP);
                }

                break;
            case MotionEvent.ACTION_UP:
                Log.d("onTouchEv", "ActionUp");
                if(motionEvent.getY() > sizeY - sizeY / 10) {
                    playerShip.setMovementState(playerShip.STOPPED);
                }
                break;
        }
        return true;
    }

}
