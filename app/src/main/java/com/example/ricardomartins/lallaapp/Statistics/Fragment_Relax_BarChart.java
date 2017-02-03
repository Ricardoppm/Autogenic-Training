package com.example.ricardomartins.lallaapp.Statistics;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ricardomartins.lallaapp.Database.DatabaseContract;
import com.example.ricardomartins.lallaapp.Database.DatabaseHelper;
import com.example.ricardomartins.lallaapp.DateCalculator.DateCalculator;
import com.example.ricardomartins.lallaapp.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.honorato.multistatetogglebutton.MultiStateToggleButton;
import org.honorato.multistatetogglebutton.ToggleButton;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_Relax_BarChart.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_Relax_BarChart#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Relax_BarChart extends Fragment {

    private static final String TAG = "STATS_BAR";


    private LineChart mChart;
    private DateCalculator dateCalculator;
    private MultiStateToggleButton Weektoggle, TypeToggle;

    private  DatabaseHelper dbHelper;
    private SQLiteDatabase dbase;

    private MyXAxisValueFormatter valueFormatter;

    private OnFragmentInteractionListener mListener;

    public Fragment_Relax_BarChart() {
        // Required empty public constructor
    }

    public static Fragment_Relax_BarChart newInstance(String param1, String param2) {
        Fragment_Relax_BarChart fragment = new Fragment_Relax_BarChart();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_relax_chart, container, false);

        mChart = (LineChart) view.findViewById(R.id.Statistics_Relax_Chart);
        mChart.clear();

        Weektoggle = (MultiStateToggleButton) view.findViewById(R.id.Statistics_WeekToggle_Relax);

        Weektoggle.setElements(R.array.Statistics_WeekToggle,0);
        Weektoggle.setColorRes(R.color.Toggle_on, R.color.Toggle_off);
        Weektoggle.setOnValueChangedListener(new ToggleButton.OnValueChangedListener() {
            @Override
            public void onValueChanged(int position) {
                Log.i(TAG, "Week Toogle change to "+ position);
                setWeekData(position);
            }
        });

        TypeToggle = (MultiStateToggleButton) view.findViewById(R.id.Statistics_Relax_Type);
        TypeToggle.setElements(R.array.Statistics_TimeValues,0);
        TypeToggle.setColorRes(R.color.Toggle_on, R.color.Toggle_off);
        TypeToggle.setOnValueChangedListener(new ToggleButton.OnValueChangedListener() {
            @Override
            public void onValueChanged(int position) {
                Log.i(TAG, "Type Toogle change to "+ position);
                if (position==0){
                    setWeekData(0);
                    Weektoggle.setValue(0);
                    Weektoggle.setVisibility( View.VISIBLE);

                    XAxis xAxis = mChart.getXAxis();
                    valueFormatter = new MyXAxisValueFormatter(getAxisValues());
                }else{
                    setAllData();
                    Weektoggle.setVisibility( View.INVISIBLE);

                    XAxis xAxis = mChart.getXAxis();
                    valueFormatter = new MyXAxisValueFormatter( getResources().getStringArray(R.array.Statistics_WeekToggle));
                }
            }
        });

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.Pref_FileName), Context.MODE_PRIVATE);;

        dbHelper = new DatabaseHelper(getActivity());
        dbase = dbHelper.getReadableDatabase();

        dateCalculator = new DateCalculator(
                sharedPreferences.getInt(getString(R.string.Pref_day), -1),
                sharedPreferences.getInt(getString(R.string.Pref_month), -1),
                sharedPreferences.getInt(getString(R.string.Pref_year), -1)
        );

        mChart.getDescription().setEnabled(false);


        mChart.setPinchZoom(false);
        mChart.setDrawGridBackground(false);
        mChart.setTouchEnabled(false);


        LimitLine ll1 = new LimitLine(10f, "Upper Limit");
        ll1.setLineWidth(4f);
        ll1.enableDashedLine(10f, 10f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll1.setTextSize(10f);

        LimitLine ll2 = new LimitLine(0f, "Lower Limit");
        ll2.setLineWidth(4f);
        ll2.enableDashedLine(10f, 10f, 0f);
        ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        ll2.setTextSize(10f);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.addLimitLine(ll1);
        leftAxis.addLimitLine(ll2);
        leftAxis.setAxisMaximum(11f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(true);

        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(true);


        mChart.animateX(2500);




        setWeekData(0);

        Legend l = mChart.getLegend();
        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);


        XAxis xAxis = mChart.getXAxis();
        valueFormatter = new MyXAxisValueFormatter(getAxisValues());
        xAxis.setValueFormatter( valueFormatter );
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(5f);



        mChart.getAxisRight().setEnabled(false);
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Fragment_Frequency_BarChart.OnFragmentInteractionListener) {
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


    private void setWeekData(int week){
        ArrayList<Entry> WeekValues = new ArrayList<Entry>();

        Cursor dataset = ReadDB(week);

        float[] Week_relax = new float[]{0,0,0,0,0,0,0};
        int[] frequency = new int[]{0,0,0,0,0,0,0};

        int relax, day;
        while(dataset.moveToNext()) {
            relax = dataset.getInt(dataset.getColumnIndexOrThrow(DatabaseContract.Exercises.COLUMN_RELAX));
            day = dataset.getInt(dataset.getColumnIndexOrThrow(DatabaseContract.Exercises.COLUMN_DAYOFWEEK));

            Log.i(TAG, "Adding day " + day + " for " + relax);
            Week_relax[day-1]= Week_relax[day-1] + relax;
            frequency[day-1]++;
        }
        for (int i=0; i<7 ; i++){
            if (frequency[i]!=0){
                Week_relax[i] = Week_relax[i]/frequency[i];
            }
            WeekValues.add(new BarEntry(i, Week_relax[i]));
        }

        dataset.close();

        LineDataSet set1;

        set1 = new LineDataSet(WeekValues, "Relax Average");


        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set1);

        LineData data = new LineData(dataSets);

        data.setValueTextColor(Color.BLACK);

        data.setValueFormatter( new MyValueFormatter());

        mChart.setData(data);

        mChart.invalidate();

    }

    private void setAllData(){

        ArrayList<Entry> WeekValues = new ArrayList<Entry>();

        Cursor dataset = ReadDB(-1);

        float[] Week_relax = new float[]{0,0,0,0,0,0,0};
        int[] frequency = new int[]{0,0,0,0,0,0,0};

        int relax, week;
        while(dataset.moveToNext()) {
            relax = dataset.getInt(dataset.getColumnIndexOrThrow(DatabaseContract.Exercises.COLUMN_RELAX));
            week = dataset.getInt(dataset.getColumnIndexOrThrow(DatabaseContract.Exercises.COLUMN_WEEK));

            Log.i(TAG, "Adding day " + week + " for " + relax);
            Week_relax[week]= Week_relax[week] + relax;
            frequency[week]++;
        }

        for (int i=0; i<7 ; i++){
            if (frequency[i]!=0){
                Week_relax[i] = Week_relax[i]/frequency[i];
            }
            WeekValues.add(new BarEntry(i, Week_relax[i]));
        }

        dataset.close();

        LineDataSet set1;

        set1 = new LineDataSet(WeekValues, "Relax Average");


        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set1);

        LineData data = new LineData(dataSets);

        data.setValueTextColor(Color.BLACK);

        data.setValueFormatter( new MyValueFormatter());

        mChart.setData(data);

        mChart.invalidate();
    }


    private Cursor ReadDB(int week){
        Log.i("DB", "reading db");

        if(week==-1){
            return dbase.rawQuery("select * from " + DatabaseContract.Exercises.TABLE_NAME ,null);
        }

        // Define a projection that specifies which columns from the database
// you will actually use after this query.
        String[] projection = {
                DatabaseContract.Exercises._ID,
                DatabaseContract.Exercises.COLUMN_DATE,
                DatabaseContract.Exercises.COLUMN_PERIOD,
                DatabaseContract.Exercises.COLUMN_PHYSICAL,
                DatabaseContract.Exercises.COLUMN_RELAX,
                DatabaseContract.Exercises.COLUMN_REMARK,
                DatabaseContract.Exercises.COLUMN_DAYOFWEEK,
                DatabaseContract.Exercises.COLUMN_WEEK
        };

        int query_param = week;

        // Filter results WHERE "title" = 'My Title'
        String selection = DatabaseContract.Exercises.COLUMN_WEEK + " = ?";
        String[] selectionArgs = { Integer.toString(week) };

        // How you want the results sorted in the resulting Cursor
        String sortOrder = DatabaseContract.Exercises.COLUMN_DAYOFWEEK + " DESC";

        Cursor cursor = dbase.query(
                DatabaseContract.Exercises.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );


        while(cursor.moveToNext()) {
            long itemId = cursor.getLong(
                    cursor.getColumnIndexOrThrow(DatabaseContract.Exercises._ID));
            StringBuilder date = new StringBuilder(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Exercises.COLUMN_DATE)));
            int period = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.Exercises.COLUMN_PERIOD));
            StringBuilder phy = new StringBuilder(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Exercises.COLUMN_PHYSICAL)));
            int relax = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.Exercises.COLUMN_RELAX));
            StringBuilder remark = new StringBuilder(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Exercises.COLUMN_REMARK)));
            int dayweek = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.Exercises.COLUMN_DAYOFWEEK));
            int weekoftraining = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.Exercises.COLUMN_WEEK));

            Log.i("DBReader", "Entry:" + date + " _ " + period+ " _ " +phy + " _ " +relax + " _ " + remark + "- day " +dayweek + " of " + weekoftraining);
        }

        cursor.moveToFirst();

        return cursor;
    }

    private String[] getAxisValues(){
        String[] weekdays = getResources().getStringArray(R.array.Week_Weekdays);

        String[] returnarray = new String[7];

        int initialday = dateCalculator.getInitialDayofWeek();

        for (int p=0; p < 7; p++){
            returnarray[p]= weekdays[(initialday+p)%7];
        }
        return  returnarray;
    }

    private class MyXAxisValueFormatter implements IAxisValueFormatter {

        private String[] mValues;

        private MyXAxisValueFormatter(String[] values) {
            this.mValues = values;
        }

        public void updateValues(String[] values ){ mValues = values; }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            // "value" represents the position of the label on the axis (x or y)
            return mValues[(int) value];
        }
    }

    private class MyValueFormatter implements IValueFormatter {

        public MyValueFormatter() {
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            // write your logic here
            return "" + value;
        }
    }

    public interface OnFragmentInteractionListener {
        void onOverallInteraction();
    }
}
