package com.example.photoassistant;

import android.content.Context;

import android.graphics.Point;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;

import android.content.pm.ActivityInfo;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.Volley;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.ContentValues.TAG;

public class Sun extends Fragment {


    public Sun() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_sun, container, false);

        //CIRCLE PARAMETERS
        int margin = 50; int thickness = 75, topMargin = 25;
        //POSITION CIRCLE
        ImageView whiteCircle = rootView.findViewById(R.id.whiteCircle);
        ImageView sunCircle = rootView.findViewById(R.id.gradientRing);
        Display display = ((Activity)getContext()).getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics ();
        display.getMetrics(outMetrics);
        float density  = getResources().getDisplayMetrics().density;
        float dpHeight = outMetrics.heightPixels / density;
        float dpWidth  = outMetrics.widthPixels / density;
        float shorter = outMetrics.heightPixels<outMetrics.widthPixels ? outMetrics.heightPixels : outMetrics.widthPixels;
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int)(shorter-convertDpToPixel(margin,getContext())), (int)(shorter-convertDpToPixel(margin,getContext())));
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        layoutParams.setMargins(0,(int)convertDpToPixel(topMargin,getContext()),0,0);
        sunCircle.setLayoutParams(layoutParams);
        layoutParams = new RelativeLayout.LayoutParams((int)(layoutParams.width-convertDpToPixel(thickness,getContext())), (int)(layoutParams.height-convertDpToPixel(thickness,getContext())));
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        layoutParams.setMargins(0,(int)convertDpToPixel(topMargin+thickness/2.0f,getContext()),0,0);
        whiteCircle.setLayoutParams(layoutParams);

        return rootView;

}
    public static float convertDpToPixel(float dp, Context context){
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
    public static float convertPixelsToDp(float px, Context context){
        return px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        final RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        Button getSunriseSunsetButton = (Button)getView().findViewById(R.id.getSunriseSunsetButton);
        final TextView sunTextView = (TextView)getView().findViewById(R.id.sunTextView);



        //String url="https://api.sunrise-sunset.org/json?lat=37.421&lng=-122.084&date=today";
        String url="https://api.sunrise-sunset.org/json?";
        GPS gps=new GPS(getActivity().getApplicationContext());
        if (gps.canGetLocation()) {


            double longitude = gps.getLongitude();
            double latitude = gps.getLatitude();

            Toast.makeText(getActivity().getApplicationContext(), "Longitude:" + Double.toString(longitude) + "\nLatitude:" + Double.toString(latitude), Toast.LENGTH_SHORT).show();
            sunTextView.setText("Longitude:" + Double.toString(longitude) + "\nLatitude:" + Double.toString(latitude));
            url=url+"lat="+latitude+"&lng="+longitude+"&date=today";
            // Add the request to the RequestQueue.
            sunTextView.append("\n"+url+"\n");


        } else {

            gps.showSettingsAlert();
        }
        // Request a string response from the provided URL.
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.


                        String a="";
                        try{
                            JSONObject reader = new JSONObject(response);
                            JSONObject extract = reader.getJSONObject("results");
                            a+="astronomical sunrise"+extract.getString("astronomical_twilight_begin")+"\n";
                            a+="nautical sunrise"+extract.getString("nautical_twilight_begin")+"\n";
                            a+="civil sunrise"+extract.getString("civil_twilight_begin")+"\n";
                            a+="sunrise"+extract.getString("sunrise")+"\n";
                            a+="sunset"+extract.getString("sunset")+"\n";
                            a+="civil sunset"+extract.getString("civil_twilight_end")+"\n";
                            a+="nautical sunset"+extract.getString("nautical_twilight_end")+"\n";
                            a+="astronomical sunset"+extract.getString("astronomical_twilight_end")+"\n";

                        }catch (JSONException e){

                        }finally {
                            sunTextView.append("Response is: "+ a+"\n" );
                        }



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                sunTextView.append("That didn't work!");
            }
        });
        queue.add(stringRequest);


                final ShapeDrawable circle = new ShapeDrawable(new OvalShape());

                ShapeDrawable.ShaderFactory shaderFactory = new ShapeDrawable.ShaderFactory() {

                    @Override
                    public Shader resize(int width, int height) {
                        int mWidth= circle.getBounds().width()/2;
                        int mHeight= circle.getBounds().height()/2;
                        SweepGradient sweepGradient = new SweepGradient(mWidth,mHeight,
                                new int[]{
                                        0xFF1e5799,
                                        0xFFeeeeee,
                                        0xFF111111,
                                        0xFFeeeeee,}, //substitute the correct colors for these
                                new float[]{
                                        0, 0.40f, 0.60f, 1});
                        return sweepGradient;
                    }
                };circle.setShaderFactory(shaderFactory);
                ImageView iv = (ImageView)(getView().findViewById(R.id.gradientRing));
                iv.setBackground(circle);





            }

}

