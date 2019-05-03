package com.example.wanderdots;

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

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

import WanderDots.Dot;

public class DotListAdapter extends RecyclerView.Adapter<DotListAdapter.ViewHolder> {

    private static final String TAG = "DotListAdapter";
    private static float METERS_TO_MILES = 0.000621371f;

    public ArrayList<Dot> dotList;
    Context ctx;
    Location currentPosition;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    public DotListAdapter(ArrayList<Dot> dotList, Context ctx){
        this.dotList = dotList;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public DotListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dot_list_item, viewGroup, false);
        return new ViewHolder(view, ctx, dotList);
    }

    @Override
    public void onBindViewHolder(@NonNull DotListAdapter.ViewHolder viewHolder, int i) {
        viewHolder.title.setText(dotList.get(i).getName());
        if(currentPosition == null){
            viewHolder.distance.setText("0 mi");
        }else{
            viewHolder.distance.setText(calculateDistance(dotList.get(i), currentPosition));
        }
        viewHolder.rating.setText("5 Stars");
    }

    @Override
    public int getItemCount() {
        return dotList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        View mView;

        public TextView title;
        public TextView distance;
        public TextView rating;
        ArrayList<Dot> dotList;
        Context ctx;

        public ViewHolder(@NonNull View itemView, Context ctx, ArrayList<Dot> dots){
            super(itemView);
            mView = itemView;
            this.ctx = ctx;
            this.dotList = dots;
            itemView.setOnClickListener(this);

            title = mView.findViewById(R.id.dot_item_title);
            distance = mView.findViewById(R.id.dot_item_dist);
            rating = mView.findViewById(R.id.dot_item_rating);

        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(this.ctx, DotDetailActivity.class);
            intent.putExtra("title", title.getText());
            this.ctx.startActivity(intent);
        }
    }

    public void setLocation(Location loc){
        currentPosition = loc;
    }

    /* Calculates distance from dot to devices current location */
    private String calculateDistance(Dot dot, Location loc){
        float result[] = new float[1];
        Location.distanceBetween(dot.getLatitude(), dot.getLongitude(), loc.getLatitude(), loc.getLongitude(), result);
        result[0] = result[0] * METERS_TO_MILES; // Convert meters to miles
        String dis = String.format("%.1f", result[0]); // Format String to one decimal
        return dis + " mi";
    }
}
