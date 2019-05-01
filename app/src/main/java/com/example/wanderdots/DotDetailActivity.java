package com.example.wanderdots;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DotDetailActivity extends AppCompatActivity {

    private TextView titleTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dot_detail);

        titleTxt = (TextView) findViewById(R.id.dotTitle);
        Intent intent = getIntent();
        titleTxt.setText(intent.getStringExtra("title"));
    }
}
