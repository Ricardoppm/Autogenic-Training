package pt.psychapp.ricardomartins.lallaapp.Week;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ricardomartins.lallaapp.R;


public class Week_Fragment extends Fragment {

    private static final String TAG = "Week_Frag";

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_DAY  = "day";
    private static final String ARG_MONTH = "month";
    private static final String ARG_YEAR = "year";
    private static final String ARG_WEEKDAY= "weekday";
    private static final String ARG_ELAPSED= "elapsed";
    private static final String ARG_WEEKNB= "weeknumber";


    private OnWeekFragmentListener mListener;

    private ExerciseFragment frag;
    private TextView WeekText;

    private static int day,month,year,weekday, elapsed_days, weeknb;

    public Week_Fragment() {
    }

    public static Week_Fragment newInstance(int day, int month, int year, int weekday, int elapsed_days, int week_number) {
        Week_Fragment fragment = new Week_Fragment();
        Bundle args = new Bundle();
        args.putInt(ARG_DAY, day);
        args.putInt(ARG_MONTH, month);
        args.putInt(ARG_YEAR, year);
        args.putInt(ARG_WEEKDAY, weekday);
        args.putInt(ARG_ELAPSED, elapsed_days);
        args.putInt(ARG_WEEKNB, week_number);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        day = getArguments().getInt(ARG_DAY);
        month = getArguments().getInt(ARG_MONTH);
        year = getArguments().getInt(ARG_YEAR);
        weekday = getArguments().getInt(ARG_WEEKDAY);
        elapsed_days = getArguments().getInt(ARG_ELAPSED);
        weeknb = getArguments().getInt(ARG_WEEKNB);

        //Log.i(TAG, "On create, instance ex_frag");
        frag = ExerciseFragment.newInstance(day,month,year,weekday, elapsed_days, weeknb);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_week_, container, false);

        int[] week_colors = getResources().getIntArray(R.array.Week_Colors);

        String[] texts = getResources().getStringArray(R.array.Week_Text);
        WeekText = (TextView) view.findViewById(R.id.Week_phrase);
        WeekText.setText(texts[weeknb]);
        WeekText.setBackgroundColor(week_colors[weeknb]);

        mListener.onWeekInteraction();
        FragmentManager fm = getActivity().getSupportFragmentManager();

        fm.beginTransaction()
                .add(R.id.week_list, frag , "list")
                .commit();

        return view;

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnWeekFragmentListener) {
            mListener = (OnWeekFragmentListener) context;
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



    public interface OnWeekFragmentListener {
        void onWeekInteraction();
    }

    public void UpdateData(WeekContent.Day item, int period){
        frag.UpdateData(item, period);
    }
}
