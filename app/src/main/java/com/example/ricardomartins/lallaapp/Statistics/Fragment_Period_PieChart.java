package com.example.ricardomartins.lallaapp.Statistics;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ricardomartins.lallaapp.Database.DatabaseContract;
import com.example.ricardomartins.lallaapp.Database.DatabaseHelper;
import com.example.ricardomartins.lallaapp.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class Fragment_Period_PieChart extends Fragment {

    private static final String TAG = "STATS_PIE";

    private DatabaseHelper dbHelper;
    private SQLiteDatabase dbase;
    private PieChart mChart;

    private OnFragmentInteractionListener mListener;

    public Fragment_Period_PieChart() {
        // Required empty public constructor
    }


    public static Fragment_Period_PieChart newInstance() {
        Fragment_Period_PieChart fragment = new Fragment_Period_PieChart();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_period_pie_chart, container, false);

        dbHelper = new DatabaseHelper(getActivity());
        dbase = dbHelper.getReadableDatabase();

        mChart = (PieChart) view.findViewById(R.id.Statistics_Period);
        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);
        mChart.setExtraOffsets(5, 10, 5, 5);

        mChart.setDragDecelerationFrictionCoef(0.95f);


        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);

        mChart.setDrawCenterText(true);

        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);

        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener

        setData();

        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);


        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        mChart.setEntryLabelColor(Color.WHITE);
        mChart.setEntryLabelTextSize(12f);


        return view;
    }

    private void setData() {

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        Cursor dataset = dbase.rawQuery("select * from " + DatabaseContract.Exercises.TABLE_NAME ,null);

        int[] period_values = new int[]{0,0,0};
        int period;
        while(dataset.moveToNext()) {
            period = dataset.getInt(dataset.getColumnIndexOrThrow(DatabaseContract.Exercises.COLUMN_PERIOD));

            Log.i(TAG, "Adding exercise of period " + period);
            period_values[period-1]++;
        }

        entries.add(new PieEntry((float) (period_values[0]),"Morning"));
        entries.add(new PieEntry((float) (period_values[1]),"Afternoon"));
        entries.add(new PieEntry((float) (period_values[2]),"Night"));

        PieDataSet dataSet = new PieDataSet(entries, "Period Distribution");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        dataSet.setColors(new int[] { R.color.Red, R.color.Yellow, R.color.Blue, R.color.colorAccent }, getContext());
        
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
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


    public interface OnFragmentInteractionListener {
        void onPeriodPieInteraction();
    }
}
