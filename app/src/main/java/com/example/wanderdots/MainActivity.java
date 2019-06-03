package com.example.wanderdots;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.wanderdots.find.FindExperiencesActivity;

import wanderdots.Adventure;
import wanderdots.Dot;

public class MainActivity extends AppCompatActivity {

    private static Context defaultContext;
    private final int LOGIN_ID = 420 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.defaultContext = getApplicationContext() ;
        Intent loginIntent = new Intent(this, LoginActivity.class) ;
        startActivityForResult(loginIntent, LOGIN_ID);
    }

    @Override
    //Runs after CreateDotActivity
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LOGIN_ID && resultCode == RESULT_CANCELED){
            Intent intent = new Intent(this, FindExperiencesActivity.class);
            startActivity(intent);
        }
    }
    //TODO: implement check for login token, and redirect if token not given

    //SHOULD BE USED FOR TESTING ONLY
    public static void setDefaultContext(Context context){
        defaultContext = context ;
    }

    public static Context getDefaultContext(){
        return defaultContext ;
    }
}
