package pt.psychapp.ricardomartins.lallaapp.Quizz;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pt.psychapp.ricardomartins.lallaapp.Database.DatabaseContract;
import pt.psychapp.ricardomartins.lallaapp.Database.DatabaseHelper;
import com.example.ricardomartins.lallaapp.R;


/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnQuestionFragmentInteractionListener}
 * interface.
 */
public class QuestionFragment extends Fragment {

    private static final String TAG = "Question_FRAG";

    private String[] questionText;

    private QuestionContent content = new QuestionContent();
    private SQLiteDatabase dbase;
    private RecyclerView recycler;


    private OnQuestionFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public QuestionFragment() {
    }

    @SuppressWarnings("unused")
    public static QuestionFragment newInstance() {
        QuestionFragment fragment = new QuestionFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        content.Clear();

        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        dbase = dbHelper.getReadableDatabase();

        questionText = getResources().getStringArray(R.array.Quiz_Questions);
        CreateItemList();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recycler = recyclerView;
            //recyclerView.setBackgroundColor(week_colors[weeknb]);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new MyQuestionRecyclerViewAdapter(QuestionContent.ITEMS,
                                                                        mListener,
                                                                        getResources().getStringArray(R.array.Results_Comment),
                                                                        getResources().getStringArray(R.array.Results_Progress)));
        }
        return view;
    }

    private void CreateItemList(){
        //Log.i(TAG," Creating List...");
        int answer1, number1;

        int[] answer = new int[questionText.length];

        Cursor cursor = dbase.rawQuery("select * from " + DatabaseContract.QuizAnswers.TABLE_NAME ,null);
        int index = 0;
        while(index < questionText.length ) {
            cursor.moveToNext();
            answer[index]= cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.QuizAnswers.COLUMN_ANSWER));
            index++;
        }


        for(int i = 0; i<questionText.length ; i++){
            Log.i(TAG, "Adding question "+ (i+1));

            cursor.moveToNext();
            answer1 = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.QuizAnswers.COLUMN_ANSWER));
            number1 = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.QuizAnswers.COLUMN_NB));

            content.addItem(
                    new QuestionContent.Question(number1, answer[number1], answer1));
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnQuestionFragmentInteractionListener) {
            mListener = (OnQuestionFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnQuestionFragmentInteractionListener {
        void onQuestionFragmentInteraction(QuestionContent.Question item);
    }
}
