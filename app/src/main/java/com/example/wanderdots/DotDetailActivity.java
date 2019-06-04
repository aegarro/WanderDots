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

    private static final String TAG = "arodr" ;
    private ImageGetter imageGetter ;
    private ImageButton mainImage ;
    private Double latitude ;
    private Double longitude ;
    private ImageButton weatherBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "DotDetailActivity: onCreate") ;
        setContentView(R.layout.activity_dot_detail);

        this.imageGetter = new ImageGetter(this) ;
        this.mainImage = findViewById(R.id.DotDetailImage) ;
        weatherBtn = findViewById(R.id.weatherButton);

        weatherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DotDetailActivity.this, WeatherForecast.class);
                startActivity(intent);
            }
        });

        TextView titleTxt = findViewById(R.id.dotTitle);
        TextView distanceTxt = findViewById(R.id.dotDistance);
        TextView ratingTxt = findViewById(R.id.dotRating);
        TextView descriptionBox = findViewById(R.id.descriptionBox);

        Intent intent = getIntent(); // See ListItem to get these fields

        String dotTitle = intent.getStringExtra("title") ;
        String dotDescription = intent.getStringExtra("description") ;
        String dotDistance = intent.getStringExtra("distance") ;
        String dotRating = intent.getStringExtra("rating") ;

        this.latitude = Double.valueOf(intent.getStringExtra("latitude")) ;
        this.longitude = Double.valueOf(intent.getStringExtra("longitude")) ;

        titleTxt.setText(dotTitle);
        distanceTxt.setText("Distance: " + dotDistance);
        ratingTxt.setText("Rating: " + dotRating);
        descriptionBox.setText(dotDescription) ;

        if(intent.hasExtra("pictureID")){
            String dotPictureID = intent.getStringExtra("pictureID") ;
            this.imageGetter.loadImage(dotPictureID);
        }

        ImageButton imageButton = findViewById(R.id.imageButton);
        imageButton.setOnClickListener(this);
    }

    public void onClick(View v) {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
        Uri navigationIntentUri = Uri.parse("google.navigation:q=" + this.latitude + "," + this.longitude);
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
