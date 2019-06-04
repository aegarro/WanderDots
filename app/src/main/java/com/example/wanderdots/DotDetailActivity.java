package com.example.wanderdots;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import wanderdots.server.get.ImageGetter ;
import wanderdots.Observer ;

public class DotDetailActivity extends AppCompatActivity implements Observer, View.OnClickListener {

    private static String TAG = "arodr" ;
    private ImageGetter imageGetter ;
    private ImageButton mainImage ;
    private String dotTitle, dotDescription, dotRating;
    private String dotDistance;
    private String dotPictureID;
    private double dotLatitude;
    private double dotLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "DotDetailActivity: onCreate") ;
        setContentView(R.layout.activity_dot_detail);

        this.imageGetter = new ImageGetter(this) ;
        this.mainImage = findViewById(R.id.DotDetailImage) ;

        TextView titleTxt = findViewById(R.id.dotTitle);
        TextView distanceTxt = findViewById(R.id.dotDistance);
        TextView ratingTxt = findViewById(R.id.dotRating);
        TextView descriptionBox = findViewById(R.id.descriptionBox);

        Intent intent = getIntent();

        this.dotTitle = intent.getStringExtra("dotTitle") ;
        this.dotDescription = intent.getStringExtra("dotDescription") ;
        this.dotDistance = intent.getStringExtra("dotDistance") ;
        this.dotRating = intent.getStringExtra("dotRating") ;
        this.dotLatitude = Double.valueOf(intent.getStringExtra("dotLatitude")) ;
        this.dotLongitude = Double.valueOf(intent.getStringExtra("dotLongitude")) ;

        titleTxt.setText(this.dotTitle);
        distanceTxt.setText("Distance: " + this.dotDistance);
        ratingTxt.setText("Rating: " + this.dotRating);
        descriptionBox.setText(this.dotDescription) ;

        if(intent.hasExtra("pictureID")){
            this.dotPictureID = intent.getStringExtra("pictureID") ;
            this.imageGetter.loadImage(this.dotPictureID);
        }

        ImageButton imageButton = findViewById(R.id.imageButton);
        imageButton.setOnClickListener(this);
    }

    public void onClick(View v) {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
        Uri navigationIntentUri = Uri.parse("google.navigation:q=" + dotLatitude + "," + dotLongitude);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, navigationIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    public void subscriberHasChanged(String message){
       if(imageGetter.hasError()){
           Log.d(TAG, "Error loading image") ;
           Log.d(TAG, imageGetter.getError()) ;
           return ;
       }
       mainImage.setImageBitmap(imageGetter.getImage());
    }
}
