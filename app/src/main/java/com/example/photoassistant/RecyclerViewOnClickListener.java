package com.example.photoassistant;

/**
 * this interface is used to help recyclerViewAdapter make dynamic
 * decision on how onClick is handled by passing an instance of this
 * into the constructor.
 */
public interface RecyclerViewOnClickListener {

    void onItemClick(ListItem item);

}
