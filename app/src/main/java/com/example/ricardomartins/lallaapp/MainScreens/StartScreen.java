package com.example.ricardomartins.lallaapp.MainScreens;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.ricardomartins.lallaapp.Quizz.Results_Activity;
import com.example.ricardomartins.lallaapp.Statistics.Activity_Statistics;
import com.example.ricardomartins.lallaapp.Pager_Activities.Activity_HelpPager;
import com.example.ricardomartins.lallaapp.Week.Activity_Progress;
import com.example.ricardomartins.lallaapp.Database.DatabaseContract;
import com.example.ricardomartins.lallaapp.Database.DatabaseHelper;
import com.example.ricardomartins.lallaapp.DateCalculator.DateCalculator;
import com.example.ricardomartins.lallaapp.Exercise_Activity;
import com.example.ricardomartins.lallaapp.R;
import com.example.ricardomartins.lallaapp.Week.WeekStatus;

import java.util.ArrayList;
import java.util.List;

public class StartScreen extends AppCompatActivity {

    private static final String TAG = "StartScreen";

    private SharedPreferences sharedPref;

    private TextView WelcomeText;
    private Button week,add ,db, delete, progress, statistics, results;
    private ImageButton help;

    private  DatabaseHelper dbHelper;
    private SQLiteDatabase dbase;

    private DateCalculator dateCalculator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        sharedPref = this.getSharedPreferences(getString(R.string.Pref_FileName),Context.MODE_PRIVATE);

        String name = sharedPref.getString(getString(R.string.Pref_name), "error");
        WelcomeText = (TextView) findViewById(R.id.Start_WelcomeText);
        WelcomeText.setText( String.format(getResources().getString(R.string.Start_Welcome), name));

        dbHelper = new DatabaseHelper(this);
        dbase = dbHelper.getReadableDatabase();

        dateCalculator = new DateCalculator(
                sharedPref.getInt(getString(R.string.Pref_day), -1),
                sharedPref.getInt(getString(R.string.Pref_month), -1),
                sharedPref.getInt(getString(R.string.Pref_year), -1)
        );

        week = (Button) findViewById(R.id.Start_WeekStatus);
        week.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {

                DateCalculator.Week myweek = dateCalculator.getCurrentWeek();
                Log.i("CALCULATOR RESULT!", "Display Values: " + myweek.getDay() + " _ " + myweek.getMonth() + " _ " + myweek.getYear() + " _ " + myweek.getWeek_day() + " _ " + myweek.getElapsed_days() + " _ " + myweek.getWeek_number());

                Intent i = new Intent(getBaseContext(),WeekStatus.class);
                i.putExtra(getString(R.string.Intent_Day), myweek.getDay());
                i.putExtra(getString(R.string.Intent_Month), myweek.getMonth());
                i.putExtra(getString(R.string.Intent_Year), myweek.getYear());
                i.putExtra(getString(R.string.Intent_WeekDay), myweek.getWeek_day());
                i.putExtra(getString(R.string.Intent_ElapsedDays), myweek.getElapsed_days());
                i.putExtra(getString(R.string.Intent_WeekNumber), myweek.getWeek_number());
                startActivity(i);
            }
        });

        add = (Button) findViewById(R.id.addex);
        add.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(),Exercise_Activity.class);
                startActivity(i);
            }
        });

        db = (Button) findViewById(R.id.db);
        db.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                ReadDB();
            }
        });

        delete = (Button) findViewById(R.id.Bdelete);
        delete.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                dbase.delete(DatabaseContract.Exercises.TABLE_NAME, null, null);
            }
        });

        progress = (Button) findViewById(R.id.Start_Progress);
        progress.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(),Activity_Progress.class);
                startActivity(i);
            }
        });

        help = (ImageButton) findViewById(R.id.Start_Help);
        help.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(),Activity_HelpPager.class);
                startActivity(i);
            }
        });

        statistics = (Button) findViewById(R.id.Start_Statistics);
        statistics.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(),Activity_Statistics.class);
                startActivity(i);
            }
        });


        results = (Button) findViewById(R.id.Start_Final);
        results.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(),Results_Activity.class);
                startActivity(i);
            }
        });

        if( dateCalculator.getCurrentWeekIndex() >=7){
            results.setVisibility(View.VISIBLE);
        }
        else{
            results.setVisibility(View.VISIBLE);
            Log.i(TAG, "Results Shoudl be hidden!");
        }

    }

    private void ReadDB(){
        Log.i("DB", "reading db");

        Cursor  cursor = dbase.rawQuery("select * from " + DatabaseContract.Exercises.TABLE_NAME ,null);

        while(cursor.moveToNext()) {
            StringBuilder date = new StringBuilder(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Exercises.COLUMN_DATE)));
            int period = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.Exercises.COLUMN_PERIOD));
            StringBuilder phy = new StringBuilder(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Exercises.COLUMN_PHYSICAL)));
            int relax = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.Exercises.COLUMN_RELAX));
            StringBuilder remark = new StringBuilder(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Exercises.COLUMN_REMARK)));
            int dayweek = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.Exercises.COLUMN_DAYOFWEEK));
            int week = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.Exercises.COLUMN_WEEK));

            Log.i("DBReader", "Entry:" + date + " _ " + period+ " _ " +phy + " _ " +relax + " _ " + remark + "- day " +dayweek + " of " + week);
        }


        cursor.close();


    }


}
