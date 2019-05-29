package com.example.wanderdots.findExperienceActivity.state;

import android.content.Context ;

import wanderDots.Dot;

public class DotState extends State<Dot> {

    public DotState(Context context){
        super(context) ;
        Dot.addObserver(this);
    }

    public void subscriberHasChanged(String message){
        setData(Dot.getData()) ; //auto updates DotState
    }
}
