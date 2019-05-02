package com.example.wanderdots;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabItem;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import WanderDots.Dot;

public class MainActivity extends AppCompatActivity implements Response.ErrorListener, Response.Listener<String>, OnMapReadyCallback {

    private static final String TAG = "MainActivty";
    private static final float DEFAULT_ZOOM = 11f;

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    private FloatingActionButton newDotBtn;
    private RecyclerView main_list;
    private GoogleMap mMap;

    private ArrayList<Dot> dotList ;
    private DotListAdapter dotListAdapter;

    /* Whether or not user has pemitted us to access the devices location */
    private Boolean mLocationPermissionGranted = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Connect GUI elements with code here */
        newDotBtn = (FloatingActionButton) findViewById(R.id.new_dot_btn);
        main_list = (RecyclerView) findViewById(R.id.main_recycler_view);

        getLocationPermission();

        this.dotList = new ArrayList<Dot>() ;
        dotListAdapter = new DotListAdapter(this.dotList, MainActivity.this);
        loadDotsFromServer() ;
        dotList.add(new Dot("Terrace Hill", 35.273169, -120.650794));
        dotList.add(new Dot("The P", 35.302792, -120.651527));
        dotList.add(new Dot("Bishop's Peak", 35.301284, -120.697450));
        dotList.add(new Dot("Pirate's Cove", 35.174447, -120.717077));
        dotList.add(new Dot("Spooner's Cove", 35.273905, -120.888717));

        dotListAdapter.notifyDataSetChanged();

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

    private void addDotsToMap(){
        for(Dot dot : dotList){
            MarkerOptions options = new MarkerOptions()
                    .title(dot.getName())
                    .position(new LatLng(dot.getLatitude(), dot.getLongitude()));

            Marker mark = mMap.addMarker(options);
        }
    }

    private void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(), COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void initMap(){
        Log.d(TAG, "intMap: intializing Map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.the_map);
        mapFragment.getMapAsync(MainActivity.this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;

        switch (requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionGranted = false;
                            Log.d(TAG, "onRequestPermissionResult: permissions failed");
                            return;
                        }
                    }
                    mLocationPermissionGranted = true;
                    Log.d(TAG, "onRequestPermissionResult: permissions granted");
                    initMap();
                }
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapRead: Map is ready");
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        if (mLocationPermissionGranted){
            getDeviceLocation();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
        }
        addDotsToMap();
    }

    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: getting devices location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try{
            if(mLocationPermissionGranted){
                final Task location = mFusedLocationProviderClient.getLastLocation();
                Task task = location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()){
                            Log.d(TAG, "onComplete: found location");
                            Location currentLocation = (Location) task.getResult();
                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM);
                        } else{
                            Log.d(TAG, "onComplete: current location is null");
                        }
                    }
                });
            }
        } catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: Security Exception: " + e.getMessage());
        }
    }

    private void moveCamera(LatLng latlng, float zoom){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoom));
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
