package com.example.photoassistant;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class RecycleAdapterLens extends androidx.recyclerview.widget.RecyclerView.Adapter<RecycleAdapterLens.ViewHolder> {

    ArrayList<ListItemLens> list_item;

    public RecycleAdapterLens(ArrayList<ListItemLens> init_list_array) {
        list_item = init_list_array;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //creates the view and inflates the layout
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view, viewGroup, false);
        return new ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        //binds the data to the view.*
        viewHolder.topLine.setText(list_item.get(i).getPartName());
        //viewHolder.middleLine.setText("min : " + list_item.get(i).getMinZoom() + "max : " +  list_item.get(i).getMaxZoom());
        //viewHolder.bottomLine.setText("Lens placeholder");

    }

    @Override
    public int getItemCount() {
        return list_item.size();
    }

    public class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder{
        TextView topLine;
        //TextView middleLine;
        //TextView bottomLine; // of the cardview layout
        LinearLayout Parent_Layout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            topLine = itemView.findViewById(R.id.titleLabel);
            //middleLine = itemView.findViewById(R.id.authorLabel);
            //bottomLine = itemView.findViewById(R.id.dateLabel);
            Parent_Layout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
