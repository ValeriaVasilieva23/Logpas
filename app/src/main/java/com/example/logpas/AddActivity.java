package com.example.logpas;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.SimpleTimeZone;

public class AddActivity extends AppCompatActivity implements
        View.OnClickListener {
    Button saveTask;
    EditText edit_message;
    Button btnDatePicker, btnTimePicker,btncancel;
    EditText txtDate, txtTime;
    private int mYear, mMonth, mDay, mHour, mMinute;
    public String dayofMonth1;
    public String monthOfYear1;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        System.out.println(mHour = c.get(Calendar.HOUR_OF_DAY));
        System.out.println(mMinute = c.get(Calendar.MINUTE));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        btncancel= (Button) findViewById(R.id.cancel);
        saveTask = (Button) findViewById(R.id.btn_save);
        btnDatePicker = (Button) findViewById(R.id.btn_date);
        btnTimePicker = (Button) findViewById(R.id.btn_time);
        txtDate = (EditText) findViewById(R.id.in_date);
        txtTime = (EditText) findViewById(R.id.in_time);
        edit_message = (EditText) findViewById(R.id.edit_message);

        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);
        final String task1 = edit_message.getText().toString();
        /*do {saveTask.setEnabled(false);
        }
        while (task1=="");*/

            saveTask.setEnabled(false);

edit_message.addTextChangedListener(new TextWatcher() {
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
       String  task2=edit_message.getText().toString();
        if (task2.equals("")){
            saveTask.setEnabled(false);

        }
        else{
            saveTask.setEnabled(true);
        }
    }
});




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
    sqLiteDatabase.insert(DBHelper.TABLE_TASKS2, null, contentValues);

        /*Cursor cursor = sqLiteDatabase.query(DBHelper.TABLE_TASKS2, null,null, null,null, null, null);
        System.out.println("ЩЩЩЩЩЩЩЩЩЩЖЩЛДОТДРИЛРПМПОСААПСОРИТДЛЬЖДБЖЬДРИ" );
        /*cursor.moveToLast();
            int idIndex = cursor.getColumnIndex(DBHelper.ID);
            int taskIndex = cursor.getColumnIndex(DBHelper.TASK);
            int dateIndex = cursor.getColumnIndex(DBHelper.DATE);
            int timeIndex = cursor.getColumnIndex(DBHelper.TIME);
            int statusIndex = cursor.getColumnIndex(DBHelper.STATUS);
            System.out.println("ЩЩЩЩЩЩЩЩЩЩЖЩЛДОТДРИЛРПМПОСААПСОРИТДЛЬЖДБЖЬДРИ" + idIndex);
            System.out.println(taskIndex);
            System.out.println(dateIndex);
            System.out.println(timeIndex);
*/
        Intent intent = new Intent(this, MainActivity.class);
        //intent.putExtra("stringToAdd", task+" "+date+" " + time);
        startActivity(intent);
        //cursor.close();
        System.out.println("привет");


    }


public void onChanged(View v) {}

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {

            if (v == btncancel) {
                finish();
            }

            if (v == btnDatePicker) {

                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(this,// При создании окна его объекту передается обработчик выбора даты DatePickerDialog.OnDateSetListener, который изменяет дату на текстовом поле.
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
                                int year1=year-1900;
                                Date d = new Date();
                               d.setMonth(monthOfYear);
                               d.setYear(year1);
                               d.setDate(dayOfMonth);
                                System.out.println(dayOfMonth);
                                String strDate = dateFormatter.format(d);
                                txtDate.setText(strDate);
                                System.out.println(strDate);

                            }
                        }, mYear, mMonth, mDay);





                datePickerDialog.show();
            }
            if (v == btnTimePicker) {

                final Calendar c = Calendar.getInstance();
                System.out.println(mHour = c.get(Calendar.HOUR_OF_DAY));
                System.out.println(mMinute = c.get(Calendar.MINUTE));

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                SimpleDateFormat simpleDateFormat= new SimpleDateFormat("hh:mm");
                                Time t=new Time(hourOfDay,minute,0);//?
                                String strDate1 = simpleDateFormat.format(t);
                                txtTime.setText(strDate1);

                            }
                        }, mHour, mMinute, false);
                int l =14;
                System.out.println("0"+l);
                System.out.println(mHour);
                System.out.println(mMinute);
                timePickerDialog.show();
            }

    }
}
//Проверка связи
