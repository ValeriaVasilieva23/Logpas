package com.example.logpas;

import androidx.annotation.RequiresApi;

import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ListActivity /*implements View.OnClickListener*/{
    ArrayList<String> tasks = new ArrayList<String>();
    private Connection connection;
    ArrayList<String> additionAr = new ArrayList<String>();
    ArrayList<Integer> ids = new ArrayList<Integer>();
    ArrayList<Boolean> statuses = new ArrayList<Boolean>();
    View selectedView;
    Button btnAdd, btnEdit, btnDone, btncancel, deletetask;
    ListView list;
    DBHelper dbHelper;
    CheckBox checkBox;
    ArrayAdapter<String> adapter;
    public int selectedRowIndex;
    int idIndex;
    int taskIndex;
    int dateIndex;
    int timeIndex;
    int statusIndex;
    int additionIndex;
    boolean isChecked=false;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("вопдлывлимыьдвлмиыьдл");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_main);

        btncancel = (Button) findViewById(R.id.cancel);

       AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
selectedView=v;
                selectedRowIndex = position;
                System.out.println("onItemClick_postition:" + position);
                System.out.println("onItemClick_id:" + id);
            }
        };
        getListView().setOnItemClickListener(itemListener);

        dbHelper = new DBHelper(this);
        File dbFile = getDatabasePath(DBHelper.DATABASE_NAME);
        System.out.println(dbFile);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query(DBHelper.TABLE_TASKS2, null, null, null, null, null, " ID desc");
        System.out.println("cursor on TABLE_TASKS2 started");
        if (cursor.moveToFirst()) {

            idIndex = cursor.getColumnIndex(DBHelper.ID);
            taskIndex = cursor.getColumnIndex(DBHelper.TASK);
            dateIndex = cursor.getColumnIndex(DBHelper.DATE);
            timeIndex = cursor.getColumnIndex(DBHelper.TIME);
            statusIndex = cursor.getColumnIndex(DBHelper.STATUS);
            additionIndex=cursor.getColumnIndex(DBHelper.ADDITION);

            System.out.println(statusIndex);
            System.out.println(idIndex);
            System.out.println(taskIndex);
            System.out.println(dateIndex);
            System.out.println(timeIndex);
            System.out.println(additionIndex);
            getTasksLine(cursor);
            while (cursor.moveToNext()) {
                // int[] values = { cursor.getColumnIndex(DBHelper.ID), cursor.getColumnIndex(DBHelper.TASK), cursor.getColumnIndex(DBHelper.DATE), cursor.getColumnIndex(DBHelper.TIME)};

                Log.d("MainActivity", "id=" + cursor.getInt(idIndex) + ",task=" + cursor.getString(taskIndex) + ",date="
                        + cursor.getString(dateIndex) + ",time=" + cursor.getString(timeIndex));
                getTasksLine(cursor);
            }
        }
        Log.d("mLog", "0 rows");
        setListActivity(tasks);

            }



    private void getTasksLine(Cursor cursor) {
        int id = cursor.getInt(idIndex);
        String task = cursor.getString(taskIndex);
        String date = cursor.getString(dateIndex);
        String time = cursor.getString(timeIndex);
        String statusStr = cursor.getString(statusIndex);
        String addition =  cursor.getString(additionIndex);
        Boolean status = true;
        //System.out.println(statusStr);
        if (statusStr != null) {
            if (statusStr.equals("1")) {
                status = true;

            } else {
                status = false;
            }
        } else {
            status = false;
        }


        ids.add(id);

        statuses.add(status);
        tasks.add(task + " " + date + " " + time );
        additionAr.add(addition);





    }


    public void setListActivity(ArrayList array) {
        removeListActivity();

        setListAdapter(new Adapter(this,tasks,additionAr,statuses));

       // list.setOnItemClickListener(myOnItemClickListener);


        // ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.row_layout, R.id.txt_lan, tasks);



        //setListAdapter(adapter);



    }

    public void removeListActivity() {
        tasks.remove(tasks);
    }


    public void addTask(View view) {
        Intent intent = new Intent(this, AddActivity.class);
        startActivity(intent);
        setListActivity(tasks);

    }

    public void editTask(View view) {
        Intent intent = new Intent(this, EditActivity.class);
        int idForEdit = ids.get(selectedRowIndex);
        intent.putExtra("TaskId", idForEdit);
        startActivity(intent);
        //String strToAdd = getIntent().getStringExtra("stringToAdd");
        //tasks.set(selectedRowIn-dex, strToAdd);
        setListActivity(tasks);
    }


    public void deleteTask(View v) {
        int id = ids.get(selectedRowIndex);
        deletetask = (Button) findViewById(R.id.deleteTask);
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        sqLiteDatabase.delete(DBHelper.TABLE_TASKS2, DBHelper.ID + " = ?", new String[]{String.valueOf(id)});
        recreate();


    }




    public void doneTask(View view) throws SQLException {
        dbHelper = new DBHelper(this);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        Cursor cursor1 = sqLiteDatabase.query(DBHelper.TABLE_TASKS2, null, null, null, null, null, null/*" ID desc"*/);

        cursor1.moveToPosition(ids.get(selectedRowIndex)-1);

        System.out.println(ids.get(selectedRowIndex)+"-----");
           int statusIndex1 = cursor1.getColumnIndex(DBHelper.STATUS);
        System.out.println(statusIndex1+"_____");

        String st =  cursor1.getString(statusIndex1);
        System.out.println(st+"+++++");


        if (st==null || st.equals("0")) {
             isChecked = false;

        }else
        {

             isChecked = true;
        }

        boolean newValue = !isChecked;

        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.STATUS, newValue);
       // DBHelper dbHelper = new DBHelper(this);

        sqLiteDatabase.update(DBHelper.TABLE_TASKS2, contentValues, DBHelper.ID + "=" + ids.get(selectedRowIndex), null);
        CheckBox selectedCB =(CheckBox) selectedView.findViewById(R.id.cbBox);
selectedCB.setChecked(newValue);
        /*setListActivity(tasks);
recreate();}*/
    }






}