package com.example.photoassistant;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


public class Calculator extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    Button aperturePlusButton, shutterSpeedPlusButton, isoPlusButton, zoomPlusButton;
    Button apertureMinusButton, shutterSpeedMinusButton, isoMinusButton, zoomMinusButton;
    TextView apertureTV, shutterSpeedTV, isoTV, zoomTV;

    Activity activity;
    public Calculator() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
    //View myLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_calculator, container, false);
        Bundle bundle = getArguments();
        aperturePlusButton = rootView.findViewById(R.id.aperturePlusButton);
        shutterSpeedPlusButton = rootView.findViewById(R.id.shutterSpeedPlusButton);
        zoomPlusButton = rootView.findViewById(R.id.zoomPlusButton);
        isoPlusButton = rootView.findViewById(R.id.isoPlusButton);
        apertureMinusButton= rootView.findViewById(R.id.apertureMinusButton);
        shutterSpeedMinusButton = rootView.findViewById(R.id.shutterSpeedMinusButton);
        zoomMinusButton = rootView.findViewById(R.id.zoomMinusButton);
        isoMinusButton = rootView.findViewById(R.id.isoMinusButton);
        apertureTV = rootView.findViewById(R.id.apertureTV);
        shutterSpeedTV = rootView.findViewById(R.id.shutterSpeedTV);
        zoomTV = rootView.findViewById(R.id.zoomTV);
        isoTV = rootView.findViewById(R.id.isoTV);
        Intelligence.Current.setBody(MainActivity.body_al.get(1));
        Intelligence.Current.setLens(MainActivity.lens_al.get(1));

        aperturePlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intelligence.Current.aperturePlus();
                apertureTV.setText(Double.toString(Intelligence.Current.getAperture()));
            }
        });
        return rootView;
    }
}