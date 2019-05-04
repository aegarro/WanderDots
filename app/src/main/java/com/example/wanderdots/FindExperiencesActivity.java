package com.example.wanderdots;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.android.volley.Response;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

import WanderDots.Dot;
import WanderDots.Server.Get.GetDots;

public class FindExperiencesActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener, Response.Listener<ArrayList<Dot>> {

    private static final String TAG = "MainActivity";
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

    private GetDots getDots ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_view);

        /* Connect GUI elements with code here */
        newDotBtn = (FloatingActionButton) findViewById(R.id.new_dot_btn);
        main_list = (RecyclerView) findViewById(R.id.main_recycler_view);

        getLocationPermission();

        this.dotList = new ArrayList<Dot>() ;
        dotListAdapter = new DotListAdapter(this.dotList, this);
        dotListAdapter.notifyDataSetChanged();

        main_list.setHasFixedSize(true);
        main_list.setLayoutManager(new LinearLayoutManager(this));
        main_list.setAdapter(dotListAdapter);
        dotListAdapter.notifyDataSetChanged();

        newDotBtn.setOnClickListener(this) ;
        getDots = new GetDots(this.getApplicationContext(), this) ;
        getDots.loadDots();
    }

    public void onResponse(ArrayList<Dot> dots){
        updateDots(dots) ;
        this.dotListAdapter.notifyDataSetChanged();
        this.addDotsToMap();
        System.out.println(dotList.size()) ;
    }

    //Replaces dots in current list with new dots
    private void updateDots(ArrayList<Dot> newDots){
        for(Dot dot : this.dotList)
            this.dotList.remove(dot) ;
        for(Dot dot : newDots)
            this.dotList.add(dot) ;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this.getApplicationContext(), NewDotActivity.class);
        startActivity(intent);
    }

    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }

    private void addDotsToMap(){
        for(Dot dot : dotList){
            MarkerOptions options = new MarkerOptions()
                    .title(dot.getName())
                    .position(new LatLng(dot.getLatitude(), dot.getLongitude()));

            mMap.addMarker(options);
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
        mapFragment.getMapAsync(this);
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
                            dotListAdapter.setLocation(currentLocation);
                            dotListAdapter.notifyDataSetChanged();
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
}

