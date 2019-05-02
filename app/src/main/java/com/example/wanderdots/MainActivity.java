package com.example.wanderdots;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import WanderDots.Dot;

public class MainActivity extends AppCompatActivity implements Response.ErrorListener, Response.Listener<String> {

    private FloatingActionButton newDotBtn;
    private RecyclerView main_list;

    private ArrayList<Dot> dotList ;
    private DotListAdapter dotListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Connect GUI elements with code here */
        newDotBtn = (FloatingActionButton) findViewById(R.id.new_dot_btn);
        main_list = (RecyclerView) findViewById(R.id.main_recycler_view);
        this.dotList = new ArrayList<Dot>() ;
        loadDotsFromServer() ;
        dotListAdapter = new DotListAdapter(this.dotList, MainActivity.this);

        main_list.setHasFixedSize(true);
        main_list.setLayoutManager(new LinearLayoutManager(this));
        main_list.setAdapter(dotListAdapter);
        dotListAdapter.notifyDataSetChanged();

        newDotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewDotActivity.class);
                startActivity(intent);
            }
        });

        /*
            TODO: this needs to be wrapped in an if to see if the user is is already logged in
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
         */
    }

    private void loadDotsFromServer(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://10.0.2.2:5000/api/get/dots";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, this, this);
        queue.add(stringRequest);
    }

    public void onResponse(String response) {
        try {
            JSONArray dots = new JSONObject(response).getJSONArray("dots") ;
            for(int i=0; i<dots.length(); i++){
                this.dotList.add(new Dot(dots.getJSONObject(i))) ;
            }
            System.out.println(dotList.size()) ;
            this.dotListAdapter.notifyDataSetChanged();
        } catch(org.json.JSONException e){
            Log.e("Server Error:", e.toString());
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e("HTTPS Request Error", error.toString()) ;
    }
}
