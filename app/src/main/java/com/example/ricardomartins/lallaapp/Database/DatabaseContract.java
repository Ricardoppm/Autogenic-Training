package com.example.ricardomartins.lallaapp.Database;

import android.provider.BaseColumns;

/**
 * Created by ricardomartins on 30/12/2016.
 */

public final class DatabaseContract {

    public static final  int    DATABASE_VERSION   = 6;
    public static final  String DATABASE_NAME      = "LauraApp.db";
    private static final String TEXT_TYPE          = " TEXT";
    private static final String INTEGER_TYPE       = " INTEGER";
    private static final String COMMA_SEP          = ",";

    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private DatabaseContract() {}

    /* Inner class that defines the table contents */
    public static class Exercises implements BaseColumns {

        public static final String TABLE_NAME = "Exercises";
        public static final String COLUMN_DATE = "Date";
        public static final String COLUMN_PERIOD = "Period";
        public static final String COLUMN_RELAX = "Relax";
        public static final String COLUMN_PHYSICAL = "Physical";
        public static final String COLUMN_REMARK = "Remark";
        public static final String COLUMN_WEEK = "Week";
        public static final String COLUMN_DAYOFWEEK = "DayofWeek";


        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " +
                        TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY," +
                        COLUMN_DATE + TEXT_TYPE + COMMA_SEP +
                        COLUMN_PERIOD + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_RELAX + INTEGER_TYPE +COMMA_SEP +
                        COLUMN_PHYSICAL + TEXT_TYPE +COMMA_SEP +
                        COLUMN_REMARK + TEXT_TYPE + COMMA_SEP +
                        COLUMN_WEEK + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_DAYOFWEEK + INTEGER_TYPE + " )";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + Exercises.TABLE_NAME;
    }

    /* Inner class that defines the table contents */
    public static class QuizAnswers implements BaseColumns {

        public static final String TABLE_NAME = "QuizAnswers";
        public static final String COLUMN_NB = "QuestionNb";
        public static final String COLUMN_ANSWER = "Answer";
        public static final String COLUMN_QUIZ = "Quiz";


        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " +
                        TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY," +
                        COLUMN_NB + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_ANSWER + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_QUIZ + INTEGER_TYPE + " )";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + QuizAnswers.TABLE_NAME;
    }



}
