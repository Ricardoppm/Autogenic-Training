package com.example.ricardomartins.lallaapp.Quizz;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;

/**
 * Created by ricardomartins on 29/12/2016.
 */

public class DateFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day_of_month = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), STYLE_NORMAL, (DatePickerDialog.OnDateSetListener) getActivity(), year, month, day_of_month);
        //dialog.getDatePicker().setTag(getTag());
        return dialog;
    }

}
