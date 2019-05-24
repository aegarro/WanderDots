package com.example.wanderdots;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DotDetailActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TextView titleTxt;
        TextView distanceTxt;
        TextView ratingTxt;
        TextView describeTxt;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dot_detail);

        titleTxt = (TextView) findViewById(R.id.dotTitle);
        distanceTxt = (TextView) findViewById(R.id.dotDistance);
        ratingTxt = (TextView) findViewById(R.id.dotRating);
        describeTxt = (TextView) findViewById(R.id.userDescription);

        Intent intent = getIntent();
        titleTxt.setText(intent.getStringExtra("title"));
        distanceTxt.setText("Distance: " + intent.getStringExtra("distance"));
        ratingTxt.setText("Rating: " + intent.getStringExtra("rating"));
        describeTxt.setText("Description: ");

    }
}
