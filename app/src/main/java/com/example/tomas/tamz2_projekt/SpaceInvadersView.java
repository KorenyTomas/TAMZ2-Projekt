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

            // Draw the players bullet if active
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

        playerShip.update();

        prepareLevel();

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
