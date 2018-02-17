package edu.virginia.cs4720.final_project_final_sudowoodo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "MoneyTracker.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table item (" +
                "name varchar(25), " +
                "category varchar(25), " +
                "amount varchar(25), " +
                "details varchar(50), " +
                "date varchar(25), " +
                "year varchar(25), " +
                "month varchar(25), " +
                "day varchar(25), " +
                "image BLOB, " +
                "rowIndex varchar(25))");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL("delete table item");
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
