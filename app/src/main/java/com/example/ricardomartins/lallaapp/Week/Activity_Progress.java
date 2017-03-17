package com.example.ricardomartins.lallaapp.Week;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.ricardomartins.lallaapp.DateCalculator.DateCalculator;
import com.example.ricardomartins.lallaapp.R;
import com.example.ricardomartins.lallaapp.Week.WeekStatus;

public class Activity_Progress extends AppCompatActivity {

    private static final String TAG = "Progress_Act";

    private DateCalculator dateCalculator;
    private int CurrentWeek, ElapsedDays;
    private SharedPreferences sharedPref;
    private String[] weekText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);


        sharedPref = this.getSharedPreferences(getString(R.string.Pref_FileName), Context.MODE_PRIVATE);
        dateCalculator = new DateCalculator(
                sharedPref.getInt(getString(R.string.Pref_day), -1),
                sharedPref.getInt(getString(R.string.Pref_month), -1),
                sharedPref.getInt(getString(R.string.Pref_year), -1));

        weekText = getResources().getStringArray(R.array.Progress_WeekTheme);
        CurrentWeek = dateCalculator.getCurrentWeekIndex();
        setWeekVisibility();
    }

    private void setWeekVisibility(){

        int[] color = getResources().getIntArray(R.array.Week_Colors);
        Button Cur_Button;
        for (int i=0; i<8 ; i++){
            switch (i){
                case 0:
                    Cur_Button = (Button) findViewById(R.id.Progress_BtWeek1);
                    break;
                case 1:
                    Cur_Button = (Button) findViewById(R.id.Progress_BtWeek2);
                    break;
                case 2:
                    Cur_Button = (Button) findViewById(R.id.Progress_BtWeek3);
                    break;
                case 3:
                    Cur_Button = (Button) findViewById(R.id.Progress_BtWeek4);
                    break;
                case 4:
                    Cur_Button = (Button) findViewById(R.id.Progress_BtWeek5);
                    break;
                case 5:
                    Cur_Button = (Button) findViewById(R.id.Progress_BtWeek6);
                    break;
                case 6:
                    Cur_Button = (Button) findViewById(R.id.Progress_BtWeek7);
                    break;
                default:
                    Log.i(TAG, "Error setting visibility");
                    return;
            }
            Cur_Button.setText(String.format(getResources().getString(R.string.Progress_Week), i+1));
            if(i<=CurrentWeek) {
                Cur_Button.setBackgroundColor(color[i]);
            }else{
                Cur_Button.setEnabled(false);
            }
        }

    }

    public void ButtonHandling(View v) {
        DateCalculator.Week ChosenWeek=null;
        switch (v.getId()) {
            case R.id.Progress_BtWeek1:
                Log.i(TAG, "clicked week 1");
                ChosenWeek = dateCalculator.getWeek(0);
                Log.i("CALCULATOR RESULT!", "Display Values: " + ChosenWeek.getDay() + " _ " + ChosenWeek.getMonth() + " _ " + ChosenWeek.getYear() + " _ " + ChosenWeek.getWeek_day() + " _ " + ChosenWeek.getElapsed_days() + " _ " + ChosenWeek.getWeek_number());
                break;
            case R.id.Progress_BtWeek2:
                Log.i(TAG, "clicked week 2");
                ChosenWeek = dateCalculator.getWeek(1);
                Log.i("CALCULATOR RESULT!", "Display Values: " + ChosenWeek.getDay() + " _ " + ChosenWeek.getMonth() + " _ " + ChosenWeek.getYear() + " _ " + ChosenWeek.getWeek_day() + " _ " + ChosenWeek.getElapsed_days() + " _ " + ChosenWeek.getWeek_number());
                break;
            case R.id.Progress_BtWeek3:
                Log.i(TAG, "clicked week 1");
                ChosenWeek = dateCalculator.getWeek(2);
                Log.i("CALCULATOR RESULT!", "Display Values: " + ChosenWeek.getDay() + " _ " + ChosenWeek.getMonth() + " _ " + ChosenWeek.getYear() + " _ " + ChosenWeek.getWeek_day() + " _ " + ChosenWeek.getElapsed_days() + " _ " + ChosenWeek.getWeek_number());
                break;
            case R.id.Progress_BtWeek4:
                Log.i(TAG, "clicked week 1");
                ChosenWeek = dateCalculator.getWeek(3);
                Log.i("CALCULATOR RESULT!", "Display Values: " + ChosenWeek.getDay() + " _ " + ChosenWeek.getMonth() + " _ " + ChosenWeek.getYear() + " _ " + ChosenWeek.getWeek_day() + " _ " + ChosenWeek.getElapsed_days() + " _ " + ChosenWeek.getWeek_number());
                break;
            case R.id.Progress_BtWeek5:
                Log.i(TAG, "clicked week 1");
                ChosenWeek = dateCalculator.getWeek(4);
                Log.i("CALCULATOR RESULT!", "Display Values: " + ChosenWeek.getDay() + " _ " + ChosenWeek.getMonth() + " _ " + ChosenWeek.getYear() + " _ " + ChosenWeek.getWeek_day() + " _ " + ChosenWeek.getElapsed_days() + " _ " + ChosenWeek.getWeek_number());
                break;
            case R.id.Progress_BtWeek6:
                Log.i(TAG, "clicked week 1");
                ChosenWeek = dateCalculator.getWeek(5);
                Log.i("CALCULATOR RESULT!", "Display Values: " + ChosenWeek.getDay() + " _ " + ChosenWeek.getMonth() + " _ " + ChosenWeek.getYear() + " _ " + ChosenWeek.getWeek_day() + " _ " + ChosenWeek.getElapsed_days() + " _ " + ChosenWeek.getWeek_number());
                break;
            case R.id.Progress_BtWeek7:
                Log.i(TAG, "clicked week 1");
                ChosenWeek = dateCalculator.getWeek(6);
                Log.i("CALCULATOR RESULT!", "Display Values: " + ChosenWeek.getDay() + " _ " + ChosenWeek.getMonth() + " _ " + ChosenWeek.getYear() + " _ " + ChosenWeek.getWeek_day() + " _ " + ChosenWeek.getElapsed_days() + " _ " + ChosenWeek.getWeek_number());
                break;
        }
        if (ChosenWeek != null) {
            Intent i = new Intent(getBaseContext(), WeekStatus.class);
            i.putExtra(getString(R.string.Intent_Day), ChosenWeek.getDay());
            i.putExtra(getString(R.string.Intent_Month), ChosenWeek.getMonth());
            i.putExtra(getString(R.string.Intent_Year), ChosenWeek.getYear());
            i.putExtra(getString(R.string.Intent_WeekDay), ChosenWeek.getWeek_day());
            i.putExtra(getString(R.string.Intent_ElapsedDays), ChosenWeek.getElapsed_days());
            i.putExtra(getString(R.string.Intent_WeekNumber), ChosenWeek.getWeek_number());
            startActivity(i);
        }
    }

}
