package com.example.wanderdots;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DotDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dot_detail);


        TextView titleTxt = findViewById(R.id.dotTitle);
        TextView distanceTxt = findViewById(R.id.dotDistance);
        TextView ratingTxt = findViewById(R.id.dotRating);
        TextView describeTxt = findViewById(R.id.userDescription);

        Intent intent = getIntent();
        titleTxt.setText(intent.getStringExtra("title"));
        distanceTxt.setText("Distance: " + intent.getStringExtra("distance"));
        ratingTxt.setText("Rating: " + intent.getStringExtra("rating"));
        describeTxt.setText("Description: ");

    }
}
