package com.example.ricardomartins.lallaapp.MainScreens;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.ricardomartins.lallaapp.Database.DatabaseContract;
import com.example.ricardomartins.lallaapp.Database.DatabaseHelper;
import com.example.ricardomartins.lallaapp.R;

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
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        if(BChangeOptions) setResult(Activity.RESULT_OK, returnIntent);
        else setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }
}
