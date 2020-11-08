package com.example.logpas;

import android.content.Context;

public class MainActivity2{

    // variable to hold context
    private Context context;

//save the context received via constructor in a local variable

    public MainActivity2(Context context){

        this.context=context;
    }
    public Context getContext(){
        return context;
    }

}