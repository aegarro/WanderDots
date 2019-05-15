package com.example.wanderdots;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import WanderDots.Dot ;
import WanderDots.Server.Post.PostDot;
import WanderDots.Observer ;

public class NewDotActivity extends AppCompatActivity
        implements View.OnClickListener, Observer {

    private Button createButton ;
    private EditText nameTextbox ;
    private EditText descriptionTextbox ;
    private EditText categoriesTextbox ;
    private String dotID ;
    private PostDot dotCreator ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_dot);

        this.createButton = (Button) findViewById(R.id.CreateButton) ;
        this.nameTextbox = (EditText) findViewById(R.id.NameTextbox) ;
        this.descriptionTextbox = (EditText) findViewById(R.id.DescriptionTextbox) ;

        this.createButton.setOnClickListener(this);
        this.dotCreator = new PostDot(super.getApplicationContext(), this);
        this.dotID = null ;
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
        //DUMMY CREATE
        Dot dot = new Dot() ;
        dot.setName("Dot created in studio");
        dot.setCreator("RootUser");
        dot.setDescription("Test dot for Post route from studio");
        dot.addCategory("TestSet");
        dot.addPictureId("testPictureID") ;
        dot.setLatitude(35.123123123);
        dot.setLongitude(-120.45);

        this.dotCreator.postDot(dot) ;
    }
}
