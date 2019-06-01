package com.example.wanderdots.find.state;

import android.content.Context ;
import android.util.Log;

import wanderdots.Dot;

public class DotState extends State<Dot> {

    public DotState(Context context){
        super(context) ;
        Dot.addObserver(this);
    }

    public void subscriberHasChanged(String message){
        if(Dot.hasError())
            Log.d("arodr", "error occurred loading dots" + Dot.getError()) ;
        else
            setData(Dot.getData()) ; //auto updates DotState
    }
}
