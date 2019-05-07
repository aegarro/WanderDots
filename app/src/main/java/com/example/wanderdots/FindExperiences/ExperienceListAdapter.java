package com.example.wanderdots.FindExperiences;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.TextView;

import com.example.wanderdots.DotDetailActivity;
import com.example.wanderdots.R;
import com.google.android.gms.location.FusedLocationProviderClient;

import java.util.ArrayList;

import WanderDots.Experience;

public class ExperienceListAdapter<T extends Experience> extends RecyclerView.Adapter<ListItem> {

    private static final String TAG = "ExperienceListAdapter";
    private static float METERS_TO_MILES = 0.000621371f;

    public ArrayList<T> dotList;
    Context ctx;
    Location currentPosition;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    public ExperienceListAdapter(ArrayList<T> dotList, Context ctx){
        this.dotList = dotList;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public ListItem onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dot_list_item, viewGroup, false);
        return new ListItem(view, ctx, dotList);
    }

    @Override
    public void onBindViewHolder(@NonNull ListItem viewHolder, int i) {
        viewHolder.title.setText(dotList.get(i).getName());
        if(currentPosition == null)
            viewHolder.distance.setText("0 mi");
        else
            viewHolder.distance.setText(calculateDistance(dotList.get(i), currentPosition));
        viewHolder.rating.setText("5 Stars");
    }

    @Override
    public int getItemCount() {
        return dotList.size();
    }

    public void setLocation(Location loc){
        currentPosition = loc;
    }

    //Calculates distance between Experience like object and location
    //Returns the String representing the distance followed by the units
    private String calculateDistance(T object, Location loc){
        float result[] = new float[1];
        Location.distanceBetween(object.getLatitude(), object.getLongitude(), loc.getLatitude(), loc.getLongitude(), result);
        result[0] = result[0] * METERS_TO_MILES; // Convert meters to miles
        String dis = String.format("%.1f", result[0]); // Format String to one decimal
        return dis + " mi";
    }
}
