package com.example.wanderdots.find;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.example.wanderdots.DotDetailActivity;
import com.example.wanderdots.R;
import wanderdots.Experience;

public class ListItem extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView titleView;
    private TextView distanceView;
    private TextView ratingsView;
    private Experience experience ;

    private View mView;
    private Context ctx;

    public ListItem(@NonNull View itemView, Context ctx, Experience experience){
        super(itemView);
        this.mView = itemView;
        this.ctx = ctx;
        this.experience = experience ;

        itemView.setOnClickListener(this);
        titleView = mView.findViewById(R.id.dot_item_title);
        distanceView = mView.findViewById(R.id.dot_item_dist);
        ratingsView = mView.findViewById(R.id.dot_item_rating);
    }

    public TextView getTitleView() {
        return titleView;
    }

    public TextView getDistanceView() {
        return distanceView;
    }

    public TextView getRatingsView() {
        return ratingsView;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this.ctx, DotDetailActivity.class);

        Double distance = Math.random() * 5 ;

        Log.d("arodr", "distanceView generated: " + distance) ;

        intent.putExtra("titleView", experience.getName());
        intent.putExtra("description", experience.getDescription()) ;
        intent.putExtra("latitude", String.format("%f", experience.getLatitude()));
        intent.putExtra("longitude", String.format("%f", experience.getLongitude()));
        intent.putExtra("distanceView", String.format("%2f", distance)) ;
        intent.putExtra("ratingsView", ratingsView.getText()); //Note, this is grabbing it from the text on the list item

        if(experience.getPictureIds().size() > 0){
            String imageID = experience.getPictureIds().get(0) ;
            intent.putExtra("pictureID", imageID) ;
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.ctx.startActivity(intent);
    }
}
