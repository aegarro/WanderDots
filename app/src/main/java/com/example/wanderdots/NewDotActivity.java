package com.example.wanderdots;

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
import android.widget.TextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.content.Intent;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import WanderDots.Dot ;
import WanderDots.Server.Post.PostDot;
import WanderDots.Observer ;

public class NewDotActivity extends AppCompatActivity
        implements View.OnClickListener, Observer {

    private Button createButton ;
    private EditText nameTextbox ;
    private EditText descriptionTextbox ;
    private TextView categoriesView ;
    private ImageView imageView4;
    private ImageButton imageButton ;
    private String dotID ;
    private PostDot dotCreator ;
    private static int RESULT_LOAD_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_dot);

        this.createButton = findViewById(R.id.CreateButton) ;
        this.nameTextbox = findViewById(R.id.NameTextbox) ;
        this.descriptionTextbox = findViewById(R.id.DescriptionTextbox) ;
        this.categoriesView = findViewById(R.id.CategoriesView);
        this.imageButton = findViewById(R.id.imageButton);
        this.imageView4 = findViewById(R.id.imageView4);

        View.OnClickListener addImageListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImages();
            }
        };
        this.imageButton.setOnClickListener(addImageListener);
        this.createButton.setOnClickListener(this);
        this.dotCreator = new PostDot(super.getApplicationContext(), this);
        this.dotID = null ;
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
    }

    private void addImages(){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if (reqCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            try {
                final Uri imageUri = data.getData();
                if(imageUri != null) {
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    imageView4.setImageBitmap(selectedImage);
                    if (imageStream != null) {
                        imageStream.close();
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void dataHasChanged(String message){
        if(this.dotCreator.getError() == null){
            this.dotID = this.dotCreator.getDotID() ;
            Log.d("arodr: NewDotActivity", "Dot " + this.dotID + " created successfully") ;
            finish() ; //should return back to home screen
        }else {
            Log.d("POST Dot Error:", this.dotCreator.getError()) ;
        }
    }

    //TODO: Test is this actually creates a dot
    //TODO: Once above satisfied, verify reasonable output if server is down
    //Runs when form is being submitted
    public void onClick(View v){

        Log.d("WanderDots", "About to create a Dot...") ;
        //create
        Dot dot = new Dot();
        final EditText name = findViewById(R.id.NameTextbox);
        dot.setName(name.getText().toString());
        //Creator???
        dot.setCreator("Root");
        final EditText description = findViewById(R.id.DescriptionTextbox);
        dot.setDescription(description.getText().toString());
        //how to get data from multi-select (shaheen)
        dot.addCategory("Filler");
        //todo: multiple images
        //what is the picture id? bitmap? file path?
        //dot.addPictureId(selectedImage);
        dot.addPictureId("testPictureID");
        //how to make a map marker and get lat/long from that - for now stub using current loc
        try {
            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            dot.setLongitude(location.getLongitude());
            dot.setLatitude(location.getLatitude());
        }
        catch(SecurityException e){
            e.printStackTrace();
        }

        this.dotCreator.postDot(dot) ;
    }
}
