package com.example.wanderdots.FindExperiencesActivity.State;

import android.content.Context ;
import android.util.Log;

import WanderDots.Dot ;

public class DotState extends State<Dot> {

    public DotState(Context context){
        super(context) ;
        Dot.addObserver(this);
    }

    public void subscriberHasChanged(String message){
        setData(Dot.getData()) ; //auto updates DotState
    }
}
