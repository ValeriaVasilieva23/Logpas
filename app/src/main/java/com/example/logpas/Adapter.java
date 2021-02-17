package com.example.logpas;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class Adapter extends BaseAdapter {

    private Context context;
    public ArrayList<TaskData> tasksList;
    public ArrayList<Integer> checks;
    public static HashMap<Integer, Boolean> mCheckedMap = new HashMap<>();

    public Adapter(Context context, ArrayList<TaskData> tasks) {
        this.context = context;
        tasksList = tasks;
    }

    @Override
    public int getCount() {
        return tasksList.size();
    }

    @Override
    public Object getItem(int position) {

        return tasksList.get(position).Name;
    }

    @Override
    public long getItemId(int position) {
        System.out.println(position);
        return position;

    }

    @SuppressLint("ResourceType")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TaskData task = tasksList.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.maindop, null, false);
            CheckBox cbBox = (CheckBox) convertView.findViewById(R.id.cbBox);

            // присваиваем чекбоксу обработчик
            cbBox.setChecked(task.Status);

        }
        TextView et1 = (TextView) convertView.findViewById(R.id.task);
        TextView et2 = (TextView) convertView.findViewById(R.id.addition);

        et1.setText(task.Name);
        et2.setText(task.Description);
        et2.setVisibility(convertView.GONE);
        TextView et3 = (TextView) convertView.findViewById(R.id.time);
        et3.setText(task.Time);
        View view = convertView;

        return convertView;
    }




}

