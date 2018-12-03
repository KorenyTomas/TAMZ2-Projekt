package com.example.tomas.tamz2_projekt;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;

public class GameActivity extends Activity {

    SpaceInvadersView spaceInvadersView;

    Context context;

    int height;
    int width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.context=this;

        // Načtení sharedPreferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean fullscreen = prefs.getBoolean("fullscreen", false);

        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        int statusBarHeight=0;
        if (fullscreen) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        else{
            // Velikost notifikační listy
            int resource = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resource > 0) {
                statusBarHeight = context.getResources().getDimensionPixelSize(resource);
            }
        }
        setContentView(R.layout.activity_game);

        // Get a Display object to access screen details
        Display display = getWindowManager().getDefaultDisplay();
        // Load the resolution into a Point object
        Point size = new Point();
        display.getSize(size);

        // Initialize gameView and set it as the view
        spaceInvadersView = new SpaceInvadersView(context, size.x, size.y - statusBarHeight);
        //spaceInvadersView = new SpaceInvadersView(this, width, height);

        setContentView(spaceInvadersView);

    }

    @Override
    protected void onPause() {

        super.onPause();
        spaceInvadersView.pause();
    }

    @Override
    protected void onResume() {

        super.onResume();
        spaceInvadersView.resume();
    }
}
