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
    public static HashMap<Integer, Boolean> mCheckedMap = new HashMap<>();


    public Adapter(Context context, ArrayList<String> task, ArrayList<String> addition,ArrayList<Boolean> statuses) {
        this.context = context;
        this.tasks = task;
        this.addition = addition;
        this.statuses=statuses;
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

         //   }
            // пишем позицию
            //cbBuy.setTag(position);
            // заполняем данными из товаров: в корзине или нет


           /* CompoundButton.OnCheckedChangeListener myCheckChangeList = new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView,
                                             boolean isChecked) {
                    cbBuy.setChecked(true);
                }
            };
            cbBuy.setOnCheckedChangeListener(myCheckChangeList);*/
        }





        TextView et1 = (TextView) convertView.findViewById(R.id.task);
        TextView et2 = (TextView) convertView.findViewById(R.id.addition);
        et1.setText(tasks.get(position));
        et2.setText(addition.get(position));
        View view = convertView;
        //final CheckBox cbBuy = (CheckBox) view.findViewById(R.id.cbBox);
        // присваиваем чекбоксу обработчик
        /*for (int i = 0; i < statuses.size(); i++) {

           // cbBuy.setItemChecked(i, statuses.get(i));
            if (statuses.get(i).equals(1))*/
            //cbBuy.setChecked(true);
        //}
        //cbBuy.setChecked(true);
        // пишем позицию
       /* cbBuy.setTag(position);
        // заполняем данными из товаров: в корзине или нет
        cbBuy.setChecked(true);
        RadioGroup.OnCheckedChangeListener myCheckChangeList = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                cbBuy.setChecked(true);
            }



        return convertView;

        };
        // do some work

    }*/
        return convertView;
}}

