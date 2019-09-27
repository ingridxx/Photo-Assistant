package com.example.photoassistant;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class RecycleAdapterBody extends androidx.recyclerview.widget.RecyclerView.Adapter<RecycleAdapterBody.ViewHolder> {

    ArrayList<ListItemBody> list_item;

    public RecycleAdapterBody(ArrayList<ListItemBody> init_list_array) {
        list_item = init_list_array;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //creates the view and inflates the layout
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view_body, viewGroup, false);
        return new ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        //binds the data to the view.
        viewHolder.Title.setText(list_item.get(i).getPartName());
        viewHolder.CropFactor.setText(String.format("%.1f", list_item.get(i).getCropFactor()));
        //viewHolder.Author.setText("Aperture : " + list_item.get(i).getAperture() + " ISO : " + list_item.get(i).getIso());
        //viewHolder.Date.setText("Shutter Speed + " + list_item.get(i).getShutterSpeed());

    }

    @Override
    public int getItemCount() {
        return list_item.size();
    }

    public class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder{
        TextView Title;
        TextView CropFactor;
        //TextView Author;
        //TextView Date;
        LinearLayout Parent_Layout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Title = itemView.findViewById(R.id.bodyTitleLabel);
            CropFactor = itemView.findViewById(R.id.bodyCropFactorTextView);
            //Author = itemView.findViewById(R.id.authorLabel);
            //Date = itemView.findViewById(R.id.dateLabel);
            Parent_Layout = itemView.findViewById(R.id.parent_layout_body);
        }
    }
}