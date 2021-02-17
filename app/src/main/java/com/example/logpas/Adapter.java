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
    public ArrayList<Integer> checks;
    private ArrayList<String> tasks;
    private ArrayList<String> addition;
    private ArrayList<Boolean> statuses;
    private ArrayList<String> time;
    private int selectri;
    public static HashMap<Integer, Boolean> mCheckedMap = new HashMap<>();


    public Adapter(Context context, ArrayList<String> task, ArrayList<String> addition, ArrayList<String> time, ArrayList<Boolean> statuses, int selectedRI) {
        this.context = context;
        this.tasks = task;
        this.addition = addition;
        this.statuses = statuses;
        this.time = time;
        this.time = time;
        this.selectri = selectedRI;
        /*for (int i = 0; i < tasks.size(); i++) {
            mCheckedMap.put(i, statuses.get(i));
        }*/

    }

    @Override
    public int getCount() {
        return tasks.size();
    }

    @Override
    public Object getItem(int position) {

        return tasks.get(position);
    }

    @Override
    public long getItemId(int position) {
        System.out.println(position);
        return position;

    }

    @SuppressLint("ResourceType")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.maindop, null, false);
            CheckBox cbBox = (CheckBox) convertView.findViewById(R.id.cbBox);

            // присваиваем чекбоксу обработчик
            // for (int i = 0; i < statuses.size(); i++) {
            cbBox.setChecked(statuses.get(position));

        }
        TextView et1 = (TextView) convertView.findViewById(R.id.task);
        TextView et2 = (TextView) convertView.findViewById(R.id.addition);

        et1.setText(tasks.get(position));
        et2.setText(addition.get(position));
        et2.setVisibility(convertView.GONE);
        TextView et3 = (TextView) convertView.findViewById(R.id.time);
        et3.setText(time.get(position));
        View view = convertView;

        return convertView;
    }




}

