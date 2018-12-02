package com.example.tomas.tamz2_projekt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView lv = (ListView)findViewById(R.id.listView);

        List<MainPage> data = new ArrayList<MainPage>();

        data.add(new MainPage("Play Game", "Zapne hru", "play"));
        data.add(new MainPage("Highscore", "Nejlepší skóre", "highscore"));
        data.add(new MainPage("Settings", "Nastavení", "settings"));
        //Log.d("Result","length = " + result.size());

        MainPageAdapter adapter = new MainPageAdapter(this,
                R.layout.list_mainpage_layout, data);

        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {

                if (position==0){
                    // PlayGame
                    startActivity(new Intent(MainActivity.this, GameActivity.class));
                }else if(position==1){
                    // HighScore

                }else if(position==2){
                    // Nastavení
                    startActivity(new Intent(getBaseContext(), SettingsActivity.class));
                }


            }

        });



    }
}
