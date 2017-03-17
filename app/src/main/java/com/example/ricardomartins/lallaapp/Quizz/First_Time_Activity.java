package com.example.ricardomartins.lallaapp.Quizz;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ricardomartins.lallaapp.Database.DatabaseContract;
import com.example.ricardomartins.lallaapp.Database.DatabaseHelper;
import com.example.ricardomartins.lallaapp.DateCalculator.DateCalculator;
import com.example.ricardomartins.lallaapp.R;
import com.example.ricardomartins.lallaapp.MainScreens.StartScreen;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class First_Time_Activity extends FragmentActivity implements View.OnClickListener, Fragment_Quiz.OnFragmentInteractionListener{

    private static final String TAG = "FTAct";

    private EditText DateInput;
    private EditText NameInput;
    private Button SaveButton;

    private DatePickerDialog DatePickerDialog;
    private SimpleDateFormat DateFormatter;
    private Calendar newCalendar, MinDate;

    private Boolean HasDate=false;
    private Boolean HasName=false;
    private Boolean HasTouchedName=false;

    private int[] answers;

    FragmentManager fm;

    private long MilisecsInDay = 1000*60*60*24;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first__time_);

        DateInput = (EditText) findViewById(R.id.ND_dateinput);
        NameInput = (EditText) findViewById(R.id.ND_nameinput_);
        SaveButton = (Button)  findViewById(R.id.ND_button);

        DateFormatter = new SimpleDateFormat("dd-MM-yyyy");

        setDateTimeField();

        answers = new int[ getResources().getStringArray(R.array.Quiz_Questions).length];

        SaveButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                IsInputValid();
                if(HasDate && HasName){
                    //Toast.makeText(First_Time_Activity.this, "Proceed", Toast.LENGTH_LONG).show();
                    SaveValuesContinue();
                }else{
                    Log.i(TAG, "Can't Proceed - D= " +HasDate +" and N= "+HasName);
                }

            }
        });
    }

    private void IsInputValid(){
        if( NameInput.getText().length()!=0)HasName=true;
        else HasName=false;
    }

    private void setDateTimeField()  {
        DateInput.setOnClickListener(this);
        newCalendar = Calendar.getInstance();
        MinDate = Calendar.getInstance();


        DateInput.setText( DateFormatter.format(newCalendar.getTime()) );

        long min8week = newCalendar.getTimeInMillis()- (MilisecsInDay*7*8);

        MinDate.setTimeInMillis(min8week);
        Log.i(TAG, "Min 8week is =" + DateFormatter.format( MinDate.getTime()));

        DatePickerDialog = new DatePickerDialog(this, new android.app.DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                newCalendar = Calendar.getInstance();
                newCalendar.set(year, monthOfYear, dayOfMonth);
                if( newCalendar.getTimeInMillis() - MinDate.getTimeInMillis() < 0){
                    Log.i(TAG, "Too late, " + DateFormatter.format( newCalendar.getTime()) + " is before " + DateFormatter.format( MinDate.getTime()));
                    HasDate = false;
                }else{
                    DateInput.setText(DateFormatter.format( newCalendar.getTime()));
                    HasDate = true;
                    Log.i(TAG, "got date.");
                }
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onClick(View view) {
        DatePickerDialog.show();
    }

    private void SaveValuesContinue(){

        DateCalculator dateCalculator = new DateCalculator(
                newCalendar.get(Calendar.DAY_OF_MONTH),
                newCalendar.get(Calendar.MONTH),
                newCalendar.get(Calendar.YEAR)
        ); // get datecalculator to save which week we are on



        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.Pref_FileName),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.Pref_date), DateInput.getText().toString());
        editor.putString(getString(R.string.Pref_name), NameInput.getText().toString());
        editor.putInt(getString(R.string.Pref_day), newCalendar.get(Calendar.DAY_OF_MONTH));
        editor.putInt(getString(R.string.Pref_month), newCalendar.get(Calendar.MONTH));
        editor.putInt(getString(R.string.Pref_year), newCalendar.get(Calendar.YEAR));
        editor.putInt(getString(R.string.Pref_FirstTimeWeekDisplay), dateCalculator.getCurrentWeekIndex()-1);
        editor.commit();


        setContentView(R.layout.fragment_information);

        Button start = (Button) findViewById(R.id.Info_Go);
        start.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                setContentView(R.layout.fragment_simple_layout);


                fm = getSupportFragmentManager();

                fm.beginTransaction()
                        .add(R.id.Simple_Frame_Layout, new Fragment_Quiz(), "week_frag")
                        .commit();
                fm.executePendingTransactions();
            }
        });
        start.setText(getString(R.string.Quiz_Init_Bt));
        TextView text = (TextView) findViewById(R.id.Info_text);
        text.setText(String.format(getResources().getString(R.string.Quiz_Init_Text), NameInput.getText()));
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
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        for (int i=0 ; i < answers.length ;  i++) {
            Log.i(TAG, "Answer " + (i + 1) + " = " + answers[i]);

            ContentValues values = new ContentValues();
            values.put(DatabaseContract.QuizAnswers.COLUMN_NB, i);
            values.put(DatabaseContract.QuizAnswers.COLUMN_ANSWER, answers[i]);
            values.put(DatabaseContract.QuizAnswers.COLUMN_QUIZ, 1);

            Log.i(TAG, "Answer "+ (i+1) + " = " + answers[i]);

            long newRowId;
            newRowId = db.insert(
                    DatabaseContract.QuizAnswers.TABLE_NAME,
                    null,
                    values);
        }
    }

    private void UpdatePrefAndContinue(){
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.Pref_FileName),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(getString(R.string.Pref_first_time_starting),false);
        editor.commit();

        setContentView(R.layout.fragment_information);

        Button start = (Button) findViewById(R.id.Info_Go);
        start.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(),StartScreen.class);
                startActivity(i);
            }
        });
        start.setText(getString(R.string.Quiz_Final_Bt));
        TextView text = (TextView) findViewById(R.id.Info_text);
        text.setText(getString(R.string.Quiz_Final_Text));
    }
}
