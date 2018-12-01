package com.example.tomas.tamz2_projekt;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
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

    // Strela vetrelcu
    private Bullet[] invadersBullets = new Bullet[30];

    private Shelter[] shelters = new Shelter[3];

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

        lives=3;
        score=0;

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

        for (int i = 0; i < shelters.length; i++){
            shelters[i] = new Shelter(context, i, sizeX, sizeY);
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

            // #todo vykreslení pro všechny rozlišení
            //Log.d("posY", ""+sizeY);
            canvas.drawBitmap(playerShip.getShip(), playerShip.getX(), sizeY - 182, paint);

            // Vykreslení vetřelců
            for(int i = 0; i < numInvaders; i++){
                if(!invaders[i].isDead()) {
                    canvas.drawBitmap(invaders[i].getBitmap(i%3), invaders[i].getX(), invaders[i].getY(), paint);
                }
            }

            // Vykreslení štítu
            for (int i = 0; i < shelters.length; i++){
                canvas.drawBitmap(shelters[i].getShieldSkin(), sizeX/10 + (sizeX/3)*i, sizeY-180-(sizeY/12), paint);
            }

            paint.setColor(Color.WHITE);

            // Vykreslení hráčovi střely
            if(playerBullet.getStatus()){
                Log.d("PlayerBulletDraw", "OK");
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

            if(!invaders[i].isDead()) {
                // Aktualizace vetřelce
                invaders[i].update();

                // Střel
                if(invaders[i].takeAim(playerShip.getX(), playerShip.getLength())) {
                    invadersBullets[i].shoot(invaders[i].getX() + invaders[i].getLength() / 2, invaders[i].getY(), playerBullet.DOWN);
                }


                // Naraz do zdi?
                if (invaders[i].getX() > sizeX - invaders[i].getLength() || invaders[i].getX() < 0){
                    bumped = true;
                }
            }

        }

        // Aktualizace střel vetřelců
        for(int i = 0; i < invadersBullets.length; i++){
            if(invadersBullets[i].getStatus()) {
                invadersBullets[i].update();
            }
        }

        // Pokud vetřelci narazili, posunu je dolu a změním směr
        if(bumped){

            for(int i = 0; i < numInvaders; i++){
                invaders[i].dropDownAndReverse();

                if(!invaders[i].isDead() && (RectF.intersects(shelters[0].getHitbox(), invaders[i].getHitbox()) || RectF.intersects(shelters[2].getHitbox(), invaders[i].getHitbox()))){
                    for(int j=0; j<shelters.length; j++){
                        shelters[j].setDamage(100);
                    }
                }

                // Pokud jsou už živí vetřelci dole, končím
                if(!invaders[i].isDead() &&invaders[i].getY() > sizeY - sizeY / 8){
                    loss = true;
                }
            }
        }


        if (loss){
            paused=true;
            prepareLevel();
        }

        if(playerBullet.getStatus()){
            playerBullet.update();
        }

        // Hračova střela za obrazovkou
        if(playerBullet.getImpactPointY() < 0){
            playerBullet.setStatus(false);
        }

        // Střela vetřelců úplně dole
        for(int i = 0; i < invadersBullets.length; i++){
            if(invadersBullets[i].getImpactPointY() > sizeY){
                invadersBullets[i].setStatus(false);
            }
        }

        // Hráč zasáhl vetřelce
        if(playerBullet.getStatus()) {
            for (int i = 0; i < numInvaders; i++) {
                if (!invaders[i].isDead()) {
                    if (RectF.intersects(playerBullet.getHitbox(), invaders[i].getHitbox())) {
                        invaders[i].kill();
                        playerBullet.setStatus(false);
                        score = score + 10;

                        // Hráč vyhrál
                        if(score == numInvaders * 10){
                            paused = true;
                            prepareLevel();
                        }
                    }
                }
            }
        }

        // Vetřelci zasáhli štít
        for(int i = 0; i < invadersBullets.length; i++) {
            if (invadersBullets[i].getStatus()) {
                for (int j = 0; j < shelters.length; j++) {
                    if (shelters[j].getDamage() < 100) {
                        if (RectF.intersects(invadersBullets[i].getHitbox(), shelters[j].getHitbox())) {
                            Log.d("InvaderHitShelter", "" + shelters[j].getHitbox() + " ; " + invadersBullets[i].getHitbox());
                            invadersBullets[i].setStatus(false);
                            shelters[j].setDamage(10);
                        }
                    }
                }
            }
        }

        // Hráč zasáhl štít
        if(playerBullet.getStatus()){
            for(int i = 0; i < shelters.length; i++){
                if(shelters[i].getDamage()<100){
                    if(RectF.intersects(playerBullet.getHitbox(), shelters[i].getHitbox())){
                        Log.d("PlayerHitShelter", "" + shelters[i].getHitbox() + " ; " + playerBullet.getHitbox());
                        playerBullet.setStatus(false);
                        // TODO síla střel dle nastavení
                        shelters[i].setDamage(10);
                    }
                }
            }
        }

        // Vetřelci zasáhli hráče
        for(int i = 0; i < invadersBullets.length; i++){
            if(invadersBullets[i].getStatus()){
                if(RectF.intersects(playerShip.getHitbox(), invadersBullets[i].getHitbox())){
                    invadersBullets[i].setStatus(false);
                    lives --;

                    // Is it game over?
                    if(lives == 0){
                        paused = true;
                        prepareLevel();
                    }
                }
            }
        }

        // Vypis hitboxu všech objektů - debug
        Log.d("PlayerHitbox", "" + playerShip.getHitbox());
        Log.d("PlayerBulletHitbox", "" + playerBullet.getHitbox());
        for(int i = 0; i < shelters.length; i++) {
            Log.d("ShelterHitbox", i+ ". " + shelters[i].getHitbox());
        }
        for(int i = 0; i < invaders.length; i++) {
            Log.d("InvadersHitbox", i+ ". " + invaders[i].getHitbox());
        }
        for(int i = 0; i < invadersBullets.length; i++) {
            if(invadersBullets[i].getStatus()) {
                Log.d("InvadersBulletHitbox", i + ". " + invadersBullets[i].getHitbox());
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
                    playerBullet.shoot(playerShip.getX()+playerShip.getLength()/2, sizeY-180, playerBullet.UP);
                }

                break;
            case MotionEvent.ACTION_UP:
                Log.d("onTouchEv", "ActionUp");
                if(motionEvent.getY() > sizeY - sizeY / 4) {
                    playerShip.setMovementState(playerShip.STOPPED);
                }
                break;
        }
        return true;
    }

}
