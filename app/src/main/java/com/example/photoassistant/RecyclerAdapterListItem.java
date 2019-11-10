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

/**
 * This class is a custom version of a RecyclerView adapter which takes in
 * an instance of a list item, and adapts it to display to the user through
 * the RecyclerView. This class uses polymorphism to achieve this as how it
 * displays the objects will be dependent on which object is passed in.
 * A searchView function is also implemented within this class which allows
 * the user to look for their specified item without searching through thousands
 * of items.
 */

public class RecyclerAdapterListItem extends androidx.recyclerview.widget.RecyclerView.Adapter<RecyclerAdapterListItem.ViewHolder> implements Filterable{

    private ArrayList<ListItem> list_item;
    // contains what to display
    private ArrayList<ListItem> copyList;
    //contains a copy of the whole list.
    private Context context;
    private final RecyclerViewOnClickListener listener;
    // interface which handles the on Click Event.


    /**
     * constructor which creates an instance of this class.
     *
     * @param context is the context in which this class was called from.
     * @param init_list_array is the items we need to display in arraylist format
     * @param rvocl is the recyclerViewOnClickListener passed in which we use
     *                to allow a custom onClick for each different object of this class.
     */
    public RecyclerAdapterListItem(Context context, ArrayList<ListItem> init_list_array, RecyclerViewOnClickListener rvocl) {
        this.context = context;
        list_item = init_list_array;
        copyList = new ArrayList<>();
        copyList.addAll(init_list_array);
        listener = rvocl;
    }

    /**
     *
     * @return an object of Filter exampleFilter.
     */
    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    /**
     * Actual filtering of the items for searching purposes.
     */
    boolean containsAll = true;
    private Filter exampleFilter = new Filter() {
        /**
         * filtering using the interface, and constraint to help users perform filtering.
         *
         * @param constraint is the constraint or word you are searching for
         * @return the filtered list of results to publish
         */
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

        /**
         * this sets the arrays to display the results
         *
         * @param constraint was the constraint used in perform filtering
         * @param results is the results from perform filtering occuring.
         */

        protected void publishResults(CharSequence constraint, FilterResults results) {

            list_item.clear();
            list_item.addAll((ArrayList)results.values);
            if (list_item.size()!= copyList.size())
            notifyDataSetChanged();

        }

    };

    /**
     * needed by the RecyclerView.Adapter class
     *
     * @param viewGroup is the viewgroup which holds the context of our itemView to inflate our context.
     * @param i is the position of the viewHolder being inflated
     *
     * @return an inflated instance of our viewHolder to pass to onBindViewHolder
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //creates the view and inflates the layout
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view, viewGroup, false);
        return new ViewHolder(itemView);

    }

    /**
     * This method binds the data from the passed in array to the ui viewholder.
     * it changes depending on if a Lens or a Body is being displayed.
     *
     * @param viewHolder is the infalted viewHolder returned from onCreateViewHolder
     * @param i is the position of the viewHolder being inflated
     */
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {

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

    }

    /**
     *
     * @return the amount of items being displayed
     */
    @Override
    public int getItemCount() {
        return list_item.size();
    }

    /**
     * custom class which is used by recycler view as a blueprint of the ui elements in which
     * we are using to display the data.
     *
     */
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

        /**
         * this method handles the onClick of a particular item.
         *
         * @param item is the item which is passed in from recyclerViewAdapter
         * @param listener is an overriden instance of the RecyclerViewOnClickListener
         */
        public void bind(final ListItem item, final RecyclerViewOnClickListener listener){

            Parent_Layout.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}
