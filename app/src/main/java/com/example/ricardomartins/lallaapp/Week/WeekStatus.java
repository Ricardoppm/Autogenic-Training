package com.example.ricardomartins.lallaapp.Week;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.ricardomartins.lallaapp.Database.DatabaseContract;
import com.example.ricardomartins.lallaapp.Database.DatabaseHelper;
import com.example.ricardomartins.lallaapp.R;

public class WeekStatus extends AppCompatActivity implements ExerciseFragment.OnListFragmentInteractionListener, Week_Fragment.OnWeekFragmentListener, Add_Exercise_Fragment.OnFragmentInteractionListener {

    FragmentManager fm;
    Week_Fragment list;

    private WeekContent.Day Added_item;
    private int Added_period;
    private boolean Add_Fragment_On;

    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private int weeknumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_status);

        dbHelper = new DatabaseHelper(getApplicationContext());
        db = dbHelper.getWritableDatabase();

        fm = getSupportFragmentManager();

        Intent intent = getIntent();

        Log.i("Stuff", "Display Values: " + intent.getIntExtra(getString(R.string.Intent_Day), -1) + " _ " + intent.getIntExtra(getString(R.string.Intent_Month), -1) + " _ " + intent.getIntExtra(getString(R.string.Intent_Year), -1) + " _ " + intent.getIntExtra(getString(R.string.Intent_WeekDay),-1) + "_" + intent.getIntExtra(getString(R.string.Intent_ElapsedDays),-1) + " _ " + intent.getIntExtra(getString(R.string.Intent_WeekNumber),-1));

        weeknumber = intent.getIntExtra(getString(R.string.Intent_WeekNumber),0);

        list = Week_Fragment.newInstance(
                intent.getIntExtra(getString(R.string.Intent_Day), 1),
                intent.getIntExtra(getString(R.string.Intent_Month), 1),
                intent.getIntExtra(getString(R.string.Intent_Year), 2017),
                intent.getIntExtra(getString(R.string.Intent_WeekDay),1),
                intent.getIntExtra(getString(R.string.Intent_ElapsedDays),0),
                weeknumber  );

        fm.beginTransaction()
        .add(R.id.Week_Frame_layout, list, "week_frag")
        .commit();
        fm.executePendingTransactions();
    }

    public void onListFragmentInteraction(WeekContent.Day item){
        Log.i("WeekACT", "User clicked " + item.weekday);
    }


    public void onWeekInteraction(){
        Log.i("WeekACT", "ok..");
        Add_Fragment_On=false;
    }

    public void onExerciseInteraction(final WeekContent.Day item,final int period, final int day, boolean delete_record){
        if( delete_record){
            Log.i("WeekACT", "User want to DELETE " + period + " of " + item.weekday + "- day " + day);
            new AlertDialog.Builder(WeekStatus.this)
                    .setTitle("Delete entry")
                    .setMessage("Are you sure you want to delete the entry?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            DeleteDBEntry(item,period);
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }else {
            if (!Add_Fragment_On) {
                Add_Fragment_On = true;

                Added_item = item;
                Added_period = period;
                Log.i("WeekACT", "User want to add " + period + " of " + item.weekday + "- day " + day);
                fm.beginTransaction()
                        .add(R.id.Week_Frame_layout, Add_Exercise_Fragment.newInstance(item.day, item.month, item.year, period, day, weeknumber), "add")
                        .addToBackStack(null)
                        .commit();
                fm.executePendingTransactions();
            }
        }
    }

    private void DeleteDBEntry(WeekContent.Day item, int period){

        String date = Integer.toString(item.day) + "-" + Integer.toString(item.month) + "-" + Integer.toString(item.year);

        String selection = DatabaseContract.Exercises.COLUMN_DATE + " LIKE ? AND " + DatabaseContract.Exercises.COLUMN_PERIOD + " LIKE ?" ;


        String[] selectionArgs = { date, Integer.toString(period)};

        int RemovedRows =db.delete(DatabaseContract.Exercises.TABLE_NAME, selection,  selectionArgs );

        Log.i("WeekACT", "Removed " + RemovedRows + " rows from db");

        list.UpdateData(item, period);
    }

    @Override
    public void onBackPressed() {
        Log.i("WeekACT", "back pressed!");
        Add_Fragment_On=false;
        super.onBackPressed();
    }


    public void onExerciseAdded(){
        Log.i("Add_Frag", "Saved clicked!");
        list.UpdateData(Added_item, Added_period);
        Add_Fragment_On=false;

        fm.popBackStack();
    }
}
