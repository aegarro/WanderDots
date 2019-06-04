package com.example.wanderdots.find;

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
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.wanderdots.find.state.State;
import com.example.wanderdots.find.state.AdventureState;
import com.example.wanderdots.find.state.DotState;
import com.example.wanderdots.newdot.NewDotActivity;
import com.example.wanderdots.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import wanderdots.Adventure;
import wanderdots.Dot;

public class FindExperiencesActivity extends AppCompatActivity implements OnMapReadyCallback,
        View.OnClickListener, CompoundButton.OnCheckedChangeListener, OnCompleteListener {

    private static final int CREATE_DOT_ACTIVITY_ID = 69 ;
    private static final String TAG = "MainActivity";
    private static final float DEFAULT_ZOOM = 11f;

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    private RecyclerView listContainer;
    private GoogleMap mMap;
    private ToggleButton toggle;

    private Boolean mLocationPermissionGranted = false ; // Whether user has permitted us to access the devices location

    private State state ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        FloatingActionButton newDotButton;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_view);
        validateLocationPermission();

        this.toggle = findViewById(R.id.listToggleButton);

        this.toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    // Toggle was changed
                    Toast.makeText(FindExperiencesActivity.this, "Adventure Coming Soon!", Toast.LENGTH_SHORT)
                            .show();
                    toggle.setChecked(true);
                }
            }
        });

        this.listContainer = (RecyclerView) findViewById(R.id.main_recycler_view);
        this.listContainer.setHasFixedSize(true);
        this.listContainer.setLayoutManager(new LinearLayoutManager(this));

        //SET CREATEBUTTON HANDLER
        newDotButton = (FloatingActionButton) findViewById(R.id.new_dot_btn);
        newDotButton.setOnClickListener(this) ;

        //STATE
        Context context = getApplicationContext() ;
        DotState dotState = new DotState(context) ;
        AdventureState adventureState = new AdventureState(context) ;

        dotState.setNextState(adventureState);
        adventureState.setNextState(dotState) ;

        //Setting State on Startup
        this.listContainer.setAdapter(dotState.getAdapter());
        this.state = dotState ;
        dotState.enter() ;
    }

    //Runs whenever SwitchButton changes State and switches the list in view
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        this.setState(this.state.getNextState());
    }

    private void setState(State newState){
        this.state.exit() ;
        this.state = newState ;
        this.state.enter() ;
        listContainer.swapAdapter(newState.getAdapter(), false);
    }

    //Runs whenever the NEWDOTBUTTON has been clicked, begins NewDotActivity
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this.getApplicationContext(), NewDotActivity.class);
        startActivityForResult(intent, CREATE_DOT_ACTIVITY_ID);
    }

    @Override
    //Runs after CreateDotActivity
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CREATE_DOT_ACTIVITY_ID && resultCode == RESULT_OK) {
            Dot.reload(); //checks for to include new Dot or Adventures
            Adventure.reload();
            return ;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults.length > 0)
        {
                if (helperFunc(grantResults))
                    return;

                mLocationPermissionGranted = true;
                Log.d(TAG, "onRequestPermissionResult: permissions granted");
                startMapInitialization();
        }
    }

    public boolean helperFunc(@NonNull int[] grantResults)
    {
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = false;
                Log.d(TAG, "onRequestPermissionResult: permissions failed");
                return true;
            }
        }
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapRead: Map is ready");
        this.mMap = googleMap;
        this.mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        UiSettings mapUiSettings = this.mMap.getUiSettings();
        mapUiSettings.setZoomControlsEnabled(true);

        if (mLocationPermissionGranted){
            getDeviceLocation();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            this.mMap.setMyLocationEnabled(true);
        }
        this.state.setMap(mMap) ;
        this.state.getNextState().setMap(mMap) ;
    }

    private void validateLocationPermission() {
        Log.d(TAG, "validateLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(), COARSE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
                startMapInitialization();
            } else
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        } else
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
    }

    //Tells Maps API to start constructing the map, calling onMapReadyCallback when done
    private void startMapInitialization(){
        Log.d(TAG, "intMap: intializing Map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.the_map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onComplete(@NonNull Task task) {
        if (task.isSuccessful()){
            Log.d(TAG, "onComplete: found location");
            Location currentLocation = (Location) task.getResult();
            this.state.setAdapterLocation(currentLocation) ;
            this.state.getNextState().setAdapterLocation(currentLocation); //works if only two states
            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM);
        } else
            Log.d(TAG, "onComplete: current location is null");
    }

    private void getDeviceLocation(){
        FusedLocationProviderClient mFusedLocationProviderClient;
        Log.d(TAG, "getDeviceLocation: getting devices location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try{
            if(mLocationPermissionGranted){
                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(this);
            }
        } catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: Security Exception: " + e.getMessage());
        }
    }

    private void moveCamera(LatLng latlng, float zoom){
        this.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoom));
    }
}

