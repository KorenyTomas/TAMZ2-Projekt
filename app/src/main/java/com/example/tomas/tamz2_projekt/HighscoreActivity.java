package com.example.tomas.tamz2_projekt;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class HighscoreActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);

        int paddingRight=2;
        int paddingLeft=10;
        int paddingTop=2;
        int paddingBottom=2;

        TableLayout tl=(TableLayout)findViewById(R.id.table);

        ScoreReaderDbHelper mDbHelper = new ScoreReaderDbHelper(this);

        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                ScoreEntry._ID,
                ScoreEntry.COLUMN_NAME_NAME,
                ScoreEntry.COLUMN_NAME_TIME,
                ScoreEntry.COLUMN_NAME_SCORE
        };



        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                ScoreEntry.COLUMN_NAME_SCORE + " DESC, " + ScoreEntry.COLUMN_NAME_TIME + " ASC";

        Cursor cursor = db.query(
                ScoreEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );

        List<ScoreRow> scoreRows = new ArrayList<ScoreRow>();
        while(cursor.moveToNext()) {
            long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(ScoreEntry._ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(ScoreEntry.COLUMN_NAME_NAME));
            long time = cursor.getLong(cursor.getColumnIndexOrThrow(ScoreEntry.COLUMN_NAME_TIME));
            int score = cursor.getInt(cursor.getColumnIndexOrThrow(ScoreEntry.COLUMN_NAME_SCORE));
            scoreRows.add(new ScoreRow(itemId, name, time, score));
        }
        cursor.close();


        for (int i=0; i<scoreRows.size(); i++) {

            ScoreRow row = scoreRows.get(i);

            TableRow tr1 = new TableRow(this);
            tr1.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
            tr1.setLayoutParams(new TableRow.LayoutParams( TableRow.LayoutParams.FILL_PARENT,TableRow.LayoutParams.WRAP_CONTENT));

            TextView poradi = new TextView(this);
            poradi.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
            poradi.setText((i+1) + ".");
            tr1.addView(poradi);

            TextView name = new TextView(this);
            name.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
            name.setText(row.name);
            tr1.addView(name);

            TextView score = new TextView(this);
            score.setText(row.score + "");
            score.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
            tr1.addView(score);

            TextView time = new TextView(this);
            time.setText("" + row.time);
            time.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
            tr1.addView(time);

            tl.addView(tr1, new TableLayout.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        }



    }
}
