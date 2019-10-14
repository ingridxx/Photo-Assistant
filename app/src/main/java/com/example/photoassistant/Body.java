package com.example.photoassistant;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class Body extends Fragment {

    public Body() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        View rootView = inflater.inflate(R.layout.fragment_body, container, false);
        // 1. get a reference to recyclerView
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.body_rv);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        LoadBody(recyclerView);

        return rootView;
    }
    public void LoadBody(RecyclerView recyclerView){

        RecyclerView.Adapter adapter = new RecycleAdapterBody(MainActivity.body_al);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }
}
