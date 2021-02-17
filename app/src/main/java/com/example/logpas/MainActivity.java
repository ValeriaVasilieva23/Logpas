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
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends ListActivity {
    ArrayList<TaskData> tasksList = new ArrayList<TaskData>();

    View selectedLineView;

    ArrayAdapter<String> adapter;
    public int selectedRowIndex;
    int idIndex;
    int taskNameIndex;
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
        selectedLineView = info.targetView;
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
        ImageButton btnCancel = (ImageButton) findViewById(R.id.cancel);

        String tempString="Задания на день ("+date+" )";

        SpannableString spanString = new SpannableString(tempString);
        spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
        spanString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spanString.length(), 0);
        textView.setText(spanString);
        AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if (selectedLineView != null) {

                    if (selectedLineView == v) {
                        TextView et = (TextView) selectedLineView.findViewById(R.id.addition);
                        int currentVisibility = et.getVisibility();
                        int reverseVisibility = 0;

                        if (currentVisibility == v.GONE) {
                            reverseVisibility = v.VISIBLE;
                        } else {
                            reverseVisibility = v.GONE;
                        }
                        et.setVisibility(reverseVisibility);
                    } else {
                        TextView et = (TextView) selectedLineView.findViewById(R.id.addition);
                        et.setVisibility(v.GONE);

                        TextView newTV = (TextView) v.findViewById(R.id.addition);
                        newTV.setVisibility(v.VISIBLE);
                    }
                }
                else {
                    TextView et = (TextView) v.findViewById(R.id.addition);
                    et.setVisibility(v.VISIBLE);
                }
                selectedLineView = v;//подсвеченная строка

                selectedRowIndex = position;
                System.out.println("onItemClick_postition:" + position);
                System.out.println("onItemClick_id:" + id);
                //markTask();

                //setListActivity(tasks);


            }
        };
        getListView().setOnItemClickListener(itemListener);




        DBHelper dbHelper = new DBHelper(this);
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
            taskNameIndex = cursor.getColumnIndex(DBHelper.TASK);
            dateIndex = cursor.getColumnIndex(DBHelper.DATE);
            timeIndex = cursor.getColumnIndex(DBHelper.TIME);
            statusIndex = cursor.getColumnIndex(DBHelper.STATUS);
            additionIndex=cursor.getColumnIndex(DBHelper.ADDITION);

            System.out.println(statusIndex);
            System.out.println(idIndex);
            System.out.println(taskNameIndex);
            System.out.println(dateIndex);
            System.out.println(timeIndex);
            System.out.println(additionIndex);
            getTasksLine(cursor);
            while (cursor.moveToNext()) {
                Log.d("MainActivity", "id=" + cursor.getInt(idIndex) + ",task=" + cursor.getString(taskNameIndex) + ",date="
                        + cursor.getString(dateIndex) + ",time=" + cursor.getString(timeIndex));
                getTasksLine(cursor);
            }
        }
        Log.d("mLog", "0 rows");
        setListActivity();

            }



    private void getTasksLine(Cursor cursor) {

        TaskData task = new TaskData();

        task.Id = cursor.getInt(idIndex);
        task.Name = cursor.getString(taskNameIndex);
        task.Date = cursor.getString(dateIndex);
        task.Time = cursor.getString(timeIndex);
        task.Description =  cursor.getString(additionIndex);

        String statusStr = cursor.getString(statusIndex);

        Boolean status = true;
        if (statusStr != null) {
            if (statusStr.equals("1")) {
                status = true;
            } else {
                status = false;
            }
        } else {
            status = false;
        }
        task.Status = status;

        tasksList.add(task);

        //tasksIds.add(id);
        //System.out.println(tasksIds +"НННННННННННННННННННННННННННННННННННННГГГГГГГГГГГГГГГГГГГГГГГГГГГГГГГГ");

        //tasksNames.add(task  );
        //System.out.println(tasksNames);
        //tasksTimes.add(time);
        //tasksDescriptions.add(addition);
    }


    public void setListActivity() {
        removeListActivity();

        //setListAdapter(new Adapter(this, tasksNames, tasksDescriptions, tasksTimes, tasksStatuses,selectedRowIndex));
        setListAdapter(new Adapter(this, tasksList));
    }

    public void removeListActivity() {
        tasksList.remove(tasksList);
    }


    public void addTask(View view) {
        Intent intent = new Intent(this, AddActivity.class);
        startActivity(intent);
        setListActivity();

    }

    public void editTask(View view) {
        Intent intent = new Intent(this, EditActivity.class);
        int idForEdit = tasksList.get(selectedRowIndex).Id;
        intent.putExtra("TaskId", idForEdit);
        startActivity(intent);
        //String strToAdd = getIntent().getStringExtra("stringToAdd");
        //tasks.set(selectedRowIn-dex, strToAdd);
        setListActivity();
    }


    public void deleteTask(View v) {
        int id = tasksList.get(selectedRowIndex).Id;
        System.out.println(id+"ID");
        ImageButton btnDelete = (ImageButton) findViewById(R.id.deleteTask);
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        sqLiteDatabase.delete(DBHelper.TABLE_TASKS2, DBHelper.ID + " = ?", new String[]{String.valueOf(id)});

        //tasksNames = new ArrayList<String>();
        Cursor cursor = sqLiteDatabase.query(DBHelper.TABLE_TASKS2, null, "date<date() and status =0 or date=date()", null, null, null, " ID desc");
        System.out.println("cursor on TABLE_TASKS2 started");
        tasksList.clear();
        if (cursor.moveToFirst()) {

            idIndex = cursor.getColumnIndex(DBHelper.ID);
            taskNameIndex = cursor.getColumnIndex(DBHelper.TASK);
            dateIndex = cursor.getColumnIndex(DBHelper.DATE);
            timeIndex = cursor.getColumnIndex(DBHelper.TIME);
            statusIndex = cursor.getColumnIndex(DBHelper.STATUS);
            additionIndex=cursor.getColumnIndex(DBHelper.ADDITION);

            System.out.println(statusIndex);
            System.out.println(idIndex);
            System.out.println(taskNameIndex);
            System.out.println(dateIndex);
            System.out.println(timeIndex);
            System.out.println(additionIndex);
            getTasksLine(cursor);
            while (cursor.moveToNext()) {
                Log.d("MainActivity", "id=" + cursor.getInt(idIndex) + ",task=" + cursor.getString(taskNameIndex) + ",date="
                        + cursor.getString(dateIndex) + ",time=" + cursor.getString(timeIndex));
                getTasksLine(cursor);
            }
        }
        Log.d("mLog", "0 rows");
        setListActivity();

    }
public void markTask (){
    DBHelper dbHelper = new DBHelper(this);
    SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
    int index = tasksList.get(selectedRowIndex).Id;
    Cursor selectRowCursor = sqLiteDatabase.query(DBHelper.TABLE_TASKS2, null, DBHelper.ID + "=" + tasksList.get(selectedRowIndex).Id, null, null, null, " ID desc");
    selectRowCursor.moveToFirst();
    //System.out.println(tasksIds.get(selectedRowIndex)+"-----");
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
    sqLiteDatabase.update(DBHelper.TABLE_TASKS2, contentValues, DBHelper.ID + "=" + tasksList.get(selectedRowIndex).Id, null);
    CheckBox selectedCB =(CheckBox) selectedLineView.findViewById(R.id.cbBox);
    selectedCB.setChecked(newValue);
}



    public void doneTask(View view) throws SQLException {
    markTask();
    }







}