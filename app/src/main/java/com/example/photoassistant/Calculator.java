package com.example.photoassistant;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class Calculator extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    FloatingActionButton fab;
    private Button aperturePlusButton, shutterSpeedPlusButton, isoPlusButton, zoomPlusButton;
    Button apertureMinusButton, shutterSpeedMinusButton, isoMinusButton, zoomMinusButton;
    TextView apertureTV, shutterSpeedTV, isoTV, zoomTV;
    Button bodyButton, lensButton;
    TextView evTextView;

    Activity activity;
    public Calculator() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }
    //View myLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_calculator, container, false);
        fab = rootView.findViewById(R.id.floatingActionButton);

        return rootView;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //hide notification bar
        View tempView = getActivity().getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE;
        tempView.setSystemUiVisibility(uiOptions);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("myDebugTag", "onClickOfFAB:");
                getActivity().onBackPressed();
            }
        });
        //force landscape
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //bind ui elements to java objects
        aperturePlusButton = view.findViewById(R.id.aperturePlusButton);
        shutterSpeedPlusButton = view.findViewById(R.id.shutterSpeedPlusButton);
        zoomPlusButton = view.findViewById(R.id.zoomPlusButton);
        isoPlusButton = view.findViewById(R.id.isoPlusButton);
        apertureMinusButton= view.findViewById(R.id.apertureMinusButton);
        shutterSpeedMinusButton = view.findViewById(R.id.shutterSpeedMinusButton);
        zoomMinusButton = view.findViewById(R.id.zoomMinusButton);
        isoMinusButton = view.findViewById(R.id.isoMinusButton);
        apertureTV = view.findViewById(R.id.apertureTV);
        shutterSpeedTV = view.findViewById(R.id.shutterSpeedTV);
        zoomTV = view.findViewById(R.id.zoomTV);
        isoTV = view.findViewById(R.id.isoTV);
        bodyButton = view.findViewById(R.id.cameraSelectButton);
        lensButton = view.findViewById(R.id.lensSelectButton);
        evTextView = view.findViewById(R.id.evTextView);
        Intelligence.Current.setBody(MainActivity.body_al.get(0));
        Intelligence.Current.setLens(MainActivity.lens_al.get(0));

        bodyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.fragmentStack.push(new Body());
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fl,new Body()).commit();

            }
        });

        lensButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.fragmentStack.push(new Lens());
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fl,new Lens()).commit();
            }
        });

        aperturePlusButton.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { Intelligence.Current.aperturePlus(); updateUI();}});
        shutterSpeedPlusButton.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { Intelligence.Current.shutterSpeedPlus(); updateUI(); }});
        zoomPlusButton.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { Intelligence.Current.focalLengthPlus(); updateUI(); }});
        isoPlusButton.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { Intelligence.Current.isoPlus(); updateUI(); }});
        apertureMinusButton.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { Intelligence.Current.apertureMinus(); updateUI(); }});
        shutterSpeedMinusButton.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { Intelligence.Current.shutterSpeedMinus(); updateUI(); }});
        zoomMinusButton.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { Intelligence.Current.focalLengthMinus(); updateUI(); }});
        isoMinusButton.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { Intelligence.Current.isoMinus(); updateUI(); }});

        updateUI();
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void updateUI()
    {
        apertureTV.setText(Intelligence.Current.getApertureString());
        shutterSpeedTV.setText(Intelligence.Current.getShutterSpeedString());
        zoomTV.setText(Intelligence.Current.getFocalLengthString());
        isoTV.setText(Intelligence.Current.getISOString());
        bodyButton.setText(Intelligence.Current.getBody().getPartName());
        lensButton.setText(Intelligence.Current.getLens().getPartName());
        evTextView.setText(Double.toString(Intelligence.ExposureCalculator()));
    }

}