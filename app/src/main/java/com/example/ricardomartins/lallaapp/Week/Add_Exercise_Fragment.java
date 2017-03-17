package com.example.ricardomartins.lallaapp.Week;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.ricardomartins.lallaapp.Database.DatabaseContract;
import com.example.ricardomartins.lallaapp.Database.DatabaseHelper;
import com.example.ricardomartins.lallaapp.R;


import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Add_Exercise_Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Add_Exercise_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Add_Exercise_Fragment extends Fragment implements DatePickerDialog.OnDateSetListener {
    private static final String TAG = "Add_exercise";


    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_DAY  = "day";
    private static final String ARG_MONTH = "month";
    private static final String ARG_YEAR = "year";
    private static final String ARG_PERIOD = "period";
    private static final String ARG_DAYNB = "daynb";
    private static final String ARG_WEEKNB = "weeknb";


    private String date_text;
    private String period_text;
    private int daynb;
    private int weeknb;

    private OnFragmentInteractionListener mListener;

    private TextView date;
    private SeekBar relax;
    private TextView physical;
    private TextView remark;
    private TextView period;


    public Add_Exercise_Fragment() {
        // Required empty public constructor
    }


    public static Add_Exercise_Fragment newInstance(int day, int month, int year, int period, int daynb, int weeknb) {
        Add_Exercise_Fragment fragment = new Add_Exercise_Fragment();
        Bundle args = new Bundle();
        args.putInt(ARG_DAY, day);
        args.putInt(ARG_MONTH, month);
        args.putInt(ARG_YEAR, year);
        args.putInt(ARG_PERIOD, period);
        args.putInt(ARG_DAYNB, daynb);
        args.putInt(ARG_WEEKNB, weeknb);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        daynb = getArguments().getInt(ARG_DAYNB);
        weeknb = getArguments().getInt(ARG_WEEKNB);

        if (getArguments() != null) {
            date_text = Integer.toString(getArguments().getInt(ARG_DAY)) + "-" + Integer.toString(getArguments().getInt(ARG_MONTH)) + "-" + Integer.toString(getArguments().getInt(ARG_YEAR));
            switch (getArguments().getInt(ARG_PERIOD)){
                case 1:
                    period_text = "Morning";
                    break;
                case 2:
                    period_text = "Afternoon";
                    break;
                case 3:
                    period_text = "Night";
                    break;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_add_exercise_, container, false);

        //int[] weekcolor = getResources().getIntArray(R.array.Week_Colors);

        LinearLayout layout = (LinearLayout) view.findViewById(R.id.Add_layout);
        layout.setBackgroundColor(getResources().getColor(R.color.Add_bg));

        Calendar c = Calendar.getInstance();

        date = (TextView) view.findViewById(R.id.date);
        if( !date_text.isEmpty()){
            date.setText(date_text);
        }else {
            date.setText("" + c.get(Calendar.DAY_OF_MONTH) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.YEAR));
        }
        Button save = (Button) view.findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                Log.i(TAG,"Save clicked");
                AddExercise2DB();
            }
        });

        period = (TextView) view.findViewById(R.id.period);
        if(!period_text.isEmpty()) period.setText(period_text);

        relax = (SeekBar) view.findViewById(R.id.relax);
        physical = (TextView) view.findViewById(R.id.physical);
        remark = (TextView) view.findViewById(R.id.remark);

        // Inflate the layout for this fragment
        return view;
    }

    public void AddExercise2DB() {

        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseContract.Exercises.COLUMN_DATE, date.getText().toString());
        values.put(DatabaseContract.Exercises.COLUMN_PERIOD, GetPeriodValue());
        values.put(DatabaseContract.Exercises.COLUMN_RELAX, relax.getProgress()  );
        values.put(DatabaseContract.Exercises.COLUMN_DAYOFWEEK, daynb  );
        values.put(DatabaseContract.Exercises.COLUMN_WEEK, weeknb  );


        if( physical.getText().toString().equals("") ){
            values.put(DatabaseContract.Exercises.COLUMN_PHYSICAL,"");
        }else{
            values.put(DatabaseContract.Exercises.COLUMN_PHYSICAL,physical.getText().toString());
        }

        if( remark.getText().toString().equals("") ){
            values.put(DatabaseContract.Exercises.COLUMN_REMARK,"");
        }else{
            values.put(DatabaseContract.Exercises.COLUMN_REMARK,remark.getText().toString());
        }

        Log.i("ADD EX", "Adding period ->" + period.getText());
        Log.i("ADD EX", "Adding remark ->" + values.getAsString(DatabaseContract.Exercises.COLUMN_REMARK));

        long newRowId;
        newRowId = db.insert(
                DatabaseContract.Exercises.TABLE_NAME,
                null,
                values);

        mListener.onExerciseAdded();
    }

    private int GetPeriodValue(){
        if(period.getText().equals("Morning"))return 1;
        if(period.getText().equals("Afternoon"))return 2;
        if(period.getText().equals("Night"))return 3;
        return 0;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        date.setText(monthOfYear + "/" + dayOfMonth + "/" + year);
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onExerciseAdded();
    }
}
