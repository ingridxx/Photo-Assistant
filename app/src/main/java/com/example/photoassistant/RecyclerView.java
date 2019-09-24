package com.example.photoassistant;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class RecyclerView extends androidx.recyclerview.widget.RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<String> a_books;

    public RecyclerView(ArrayList<String> init_book_array) {
        a_books = init_book_array;
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
        //binds the data to the view.
        viewHolder.Title.setText(a_books.get(i));
        viewHolder.Author.setText(a_books.get(i));
        viewHolder.Date.setText(a_books.get(i));

    }

    @Override
    public int getItemCount() {
        return a_books.size();
    }

    public class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder{
        TextView Title;
        TextView Author;
        TextView Date;
        LinearLayout Parent_Layout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Title = itemView.findViewById(R.id.titleLabel);
            Author = itemView.findViewById(R.id.authorLabel);
            Date = itemView.findViewById(R.id.dateLabel);
            Parent_Layout = itemView.findViewById(R.id.parent_layout);
        }
    }
}