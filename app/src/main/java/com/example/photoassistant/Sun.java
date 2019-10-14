package com.example.photoassistant;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
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
        return rootView;

}

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        final RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        Button getSunriseSunsetButton = (Button)getView().findViewById(R.id.getSunriseSunsetButton);
        final TextView sunTextView = (TextView)getView().findViewById(R.id.sunTextView);

        getSunriseSunsetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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


            }
        });
    }

}

