package com.example.wanderdots.find.state;

import android.content.Context;
import android.util.Log;

import wanderdots.Adventure;

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
