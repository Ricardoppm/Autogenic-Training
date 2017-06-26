package pt.psychapp.ricardomartins.lallaapp.Statistics;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pt.psychapp.ricardomartins.lallaapp.Database.DatabaseContract;
import pt.psychapp.ricardomartins.lallaapp.Database.DatabaseHelper;
import pt.psychapp.ricardomartins.lallaapp.DateCalculator.DateCalculator;
import com.example.ricardomartins.lallaapp.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.honorato.multistatetogglebutton.MultiStateToggleButton;
import org.honorato.multistatetogglebutton.ToggleButton;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_Frequency_BarChart.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_Frequency_BarChart#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Frequency_BarChart extends Fragment {

    private static final String TAG = "STATS_BAR";

    private BarChart mChart;
    private ScatterChart sChart;
    private DateCalculator dateCalculator;
    private MultiStateToggleButton Weektoggle, TypeToggle;

    private DatabaseHelper dbHelper;
    private SQLiteDatabase dbase;

    private OnFragmentInteractionListener mListener;

    public Fragment_Frequency_BarChart() {
        // Required empty public constructor
    }


    public static Fragment_Frequency_BarChart newInstance(String param1, String param2) {
        Fragment_Frequency_BarChart fragment = new Fragment_Frequency_BarChart();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_frequency_chart, container, false);


        mChart = (BarChart) view.findViewById(R.id.Statistics_Bar_Week1);
        mChart.clear();

        sChart = (ScatterChart) view.findViewById(R.id.Statistics_Scatter);
        sChart.clear();

        Weektoggle = (MultiStateToggleButton) view.findViewById(R.id.Statistics_WeekToggle);

        Weektoggle.setElements(R.array.Statistics_WeekToggle,0);
        Weektoggle.setColorRes(R.color.Toggle_on, R.color.Toggle_off);
        Weektoggle.setOnValueChangedListener(new ToggleButton.OnValueChangedListener() {
            @Override
            public void onValueChanged(int position) {
                //Log.i(TAG, "Week Toogle change to "+ position);
                setWeekDataScatter(position);
            }
        });

        TypeToggle = (MultiStateToggleButton) view.findViewById(R.id.Statistics_Freq_Type);
        TypeToggle.setElements(R.array.Statistics_TimeValues,0);
        TypeToggle.setColorRes(R.color.Toggle_on, R.color.Toggle_off);
        TypeToggle.setOnValueChangedListener(new ToggleButton.OnValueChangedListener() {
            @Override
            public void onValueChanged(int position) {
                //Log.i(TAG, "Type Toogle change to "+ position);
                if (position==0){
                    setWeekDataScatter(0);
                    Weektoggle.setValue(1);
                    Weektoggle.setVisibility( View.VISIBLE);

                    sChart.setVisibility(View.VISIBLE);
                    mChart.setVisibility(View.INVISIBLE);

                    XAxis xAxis = sChart.getXAxis();
                    xAxis.setValueFormatter(new MyXAxisValueFormatter(getAxisValues()));


                }else{

                    setAllData();
                    Weektoggle.setVisibility( View.INVISIBLE);

                    sChart.setVisibility(View.INVISIBLE);
                    mChart.setVisibility(View.VISIBLE);

                    XAxis xAxis = mChart.getXAxis();
                    xAxis.setValueFormatter(new MyXAxisValueFormatter( getResources().getStringArray(R.array.Statistics_WeekToggle)));
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
        sChart.getDescription().setEnabled(false);


        sChart.setDrawGridBackground(false);
        sChart.setTouchEnabled(true);
        sChart.setMaxHighlightDistance(50f);

        // enable scaling and dragging
        sChart.setDragEnabled(false);
        sChart.setScaleEnabled(false);
        mChart.setDragEnabled(false);
        mChart.setScaleEnabled(false);


        Legend ls = sChart.getLegend();
        ls.setEnabled(false);

        YAxis yl = sChart.getAxisLeft();
        yl.setAxisMinimum(0.1f); // this replaces setStartAtZero(true)
        yl.setAxisMaximum(3.9f);
        yl.setGranularity(1.0f);
        yl.setTextSize(12f);
        yl.setValueFormatter( new ScatterYAxisValueFormatter());
        sChart.getAxisRight().setEnabled(false);


        XAxis xl = sChart.getXAxis();
        xl.setDrawGridLines(false);
        xl.setValueFormatter( new MyXAxisValueFormatter(getAxisValues()) );
        xl.setAxisMinimum(-0.5f);
        xl.setAxisMaximum(6.5f);
        xl.setGranularity(1f);
        xl.setTextSize(12f);
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);


        setWeekDataScatter(0);


        mChart.setPinchZoom(false);
        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(false);
        mChart.setHighlightFullBarEnabled(false);

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(10f);
        l.setFormToTextSpace(5f);
        l.setTextSize(15f);
        l.setXEntrySpace(6f);


        XAxis xAxis = mChart.getXAxis();
        xAxis.setValueFormatter( new MyXAxisValueFormatter(getAxisValues()) );
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);



        mChart.getAxisRight().setEnabled(false);
        // Inflate the layout for this fragment
        return view;
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

    private void setWeekDataScatter(int week){

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        ArrayList<Entry> yVals2 = new ArrayList<Entry>();
        ArrayList<Entry> yVals3 = new ArrayList<Entry>();

        Cursor dataset = ReadDB(week);

        int[][] frequency = new int[][]{    {0,0,0,0,0,0,0},
                                            {0,0,0,0,0,0,0},
                                            {0,0,0,0,0,0,0}};

        int period, day;
        while(dataset.moveToNext()) {
            period = dataset.getInt(dataset.getColumnIndexOrThrow(DatabaseContract.Exercises.COLUMN_PERIOD));
            day = dataset.getInt(dataset.getColumnIndexOrThrow(DatabaseContract.Exercises.COLUMN_DAYOFWEEK));

            //Log.i(TAG, "Adding day " + day + " on " + period);
            switch (period){
                case 1:
                    frequency[0][day-1]++;
                    break;
                case 2:
                    frequency[1][day-1]++;
                    break;
                case 3:
                    frequency[2][day-1]++;
                    break;
            }
        }
        for( int i =0; i < 7; i++){
            yVals1.add( new Entry(i, frequency[0][i]));
            yVals2.add( new Entry(i, frequency[1][i]*2));
            yVals3.add( new Entry(i, frequency[2][i]*3));
        }

        // create a dataset and give it a type
        ScatterDataSet set1 = new ScatterDataSet(yVals1, getString(R.string.Add_Morning));
        set1.setScatterShape(ScatterChart.ScatterShape.SQUARE);
        set1.setColor(Color.rgb(100, 240, 100));
        set1.setDrawValues(false);

        ScatterDataSet set2 = new ScatterDataSet(yVals2, getString(R.string.Add_Anoon));
        set2.setScatterShape(ScatterChart.ScatterShape.SQUARE);
        set2.setScatterShapeHoleColor(ColorTemplate.COLORFUL_COLORS[3]);
        set2.setDrawValues(false);
        set2.setColor(Color.rgb(240, 35, 35));

        ScatterDataSet set3 = new ScatterDataSet(yVals3, getString(R.string.Add_Night));
        set3.setScatterShape(ScatterChart.ScatterShape.SQUARE);
        set3.setColor(Color.rgb(20, 37, 240));
        set3.setDrawValues(false);

        set1.setScatterShapeSize(30f);
        set2.setScatterShapeSize(30f);
        set3.setScatterShapeSize(30f);

        ArrayList<IScatterDataSet> dataSets = new ArrayList<IScatterDataSet>();
        dataSets.add(set1); // add the datasets
        dataSets.add(set2);
        dataSets.add(set3);

        // create a data object with the datasets
        ScatterData datas = new ScatterData(dataSets);

        sChart.setData(datas);
        sChart.invalidate();
    }



    private void setAllData(){

        ArrayList<BarEntry> WeekValues = new ArrayList<BarEntry>();

        Cursor dataset = ReadDB(-1);

        int[] Week_frequency = new int[]{0,0,0,0,0,0,0};

        int week;
        while(dataset.moveToNext()) {
            week = dataset.getInt(dataset.getColumnIndexOrThrow(DatabaseContract.Exercises.COLUMN_WEEK));

            //Log.i(TAG, "Adding day of month " + week);
            Week_frequency[week]++;
        }
        for (int i=0; i< Week_frequency.length ; i++) WeekValues.add(new BarEntry(i, Week_frequency[i]));

        dataset.close();

        BarDataSet set1;

        set1 = new BarDataSet(WeekValues, getContext().getString(R.string.Statistics_Week_Freq));
        set1.setColor( Color.rgb(48, 47, 131));


        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(dataSets);
        data.setValueTextColor(Color.WHITE);

        data.setValueFormatter( new MyValueFormatter());

        mChart.setData(data);

        mChart.setFitBars(true);
        mChart.invalidate();


        // change the position of the y-labels
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        leftAxis.setAxisMaximum(22f);
        leftAxis.setGranularity(3f); // interval 1        mChart.getAxisRight().setEnabled(false);
        leftAxis.setDrawZeroLine(true);
        leftAxis.setDrawGridLines(false);
    }


    private Cursor ReadDB(int week){
        //Log.i("DB", "reading db");

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


        /*while(cursor.moveToNext()) {
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
        }*/

        //cursor.moveToFirst();

        return cursor;
    }

    private String[] getAxisValues(){
        String[] weekdays = getResources().getStringArray(R.array.Stat_Weekdays);

        String[] returnarray = new String[7];

        int initialday = dateCalculator.getInitialDayofWeek() -1;
        //Log.i(TAG, "Initial day is " + (initialday));

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


    private class ScatterYAxisValueFormatter implements IAxisValueFormatter {

        private String[] mValues = new String[3];

        private ScatterYAxisValueFormatter() {
            this.mValues[0] = getResources().getString(R.string.Add_Morning);
            this.mValues[1] = getResources().getString(R.string.Add_Anoon);
            this.mValues[2] = getResources().getString(R.string.Add_Night);
        }

        public void updateValues(String[] values ){ mValues = values; }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            switch( (int)value){
                case 1:
                    return mValues[0];
                case 2:
                    return mValues[1];
                case 3:
                    return mValues[2];
            }
            return "";
        }
    }

    private class MyValueFormatter implements IValueFormatter {

        public MyValueFormatter() {
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            // write your logic here
            int intvalue = (int)value;

            return Integer.toString(intvalue);
        }
    }

    public interface OnFragmentInteractionListener {
        void onBarChartInteraction();
    }
}
