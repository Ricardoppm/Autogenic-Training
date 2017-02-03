package com.example.ricardomartins.lallaapp.MainScreens;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.ricardomartins.lallaapp.Database.DatabaseContract;
import com.example.ricardomartins.lallaapp.Database.DatabaseHelper;
import com.example.ricardomartins.lallaapp.R;

import java.util.Locale;

public class OptScreen extends AppCompatActivity {

    private Button buttonReset;
    private Boolean BChangeOptions=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opt_screen);

        buttonReset = (Button) findViewById(R.id.Opt_Reset_Button);
        buttonReset.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Log.i("opt", "Clearing");
                SharedPreferences SharedPref = getApplication().getSharedPreferences(getString(R.string.Pref_FileName), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = SharedPref.edit();
                editor.clear();
                editor.commit();


                DatabaseHelper dbHelper = new DatabaseHelper(getApplication());
                SQLiteDatabase dbase = dbHelper.getReadableDatabase();
                dbase.delete(DatabaseContract.Exercises.TABLE_NAME, null, null);
                dbase.delete(DatabaseContract.QuizAnswers.TABLE_NAME, null, null);

                BChangeOptions = true;
            }
        });

        Button Benglish = (Button) findViewById(R.id.Opt_English);
        Benglish.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Log.i("Opt", "Changing to english");

                Locale locale = new Locale("en");
                Locale.setDefault(locale);
                Configuration config = getBaseContext().getResources().getConfiguration();
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config,
                        getBaseContext().getResources().getDisplayMetrics());
                RestartActivity(); //Run the method as the last thing

            }
        });

        Button Bitalia = (Button) findViewById(R.id.Opt_Italian);
        Bitalia.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Log.i("Opt", "Changing to italian");

                Locale locale = new Locale("it");
                Locale.setDefault(locale);
                Configuration config = getBaseContext().getResources().getConfiguration();
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config,
                        getBaseContext().getResources().getDisplayMetrics());
                RestartActivity(); //Run the method as the last thing

            }
        });
    }

    //Restarts the activity after changing the languages
    private void RestartActivity(){
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}
