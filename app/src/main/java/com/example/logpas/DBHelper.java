package com.example.logpas;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;


public class DBHelper extends SQLiteOpenHelper /*implements Parcelable*/ {
    public static int DATABASE_VERSION=1;
    public static String DATABASE_NAME="logps45";
    public static final String TABLE_TASKS2="tasks1000";

    public static final String ID="id";
    public static final String TIME="time";
    public static final String DATE="date";
    public static final String TASK="task";
    public static final String STATUS="status";
    public static final String ADDITION="addition";


    public DBHelper( Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("create table "+TABLE_TASKS2+"(" +ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+TASK+","+DATE+","+STATUS+","+TIME+","+ADDITION+")");

    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("drop table if exists "+TABLE_TASKS2);

        onCreate(sqLiteDatabase);
    }


}
