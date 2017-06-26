package pt.psychapp.ricardomartins.lallaapp.Week;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ricardomartins.lallaapp.R;
import pt.psychapp.ricardomartins.lallaapp.Week.ExerciseFragment.OnListFragmentInteractionListener;
import pt.psychapp.ricardomartins.lallaapp.Week.WeekContent.Day;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Day} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class MyExerciseRecyclerViewAdapter extends RecyclerView.Adapter<MyExerciseRecyclerViewAdapter.ViewHolder> {

    private final List<Day> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyExerciseRecyclerViewAdapter(List<Day> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_exercise, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        //Log.i("List", "Adding "+mValues.get(position).weekday);
        holder.mItem = mValues.get(position);
        holder.mDayView.setText( Integer.toString(mValues.get(position).day));
        holder.mWeekdayView.setText(mValues.get(position).weekday);
        if(mValues.get(position).Bmorning){
            //Log.i("List", "Change morning to green");
            holder.BtMorning.setImageResource( R.drawable.mattinoverde);
        }else{ holder.BtMorning.setImageResource( R.drawable.mattinorosso); }
        if(mValues.get(position).Bafternoon){
            //Log.i("List", "Change afternoon to green");
            holder.BtAnoon.setImageResource( R.drawable.giornoverde);
        }else{ holder.BtAnoon.setImageResource( R.drawable.giornorosso); }
        if(mValues.get(position).Bnight){
            //Log.i("List", "Change night to green");
            holder.BtNight.setImageResource( R.drawable.notteverde);
        }else{ holder.BtNight.setImageResource( R.drawable.notterosso); }

        holder.BtMorning.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                ButtonClicked(1, holder);
            }
        });
        holder.BtAnoon.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                ButtonClicked(2, holder);
            }
        });
        holder.BtNight.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                ButtonClicked(3, holder);
            }
        });




        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });

        if(holder.mItem.Active) {
            //Log.i("List", mValues.get(position).weekday+ " should be active!");
            holder.LLayout.setBackgroundResource( R.drawable.rectangle_inactive);
        }else{
            //Log.i("List", mValues.get(position).weekday+ " Doesn't EXIST! :D !");
            holder.LLayout.setBackgroundResource( R.drawable.rectangle_add);
        }
    }

    private void ButtonClicked(int period, ViewHolder holder){
        if (null != mListener) {
            switch (period){
                case 1:
                    if(holder.mItem.Bmorning){
                        //Log.i("ExerciseButton", "clicked on morning of " + holder.mItem.weekday);
                        mListener.onExerciseInteraction(holder.mItem,1, holder.mItem.TrainingDayNb, true );
                    }else{ mListener.onExerciseInteraction(holder.mItem,1,holder.mItem.TrainingDayNb, false); }
                    break;
                case 2:
                    if(holder.mItem.Bafternoon){
                        //Log.i("ExerciseButton", "clicked on Afternoon of " + holder.mItem.weekday);
                        mListener.onExerciseInteraction(holder.mItem,2,holder.mItem.TrainingDayNb, true);
                    }else{ mListener.onExerciseInteraction(holder.mItem,2,holder.mItem.TrainingDayNb, false); }
                    break;
                case 3:
                    if(holder.mItem.Bnight){
                        //Log.i("ExerciseButton", "clicked on Night of " + holder.mItem.weekday);
                        mListener.onExerciseInteraction(holder.mItem,3,holder.mItem.TrainingDayNb, true);
                    }else{ mListener.onExerciseInteraction(holder.mItem,3,holder.mItem.TrainingDayNb, false); }
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mDayView;
        public final TextView mWeekdayView;
        public final ImageButton BtMorning;
        public final ImageButton BtAnoon;
        public final ImageButton BtNight;
        public final LinearLayout LLayout;
        public Day mItem;

        public ViewHolder(View view) {
            super(view);
            mView       = view;
            mDayView    = (TextView) view.findViewById(R.id.DayNb);
            mWeekdayView= (TextView) view.findViewById(R.id.WeekDay);
            BtMorning   = (ImageButton) view.findViewById(R.id.Morning);
            BtAnoon     = (ImageButton) view.findViewById(R.id.Anoon);
            BtNight     = (ImageButton) view.findViewById(R.id.Night);
            LLayout     = (LinearLayout) view.findViewById(R.id.Exercise_layout);

        }

    }
}
