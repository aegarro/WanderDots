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

    public TextView title;
    public TextView distance;
    public TextView rating;
    private Experience experience ;

    private View mView;
    private Context ctx;

    public ListItem(@NonNull View itemView, Context ctx, Experience experience){
        super(itemView);
        this.mView = itemView;
        this.ctx = ctx;
        this.experience = experience ;

        Log.d("arodr", "experience: " + experience.toString());

        itemView.setOnClickListener(this);
        title = mView.findViewById(R.id.dot_item_title);
        distance = mView.findViewById(R.id.dot_item_dist);
        rating = mView.findViewById(R.id.dot_item_rating);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this.ctx, DotDetailActivity.class);
        intent.putExtra("title", title.getText());
        intent.putExtra("distance", distance.getText());
        intent.putExtra("latitude", String.format("%f", experience.getLatitude()));
        intent.putExtra("longitude", String.format("%f", experience.getLongitude()));
        intent.putExtra("rating", rating.getText());
        String imageID = experience.getPictureIds().get(0) ;
        Log.d("arodr:ListItem", "sending picture id " + imageID) ;
        intent.putExtra("pictureID", imageID) ;
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.ctx.startActivity(intent);
    }
}
