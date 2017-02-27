package com.example.ricardomartins.lallaapp.Quizz;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.ricardomartins.lallaapp.Database.DatabaseContract;
import com.example.ricardomartins.lallaapp.Database.DatabaseHelper;
import com.example.ricardomartins.lallaapp.MainScreens.StartScreen;
import com.example.ricardomartins.lallaapp.R;

public class Results_Activity extends FragmentActivity implements  Fragment_Quiz.OnFragmentInteractionListener{

    private static final String TAG = "Results";

    private int[] answers;
    FragmentManager fm;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_simple_layout);

        answers = new int[ getResources().getStringArray(R.array.Quiz_Questions).length];

        fm = getSupportFragmentManager();

        fm.beginTransaction()
                .add(R.id.Simple_Frame_Layout, new Fragment_Quiz(), "week_frag")
                .commit();
        fm.executePendingTransactions();
    }


    public void onAnswerSelected(int id, int answer){
        if(id==-1){
            Log.i(TAG,"Test over, adding answers to db");
            AddToAnswersDB();
            UpdatePrefAndContinue();
        }else{
            answers[id-1] = answer;
        }
    }

    private void AddToAnswersDB(){
        Log.i(TAG, "Adding answers");
        for (int i=0 ; i < answers.length ;  i++){
        }

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        for (int i=0 ; i < answers.length ;  i++) {
            Log.i(TAG, "Answer " + (i + 1) + " = " + answers[i]);

            ContentValues values = new ContentValues();
            values.put(DatabaseContract.QuizAnswers.COLUMN_NB, i);
            values.put(DatabaseContract.QuizAnswers.COLUMN_ANSWER, answers[i]);
            values.put(DatabaseContract.QuizAnswers.COLUMN_QUIZ, 2);

            Log.i(TAG, "Answer "+ (i+1) + " = " + answers[i]);

            long newRowId;
            newRowId = db.insert(
                    DatabaseContract.QuizAnswers.TABLE_NAME,
                    null,
                    values);
        }
    }

    private void UpdatePrefAndContinue(){
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.Pref_FileName), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(getString(R.string.Pref_first_time_starting),false);
        editor.commit();

        setContentView(R.layout.activity_results);

        Cursor cursor = db.rawQuery("select * from " + DatabaseContract.QuizAnswers.TABLE_NAME ,null);

        while(cursor.moveToNext()) {
            int quiz = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.QuizAnswers.COLUMN_QUIZ));
            int answer = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.QuizAnswers.COLUMN_ANSWER));
            int number = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.QuizAnswers.COLUMN_NB));

            Log.i("DBReader", "Question " + number + " of quiz  " +  quiz + " --> " + answer);
        }

    }
}
