package com.example.logpas;

import androidx.annotation.RequiresApi;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends ListActivity /*implements View.OnClickListener*/ {
    ArrayList<String> tasks = new ArrayList<String>();
    private Connection connection;
    ArrayList<String> additionAr = new ArrayList<String>();
    ArrayList<Integer> ids = new ArrayList<Integer>();
    ArrayList<Integer> ids1 = new ArrayList<Integer>();

    ArrayList<Boolean> statuses = new ArrayList<Boolean>();
    View selectedView;
    ImageButton btnAdd, btnEdit, btnDone, btncancel, deletetask;
    ListView list;
    DBHelper dbHelper;
    CheckBox checkBox;
    ArrayList<String> time1= new ArrayList<String>();
    ArrayAdapter<String> adapter;
    public int selectedRowIndex;
    int idIndex;
    int taskIndex;
    int dateIndex;
    int timeIndex;
    int statusIndex;
    int additionIndex;
    boolean isChecked=false;
    int id;
    int id1;


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);

    }
    @Override
    public boolean onContextItemSelected(MenuItem item)  {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        selectedRowIndex = info.position;
        selectedView = info.targetView;
        switch (item.getItemId()) {
            case R.id.add:
                addTask(null); // метод, выполняющий действие при редактировании пункта меню
                return true;
            case R.id.edit:
                editTask(null); // метод, выполняющий действие при редактировании пункта меню
                return true;
            case R.id.delete:
                deleteTask(null); //метод, выполняющий действие при удалении пункта меню
                return true;
            case R.id.done:
                try {
                    doneTask(null); // метод, выполняющий действие при редактировании пункта меню
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @SuppressLint("WrongViewCast")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("вопдлывлимыьдвлмиыьдл");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_main);

        ListView lvMain = (ListView) findViewById(android.R.id.list);
        registerForContextMenu(lvMain);


        TextView textView = (TextView) findViewById(R.id.textView);
        Locale rus = new Locale("ru", "RU");
        SimpleDateFormat sdf = new SimpleDateFormat(" E dd.MM", Locale.getDefault());
        String date = sdf.format(Calendar.getInstance().getTime());
        textView.setText("Задания на день ("+date+" )");
        btncancel = (ImageButton) findViewById(R.id.cancel);

        String tempString="Задания на день ("+date+" )";

        SpannableString spanString = new SpannableString(tempString);
        spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
        spanString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spanString.length(), 0);
        textView.setText(spanString);
        AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if (selectedView != null) {

                    if (selectedView == v) {
                        TextView et = (TextView) selectedView.findViewById(R.id.addition);
                        int currentVisibility = et.getVisibility();
                        int reverseVisibility = 0;

                        if (currentVisibility == v.GONE) {
                            reverseVisibility = v.VISIBLE;
                        } else {
                            reverseVisibility = v.GONE;
                        }
                        et.setVisibility(reverseVisibility);
                    } else {
                        TextView et = (TextView) selectedView.findViewById(R.id.addition);
                        et.setVisibility(v.GONE);

                        TextView newTV = (TextView) v.findViewById(R.id.addition);
                        newTV.setVisibility(v.VISIBLE);
                    }
                }
                else {
                    TextView et = (TextView) v.findViewById(R.id.addition);
                    et.setVisibility(v.VISIBLE);
                }
                selectedView = v;//подсвеченная строка

                selectedRowIndex = position;
                System.out.println("onItemClick_postition:" + position);
                System.out.println("onItemClick_id:" + id);
                //markTask();

                //setListActivity(tasks);


            }
        };
        getListView().setOnItemClickListener(itemListener);




        dbHelper = new DBHelper(this);
        File dbFile = getDatabasePath(DBHelper.DATABASE_NAME);
        System.out.println(dbFile);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        Date date1 = new Date();
        Calendar cal = Calendar.getInstance();
       String date3= date1.toString();
        Date dateWithoutTime = cal.getTime();

        Cursor cursor = sqLiteDatabase.query(DBHelper.TABLE_TASKS2, null, "date<date() and status =0 or date=date()  ", null, null, null, " ID desc");
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
         id = cursor.getInt(idIndex);
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
        System.out.println(ids+"НННННННННННННННННННННННННННННННННННННГГГГГГГГГГГГГГГГГГГГГГГГГГГГГГГГ");
        statuses.add(status);
        tasks.add(task  );
        System.out.println(tasks);
        time1.add(time);
        additionAr.add(addition);
    }


    public void setListActivity(ArrayList array) {
        removeListActivity();

        setListAdapter(new Adapter(this,tasks,additionAr,time1,statuses,selectedRowIndex));

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
        System.out.println(id+"ID");
        deletetask = (ImageButton) findViewById(R.id.deleteTask);
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        sqLiteDatabase.delete(DBHelper.TABLE_TASKS2, DBHelper.ID + " = ?", new String[]{String.valueOf(id)});

        // DBHelper dbHelper = newпуе DBHelper(this);

        //recreate();

        tasks = new ArrayList<String>();
        Cursor cursor = sqLiteDatabase.query(DBHelper.TABLE_TASKS2, null, "date<date() and status =0 or date=date()", null, null, null, " ID desc");
        System.out.println("cursor on TABLE_TASKS2 started");
        ids.clear();
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
public void markTask (){
    dbHelper = new DBHelper(this);
    SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
    int index = ids.get(selectedRowIndex);
    Cursor selectRowCursor = sqLiteDatabase.query(DBHelper.TABLE_TASKS2, null, DBHelper.ID + "=" + ids.get(selectedRowIndex), null, null, null, " ID desc");
    selectRowCursor.moveToFirst();
    System.out.println(ids.get(selectedRowIndex)+"-----");
    int statusIndex1 = selectRowCursor.getColumnIndex(DBHelper.STATUS);
    String st =  selectRowCursor.getString(statusIndex1);
    System.out.println(st+"+++++");
    if (st.equals("0")) {
        isChecked = false;
    }else
    {
        isChecked = true;
    }
    boolean newValue = !isChecked;
    ContentValues contentValues = new ContentValues();
    contentValues.put(DBHelper.STATUS, newValue);
    sqLiteDatabase.update(DBHelper.TABLE_TASKS2, contentValues, DBHelper.ID + "=" + ids.get(selectedRowIndex), null);
    CheckBox selectedCB =(CheckBox) selectedView.findViewById(R.id.cbBox);
    selectedCB.setChecked(newValue);
}



    public void doneTask(View view) throws SQLException {
    markTask();

        /*setListActivity(tasks);
recreate();}*/

    }







}