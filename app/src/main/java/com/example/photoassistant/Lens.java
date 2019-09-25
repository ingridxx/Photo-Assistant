package com.example.photoassistant;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class Lens extends Fragment {

    public Lens() {
        // Required empty public constructor
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this


        View rootView = inflater.inflate(R.layout.fragment_lens, container, false);
        // 1. get a reference to recyclerView
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.lens_rv);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        LoadLenses(recyclerView);

        return rootView;


    }

    public void LoadLenses(RecyclerView recyclerView){

        RecyclerView.Adapter adapter = new RecycleAdapterLens(MainActivity.lens_al);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

}
