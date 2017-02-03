package com.example.ricardomartins.lallaapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.ricardomartins.lallaapp.Week.Add_Exercise_Fragment;

public class Exercise_Activity extends AppCompatActivity implements Add_Exercise_Fragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);
    }

    public void onExerciseAdded(){
        Log.i("Add_Frag", "Saved clicked!");
    }
}
