package pt.psychapp.ricardomartins.lallaapp.Quizz;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ricardomartins.lallaapp.R;

import java.util.List;


public class MyQuestionRecyclerViewAdapter extends RecyclerView.Adapter<MyQuestionRecyclerViewAdapter.ViewHolder> {

    private final List<QuestionContent.Question> mValues;
    private final QuestionFragment.OnQuestionFragmentInteractionListener mListener;
    private String[] comments;
    private String[] valuation;

    public MyQuestionRecyclerViewAdapter(List<QuestionContent.Question> items, QuestionFragment.OnQuestionFragmentInteractionListener listener, String[] comment, String[] val ) {
        mValues = items;
        mListener = listener;
        comments = comment;
        valuation = val;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_question, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        int index;
        //Log.i("R", " Question " + position + ": " + holder.mItem.answer1 + " -> " + holder.mItem.answer2);
        if( holder.mItem.answer2 - holder.mItem.answer1 < -2)
            index = -3;
        else if( holder.mItem.answer2 - holder.mItem.answer1 > 2)
            index = 3;
        else
            index = holder.mItem.answer2 - holder.mItem.answer1;
        index+=3;
        holder.mText.setText(String.format(comments[position], valuation[index]));
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mText;
        public QuestionContent.Question mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mText = (TextView) view.findViewById(R.id.Question_Text);
        }
    }
}
