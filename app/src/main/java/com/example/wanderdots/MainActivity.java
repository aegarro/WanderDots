package com.example.wanderdots;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton newDotBtn;
    private RecyclerView main_list;

    private ArrayList<Dot> dotList;
    private DotListAdapter dotListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Connect GUI elements with code here */
        newDotBtn = (FloatingActionButton) findViewById(R.id.new_dot_btn);
        main_list = (RecyclerView) findViewById(R.id.main_recycler_view);
        dotList = new ArrayList<Dot>();
        dotListAdapter = new DotListAdapter(dotList, MainActivity.this);

        main_list.setHasFixedSize(true);
        main_list.setLayoutManager(new LinearLayoutManager(this));
        main_list.setAdapter(dotListAdapter);


        dotList.add(new Dot(32, "Avila Caves", "Cool Caves", 33.1, 127.6, 36));
        dotList.add(new Dot(32, "Bishops Peak", "Cool Caves", 3.2, 127.6, 36));
        dotList.add(new Dot(32, "Terrace Hill", "Cool Caves", 66.4, 127.6, 36));
        dotList.add(new Dot(32, "Morro Bay", "Cool Caves", 44.2, 127.6, 36));
        dotList.add(new Dot(32, "Cayucos", "Cool Caves", 1.0, 127.6, 36));
        dotList.add(new Dot(32, "Cuesta Ridge", "Cool Caves", 0.5, 127.6, 36));
        dotListAdapter.notifyDataSetChanged();


        newDotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewDotActivity.class);
                startActivity(intent);
            }
        });


        /* TODO: this needs to be wrapped in an if to see if the user is
            is already logged in */
        /*Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);*/
    }
}
