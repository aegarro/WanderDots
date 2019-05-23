package com.example.wanderdots;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DotDetailActivity extends AppCompatActivity {

    private TextView titleTxt;
    private TextView distanceTxt;
    private TextView ratingTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dot_detail);

        titleTxt = (TextView) findViewById(R.id.dotTitle);
        distanceTxt = (TextView) findViewById(R.id.dotDistance);
        ratingTxt = (TextView) findViewById(R.id.dotRating);

        Intent intent = getIntent();
        titleTxt.setText(intent.getStringExtra("title"));
        distanceTxt.setText("Distance: " + intent.getStringExtra("distance"));
        ratingTxt.setText("Rating: " + intent.getStringExtra("rating"));

    }
}
