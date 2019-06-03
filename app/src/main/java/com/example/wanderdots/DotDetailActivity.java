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

    private ImageGetter imageGetter ;
    private ImageButton mainImage ;
    private double latitude;
    private double longitude;

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
        latitude = Double.valueOf(intent.getStringExtra("latitude"));
        longitude = Double.valueOf(intent.getStringExtra("longitude"));

        String pictureID = intent.getStringExtra("pictureID") ;
        this.imageGetter.loadImage(pictureID);

        ImageButton imageButton = findViewById(R.id.imageButton);
        imageButton.setOnClickListener(this);
    }

    public void onClick(View v) {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
        Uri navigationIntentUri = Uri.parse("google.navigation:q=" + latitude + "," + longitude);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, navigationIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
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
