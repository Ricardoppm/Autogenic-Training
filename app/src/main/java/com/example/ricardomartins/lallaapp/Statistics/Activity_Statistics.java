package com.example.ricardomartins.lallaapp.Statistics;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.ricardomartins.lallaapp.R;


public class Activity_Statistics extends AppCompatActivity implements Fragment_Period_PieChart.OnFragmentInteractionListener ,Fragment_Frequency_BarChart.OnFragmentInteractionListener, Fragment_Relax_BarChart.OnFragmentInteractionListener, Fragment_Note_Physical.OnListFragmentInteractionListener , Fragment_Note_Remarks.OnListFragmentInteractionListener {

    private static final String TAG = "STATS";

    private FragmentManager fm;
    private Fragment_Frequency_BarChart BarFrag;
    private Fragment_Relax_BarChart AllBarFrag;
    private Fragment_Period_PieChart PieFrag;

    private LinearLayout selector;
    private FrameLayout frame;
    private boolean hasChosen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        selector = (LinearLayout) findViewById(R.id.Statistics_Selector);
        frame = (FrameLayout) findViewById(R.id.Statistics_ChartLayout);


        BarFrag = new Fragment_Frequency_BarChart();
        AllBarFrag = new Fragment_Relax_BarChart();
        PieFrag = new Fragment_Period_PieChart();

        fm = getSupportFragmentManager();

    }

    public void onPopUpClick(View view) {
        //Creating the instance of PopupMenu
        PopupMenu popup = new PopupMenu(Activity_Statistics.this, selector);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.statistics_menu, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                Log.i(TAG, "Clicked on " + item.getItemId() + " - " + R.id.Stats_freq );
                ChangeStatType(item.getItemId());
                return true;
            }
        });

        popup.show();
    }

    private void ChangeStatType(int id){
        if(!hasChosen){
            hasChosen=true;
            frame.setBackgroundResource(android.R.drawable.screen_background_light_transparent);
            frame.setScaleY(1.f);
        }
        switch(id){
            case R.id.Stats_freq:
                fm.beginTransaction()
                        .replace(R.id.Statistics_ChartLayout, BarFrag)
                        .commit();
                fm.executePendingTransactions();
                break;
            case R.id.Stats_relax:
                fm.beginTransaction()
                        .replace(R.id.Statistics_ChartLayout, AllBarFrag)
                        .commit();
                fm.executePendingTransactions();
                break;
            case R.id.Stats_period:
                fm.beginTransaction()
                        .replace(R.id.Statistics_ChartLayout, PieFrag)
                        .commit();
                fm.executePendingTransactions();
                break;

            case R.id.Stats_phy:
                fm.beginTransaction()
                        .replace(R.id.Statistics_ChartLayout, new Fragment_Note_Physical())
                        .commit();
                fm.executePendingTransactions();
                break;

            case R.id.Stats_remark:
                fm.beginTransaction()
                        .replace(R.id.Statistics_ChartLayout, new Fragment_Note_Remarks())
                        .commit();
                fm.executePendingTransactions();
                break;
        }
    }

    public void onBarChartInteraction(){}

    public void onOverallInteraction(){}

    public void onPeriodPieInteraction(){}

    public void onPhysicalNoteFragmentInteraction(){}

    public void onRemarksNoteFragmentInteraction(){}

}
