package com.example.wanderdots;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import wanderdots.server.get.ImageGetter ;
import wanderdots.Observer ;

public class DotDetailActivity extends AppCompatActivity implements Observer {

    private ImageGetter imageGetter ;
    private ImageButton mainImage ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dot_detail);

        this.imageGetter = new ImageGetter(this) ;
        this.mainImage = findViewById(R.id.DotDetailImage) ;

        TextView titleTxt = findViewById(R.id.dotTitle);
        TextView distanceTxt = findViewById(R.id.dotDistance);
        TextView ratingTxt = findViewById(R.id.dotRating);
        TextView describeTxt = findViewById(R.id.userDescription);

        Intent intent = getIntent();
        titleTxt.setText(intent.getStringExtra("title"));
        distanceTxt.setText("Distance: " + intent.getStringExtra("distance"));
        ratingTxt.setText("Rating: " + intent.getStringExtra("rating"));
        describeTxt.setText("Description: ");

        String pictureID = intent.getStringExtra("pictureID") ;
        this.imageGetter.loadImage(pictureID);
    }

    public void subscriberHasChanged(String message){
       if(imageGetter.hasError()){
           Log.d("arodr", "Error loading image") ;
           Log.d("arodr", imageGetter.getError()) ;
           return ;
       }
       mainImage.setImageBitmap(imageGetter.getImage());
    }
}
