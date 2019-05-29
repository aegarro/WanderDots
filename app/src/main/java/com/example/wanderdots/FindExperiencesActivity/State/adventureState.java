package com.example.wanderdots.FindExperiencesActivity.State;

import android.content.Context;
import android.util.Log;

import wanderDots.adventure;

public class adventureState extends state<adventure> {

    public adventureState(Context context){
        super(context) ;
        adventure.addObserver(this);
    }

    public void subscriberHasChanged(String message){

        if(adventure.hasError()){
            Log.d("arodr", "error occurred while loading adventures") ;
            Log.d("arodr", "error: " + adventure.getError()) ;
        }else {
            setData(adventure.getData()) ;
        }
    }
}
