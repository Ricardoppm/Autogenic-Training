package pt.psychapp.ricardomartins.lallaapp.Statistics;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ricardomartins.lallaapp.R;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


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
                //Log.i(TAG, "Clicked on " + item.getItemId() + " - " + R.id.Stats_freq );
                ChangeStatType(item.getItemId());
                return true;
            }
        });

        popup.show();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    private void ChangeStatType(int id){
        if(!hasChosen){
            hasChosen=true;
            frame.setBackgroundResource(android.R.drawable.screen_background_light_transparent);
            frame.setScaleY(1.f);
        }
        switch(id){
            case R.id.Stats_freq:
                changeText(getResources().getString(R.string.Statistics_Frequency));
                fm.beginTransaction()
                        .replace(R.id.Statistics_ChartLayout, BarFrag)
                        .commit();
                fm.executePendingTransactions();
                break;
            case R.id.Stats_relax:
                changeText(getResources().getString(R.string.Statistics_Relax));
                fm.beginTransaction()
                        .replace(R.id.Statistics_ChartLayout, AllBarFrag)
                        .commit();
                fm.executePendingTransactions();
                break;
            case R.id.Stats_period:
                changeText(getResources().getString(R.string.Statistics_Period));
                fm.beginTransaction()
                        .replace(R.id.Statistics_ChartLayout, PieFrag)
                        .commit();
                fm.executePendingTransactions();
                break;

            case R.id.Stats_phy:
                changeText(getResources().getString(R.string.Statistics_Physical));
                fm.beginTransaction()
                        .replace(R.id.Statistics_ChartLayout, new Fragment_Note_Physical())
                        .commit();
                fm.executePendingTransactions();
                break;

            case R.id.Stats_remark:
                changeText(getResources().getString(R.string.Statistics_Remarks ));
                fm.beginTransaction()
                        .replace(R.id.Statistics_ChartLayout, new Fragment_Note_Remarks())
                        .commit();
                fm.executePendingTransactions();
                break;
        }
    }

    private void changeText(String text){
        TextView txt = (TextView)findViewById(R.id.Statistics_SelectorTxt);
        txt.setText(text);
    }

    public void onBarChartInteraction(){}

    public void onOverallInteraction(){}

    public void onPeriodPieInteraction(){}

    public void onPhysicalNoteFragmentInteraction(){}

    public void onRemarksNoteFragmentInteraction(){}

}
