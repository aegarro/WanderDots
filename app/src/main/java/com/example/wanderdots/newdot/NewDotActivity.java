package com.example.wanderdots.newdot;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.content.Intent;

import com.example.wanderdots.R;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import wanderdots.Dot;
import wanderdots.server.post.DotPoster;
import wanderdots.Observer;
import wanderdots.server.post.ImagePoster;

public class NewDotActivity extends AppCompatActivity
        implements View.OnClickListener, Observer, OnMapReadyCallback, GoogleMap.OnMarkerDragListener {

    private static final String TAG = "newDotActivity";
    private static final String LOGTAG = "arodr";
    private ImageView imageView4;
    private DotPoster dotPoster;
    private ImagePoster imagePoster ;
    private static int selectImageActivityCode = 1;
    private double latitude;
    private double longitude;
    private GoogleMap map;
    private ArrayList<String> pictureIds ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_dot);

        this.pictureIds = new ArrayList<>() ;
        this.imageView4 = findViewById(R.id.imageView4);
        this.dotPoster = new DotPoster(this);
        this.imagePoster = new ImagePoster(this);

        Button createButton = findViewById(R.id.CreateButton) ;
        ImageButton imageButton = findViewById(R.id.imageButton);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if(mapFragment != null){
            mapFragment.getMapAsync(this);
        }

        View.OnClickListener addImageListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImages();
            }
        };
        imageButton.setOnClickListener(addImageListener);
        createButton.setOnClickListener(this);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        this.map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        UiSettings mapUiSettings = this.map.getUiSettings();
        mapUiSettings.setZoomControlsEnabled(true);
        try {
            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            LatLng pos = new LatLng(latitude, longitude);
            Marker marker = googleMap.addMarker(new MarkerOptions().position(pos).title("Hold and drag to the Dot's location."));
            float zoom = 11f;
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, zoom));
            marker.setDraggable(true);
            this.map.setMyLocationEnabled(true);
        }
        catch(SecurityException e){
            Log.e(TAG, e.toString());
        }
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        //do nothing because it must override and do nothing, comment to satisfy sonarcloud!
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        //do nothing because it must override and do nothing, comment to satisfy sonarcloud!
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        //Getting the coordinates
        latitude = marker.getPosition().latitude;
        longitude = marker.getPosition().longitude;

        //Moving the map
        LatLng latLng = new LatLng(latitude, longitude);

        //Adding marker to map
        map.addMarker(new MarkerOptions()
                .position(latLng) //setting position
                .draggable(true) //Making the marker draggable
                .title("Hold and drag to the Dot's location.")); //Adding a title

        //Moving the camera
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        //Animating the camera
        map.animateCamera(CameraUpdateFactory.zoomTo(11));
    }

    private void addImages(){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(photoPickerIntent, selectImageActivityCode);
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (reqCode == selectImageActivityCode && resultCode == RESULT_OK && data != null)
            processSelectedImage(data);
    }

    /* Uploads given image data selected from the selectImageActivity to server */
    private void processSelectedImage(Intent data){

        final Uri imageUri = data.getData();

        if(imageUri == null){
            Log.d(TAG, "image from selectImageActivity is null") ;
            return ;
        }

        Bitmap selectedImage = convertImageUriToBitmap(imageUri) ;
        this.imageView4.setImageBitmap(selectedImage);
        this.imagePoster.postImage(selectedImage) ;
    }

    private Bitmap convertImageUriToBitmap(Uri imageUri){
        InputStream imageStream ;
        Bitmap selectedImage ;

        try {
            imageStream = getContentResolver().openInputStream(imageUri);
            selectedImage = BitmapFactory.decodeStream(imageStream);
            if(imageStream != null)
                imageStream.close() ;
        }catch(FileNotFoundException e){
            Log.d(TAG, "Could not find selected file") ;
            Log.d(TAG, e.toString()) ;
            selectedImage = null ;
        }catch(IOException e){
            Log.d(TAG, "Could not close image stream") ;
            Log.d(TAG, e.toString()) ;
            selectedImage = null ;
        }

        return selectedImage ;
    }
    public void subscriberHasChanged(String message){
        if(message.equals("ImagePoster"))
            processPostImageResponse();

        if(message.equals("DotPoster"))
            processPostDotResponse();
    }

    /* Processes response from posting a selected image, stores image ID if successful.
     * Otherwise, prints the error.
     */
    private void processPostImageResponse(){
        if(this.imagePoster.hasError()){
            Log.d(LOGTAG, "error posting image to server") ;
            return ;
        }

        try {
            JSONObject response = this.imagePoster.getResponse() ;
            if(response.has("error")){
                Log.d(LOGTAG, "server send back an error while posting image") ;
                Log.d(LOGTAG, response.getString("error")) ;
                return ;
            }

            if(!response.has("id")){
                Log.d(LOGTAG, "received unknown server response while posting image") ;
                Log.d(LOGTAG, response.toString()) ;
                return ;
            }

            String id = response.getString("id") ;
            this.pictureIds.add(id) ;
        }catch(JSONException e){
            Log.d(LOGTAG, "error occurred processing server json response") ;
            Log.d(LOGTAG, e.toString()) ;
        }
    }

    private void processPostDotResponse(){
        if(this.dotPoster.hasError()){
            Log.d(LOGTAG, "error occurred while posting a dot") ;
            Log.d(LOGTAG, this.dotPoster.getError()) ;
            return;
        }

        finish() ;
    }

    //Runs when form is being submitted
    public void onClick(View v){

        Log.d("wanderdots", "About to create a Dot...") ;
        //create
        Dot dot = new Dot();
        final EditText name = findViewById(R.id.NameTextbox);
        dot.setName(name.getText().toString());
        dot.setCreator("Username");
        final EditText description = findViewById(R.id.DescriptionTextbox);
        dot.setDescription(description.getText().toString());
        //how to Get data from multi-select (shaheen)
        dot.addCategory("Filler");

        for(String pictureID : this.pictureIds)
            dot.addPictureId(pictureID);
        dot.setLongitude(longitude);
        dot.setLatitude(latitude);

        this.dotPoster.postDot(dot) ;
    }
}
