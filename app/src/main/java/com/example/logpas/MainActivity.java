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
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends ListActivity /*implements View.OnClickListener*/ {
    ArrayList<String> tasks = new ArrayList<String>();
    ArrayList<Integer> ids = new ArrayList<Integer>();
    ArrayList<Boolean>statuses =new ArrayList<Boolean>();
    Button btnAdd, btnEdit, btnDone,btncancel,deletetask;
    ListView list;
    DBHelper dbHelper;
    public int selectedRowIndex;
    int idIndex ;
    int taskIndex ;
    int dateIndex ;
    int timeIndex ;
    int statusIndex;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("вопдлывлимыьдвлмиыьдл");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_main);

        btncancel = (Button) findViewById(R.id.cancel);

       /* AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                selectedRowIndex = position;
                System.out.println("onItemClick_postition:" + position);
                System.out.println("onItemClick_id:" + id);
            }
        };
        getListView().setOnItemClickListener(itemListener);*/

        dbHelper = new DBHelper(this);
        File dbFile = getDatabasePath(DBHelper.DATABASE_NAME);
        System.out.println(dbFile);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
       // dbHelper.CreateTasksTable(sqLiteDatabase);
        Cursor cursor = sqLiteDatabase.query(DBHelper.TABLE_TASKS2, null, null, null, null, null, null/*" ID desc"*/);
        System.out.println("cursor on TABLE_TASKS2 started");
        if (cursor.moveToFirst()){

            idIndex = cursor.getColumnIndex(DBHelper.ID);
            taskIndex = cursor.getColumnIndex(DBHelper.TASK);
            dateIndex = cursor.getColumnIndex(DBHelper.DATE);
            timeIndex = cursor.getColumnIndex(DBHelper.TIME);
            statusIndex = cursor.getColumnIndex(DBHelper.STATUS);
            System.out.println(statusIndex);
            System.out.println(idIndex);
            System.out.println(taskIndex);
            System.out.println(dateIndex);
            System.out.println(timeIndex);
            getTasksLine(cursor);
            while (cursor.moveToNext()){
                // int[] values = { cursor.getColumnIndex(DBHelper.ID), cursor.getColumnIndex(DBHelper.TASK), cursor.getColumnIndex(DBHelper.DATE), cursor.getColumnIndex(DBHelper.TIME)};

                Log.d("MainActivity", "id=" + cursor.getInt(idIndex) + ",task=" + cursor.getString(taskIndex) + ",date="
                        + cursor.getString(dateIndex) + ",time=" + cursor.getString(timeIndex));

                getTasksLine(cursor);
                //tasks.add(date);
                //tasks.add(time);

               /* ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1, tasks);
                setListAdapter(adapter);*/

            } 
            setListActivity(tasks);
        }  Log.d("mLog", "0 rows");
        setListActivity(tasks);
        //list = (ListView) findViewById(android.R.id.list);
       // ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.row_layout, R.id.txt_lan, tasks);
        //list.setAdapter(adapter);
        // создаем адаптер
       ArrayAdapter<String> adapter = new ArrayAdapter(this,
              android.R.layout.simple_list_item_multiple_choice, tasks);
        // устанавливаем для списка адаптер
      list.setAdapter(adapter);
        // добвляем для списка слушатель
        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                SparseBooleanArray sp=list.getCheckedItemPositions();

                list.setItemChecked(position, false);
                selectedRowIndex = position;
                System.out.println(selectedRowIndex);
                System.out.println(id);

               /* String selectedItems="";
                for(int i=0;i < tasks.size();i++)
                {
                    if(sp.get(i))
                        selectedItems+= tasks.get(i) +",";
                }
                // установка текста элемента TextView*/

            }
        });

      /*  list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = (String) pa.getItemAtPosition(i);

                if (tasks.contains(selectedItem)) {
                    tasks.remove(selectedItem);
                } else tasks.add(selectedItem);*/
                // Получаем флажок
               // CheckedTextView language = (CheckedTextView) view;
                // Получаем, отмечен ли данный флажок
               /* boolean checked = language.isChecked();

                TextView selection = (TextView) findViewById(R.id.selection);
                // Смотрим, какой именно из флажков отмечен
                switch(view.getId()) {
                    case R.id.java:
                        if (checked){
                            selection.setText("Java");
                        }
                        break;
                    case R.id.javascript:
                        if (checked)
                            selection.setText("JavaScript");
                        break;
                }
            }*/
            //}
       // });

        cursor.close();
        System.out.println(selectedRowIndex);
    }
    private void getTasksLine(Cursor cursor){
        int id = cursor.getInt(idIndex);
        String task = cursor.getString(taskIndex);
        String date = cursor.getString(dateIndex);
        String time = cursor.getString(timeIndex);
        String statusStr = cursor.getString(statusIndex);
        Boolean status=false;
        if (statusStr!=null)
            status=Boolean.parseBoolean(statusStr);
        ids.add(id);

        statuses.add(status);
        tasks.add(task + " " + date + " " + time);
    }

    /*
    private class ViewHolder {
        TextView code;
        CheckBox name;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;



        if (convertView == null) {

            LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = vi.inflate(R.layout.row_layout, null);

            holder = new ViewHolder();
            holder.code = (TextView) convertView.findViewById(R.id.code);
            holder.name = (CheckedTextView) convertView
                    .findViewById(R.id.txt_lan);

            convertView.setTag(holder);

            holder.name.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    States _state = (States) cb.getTag();

                    Toast.makeText(
                            getApplicationContext(),
                            "Checkbox: " + cb.getText() + " -> "
                                    + cb.isChecked(), Toast.LENGTH_LONG)
                            .show();

                    _state.setSelected(cb.isChecked());
                }
            });

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        States state = stateList.get(position);

        holder.code.setText(" (" + state.getCode() + ")");
        holder.name.setText(state.getName());
        holder.name.setChecked(state.isSelected());

        holder.name.setTag(state);

        return convertView;
    }

}*/

    public void setListActivity(ArrayList array) {
        removeListActivity();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
               android.R.layout.simple_list_item_1, array);
        list = (ListView) findViewById(android.R.id.list);
        // ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.row_layout, R.id.txt_lan, tasks);

        list.setAdapter(adapter);

        setListAdapter(adapter);
        for (int i=0;i<statuses.size();i++){
            list.setItemChecked(i,statuses.get(i));
        }
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
        //tasks.set(selectedRowIndex, strToAdd);
        setListActivity(tasks);
    }


    public  void deleteTask(View v) {
        int id=ids.get(selectedRowIndex);
        deletetask=(Button) findViewById(R.id.deleteTask);
        DBHelper dbHelper= new DBHelper(this);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        sqLiteDatabase.delete(DBHelper.TABLE_TASKS2,DBHelper.ID+" = ?",new String[] {String.valueOf(id)});
        recreate();


    }


    public void doneTask(View view) {
        boolean isChecked = list.isItemChecked(selectedRowIndex);
        boolean newValue = !isChecked;

        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.STATUS,newValue);
        DBHelper dbHelper= new DBHelper(this);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        sqLiteDatabase.update(DBHelper.TABLE_TASKS2, contentValues, DBHelper.ID + "=" +ids.get(selectedRowIndex ), null);

        list.setItemChecked(selectedRowIndex, newValue);

    }


}