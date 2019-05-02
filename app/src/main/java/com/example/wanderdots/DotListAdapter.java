package com.example.wanderdots;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import WanderDots.Dot;

public class DotListAdapter extends RecyclerView.Adapter<DotListAdapter.ViewHolder> {

    public ArrayList<Dot> dotList;
    Context ctx;

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
        viewHolder.distance.setText(new Double(dotList.get(i).getLatitude()).toString());
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
        ArrayList<Dot> dotList ;
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
}
