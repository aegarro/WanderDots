package com.example.Tests;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class UserProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TextView titleTxt;
        TextView distanceTxt;
        TextView ratingTxt;
        TextView describeTxt;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dot_detail);


        titleTxt = findViewById(R.id.dotTitle);
        distanceTxt = findViewById(R.id.dotDistance);
        ratingTxt = findViewById(R.id.dotRating);
        describeTxt = findViewById(R.id.userDescription);

        Intent intent = getIntent();
        titleTxt.setText(intent.getStringExtra("title"));
        distanceTxt.setText("Distance: " + intent.getStringExtra("distance"));
        ratingTxt.setText("Rating: " + intent.getStringExtra("rating"));
        describeTxt.setText("Description: ");

    }
}
