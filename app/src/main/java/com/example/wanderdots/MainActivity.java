package com.example.wanderdots;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.wanderdots.find.FindExperiencesActivity;

public class MainActivity extends AppCompatActivity {

    private static Context defaultContext;
    private static final int LOGINID = 420 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState) ;
        setDefaultContext(getApplicationContext()) ;
        Intent loginIntent = new Intent(this, LoginActivity.class) ;
        startActivityForResult(loginIntent, LOGINID);
    }

    @Override
    //Runs after CreateDotActivity
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LOGINID && resultCode == RESULT_CANCELED){
            Intent intent = new Intent(this, FindExperiencesActivity.class);
            startActivity(intent);
        }
    }

    //SHOULD BE USED FOR TESTING ONLY
    public static void setDefaultContext(Context context){
        defaultContext = context ;
    }

    public static Context getDefaultContext(){
        return defaultContext ;
    }
}
