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
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


public class Calculator extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    SeekBar apertureSeekBar, shutterSpeedSeekBar, zoomSeekBar, isoSeekBar;
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

        //myLayout = LayoutInflater.from(getContext()).inflate(R.layout.activity_main,null);

        apertureSeekBar = (SeekBar)rootView.findViewById(R.id.apertureSeekbar);
        shutterSpeedSeekBar = (SeekBar)rootView.findViewById(R.id.shutterSeekBar);
        isoSeekBar = (SeekBar)rootView.findViewById(R.id.isoSeekBar);
        zoomSeekBar = (SeekBar)rootView.findViewById(R.id.zoomSeekBar);

        apertureSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onStopTrackingTouch(SeekBar seekBar) { }
            @Override public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                Intelligence.Current.aperture = progress;
            }});

        shutterSpeedSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onStopTrackingTouch(SeekBar seekBar) { }
            @Override public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                Intelligence.Current.shutterSpeed = progress;
            }});

        isoSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onStopTrackingTouch(SeekBar seekBar) { }
            @Override public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                Intelligence.Current.ISO = progress;
            }});

        zoomSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onStopTrackingTouch(SeekBar seekBar) {Intelligence.Current.focalLength = seekBar.getProgress();
                //Log.i("progress:",Double.toString(Intelligence.ExposureCalculator())); }
}
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {

                Log.i("progress:",Double.toString(Intelligence.ExposureCalculator()));
            }});
        return rootView;
    }




}
