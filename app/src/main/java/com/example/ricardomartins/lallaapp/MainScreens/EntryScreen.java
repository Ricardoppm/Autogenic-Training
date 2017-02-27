package com.example.ricardomartins.lallaapp.MainScreens;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.ricardomartins.lallaapp.Quizz.First_Time_Activity;
import com.example.ricardomartins.lallaapp.Notification.Service_Notification;
import com.example.ricardomartins.lallaapp.Pager_Activities.InfoScreen;
import com.example.ricardomartins.lallaapp.R;
import com.squareup.leakcanary.LeakCanary;

public class EntryScreen extends AppCompatActivity {

    private static final int OptionExitResult = 1;  // The request code
    private SharedPreferences sharedPref;

    Button start,info,option;
    private boolean first_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_screen);

        if (LeakCanary.isInAnalyzerProcess(getApplication())) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(getApplication());

        sharedPref = this.getSharedPreferences(getString(R.string.Pref_FileName),Context.MODE_PRIVATE);
        first_time = sharedPref.getBoolean(getString(R.string.Pref_first_time_starting), true);

        String name = sharedPref.getString(getString(R.string.Pref_name), "nope");
        String date = sharedPref.getString(getString(R.string.Pref_date), "nope");

        Log.i("INIT", "Name = " + name + " ---- Date = " + date + " --- Started = " + first_time);


        start = (Button) findViewById(R.id.Bstart);
        start.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                if(first_time){
                    Intent i = new Intent(getBaseContext(),First_Time_Activity.class);
                    startActivity(i);
                }else{
                    Intent i = new Intent(getBaseContext(),StartScreen.class);
                    startActivity(i);
                }
            }
        });

        info = (Button) findViewById(R.id.Binfo);
        info.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(),InfoScreen.class);
                startActivity(i);
            }
        });

        option = (Button) findViewById(R.id.Bopt);
        option.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(),OptScreen.class);
                startActivityForResult(i, OptionExitResult);
            }
        });



        if (!isMyServiceRunning()){
            Log.i("Entry", "Starting Service!");

           /* SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.Pref_FileName),Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(getString(R.string.Pref_Notification_0), true);
            editor.putBoolean(getString(R.string.Pref_Notification_1), true);
            editor.putBoolean(getString(R.string.Pref_Notification_2), true);

            editor.commit();*/

            Intent serviceIntent = new Intent(this, Service_Notification.class);
            serviceIntent.putExtra(getString(R.string.Intent_Notification_0),sharedPref.getBoolean(getString(R.string.Pref_Notification_0), false));
            serviceIntent.putExtra(getString(R.string.Intent_Notification_1),sharedPref.getBoolean(getString(R.string.Pref_Notification_1), false));
            serviceIntent.putExtra(getString(R.string.Intent_Notification_2),sharedPref.getBoolean(getString(R.string.Pref_Notification_2), false));
            this.startService(serviceIntent);
        }
    }

    private boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (Service_Notification.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == OptionExitResult) {
            Log.i("Entry", "Exited from options");

            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    }
}
