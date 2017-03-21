package com.example.ricardomartins.lallaapp.Statistics;

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

import com.example.ricardomartins.lallaapp.Database.DatabaseContract;
import com.example.ricardomartins.lallaapp.Database.DatabaseHelper;
import com.example.ricardomartins.lallaapp.R;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class Fragment_Note_Remarks extends Fragment {
    private static final String TAG = "Note_FRAG";

    private OnListFragmentInteractionListener mListener;

    private NoteContent content = new NoteContent();
    private SQLiteDatabase dbase;
    private RecyclerView recycler;

    public Fragment_Note_Remarks() {
    }

    public static Fragment_Note_Remarks newInstance(int columnCount) {
        Fragment_Note_Remarks fragment = new Fragment_Note_Remarks();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        content.Clear();

        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        dbase = dbHelper.getReadableDatabase();

        CreateItemList();
    }

    private void CreateItemList(){
        Log.i(TAG," Creating List...");
        int answer1, number1;

        Cursor cursor = dbase.rawQuery("select * from " + DatabaseContract.Exercises.TABLE_NAME ,null);

        String noteContent;
        while(cursor.moveToNext()){

            if( (noteContent = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Exercises.COLUMN_REMARK))).length()!=0){
                String date = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Exercises.COLUMN_DATE));
                Log.i(TAG, "Creating Note: " + noteContent + "  on " + date);
                content.addItem(
                        new NoteContent.Note(date,noteContent ));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recycler = recyclerView;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new MyNoteRecyclerViewAdapter(NoteContent.ITEMS));
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
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
    public interface OnListFragmentInteractionListener {
        void onRemarksNoteFragmentInteraction();
    }
}
