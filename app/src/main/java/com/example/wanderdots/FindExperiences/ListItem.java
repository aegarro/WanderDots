package com.example.wanderdots.FindExperiences;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.wanderdots.DotDetailActivity;
import com.example.wanderdots.R;

import java.util.ArrayList;

import WanderDots.Experience;

public class ListItem <T extends Experience> extends RecyclerView.ViewHolder implements View.OnClickListener {

    View mView;

    public TextView title;
    public TextView distance;
    public TextView rating;
    ArrayList<T> dotList;
    Context ctx;

    public ListItem(@NonNull View itemView, Context ctx, ArrayList<T> dots){
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
        Log.d("arodr", this.ctx.toString()) ;
        Log.d("arodr", title.getText().toString()) ;
        Log.d("arodr", DotDetailActivity.class.toString()) ;
        Intent intent = new Intent(this.ctx, DotDetailActivity.class);
        intent.putExtra("title", title.getText());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.ctx.startActivity(intent);
    }
}
