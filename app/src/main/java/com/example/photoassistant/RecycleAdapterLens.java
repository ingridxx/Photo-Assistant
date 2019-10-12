package com.example.photoassistant;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecycleAdapterLens extends androidx.recyclerview.widget.RecyclerView.Adapter<RecycleAdapterLens.ViewHolder> implements Filterable {

    ArrayList<ListItemLens> list_item;
    ArrayList<ListItemLens> copyList;
    Context context;

    public RecycleAdapterLens(Context context,ArrayList<ListItemLens> init_list_array) {
        this.context = context;
        list_item = init_list_array;
        copyList = new ArrayList<ListItemLens>();
        copyList.addAll(init_list_array);

    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }
    boolean containsAll = true;
    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            ArrayList<ListItemLens> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {

                filteredList.addAll(copyList);

            } else {
                String filtered = constraint.toString().toLowerCase().trim();
                List<String> filterIndividualWords = Arrays.asList(filtered.split(" "));

                for (ListItemLens list_item : copyList) {
                    containsAll = true;
                    for (String word:filterIndividualWords) {
                        if (!list_item.getPartName().toLowerCase().contains(word)) {
                            containsAll = false;
                            break;
                        }
                    }
                    if(containsAll)filteredList.add(list_item);
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        protected void publishResults(CharSequence constraint, FilterResults results) {

            list_item.clear();
            list_item.addAll((ArrayList)results.values);
            notifyDataSetChanged();

        }
    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //creates the view and inflates the layout
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view, viewGroup, false);
        return new ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        //binds the data to the view.*
        viewHolder.topLine.setText(list_item.get(i).getPartName());
        //viewHolder.middleLine.setText("min : " + list_item.get(i).getMinZoom() + "max : " +  list_item.get(i).getMaxZoom());
        //viewHolder.bottomLine.setText("Lens placeholder");

        viewHolder.topLine.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int adapterPos = viewHolder.getAdapterPosition();

                Lens.addLensToArrays(list_item.get(adapterPos));
                Lens.updateArrayAdapter();
            }
        });

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
