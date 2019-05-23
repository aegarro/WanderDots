package com.example.wanderdots.FindExperiencesActivity.State;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import WanderDots.Adventure;
import WanderDots.Dot;

public class AdventureState extends State<Adventure> {

    public AdventureState(Context context){
        super(context) ;
        Adventure.addObserver(this);
    }

    public void subscriberHasChanged(String message){

        if(Adventure.hasError()){
            Log.d("arodr", "error occurred while loading adventures") ;
            Log.d("arodr", "error: " + Adventure.getError()) ;
        }else {
            setData(Adventure.getData()) ;
        }
    }
}
