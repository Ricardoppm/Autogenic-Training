package pt.psychapp.ricardomartins.lallaapp.Statistics;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ricardomartins.lallaapp.R;


import java.util.List;


public class MyNoteRecyclerViewAdapter extends RecyclerView.Adapter<MyNoteRecyclerViewAdapter.ViewHolder> {

    private final List<NoteContent.Note> mValues;

    public MyNoteRecyclerViewAdapter(List<NoteContent.Note> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_note, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        String date = holder.mItem.date;
        int index = date.indexOf('-');
        StringBuilder processedDate= new StringBuilder(date.substring(0, index));
        processedDate.append("-");
        processedDate.append( date.substring(index+1, date.indexOf('-', index+1)));

        holder.mDateView.setText( processedDate);
        holder.mContentView.setText( holder.mItem.content);

        if(position%2==0)
            holder.mLayout.setBackgroundResource(R.color.Note_filler);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final LinearLayout mLayout;
        public final TextView mDateView;
        public final TextView mContentView;
        public NoteContent.Note mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mLayout = (LinearLayout) view.findViewById(R.id.Note_Layout);
            mDateView = (TextView) view.findViewById(R.id.Note_date);
            mContentView = (TextView) view.findViewById(R.id.Note_Content);
        }
    }
}
