package com.example.ricardomartins.lallaapp.Statistics;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.ricardomartins.lallaapp.R;

import org.honorato.multistatetogglebutton.MultiStateToggleButton;
import org.honorato.multistatetogglebutton.ToggleButton;


public class Activity_Statistics extends AppCompatActivity implements Fragment_Period_PieChart.OnFragmentInteractionListener ,Fragment_Frequency_BarChart.OnFragmentInteractionListener, Fragment_Relax_BarChart.OnFragmentInteractionListener {

    private static final String TAG = "STATS";

    private MultiStateToggleButton StatToggle;

    private FragmentManager fm;
    private Fragment_Frequency_BarChart BarFrag;
    private Fragment_Relax_BarChart AllBarFrag;
    private Fragment_Period_PieChart PieFrag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        StatToggle = (MultiStateToggleButton) findViewById(R.id.Statistics_StatToggle);
        StatToggle.setElements(R.array.Statistics_StatToggle,0);
        StatToggle.setColorRes(R.color.Toggle_on, R.color.Toggle_off);
        StatToggle.setOnValueChangedListener(new ToggleButton.OnValueChangedListener() {
            @Override
            public void onValueChanged(int position) {
                Log.i(TAG, "Type Toogle change to "+ position);
                ChangeStatType(position);
            }
        });

        BarFrag = new Fragment_Frequency_BarChart();
        AllBarFrag = new Fragment_Relax_BarChart();
        PieFrag = new Fragment_Period_PieChart();

        fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.Statistics_ChartLayout, BarFrag)
                .commit();
        fm.executePendingTransactions();
    }

    private void ChangeStatType(int type){
        switch(type){
            case 0:
                fm.beginTransaction()
                        .replace(R.id.Statistics_ChartLayout, BarFrag)
                        .commit();
                fm.executePendingTransactions();
                break;
            case 1:
                fm.beginTransaction()
                        .replace(R.id.Statistics_ChartLayout, AllBarFrag)
                        .commit();
                fm.executePendingTransactions();
                break;
            case 2:
                fm.beginTransaction()
                        .replace(R.id.Statistics_ChartLayout, PieFrag)
                        .commit();
                fm.executePendingTransactions();
                break;
        }
    }

    public void onBarChartInteraction(){}

    public void onOverallInteraction(){}

    public void onPeriodPieInteraction(){}


}
