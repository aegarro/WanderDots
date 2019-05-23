package com.example.wanderdots.FindExperiencesActivity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.example.wanderdots.DotDetailActivity;
import com.example.wanderdots.R;
import WanderDots.Experience;

public class ListItem <T extends Experience> extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView title;
    public TextView distance;
    public TextView rating;

    private View mView;
    private Context ctx;

    public ListItem(@NonNull View itemView, Context ctx){
        super(itemView);
        this.mView = itemView;
        this.ctx = ctx;

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
        intent.putExtra("rating", rating.getText());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.ctx.startActivity(intent);
    }
}
