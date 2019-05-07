package com.example.wanderdots;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import WanderDots.Dot ;

public class NewDotActivity extends AppCompatActivity implements View.OnClickListener {

    private Button createButton ;
    private EditText nameTextbox ;
    private EditText descriptionTextbox ;
    private EditText categoriesTextbox ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_dot);

        this.createButton = (Button) findViewById(R.id.CreateButton) ;
        this.nameTextbox = (EditText) findViewById(R.id.NameTextbox) ;
        this.descriptionTextbox = (EditText) findViewById(R.id.DescriptionTextbox) ;

        this.createButton.setOnClickListener(this);
    }

    //Runs when form is being submitted
    public void onClick(View v){

        Dot dot = new Dot() ;
        dot.setName(nameTextbox.getText().toString()) ;
        String creator = "Root User" ;

    }
}
