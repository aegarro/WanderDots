package com.example.wanderdots;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.wanderdots.find.FindExperiencesActivity;

public class MainActivity extends AppCompatActivity {

    public static Context DEFAULT_CONTEXT ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.DEFAULT_CONTEXT = getApplicationContext() ;
        Intent intent = new Intent(this, FindExperiencesActivity.class);
        startActivity(intent);
    }

    //TODO: implement check for login token, and redirect if token not given

    //SHOULD BE USED FOR TESTING ONLY
    public static void setDefaultContext(Context context){
        DEFAULT_CONTEXT = context ;
    }
}
