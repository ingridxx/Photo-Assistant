package com.example.photoassistant;

import android.util.Log;

import androidx.fragment.app.Fragment;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class getWeatherAPI extends Fragment {
    public static JSONObject getWeather(String dataset) {
        String NEA_weather = "https://api.data.gov.sg/v1/environment/" + dataset;
        try {
            URL obj = new URL(NEA_weather);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String inputLine;
            StringBuffer response = new StringBuffer();
            while((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
            }
            reader.close();

            JSONObject data = new JSONObject(response.toString());

            if(con.getResponseCode() != 200){
                Log.e("getWeatherAPI", "getWeather(): Error in getting data from NEA.");
            }
            return data;
        } catch(Exception e) {
            Log.e("getWeatherAPI", "getWeather(): Exception in getting data from NEA.");
            return null;
        }
    }
}
