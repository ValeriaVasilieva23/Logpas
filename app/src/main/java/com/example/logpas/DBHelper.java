package com.example.logpas;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;


public class DBHelper extends SQLiteOpenHelper /*implements Parcelable*/ {
   /* public static final Creator <DBHelper> CREATOR = new DBHelperCreator();*/
    public static int DATABASE_VERSION=1;
    public static String DATABASE_NAME="task946390860935877";
    public static final String TABLE_TASKS2="tasks1000";

    public static final String ID="id";
    public static final String TIME="time";
    public static final String DATE="date";
    public static final String TASK="task";
    public static final String STATUS="status";

    public DBHelper( Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
   /* public static class DBHelperCreator implements Creator<DBHelper> {
        @Override
        public DBHelper createFromParcel(Parcel source) {
            DATABASE_VERSION=source.readInt();
            DATABASE_NAME=source.readString();

            return new DBHelper(DBHelper.this);
        }

        @Override
        public DBHelper[] newArray(int size) {
            return new DBHelper[size];
        }
    };*/

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table "+TABLE_TASKS2+"(" +ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+TASK+","+DATE+","+STATUS+","+TIME+")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("drop table if exists "+TABLE_TASKS2);
        onCreate(sqLiteDatabase);
    }

   /* @Override
    public int describeContents() {
        return 0;
    }*/

   /* @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(DATABASE_NAME);
        parcel.writeInt(DATABASE_VERSION);

    }*/
}
