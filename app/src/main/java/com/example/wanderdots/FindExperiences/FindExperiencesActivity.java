package com.example.wanderdots.FindExperiences;

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
import android.widget.ToggleButton;

import com.example.wanderdots.NewDotActivity;
import com.example.wanderdots.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import WanderDots.Adventure;
import WanderDots.Dot;
import WanderDots.Server.Get.DataCreator;

public class FindExperiencesActivity extends AppCompatActivity implements OnMapReadyCallback,
        View.OnClickListener, CompoundButton.OnCheckedChangeListener, OnCompleteListener {

    private static final String TAG = "MainActivity";
    private static final float DEFAULT_ZOOM = 11f;

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    private FloatingActionButton newDotButton;
    private RecyclerView listContainer;
    private GoogleMap mMap;

    private Boolean mLocationPermissionGranted = false ; // Whether user has permitted us to access the devices location
    private FusedLocationProviderClient mFusedLocationProviderClient;

    private State state ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_view);
        validateLocationPermission();

        //SET CREATEBUTTON HANDLER
        this.newDotButton = (FloatingActionButton) findViewById(R.id.new_dot_btn);
        this.newDotButton.setOnClickListener(this) ;

        //SET TOGGLE BUTTON HANDLER
        ToggleButton toggle = (ToggleButton) findViewById(R.id.listToggleButton) ;
        toggle.setOnCheckedChangeListener(this)  ;

        //STATE
        Context context = getApplicationContext() ;
        DataCreator<Dot> dotCreator = new DataCreator<Dot>("dot", Dot.class);
        State dotState = new State<Dot>(context, dotCreator, true) ;

        DataCreator<Adventure> adventureCreator = new DataCreator<Adventure>("adventure", Adventure.class);
        State adventureState = new State<Adventure>(context, adventureCreator, false) ;

        dotState.transitionToNextState(adventureState);
        adventureState.transitionToNextState(dotState) ;

        //MAIN LIST ADAPTER
        this.listContainer = (RecyclerView) findViewById(R.id.main_recycler_view);
        this.listContainer.setHasFixedSize(true);
        this.listContainer.setLayoutManager(new LinearLayoutManager(this));

        setState(dotState) ; //this must be last statement
    }

    //Runs whenever SwitchButton changes state and switches the list in view
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        this.state.transitionToNextState(this) ;
    }

    //Runs whenever the NEWDOTBUTTON has been clicked, begins NewDotActivity
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this.getApplicationContext(), NewDotActivity.class);
        startActivity(intent);
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
                    startMapInitialization();
                }
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapRead: Map is ready");
        this.mMap = googleMap;
        this.mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

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

    public void setState(State state){
        this.state = state ;
        this.state.enter(this) ;
    }

    public void setListAdapter(ExperienceListAdapter adapter){ //public scope
        setAdapterOnFocus(adapter); //this has private scope
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

    //Given adapter becomes the list that is displayed in listContainer
    private void setAdapterOnFocus(RecyclerView.Adapter<ExperienceListAdapter.ViewHolder> newAdapter){
        listContainer.setAdapter(newAdapter);
        newAdapter.notifyDataSetChanged();
    }

    @Override
    public void onComplete(@NonNull Task task) {
        if (task.isSuccessful()){
            Log.d(TAG, "onComplete: found location");
            Location currentLocation = (Location) task.getResult();
            state.setAdapterLocation(currentLocation) ;
            state.getNextState().setAdapterLocation(currentLocation); //works if only two states
            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM);
        } else{
            Log.d(TAG, "onComplete: current location is null");
        }
    }

    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: getting devices location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try{
            if(mLocationPermissionGranted){
                final Task location = mFusedLocationProviderClient.getLastLocation();
                Task task = location.addOnCompleteListener(this);
            }
        } catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: Security Exception: " + e.getMessage());
        }
    }

    private void moveCamera(LatLng latlng, float zoom){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoom));
    }
}

