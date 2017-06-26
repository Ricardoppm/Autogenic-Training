package pt.psychapp.ricardomartins.lallaapp.Week;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import pt.psychapp.ricardomartins.lallaapp.Database.DatabaseContract;
import pt.psychapp.ricardomartins.lallaapp.Database.DatabaseHelper;
import com.example.ricardomartins.lallaapp.R;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class WeekStatus extends AppCompatActivity implements ExerciseFragment.OnListFragmentInteractionListener, Week_Fragment.OnWeekFragmentListener, Add_Exercise_Fragment.OnFragmentInteractionListener {

    FragmentManager fm;
    Week_Fragment list;

    private WeekContent.Day Added_item;
    private int Added_period;
    private boolean Add_Fragment_On;

    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private int weeknumber;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = this.getSharedPreferences(getString(R.string.Pref_FileName), Context.MODE_PRIVATE);

        intent = getIntent();
        weeknumber = intent.getIntExtra(getString(R.string.Intent_WeekNumber),0);

        dbHelper = new DatabaseHelper(getApplicationContext());
        db = dbHelper.getWritableDatabase();

        fm = getSupportFragmentManager();

        int lastShownWeek = sharedPreferences.getInt(getString(R.string.Pref_FirstTimeWeekDisplay),-1);
        //Log.i("WeekAct", "We are on "+weeknumber + " and the saved was " + lastShownWeek);

        if( lastShownWeek < weeknumber){
            setContentView(R.layout.fragment_week_messagedisplay);
            TextView phrase = (TextView) findViewById(R.id.Week_PhraseDisplay);

            String[] week_text = getResources().getStringArray(R.array.Week_Text);


            int[] colors = getResources().getIntArray(R.array.Week_Colors);
            LinearLayout layout = (LinearLayout) findViewById(R.id.Week_LAY);
            layout.setBackgroundColor(colors[weeknumber]);
            phrase.setText( week_text[weeknumber]);

            //Log.i("WeekAct", "Waiting 5!");

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(getString(R.string.Pref_FirstTimeWeekDisplay), weeknumber);
            editor.commit();

            final Handler mhandler = new Handler();
            new Thread( new Thread() {
                @Override
                public void run() {
                    // This is the delay
                    try{
                        this.sleep(1000*5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mhandler.post(new Runnable() {
                        @Override
                        public void run() {
                            startFragment();
                        }
                    });

                }
            }).start();
        }else{
            startFragment();
        }



    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void startFragment(){
        setContentView(R.layout.activity_week_status);

        //Log.i("Stuff", "Display Values: " + intent.getIntExtra(getString(R.string.Intent_Day), -1) + " _ " + intent.getIntExtra(getString(R.string.Intent_Month), -1) + " _ " + intent.getIntExtra(getString(R.string.Intent_Year), -1) + " _ " + intent.getIntExtra(getString(R.string.Intent_WeekDay),-1) + "_" + intent.getIntExtra(getString(R.string.Intent_ElapsedDays),-1) + " _ " + intent.getIntExtra(getString(R.string.Intent_WeekNumber),-1));

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
        //Log.i("WeekACT", "User clicked " + item.weekday);
    }


    public void onWeekInteraction(){
        //Log.i("WeekACT", "ok..");
        Add_Fragment_On=false;
    }

    public void onExerciseInteraction(final WeekContent.Day item,final int period, final int day, boolean delete_record){
        if( delete_record){
            //Log.i("WeekACT", "User want to DELETE " + period + " of " + item.weekday + "- day " + day);
            new AlertDialog.Builder(WeekStatus.this)
                    .setTitle( getString(R.string.Week_Dialog_Title))
                    .setMessage( getString(R.string.Week_Dialog_Text))
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
                //Log.i("WeekACT", "User want to add " + period + " of " + item.weekday + "- day " + day);
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

        //Log.i("WeekACT", "Removed " + RemovedRows + " rows from db");

        list.UpdateData(item, period);
    }

    @Override
    public void onBackPressed() {
        //Log.i("WeekACT", "back pressed!");
        Add_Fragment_On=false;
        super.onBackPressed();
    }


    public void onExerciseAdded(){
        //Log.i("Add_Frag", "Saved clicked!");
        list.UpdateData(Added_item, Added_period);
        Add_Fragment_On=false;

        fm.popBackStack();
    }
}
