package com.example.wanderdots;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.wanderdots.FindExperiences.FindExperiencesActivity;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, FindExperiencesActivity.class);
        startActivity(intent);

        //TODO: implement check for login token, and redirect if token not given
    }
}
