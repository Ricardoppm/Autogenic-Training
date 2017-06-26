package pt.psychapp.ricardomartins.lallaapp.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static pt.psychapp.ricardomartins.lallaapp.Database.DatabaseContract.DATABASE_VERSION;

/**
 * Created by ricardomartins on 30/12/2016.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context, DatabaseContract.DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DatabaseContract.Exercises.SQL_CREATE_ENTRIES);
        db.execSQL(DatabaseContract.QuizAnswers.SQL_CREATE_ENTRIES);

    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(DatabaseContract.Exercises.SQL_DELETE_ENTRIES);
        db.execSQL(DatabaseContract.QuizAnswers.SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
