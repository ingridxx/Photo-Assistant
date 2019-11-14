package com.example.photoassistant;

import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;

import android.content.pm.ActivityInfo;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import android.app.Activity;
import android.os.Build;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.Volley;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;


import static android.content.ContentValues.TAG;

public class Sun extends Fragment {

    private static double asr, nsr, csr, sr, ss, css, nss, ass;
    private int length;
    private Canvas canvas;
    private Paint paint;
    ImageView blackCircle;
    Handler handler;
    public Sun() {
        // Required empty public constructor
    }
    private JSONObject reader;
    private JSONObject extract;
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    View rootView;
    private void updateSun(final double longitude, final double latitude){
        new Thread(){
            public void run(){
                final JSONObject response =getSunriseSunsetAPI.getSunriseSunset(longitude,latitude);
                if (response == null) {
                    handler.post(new Runnable() {
                        public void run() {
                            Log.e("sun", "sun error");
                            Toast.makeText(getActivity(), "Error in getting data from API.", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        public void run() {
                            updateUI(response);
                        }
                    });
                }

            }

        }.start();
    }

    TextView sunriseTextView;
    TextView sunsetTextView;
    TextView fourTimesTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true); handler = new Handler();

        final View rootView = inflater.inflate(R.layout.fragment_sun, container, false);
        this.rootView = rootView;
        //CIRCLE PARAMETERS
        final int margin = 50,thickness = 75, topMargin = 25;

        //POSITION CIRCLE
        blackCircle = rootView.findViewById(R.id.black_circle);
        ImageView sunCircle = rootView.findViewById(R.id.gradientRing);
        Display display = ((Activity)getContext()).getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics ();
        display.getMetrics(outMetrics);
        final float shorter = outMetrics.heightPixels<outMetrics.widthPixels ? outMetrics.heightPixels : outMetrics.widthPixels;
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int)(shorter-convertDpToPixel(margin,getContext())), (int)(shorter-convertDpToPixel(margin,getContext())));
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        layoutParams.setMargins(0,(int)convertDpToPixel(topMargin,getContext()),0,0);
        sunCircle.setLayoutParams(layoutParams);
        layoutParams = new RelativeLayout.LayoutParams((int)(layoutParams.width-convertDpToPixel(thickness,getContext())), (int)(layoutParams.height-convertDpToPixel(thickness,getContext())));
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        layoutParams.setMargins(0,(int)convertDpToPixel(topMargin+thickness/2.0f,getContext()),0,0);
        sunriseTextView = rootView.findViewById(R.id.sunriseTextView);
        sunsetTextView = rootView.findViewById(R.id.sunsetTextView);
        fourTimesTextView = rootView.findViewById(R.id.fourTimesTextView);
        blackCircle.setLayoutParams(layoutParams);

        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        final RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        double longitude, latitude;
        String url="https://api.sunrise-sunset.org/json?";
        GPS gps=new GPS(getActivity().getApplicationContext());
        //check network connectivity
        if (isNetworkAvailable() == false) {
            Toast.makeText(getActivity(), "Please check your Internet connection and try again.", Toast.LENGTH_LONG).show();
            return rootView;
        }
        if (gps.canGetLocation()) {


            longitude = gps.getLongitude();
            latitude = gps.getLatitude();

            url=url+"lat="+latitude+"&lng="+longitude+"&date=today";
            // Add the request to the RequestQueue.
            Log.d("drawingLines", "onCreateView: can get Location");

        } else {
            Toast.makeText(getActivity(), "Please check your GPS settings and try again.", Toast.LENGTH_LONG).show();
            return rootView;
        }


        updateSun(longitude, latitude);
        return rootView;
}

    public void updateUI(JSONObject response)
    {
        String sunrise="";
        String sunset="";
        String temp="";
        LocalTime lt;

        try{
            reader = response;
            extract = reader.getJSONObject("results");
            if(extract.length()<=0) {
                Log.e("sun", "Sun: Error retrieving data.");
                Toast.makeText(getActivity(), "Error retrieving data.", Toast.LENGTH_SHORT).show();
                return;
            }
            Log.d(TAG, "onResponse:");
            sunrise+=setSrSsLabel(R.color.horizon, "sunrise","sr");

            sunrise+=setSrSsLabel(R.color.civil, "civil_twilight_begin","csr");

            sunrise+=setSrSsLabel(R.color.nautical, "nautical_twilight_begin","nsr");

            sunrise+=setSrSsLabel(R.color.astronomical,"astronomical_twilight_begin","asr");

            sunset+=setSrSsLabel(R.color.horizon,"sunset","ss");

            sunset+=setSrSsLabel(R.color.civil,"civil_twilight_end","css");

            sunset+=setSrSsLabel(R.color.nautical,"nautical_twilight_end","nss");

            sunset+=setSrSsLabel(R.color.astronomical,"astronomical_twilight_end","ass");

            sunriseTextView.setText(Html.fromHtml("<br><br>Sunrise<br>" + sunrise));
            sunsetTextView.setText(Html.fromHtml("<br><br>Sunset<br>" + sunset));
            fourTimesTextView.setText(Html.fromHtml("<font color=#FF0000>Now<br>"
                    +LocalTime.now().format(dateTimeFormatter)+"</font><br><br>Horizon<br>Civil<br>Nautical<br>Astronomical"));

            drawShapes(rootView,blackCircle);

        }catch (Exception e){e.printStackTrace();

        }finally {

        }
    }
    private void drawShapes(View rootView,ImageView blackCircle) {

        final ShapeDrawable circle = new ShapeDrawable(new OvalShape());

        final int horizon = getResources().getColor(R.color.horizon);
        final int civil = getResources().getColor(R.color.civil);
        final int nautical = getResources().getColor(R.color.nautical);
        final int astronomical = getResources().getColor(R.color.astronomical);
        final int night = getResources().getColor(R.color.night);

        ShapeDrawable.ShaderFactory shaderFactory = new ShapeDrawable.ShaderFactory() {

            @Override
            public Shader resize(int width, int height) {
                int mWidth= circle.getBounds().width()/2;
                int mHeight= circle.getBounds().height()/2;
                double halfHour = 0.0208;

                SweepGradient sweepGradient = new SweepGradient(mWidth,mHeight,
                        new int[]{
                                0xFF000000+night,
                                0xFF000000+astronomical,
                                0xFF000000+nautical,
                                0xFF000000+civil,
                                0xFF000000+horizon,
                                0xFF000000+horizon,
                                0xFF000000+civil,
                                0xFF000000+nautical,
                                0xFF000000+astronomical,
                                0xFF000000+night,
                                0xFF000000+night,

                        }, //substitute the correct colors for these
                        new float[]{
                                (float) (1-ass-halfHour),(float)(1-ass), (float)(1-nss), (float)(1-css), (float)(1-ss), (float)(1-sr), (float)(1-csr), (float)(1-nsr), (float)(1-asr),(float) (1-asr+halfHour),1});

                return sweepGradient;
            }
        };
        circle.setShaderFactory(shaderFactory);
        ImageView iv = (ImageView)(rootView.findViewById(R.id.gradientRing));

        length = blackCircle.getHeight();
        final Bitmap bitmap = Bitmap.createBitmap(length, length, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);
        canvas.drawBitmap(bitmap,0,0,null);
        Date currentTime = Calendar.getInstance().getTime();

        double nowAngle = currentTime.getHours()/24.0+ currentTime.getMinutes()/1440.0;

        drawLinesOnCanvas(asr,R.color.astronomical);

        drawLinesOnCanvas(ass,R.color.astronomical);

        drawLinesOnCanvas(nsr,R.color.nautical);

        drawLinesOnCanvas(nss,R.color.nautical);

        drawLinesOnCanvas(csr,R.color.civil);

        drawLinesOnCanvas(css,R.color.civil);

        drawLinesOnCanvas(sr,R.color.horizon);

        drawLinesOnCanvas(ss,R.color.horizon);

        drawLinesOnCanvas(nowAngle,R.color.nowColour);

        iv.setBackground(circle);
        iv.setRotation(90);
        blackCircle.setImageDrawable(new BitmapDrawable(getContext().getResources(),bitmap));

    }

    private void drawLinesOnCanvas(double srDouble, int whichColour) {

        paint.setColor(getResources().getColor(whichColour));

        canvas.drawLine((float)getCircleX(srDouble, this.length, this.length)
                ,(float)getCircleY(srDouble, this.length, this.length),(float)(this.length /2.0),(float)(this.length /2.0),paint);

    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static float convertDpToPixel(float dp, Context context){
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
    public static float convertPixelsToDp(float px, Context context){
        return px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
    public static double getCircleX(double percentageAngle, double width, double height)
    {
        percentageAngle = percentageAngle + 0.5;
        percentageAngle = 1-percentageAngle;
        double xx = percentageAngle*2* Math.PI;

        return width/2.0 + width*Math.sin(xx)/2.0;
    }
    public static double getCircleY(double percentageAngle, double width, double height)
    {
        percentageAngle = percentageAngle + 0.5;
        percentageAngle = 1-percentageAngle;
        double yy = percentageAngle*2* Math.PI;
        return height/2.0 - height*Math.cos(yy)/2.0;
    }


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



    public String setSrSsLabel(int colour, String extractString,String doubleName){
        String temp = "";
        try {
            temp =extract.getString(extractString);
            LocalTime lt = time(temp);
            setStaticDoubles(doubleName,lt.getHour()/24.0+ lt.getMinute()/1440.0);
            return "<font color="+ getResources().getColor(colour) + ">"+ lt.format(dateTimeFormatter)+"</font>"+"<br>";
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }


    public void setStaticDoubles(String staticDoubles,double value){

        switch (staticDoubles){

            case "sr":
                sr = value;
                break;
            case "asr":
                asr = value;
                break;
            case "nsr":
                nsr = value;
                break;
            case "csr":
                csr = value;
                break;

            case "ss":
                ss = value;
                break;
            case "ass":
                ass = value;
                break;
            case "nss":
                nss = value;
                break;
            case "css":
                css = value;
                break;


        }




    }
}

