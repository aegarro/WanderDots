package com.example.wanderdots.FindExperiences;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import WanderDots.Experience;
import WanderDots.Server.Get.DataCreator;
import WanderDots.Server.Get.Get;
import WanderDots.Observer ;
/*
Responsibilities:
    - Hold Data (dots or adventures)
    - disable markers when state is inactive, activate when state is active
    - load data
 */

public class State <T extends Experience> implements Observer {

    private ExperienceListAdapter<T> adapter ;
    private ArrayList<T> data ;
    private Get<T> dataGetter ;
    private State nextState ;
    private ArrayList<Marker> markers ;
    private GoogleMap map ;
    private boolean active ;

    public State(Context context, DataCreator<T> creator, boolean isDot){
        this.data = new ArrayList<T>() ;
        this.adapter = new ExperienceListAdapter<>(this.data, context) ;
        this.dataGetter = new Get(context, this, isDot, creator) ;
        this.nextState = null ;
        this.map = null ;
        this.markers = new  ArrayList<Marker>() ;
        this.dataGetter.load() ;
        this.active = false ;
    }

    //Sets this.data, and adds points to map afterwards (done bc setMap returns without data)
    public void dataHasChanged(String message){

        if(dataGetter.hasError())
            Log.d("arodr", "Received error loading data:" + dataGetter.getError()) ;
        else {
            addData(dataGetter.getData()) ;
            if(this.map != null)
                this.addPositionsToMap(this.map);
            this.adapter.notifyDataSetChanged();
        }
    }

    private void addData(ArrayList<T> newData){
        for(T data : newData)
            this.data.add(data) ;
    }

    //Sets up data points on map, if points are loaded
    public void setMap(GoogleMap map){
        this.map = map ;

        if(this.data != null)
            addPositionsToMap(map);
    }

    // Adds positions in data to given map
    private void addPositionsToMap(GoogleMap mMap){
        for(T point : this.data){
            MarkerOptions options = new MarkerOptions();
            options.title(point.getName()) ;
            options.position(new LatLng(point.getLatitude(),
                                        point.getLongitude())) ;
            this.markers.add(mMap.addMarker(options)) ;
        }
        updateMarkersVisibility();
    }

    public void enter(FindExperiencesActivity control){
        this.active = true ;
        updateMarkersVisibility();
        control.setListAdapter(this.adapter);
    }

    public void transitionToNextState(FindExperiencesActivity control){
        this.active = false ;
        updateMarkersVisibility();
        control.setState(this.nextState) ;
    }

    private void updateMarkersVisibility(){
        for(Marker marker:this.markers)
            marker.setVisible(this.active);
    }

    public void transitionToNextState(State nextState){
        this.nextState = nextState ;
    }

    public void setAdapterLocation(Location location){
        this.adapter.setLocation(location) ;
        this.adapter.notifyDataSetChanged();
    }

    public State getNextState(){
        return this.nextState ;
    }
}
