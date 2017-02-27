package com.example.ricardomartins.lallaapp.MainScreens;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.example.ricardomartins.lallaapp.Database.DatabaseContract;
import com.example.ricardomartins.lallaapp.Database.DatabaseHelper;
import com.example.ricardomartins.lallaapp.Notification.Service_Notification;
import com.example.ricardomartins.lallaapp.R;
import com.example.ricardomartins.lallaapp.Week.WeekStatus;

import java.util.Locale;

public class OptScreen extends AppCompatActivity {

    private Button buttonReset;
    private Boolean BChangeOptions=false;
    private SharedPreferences sharedPreferences;
    private Boolean changedNotifictions=false;
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opt_screen);

        sharedPreferences = getApplication().getSharedPreferences(getString(R.string.Pref_FileName), Context.MODE_PRIVATE);

        Log.i("Opt", "morning is" + sharedPreferences.getBoolean(getString(R.string.Pref_Notification_0), false));
        Log.i("Opt", "day is" + sharedPreferences.getBoolean(getString(R.string.Pref_Notification_1), false));
        Log.i("Opt", "night is" + sharedPreferences.getBoolean(getString(R.string.Pref_Notification_2), false));


        checkBox = (CheckBox) findViewById(R.id.Opt_CheckMorning);
        checkBox.setChecked( sharedPreferences.getBoolean(getString(R.string.Pref_Notification_0), false));

        checkBox = (CheckBox) findViewById(R.id.Opt_CheckAnoon);
        checkBox.setChecked( sharedPreferences.getBoolean(getString(R.string.Pref_Notification_1), false));

        checkBox = (CheckBox) findViewById(R.id.Opt_CheckNight);
        checkBox.setChecked(  sharedPreferences.getBoolean(getString(R.string.Pref_Notification_2), false));

        buttonReset = (Button) findViewById(R.id.Opt_Reset_Button);
        buttonReset.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
            new AlertDialog.Builder(OptScreen.this)
                    .setTitle( getString(R.string.Opt_Dialog_Title))
                    .setMessage( getString(R.string.Opt_Dialog_Text))
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.commit();


                        DatabaseHelper dbHelper = new DatabaseHelper(getApplication());
                        SQLiteDatabase dbase = dbHelper.getReadableDatabase();
                        dbase.execSQL(DatabaseContract.Exercises.SQL_DELETE_ENTRIES);
                        dbase.execSQL(DatabaseContract.QuizAnswers.SQL_DELETE_ENTRIES);
                        dbase.execSQL(DatabaseContract.Exercises.SQL_CREATE_ENTRIES);
                        dbase.execSQL(DatabaseContract.QuizAnswers.SQL_CREATE_ENTRIES);
                        BChangeOptions = true;
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            Log.i("opt", "Clearing");

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

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        changedNotifictions=true;
        boolean checked = ((CheckBox) view).isChecked();
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.Opt_CheckMorning:
                editor.putBoolean(getString(R.string.Pref_Notification_0), checked);
                break;

            case R.id.Opt_CheckAnoon:
                editor.putBoolean(getString(R.string.Pref_Notification_1), checked);
                break;

            case R.id.Opt_CheckNight:
                editor.putBoolean(getString(R.string.Pref_Notification_2), checked);
                break;
        }
        editor.commit();
        Log.i("Opt", "Commiting changes");
    }


    //Restarts the activity after changing the languages
    private void RestartActivity(){
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent serviceIntent = new Intent(this, Service_Notification.class);
        serviceIntent.putExtra(getString(R.string.Intent_Notification_0),sharedPreferences.getBoolean(getString(R.string.Pref_Notification_0), false));
        serviceIntent.putExtra(getString(R.string.Intent_Notification_1),sharedPreferences.getBoolean(getString(R.string.Pref_Notification_1), false));
        serviceIntent.putExtra(getString(R.string.Intent_Notification_2),sharedPreferences.getBoolean(getString(R.string.Pref_Notification_2), false));
        this.startService(serviceIntent);

        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}
