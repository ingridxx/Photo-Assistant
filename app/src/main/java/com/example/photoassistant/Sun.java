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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.ArrayList;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;


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
        final View rootView = inflater.inflate(R.layout.fragment_sun, container, false);

        //CIRCLE PARAMETERS
        int margin = 50,thickness = 75, topMargin = 25;
        //POSITION CIRCLE
        ImageView blackCircle = rootView.findViewById(R.id.black_circle);
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

        blackCircle.setLayoutParams(layoutParams);


        final TextView sunriseTextView = rootView.findViewById(R.id.sunriseTextView);
        final TextView sunsetTextView = rootView.findViewById(R.id.sunsetTextView);



        final RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
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
                        String sunrise="";
                        String sunset="";
                        String temp="";
                        LocalTime lt;
                        try{
                            JSONObject reader = new JSONObject(response);
                            JSONObject extract = reader.getJSONObject("results");

                            temp=extract.getString("astronomical_twilight_begin");
                            lt = time(temp);
                            asr = lt.getHour()/24.0+lt.getMinute()/1440.0;
                            sunrise+=lt+"\n";

                            temp=extract.getString("nautical_twilight_begin");
                            lt = time(temp);
                            nsr=lt.getHour()/24.0+lt.getMinute()/1440.0;
                            sunrise+=lt+"\n";

                            temp=extract.getString("civil_twilight_begin");
                            lt = time(temp);
                            csr=lt.getHour()/24.0+lt.getMinute()/1440.0;
                            sunrise+=lt+"\n";

                            temp=extract.getString("sunrise");
                            lt = time(temp);
                            sr=lt.getHour()/24.0+lt.getMinute()/1440.0;
                            sunrise+=lt;

                            temp=extract.getString("sunset");
                            lt = time(temp);
                            ss=lt.getHour()/24.0+lt.getMinute()/1440.0;
                            sunset+=lt+"\n";

                            temp=extract.getString("civil_twilight_end");
                            lt = time(temp);
                            css=lt.getHour()/24.0+lt.getMinute()/1440.0;
                            sunset+=lt+"\n";

                            temp=extract.getString("nautical_twilight_end");
                            lt = time(temp);
                            nss=lt.getHour()/24.0+lt.getMinute()/1440.0;
                            sunset+=lt+"\n";

                            temp=extract.getString("astronomical_twilight_end");
                            lt = time(temp);
                            ass=lt.getHour()/24.0+lt.getMinute()/1440.0;
                            sunset+=lt;

                        }catch (Exception e){
                            sunriseTextView.append(e.toString());
                        }finally {
                            sunriseTextView.append("Sunrise\n"+sunrise);
                            sunsetTextView.append("Sunset\n"+sunset);

                            final ShapeDrawable circle = new ShapeDrawable(new OvalShape());

                            ShapeDrawable.ShaderFactory shaderFactory = new ShapeDrawable.ShaderFactory() {

                                @Override
                                public Shader resize(int width, int height) {
                                    int mWidth= circle.getBounds().width()/2;
                                    int mHeight= circle.getBounds().height()/2;

                                    long offSet = TimeUnit.HOURS.convert(TimeZone.getDefault().getRawOffset(), TimeUnit.MILLISECONDS);
                                    SweepGradient sweepGradient = new SweepGradient(mWidth,mHeight,
                                            new int[]{
                                                    0xFF40284A,
                                                    0xFF73434B,
                                                    0xFFB34D25,
                                                    0xFFF07E07,
                                                    0xFFF07E07,
                                                    0xFFB34D25,
                                                    0xFF73434B,
                                                    0xFF40284A,
                                                    0xFF40284A,
                                            }, //substitute the correct colors for these
                                            new float[]{
                                                    (float)(1-ass), (float)(1-nss), (float)(1-css), (float)(1-ss), (float)(1-sr), (float)(1-csr), (float)(1-nsr), (float)(1-asr),1});
                                    //(float)asr, (float)nsr, (float)csr, (float)sr, (float)ss, (float)css,(float)nss,(float)ass,1});

                                    return sweepGradient;
                                }
                            };circle.setShaderFactory(shaderFactory);
                            ImageView iv = (ImageView)(rootView.findViewById(R.id.gradientRing));
                            iv.setBackground(circle);
                            iv.setRotation(90);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                sunriseTextView.append("That didn't work!");
            }
        });
        queue.add(stringRequest);
        return rootView;
}
    public static float convertDpToPixel(float dp, Context context){
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
    public static float convertPixelsToDp(float px, Context context){
        return px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
    static double asr, nsr, csr, sr, ss, css, nss, ass;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }

    public static LocalTime time(String a) throws ParseException {

        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss aa");
        Date date = dateFormat.parse(a);
        int add= TimeZone.getDefault().getRawOffset();
        date.setTime(date.getTime()+add);
        //ZoneId.systemDefault();
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).toLocalTime();
    }
}

