package com.example.photoassistant;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class RecyclerAdapterListItem extends androidx.recyclerview.widget.RecyclerView.Adapter<RecyclerAdapterListItem.ViewHolder> implements Filterable{

    private ArrayList<ListItem> list_item;
    private ArrayList<ListItem> copyList;
    private Context context;
    private ListItem currentBody;
    private final RecyclerViewOnClickListener listener;


    public RecyclerAdapterListItem(Context context, ArrayList<ListItem> init_list_array, RecyclerViewOnClickListener rvocl) {
        this.context = context;
        list_item = init_list_array;
        copyList = new ArrayList<>();
        copyList.addAll(init_list_array);
        listener = rvocl;
    }

//    public RecyclerAdapterListItem(Context context, ArrayList<ListItem> init_list_array,ListItem currentBody) {
//        this.context = context;
//        handleFilter(init_list_array,currentBody);
//
//    }

//    private void handleFilter(ArrayList<ListItem> init_list_array,ListItem filtered){
//
//        ArrayList<ListItem> combinationArray = new ArrayList<>();
//
//
//    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    boolean containsAll = true;
    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            ArrayList<ListItem> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {

                filteredList.addAll(copyList);

            } else {
                String filtered = constraint.toString().toLowerCase().trim();
                String[] filterIndividualWords = filtered.split(",");

                for (ListItem list_item : copyList) {
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
            if (list_item.size()!= copyList.size())
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
        viewHolder.bind(list_item.get(i), listener);

        viewHolder.TitleLine.setText(list_item.get(i).TopLineText().trim());

        if (list_item.get(i).MiddleLineText().trim() == ""){
            viewHolder.TitleLine.setPadding(0,20,0,0);
        }else {
            viewHolder.DescriptionLine.setPadding(0,10,0,0);
            viewHolder.TitleLine.setGravity(Gravity.START);
        }
        viewHolder.DescriptionLine.setText(list_item.get(i).MiddleLineText().trim());
        viewHolder.BottomLine.setText(list_item.get(i).BottomLineText().trim());

//        viewHolder.Parent_Layout.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//
//            int adapterPos = viewHolder.getAdapterPosition();
//            currentBody = list_item.get(adapterPos);
//            TextView tv = v.getRootView().findViewById(R.id.tv_selected_slot);
//            tv.setText(currentBody.getPartName());
//
//            }
//        });
//        viewHolder.TitleLine.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//            int adapterPos = viewHolder.getAdapterPosition();
//            currentBody = list_item.get(adapterPos);
//
//            TextView tv = v.getRootView().findViewById(R.id.tv_selected_slot);
//            tv.setText(currentBody.getPartName());
//
//            }
//        });

    }


    @Override
    public int getItemCount() {
        return list_item.size();
    }


    public class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder{
        TextView TitleLine;
        TextView DescriptionLine;
        TextView BottomLine; // of the cardview layout
        RelativeLayout Parent_Layout;
        View click_view;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            TitleLine = itemView.findViewById(R.id.bodyTitleLabel);
            DescriptionLine = itemView.findViewById(R.id.bodyHintLabel);
            BottomLine = itemView.findViewById(R.id.bodyCropFactorTextView);
            Parent_Layout = itemView.findViewById(R.id.relative_layout_cardview);
            click_view = itemView.findViewById(R.id.click_view);
        }

        public void bind(final ListItem item, final RecyclerViewOnClickListener listener){

//            TitleLine.setOnClickListener(new View.OnClickListener() {
//                @Override public void onClick(View v) {
//                    listener.onItemClick(item);
//                }
//            });

            Parent_Layout.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override public void onClick(View v) {
//                    listener.onItemClick(item);
//                }
//            });
        }
    }

}
