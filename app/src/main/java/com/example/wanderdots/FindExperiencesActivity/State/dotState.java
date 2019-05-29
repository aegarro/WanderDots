package com.example.wanderdots.FindExperiencesActivity.State;

import android.content.Context ;

import wanderDots.dot;

public class dotState extends state<dot> {

    public dotState(Context context){
        super(context) ;
        dot.addObserver(this);
    }

    public void subscriberHasChanged(String message){
        setData(dot.getData()) ; //auto updates dotState
    }
}
