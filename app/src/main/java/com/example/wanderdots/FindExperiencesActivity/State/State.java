package com.example.wanderdots.FindExperiencesActivity.State;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.example.wanderdots.FindExperiencesActivity.ExperienceListAdapter;
import com.example.wanderdots.FindExperiencesActivity.FindExperiencesActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import WanderDots.Experience;
import WanderDots.Observer ;
/*
Responsibilities:
    - Hold Data (dots or adventures)
    - disable markers when state is inactive, activate when state is active
    - loadData data
 */

public abstract class State <T extends Experience> implements Observer {

    private ArrayList<Observer> observers ;

    protected ExperienceListAdapter<T> adapter ;
    protected ArrayList<T> data ;
    protected State nextState ;
    protected ArrayList<Marker> markers ;
    protected GoogleMap map ;
    protected boolean active ; //whether both map and experiences are loaded

    public State(Context context){
        this.data = new ArrayList<>() ;
        this.observers = new ArrayList<Observer>() ;
        this.adapter = new ExperienceListAdapter<>(this.data, context) ;
        this.nextState = null ;
        this.map = null ;
        this.markers = new ArrayList<>() ;
        this.active = false ;
    }

    //Sets up data points on map, if points are loaded
    public void setMap(GoogleMap map){
        this.map = map ;
        if(this.data != null)
            addPositionsToMap(map);
        notifyObservers() ;
    }

    public void enter(){
        this.active = true ;
        updateMarkersVisibility();
    }

    public void exit(){
        this.active = false ;
        updateMarkersVisibility();
    }

    public void setNextState(State nextState){
        this.nextState = nextState ;
    }

    public void setAdapterLocation(Location location){
        this.adapter.setLocation(location) ;
        this.adapter.notifyDataSetChanged();
    }

    public ExperienceListAdapter<T> getAdapter(){
        return this.adapter ;
    }

    public State getNextState(){
        return this.nextState ;
    }

    public void addObserver(Observer ob){
        observers.add(ob) ;
    }

    public void notifyObservers(){
        for(Observer ob: this.observers)
            ob.subscriberHasChanged("update");
    }

    protected void setData(ArrayList<T> newData){
        this.data.clear();
        for(T data : newData)
            this.data.add(data) ;
        this.adapter.notifyDataSetChanged();
        if(this.map != null)
            this.addPositionsToMap(this.map);
        notifyObservers() ;
    }

    // Adds positions in data to given map
    private void addPositionsToMap(GoogleMap mMap){
        this.markers.clear() ;
        for(T point : this.data){
            MarkerOptions options = new MarkerOptions();
            options.title(point.getName()) ;
            options.position(new LatLng(point.getLatitude(),
                    point.getLongitude())) ;
            this.markers.add(mMap.addMarker(options)) ;
        }
        updateMarkersVisibility();
    }

    private void updateMarkersVisibility(){
        for(int i=0; i<this.markers.size(); i++){
            Marker marker = this.markers.get(i) ;
            marker.setVisible(this.active);
        }
    }

    public ArrayList<Marker> getMarkers(){
        return this.markers ;
    }

    abstract public void subscriberHasChanged(String message) ;
}
