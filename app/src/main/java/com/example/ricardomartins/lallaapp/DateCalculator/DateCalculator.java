package com.example.ricardomartins.lallaapp.DateCalculator;

import android.util.Log;

import com.example.ricardomartins.lallaapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by ricardomartins on 19/01/2017.
 */

public class DateCalculator {

    private static final String TAG = "DateCalculator";
    private static final long DayInMili = 1000*60*60*24;
    private SimpleDateFormat DateFormatter;

    private Week first_week;
    private int Current_week_index;

    private Calendar c_today;

    public DateCalculator(int day, int month, int year){

        c_today = Calendar.getInstance();
        c_today.set(year,month,day,0,0,0);
        Log.i(TAG, "Created on " + c_today.getTime() + "...." + c_today.get(Calendar.DAY_OF_WEEK));

        first_week = new Week(day, month, year,0,0,  c_today.get(Calendar.DAY_OF_WEEK));
        DateFormatter = new SimpleDateFormat("dd-MM-yyyy");

        Current_week_index = getweekindex();
    }

    private int getweekindex(){
        c_today= Calendar.getInstance();
        long Today = c_today.getTimeInMillis();

        Calendar c_first = Calendar.getInstance();
        c_first.set(first_week.getYear(),first_week.getMonth(),first_week.getDay(),0,0,0);
        long First = c_first.getTimeInMillis();

        long Cur_Week = (Today - First)/(DayInMili*7);
        Log.i(TAG, "This week is " + (Cur_Week+1));
        return (int) (Cur_Week);
    }

    public Week getCurrentWeek(){

        c_today= Calendar.getInstance();
        long Today = c_today.getTimeInMillis();

        Calendar c_first = Calendar.getInstance();
        c_first.set(first_week.getYear(),first_week.getMonth(), first_week.getDay(),0,0,0);
        long First = c_first.getTimeInMillis();


        Log.i(TAG, "This week is " + (Current_week_index+1));

        First = (First+ (Current_week_index*DayInMili*7));
        c_first.setTimeInMillis(First);
        Log.i(TAG, "We are at day nb " + (((Today - First)/DayInMili)) + " ?  = " +  (float)(((Today - First)/(DayInMili*1.0))  ) );

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(Today);
        Log.i(TAG, "we are at " + DateFormatter.format(c.getTime()));
        c.setTimeInMillis(First);
        Log.i(TAG, "we are at " + DateFormatter.format(c.getTime()));

        return new Week(
                c_first.get(Calendar.DAY_OF_MONTH),
                c_first.get(Calendar.MONTH)+1,
                c_first.get(Calendar.YEAR),
                (int) ((Today - First)/DayInMili),
                Current_week_index,
                c_first.get(Calendar.DAY_OF_WEEK)-1
                );
    }

    public Week getWeek(int index){

        int elapsed_days;
        if( index < Current_week_index) elapsed_days = 8;
        else if( index == Current_week_index) return getCurrentWeek();
        else elapsed_days = -1;

        Calendar c_first = Calendar.getInstance();
        c_first.set(first_week.getYear(),first_week.getMonth(), first_week.getDay());

        long WeekTimeMili = c_first.getTimeInMillis() + (DayInMili*7*index);
        c_first.setTimeInMillis(WeekTimeMili);
        Log.i(TAG, index +" week is " + DateFormatter.format(c_first.getTime()) );



        return new Week(
                c_first.get(Calendar.DAY_OF_MONTH),
                c_first.get(Calendar.MONTH)+1,
                c_first.get(Calendar.YEAR),
                elapsed_days,
                index,
                c_first.get(Calendar.DAY_OF_WEEK)-1
        );

    }

    public int getCurrentWeekIndex(){ return Current_week_index;}

    public int getInitialDayofWeek(){ return first_week.getWeek_day();}

    public class Week{
        private int day,month,year;
        private int elapsed_days;
        private int week_number;
        private int week_day;

        Week(int day, int month, int year, int elapsed_days, int week_number , int week_day){
            this.day            = day;
            this.month          = month;
            this.year           = year;
            this.elapsed_days   = elapsed_days;
            this.week_number    = week_number;
            this.week_day       = week_day;
        }

        public int getDay() {return day; }

        public int getMonth() {return month;}

        public int getYear() {return year;}

        public int getElapsed_days() {return elapsed_days;}

        public int getWeek_number() {return week_number;}

        public int getWeek_day() {return week_day;}

    }
}


