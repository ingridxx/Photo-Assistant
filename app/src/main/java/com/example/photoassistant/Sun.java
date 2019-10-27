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

import org.apache.commons.lang3.time.DateUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ArrayList;
import java.util.TimeZone;


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
        int margin = 50,thickness = 75, topMargin = 25;
        //POSITION CIRCLE
        ImageView whiteCircle = rootView.findViewById(R.id.whiteCircle);
        ImageView sunCircle = rootView.findViewById(R.id.gradientRing);
        Display display = ((Activity)getContext()).getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics ();
        display.getMetrics(outMetrics);
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
        final TextView sunriseTextView = (TextView)getView().findViewById(R.id.sunriseTextView);
        final TextView sunsetTextView = (TextView)getView().findViewById(R.id.sunsetTextView);



        //String url="https://api.sunrise-sunset.org/json?lat=37.421&lng=-122.084&date=today";
        String url="https://api.sunrise-sunset.org/json?";
        GPS gps=new GPS(getActivity().getApplicationContext());
        if (gps.canGetLocation()) {


            double longitude = gps.getLongitude();
            double latitude = gps.getLatitude();

            url=url+"lat="+latitude+"&lng="+longitude+"&date=today";
            // Add the request to the RequestQueue.


        } else {

            gps.showSettingsAlert();
        }
        // Request a string response from the provided URL.
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.


                        String sunrise="";
                        String sunset="";
                        String temp="";
                        LocalTime lt;
                        try{
                            JSONObject reader = new JSONObject(response);
                            JSONObject extract = reader.getJSONObject("results");
                            temp=extract.getString("astronomical_twilight_end");
                           lt = time(temp);
                            sunrise+="astronomical sunrise: "+lt+"\n";
                            temp=extract.getString("nautical_twilight_begin");
                            lt = time(temp);
                            sunrise+="nautical sunrise: "+lt+"\n";
                            temp=extract.getString("civil_twilight_begin");
                            lt=time(temp);
                            sunrise+="civil sunrise: "+lt+"\n";
                            temp=extract.getString("sunrise");
                            lt=time(temp);
                            sunrise+="sunrise: "+lt+"\n";
                            temp=extract.getString("sunset");
                            lt=time(temp);
                            sunset+="sunset: "+lt+"\n";
                            temp=extract.getString("civil_twilight_end");
                            lt=time(temp);
                            sunset+="civil sunset: "+lt+"\n";
                            temp=extract.getString("nautical_twilight_end");
                            sunset+="nautical sunset"+lt+"\n";
                            temp=extract.getString("astronomical_twilight_end");
                            sunset+="astronomical sunset"+lt+"\n";

                        }catch (Exception e){
                            sunriseTextView.append(e.toString());
                        }finally {
                            sunriseTextView.append("Sunrise: \n"+sunrise);
                            sunsetTextView.append("Sunset: \n"+sunset);
                        }



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                sunriseTextView.append("That didn't work!");
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
                                        0xFF40284A,
                                        0xFF73434B,
                                        0xFFB34D25,
                                        0xFFF07E07,
                                        0xFFF7DE55,
                                        0xFF40284A,
                                        0xFF73434B,
                                        0xFFB34D25,
                                        0xFFB34D25,
                                        0xFF73434B,
                                        0xFF40284A,
                                        0xFFF7DE55,
                                        0xFFF07E07,
                                        0xFFB34D25,
                                        0xFF73434B,
                                        0xFF40284A,
                                        0xFF1e5799,}, //substitute the correct colors for these
                                new float[]{
                                        0, 0.050f, 0.10f, 0.15f, 0.20f,
                                        0.25f,0.30f,0.35f,0.40f,0.45f,
                                        0.50f,0.55f,0.60f,0.65f,0.70f,
                                        0.75f,0.80f,1});
                        return sweepGradient;
                    }
                };circle.setShaderFactory(shaderFactory);
                ImageView iv = (ImageView)(getView().findViewById(R.id.gradientRing));
                iv.setBackground(circle);

        //#40284A,#73434B,#B34D25,#F07E07,#F7DE55,#40284A,#73434B,#B34D25



            }

    public static LocalTime time(String a){
        String rawTime=a.substring(0,8);
       if (Character.isWhitespace(rawTime.charAt(rawTime.length() - 1))){
         rawTime  = rawTime.substring(0, 0) + "0" + rawTime.substring(0,7);
        }
        LocalTime t = LocalTime.parse(rawTime, DateTimeFormatter.ofPattern("HH:mm:ss"));
        String pm = a.substring(a.length()-2);
        if(pm.equals("PM")){
            t=t.plusHours(12);
        }
        int add= TimeZone.getDefault().getRawOffset();
        t=t.plusHours(8);
        return t;
    }


}

