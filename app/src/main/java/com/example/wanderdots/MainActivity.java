package com.example.wanderdots;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton newDotBtn;
    private Button dotDetailBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newDotBtn = (FloatingActionButton) findViewById(R.id.new_dot_btn);
        dotDetailBtn = (Button) findViewById(R.id.dot_detail_btn);

        newDotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewDotActivity.class);
                startActivity(intent);
            }
        });

        dotDetailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DotDetailActivity.class);
                startActivity(intent);
            }
        });

        /* TODO: this needs to be wrapped in an if to see if the user is
            is already logged in */
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
