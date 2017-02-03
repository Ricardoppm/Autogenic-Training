package com.example.ricardomartins.lallaapp.Week;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ricardomartins.lallaapp.Database.DatabaseContract;
import com.example.ricardomartins.lallaapp.Database.DatabaseHelper;
import com.example.ricardomartins.lallaapp.R;
import com.example.ricardomartins.lallaapp.Week.WeekContent.Day;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ExerciseFragment extends Fragment {

    private static final String TAG = "EX_FRAG";

    private static final String ARG_FIRST_DAY = "first-day";
    private static final String ARG_MONTH = "month";
    private static final String ARG_YEAR = "year";
    private static final String ARG_WEEKDAY = "weekday";
    private static final String ARG_ELAPSED= "elapsed";
    private static final String ARG_WEEKNB= "weeknb";



    private int day;
    private int month;
    private int year;
    private int weekday;
    private int elapsed_days;
    private int weeknb;

    private int month_days;

    private OnListFragmentInteractionListener mListener;

    private boolean BMorning, BAnoon, BNight;

    private int[] Month_Days = new int[]{31,28,31,30,31,30,31,31,30,31,30,31};
    private int February_bisext = 2016;
    private String[] weekdays;

    private WeekContent content = new WeekContent();
    private SQLiteDatabase dbase;
    private RecyclerView recycler;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ExerciseFragment() {
    }

    @SuppressWarnings("unused")
    public static ExerciseFragment newInstance(int day, int month, int year, int weekday, int elapsed_days, int weeknb) {

        Log.i(TAG,"Instantiate Fragment");
        ExerciseFragment fragment = new ExerciseFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_FIRST_DAY, day);
        args.putInt(ARG_MONTH, month);
        args.putInt(ARG_YEAR, year);
        args.putInt(ARG_WEEKDAY, weekday);
        args.putInt(ARG_ELAPSED, elapsed_days);
        args.putInt(ARG_WEEKNB, weeknb);


        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        content.Clear();

        if (getArguments() != null) {
            day = getArguments().getInt(ARG_FIRST_DAY);
            month = getArguments().getInt(ARG_MONTH);
            year = getArguments().getInt(ARG_YEAR);
            weekday = getArguments().getInt(ARG_WEEKDAY);
            elapsed_days = getArguments().getInt(ARG_ELAPSED);
            weeknb = getArguments().getInt(ARG_WEEKNB);

            Log.i(TAG, "Display Values: " + day + " _ " + month + " _ " + year + " _ " + weekday + elapsed_days);

            if(month == 2){
                if(year%February_bisext==0){
                    month_days=29;
                }else{
                    month_days=28;
                }
            }else{
                month_days = Month_Days[month-1];
            }

            DatabaseHelper dbHelper = new DatabaseHelper(getContext());
            dbase = dbHelper.getReadableDatabase();

            weekdays = getResources().getStringArray(R.array.Week_Weekdays);
            CreateItemList();
        }

    }

    private void ReadDBData(int day, int month, int year){


        Log.i(TAG, "reading db");

        // Define a projection that specifies which columns from the database
// you will actually use after this query.
        String[] projection = {
                DatabaseContract.Exercises._ID,
                DatabaseContract.Exercises.COLUMN_DATE,
                DatabaseContract.Exercises.COLUMN_PERIOD,
                DatabaseContract.Exercises.COLUMN_PHYSICAL,
                DatabaseContract.Exercises.COLUMN_RELAX,
                DatabaseContract.Exercises.COLUMN_REMARK
        };

        String query_param = Integer.toString(day) + "-" + Integer.toString(month) + "-" + Integer.toString(year);


        // Filter results WHERE "title" = 'My Title'
        String selection = DatabaseContract.Exercises.COLUMN_DATE + " = ?";
        String[] selectionArgs = { query_param };

        // How you want the results sorted in the resulting Cursor
        String sortOrder = DatabaseContract.Exercises.COLUMN_PERIOD + " DESC";

        Cursor cursor = dbase.query(
                DatabaseContract.Exercises.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        BMorning=false;
        BAnoon=false;
        BNight=false;

        while(cursor.moveToNext()) {

            StringBuilder date = new StringBuilder(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Exercises.COLUMN_DATE)));
            int period = Integer.decode(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Exercises.COLUMN_PERIOD)));
            StringBuilder phy = new StringBuilder(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Exercises.COLUMN_PHYSICAL)));
            long relax = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseContract.Exercises.COLUMN_RELAX));
            StringBuilder remark = new StringBuilder(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Exercises.COLUMN_REMARK)));

            switch(period){
                case 1:
                    BMorning=true;
                    break;
                case 2:
                    BAnoon = true;
                    break;
                case 3:
                    BNight = true;
                    break;
                default:
                    Log.e(TAG,"Period read fail");
                    break;

            }

            Log.i("DBReader", "Entry:" + date + " _ " + period+ " _ " +phy + " _ " +relax + " _ " + remark);
            Log.i("DBReader", "Morning - "+ BMorning + " / Afternoon - " + BAnoon + " / Night - " + BNight);
        }


        cursor.close();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercise_list, container, false);

        int[] week_colors = getResources().getIntArray(R.array.Week_Colors);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recycler = recyclerView;
            recyclerView.setBackgroundColor(week_colors[weeknb]);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new MyExerciseRecyclerViewAdapter(WeekContent.ITEMS, mListener));
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void UpdateData(Day item, int period){
        content.ChangeValue(item, period);
        recycler.getAdapter().notifyDataSetChanged();
    }

    private void CreateItemList(){
        Log.i(TAG," Creating List..." + day + " . " + month + " . " + year + " . " + weekdays[weekday-1]);
        for(int i = 0; i<7; i++){
            int Currentday = day+i;
            int day_month = month;
            int day_year = year;
            Log.i(TAG, "Adding "+ weekdays[((weekday+i)%7)]);
            if( ((day+i)% (month_days+1)) < day+i ){
                Currentday = ((day+i)% (month_days+1)) +1 ;
                day_month = ((month)%12)+1;
                if(day_month==1) day_year = year+1;
            }
            ReadDBData( Currentday, day_month, day_year);

            content.addItem(
                    new Day(Integer.toString(day),
                            i+1,
                            Currentday,
                            day_month,
                            day_year,
                            weekdays[((weekday+i)%7)],
                            BMorning,
                            BAnoon,
                            BNight,
                            i<=elapsed_days?true:false));
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Day item);

        void onExerciseInteraction(Day item, int period, int day, boolean delete_record);
    }


}
