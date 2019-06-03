package com.example.Tests.find.state;

import android.content.Context ;
import android.util.Log;

import wanderdots.Dot;

/* Attaches itself as an "Observer" of Dot and retrieves data
 * whenever Dot has loaded.
 */
public class DotState extends State<Dot> {

    public DotState(Context context){
        super(context) ;
        Dot.addObserver(this);
    }

    public void subscriberHasChanged(String message){
        String tag = "arodr";
        Log.d(tag, "Dot model has changed" + message) ;
        if(Dot.hasError())
            Log.d(tag, "error occurred loading dots" + Dot.getError()) ;
        else{
            Log.d(tag, "Dot update: (new size)" + Dot.getData().size()) ;
            setData(Dot.getData()) ; //auto updates DotState
        }
    }
}
