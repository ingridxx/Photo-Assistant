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
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.Volley;
import com.android.volley.RequestQueue;
import org.json.JSONObject;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;


public class Sun extends Fragment {
    static double longitude = 0;
    static double latitude = 0;
    static String sunrise="";
    static String sunset="";
    TextView sunriseTextView;
    TextView sunsetTextView;
    ShapeDrawable circle;
    ImageView iv;
    Handler handler;


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


       sunriseTextView = rootView.findViewById(R.id.sunriseTextView);
       sunsetTextView = rootView.findViewById(R.id.sunsetTextView);
       iv = (ImageView) (rootView.findViewById(R.id.gradientRing));
       handler = new Handler();


        final RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        //String url="https://api.sunrise-sunset.org/json?lat=37.421&lng=-122.084&date=today";
        String url="https://api.sunrise-sunset.org/json?";
        GPS gps=new GPS(getActivity().getApplicationContext());
        if (gps.canGetLocation()) {
            longitude = gps.getLongitude();
            latitude = gps.getLatitude();
            url=url+"lat="+latitude+"&lng="+longitude+"&date=today";
        } else {
            gps.showSettingsAlert();
        }

        test();
        return rootView;

    }

    private void test(){
        new Thread(){
            public void run(){

                try{

                    sunrise="";
                    sunset="";
                    JSONObject reader =  getSunriseSunsetAPI.getSunriseSunset(longitude,latitude);
                    JSONObject extract = reader.getJSONObject("results");
                    String temp="";
                    LocalTime lt;

                    temp=extract.getString("astronomical_twilight_begin");
                    Log.e("temp ", "long "+temp);
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
                    Log.e("e ", "e "+e.toString());
                }
                handler.post(new Runnable() {
                    public void run() {
                        sunriseTextView.setText("Sunrise\n"+sunrise);
                        sunsetTextView.setText("Sunset\n"+sunset);

                        circle = new ShapeDrawable(new OvalShape());
                        ShapeDrawable.ShaderFactory shaderFactory = new ShapeDrawable.ShaderFactory() {
                            @Override
                            public Shader resize(int width, int height) {
                                int mWidth = circle.getBounds().width() / 2;
                                int mHeight = circle.getBounds().height() / 2;

                                long offSet = TimeUnit.HOURS.convert(TimeZone.getDefault().getRawOffset(), TimeUnit.MILLISECONDS);
                                SweepGradient sweepGradient = new SweepGradient(mWidth, mHeight,
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
                                                (float) (1 - ass), (float) (1 - nss), (float) (1 - css), (float) (1 - ss), (float) (1 - sr), (float) (1 - csr), (float) (1 - nsr), (float) (1 - asr), 1});
                                //(float)asr, (float)nsr, (float)csr, (float)sr, (float)ss, (float)css,(float)nss,(float)ass,1});

                                return sweepGradient;
                            }
                        };
                        circle.setShaderFactory(shaderFactory);

                        iv.setBackground(circle);
                        iv.setRotation(90);


                    }
                });
            }

        }.start();
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static LocalTime time(String a) throws ParseException {

        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss aa");
        Date date = dateFormat.parse(a);
        int add= TimeZone.getDefault().getRawOffset();
        date.setTime(date.getTime()+add);
        //ZoneId.systemDefault();
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).toLocalTime();
    }


}

