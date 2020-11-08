package com.example.logpas;

import androidx.annotation.RequiresApi;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

public class EditActivity extends AddActivity {
    public int taskId;
    public int mYear;
    public int mMonth;
    public int mDay;
    public String time;
    Button btncancel;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Bundle arguments = getIntent().getExtras();
        Intent intent=getIntent();
        taskId = arguments.getInt("TaskId");
        System.out.println("taskId=" + taskId);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        EditText editText1=(EditText) findViewById(R.id.in_date);
        EditText editText2=(EditText) findViewById(R.id.in_time);
        DBHelper dbhelper = new DBHelper(this);
        SQLiteDatabase sqLiteDatabase = dbhelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query(DBHelper.TABLE_TASKS2, null, "Id = " + taskId, null, null, null, null);
        cursor.moveToFirst();
        int taskIndex = cursor.getColumnIndex(DBHelper.TASK);
        String task = cursor.getString(taskIndex);
        int timeIndex = cursor.getColumnIndex(DBHelper.TIME);
        time = cursor.getString(timeIndex);
        System.out.println(time);
        int dateIndex = cursor.getColumnIndex(DBHelper.DATE);
        String date = cursor.getString(dateIndex);
        System.out.println(date);

        editText.setText("" + task + "", TextView.BufferType.EDITABLE);
        editText1.setText("" + date + "", TextView.BufferType.EDITABLE);
        editText2.setText("" + time + "", TextView.BufferType.EDITABLE);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {

        if(v== btncancel) {
            finish();
        }

        if (v == btnDatePicker) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,// При создании окна его объекту передается обработчик выбора даты DatePickerDialog.OnDateSetListener, который изменяет дату на текстовом поле.
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

        txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

    }
}, mYear, mMonth, mDay);
        datePickerDialog.show();
        }
        if (v == btnTimePicker) {
            int hour=0,minute=0;
            try {
                hour = Integer.parseInt(time.substring(0, 2));
                minute = Integer.parseInt(time.substring(3, 5));
            }
            catch(Exception ex)
            {
                final Calendar c = Calendar.getInstance();
                hour = c.get(Calendar.HOUR_OF_DAY);
                minute = c.get(Calendar.MINUTE);
            }

                        // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            txtTime.setText(hourOfDay + ":" + minute);
                        }
                    }, hour, minute, false);
            timePickerDialog.show();
        }

    }
    public void saveTask(View view) {
        String task = edit_message.getText().toString();
        String date = txtDate.getText().toString();
        String time = txtTime.getText().toString();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.DATE, date);
        contentValues.put(DBHelper.TASK, task);
        contentValues.put(DBHelper.TIME, time);
        DBHelper dbHelper= new DBHelper(this);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        //sqLiteDatabase.insert(DBHelper.TABLE_TASKS2, null, contentValues);
        sqLiteDatabase.update(DBHelper.TABLE_TASKS2, contentValues, DBHelper.ID + "=" + taskId, null);
        System.out.println(task);
        System.out.println(date);
        System.out.println(time);

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("stringToAdd", task+" "+date+" " + time);
        startActivity(intent);
        System.out.println("привет");
        finish();

    }
}