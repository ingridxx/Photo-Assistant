package com.example.photoassistant;

import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;

import android.content.pm.ActivityInfo;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
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
import android.view.Window;
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
import java.util.Calendar;
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

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        final View rootView = inflater.inflate(R.layout.fragment_sun, container, false);

        //check network connectivity
        if (isNetworkAvailable() == false) {
            Toast.makeText(getActivity(), "Please check your Internet connection and try again.", Toast.LENGTH_LONG).show();
            //getActivity().finish();
        }

        //CIRCLE PARAMETERS
        final int margin = 50,thickness = 75, topMargin = 25;
        //POSITION CIRCLE
        final ImageView blackCircle = rootView.findViewById(R.id.black_circle);
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

        blackCircle.setLayoutParams(layoutParams);

        final TextView sunriseTextView = rootView.findViewById(R.id.sunriseTextView);
        final TextView sunsetTextView = rootView.findViewById(R.id.sunsetTextView);
        final TextView fourTimesTextView = rootView.findViewById(R.id.fourTimesTextView);

        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

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
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(String response) {
                        String sunrise="";
                        String sunset="";
                        String temp="";
                        LocalTime lt;
                        try{
                            JSONObject reader = new JSONObject(response);
                            JSONObject extract = reader.getJSONObject("results");

                            temp=extract.getString("sunrise");
                            lt = time(temp);
                            sr=lt.getHour()/24.0+lt.getMinute()/1440.0;
                            sunrise+="<font color=#F07E07>"+lt.format(dateTimeFormatter)+"</font>"+"<br>";
                            temp=extract.getString("civil_twilight_begin");
                            lt = time(temp);
                            csr=lt.getHour()/24.0+lt.getMinute()/1440.0;
                            sunrise+="<font color=#B34D25>"+lt.format(dateTimeFormatter)+"</font>"+"<br>";
                            temp=extract.getString("nautical_twilight_begin");
                            lt = time(temp);
                            nsr=lt.getHour()/24.0+lt.getMinute()/1440.0;
                            sunrise+="<font color=#73434B>"+lt.format(dateTimeFormatter)+"</font>"+"<br>";

                            temp=extract.getString("astronomical_twilight_begin");
                            lt = time(temp);
                            asr = lt.getHour()/24.0+lt.getMinute()/1440.0;
                            sunrise+="<font color=#40284A>"+lt.format(dateTimeFormatter)+"</font>"+"";




                            temp=extract.getString("sunset");
                            lt = time(temp);
                            ss=lt.getHour()/24.0+lt.getMinute()/1440.0;
                            sunset+="<font color=#F07E07>"+lt.format(dateTimeFormatter)+"</font>"+"<br>";
                            temp=extract.getString("civil_twilight_end");
                            lt = time(temp);
                            css=lt.getHour()/24.0+lt.getMinute()/1440.0;
                            sunset+="<font color=#B34D25>"+lt.format(dateTimeFormatter)+"</font>"+"<br>";
                            temp=extract.getString("nautical_twilight_end");
                            lt = time(temp);
                            nss=lt.getHour()/24.0+lt.getMinute()/1440.0;
                            sunset+="<font color=#73434B>"+lt.format(dateTimeFormatter)+"</font>"+"<br>";
                            temp=extract.getString("astronomical_twilight_end");
                            lt = time(temp);
                            ass=lt.getHour()/24.0+lt.getMinute()/1440.0;
                            sunset+="<font color=#40284A>"+lt.format(dateTimeFormatter)+"</font>"+"";



                        }catch (Exception e){e.printStackTrace();
                        }finally {


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

                            int length = blackCircle.getHeight();
                            final Bitmap bitmap = Bitmap.createBitmap(length, length, Bitmap.Config.ARGB_8888);
                            Canvas canvas = new Canvas(bitmap);
                            Paint paint = new Paint();
                            paint.setStyle(Paint.Style.STROKE);
                            paint.setColor(0xFF40284A);
                            paint.setStrokeWidth(3);
                            canvas.drawBitmap(bitmap,0,0,null);
                            Date currentTime = Calendar.getInstance().getTime();
                            double nowAngle = currentTime.getHours()/24.0+ currentTime.getMinutes()/1440.0;
                            canvas.drawLine((float)getCircleX(asr, length,length)
                                    ,(float)getCircleY(asr,length,length),(float)(length/2.0),(float)(length/2.0),paint);
                            canvas.drawLine((float)getCircleX(ass, length,length)
                                    ,(float)getCircleY(ass,length,length),(float)(length/2.0),(float)(length/2.0),paint);
                            paint.setColor(0xFF73434B);
                            canvas.drawLine((float)getCircleX(nsr, length,length)
                                    ,(float)getCircleY(nsr,length,length),(float)(length/2.0),(float)(length/2.0),paint);
                            canvas.drawLine((float)getCircleX(nss, length,length)
                                    ,(float)getCircleY(nss,length,length),(float)(length/2.0),(float)(length/2.0),paint);
                            paint.setColor(0xFFB34D25);

                            canvas.drawLine((float)getCircleX(csr, length,length)
                                    ,(float)getCircleY(csr,length,length),(float)(length/2.0),(float)(length/2.0),paint);
                            canvas.drawLine((float)getCircleX(css, length,length)
                                    ,(float)getCircleY(css,length,length),(float)(length/2.0),(float)(length/2.0),paint);
                            paint.setColor(0xFFF07E07);
                            canvas.drawLine((float)getCircleX(sr, length,length)
                                    ,(float)getCircleY(sr,length,length),(float)(length/2.0),(float)(length/2.0),paint);
                            canvas.drawLine((float)getCircleX(ss, length,length)
                                    ,(float)getCircleY(ss,length,length),(float)(length/2.0),(float)(length/2.0),paint);
                            paint.setColor(0xFFFF0000);

                            canvas.drawLine((float)getCircleX(nowAngle, length,length)
                                    ,(float)getCircleY(nowAngle,length,length),(float)(length/2.0),(float)(length/2.0),paint);
                            iv.setBackground(circle);
                            iv.setRotation(90);
                            blackCircle.setImageDrawable(new BitmapDrawable(getContext().getResources(),bitmap));
                            sunriseTextView.setText(Html.fromHtml("<br><br>Sunrise<br>" + sunrise));
                            sunsetTextView.setText(Html.fromHtml("<br><br>Sunset<br>" + sunset));
                            fourTimesTextView.setText(Html.fromHtml("<font color=#FF0000>Now<br>"
                                    +LocalTime.now().format(dateTimeFormatter)+"</font><br><br>Horizon<br>Civil<br>Nautical<br>Astronomical"));

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);
        return rootView;
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

