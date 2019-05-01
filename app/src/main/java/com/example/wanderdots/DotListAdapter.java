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
        this.ctx = ctx;
        return new ViewHolder(view, ctx, dotList);
    }

    @Override
    public void onBindViewHolder(@NonNull DotListAdapter.ViewHolder viewHolder, int i) {
        viewHolder.title.setText(dotList.get(i).getTitle());
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
        ArrayList<Dot> dotList = new ArrayList<Dot>();
        Context ctx;

        public ViewHolder(@NonNull View itemView, Context ctx, ArrayList<Dot> dots){
            super(itemView);
            mView = itemView;
            this.ctx = ctx;
            this.dotList = dots;
            itemView.setOnClickListener(this);

            title = (TextView) mView.findViewById(R.id.dot_item_title);
            distance = (TextView) mView.findViewById(R.id.dot_item_dist);
            rating = (TextView) mView.findViewById(R.id.dot_item_rating);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(this.ctx, DotDetailActivity.class);
            this.ctx.startActivity(intent);
        }
    }
}
