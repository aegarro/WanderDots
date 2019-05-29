package com.example.wanderdots.find.state;

import android.content.Context ;

import wanderdots.Dot;

public class DotState extends State<Dot> {

    public DotState(Context context){
        super(context) ;
        Dot.addObserver(this);
    }

    public void subscriberHasChanged(String message){
        setData(Dot.getData()) ; //auto updates DotState
    }
}
