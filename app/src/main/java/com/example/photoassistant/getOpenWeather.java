package com.example.photoassistant;

import android.util.Log;

import androidx.fragment.app.Fragment;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class getOpenWeather extends Fragment {

    /**
     * OpenWeather API call
     * @return weather data in json
     */
    public static JSONObject getForecast(){
        try {
            URL OpenWeatherAPI = new URL("https://api.openweathermap.org/data/2.5/forecast?q=Singapore&units=metric&appid=ce57d5369816e2ed09de4a6545992df5");
            HttpURLConnection con = (HttpURLConnection) OpenWeatherAPI.openConnection();

            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            StringBuffer json = new StringBuffer(1024);
            String tmp = "";
            while ((tmp = reader.readLine()) != null)
                json.append(tmp).append("\n");
            reader.close();

            JSONObject data = new JSONObject(json.toString());

            if (data.getInt("cod") != 200) {
                Log.e("getOpenWeather", "getForecast(): Error in getting data from OpenWeather.");
                return null;
            }

            return data;
        } catch (Exception e) {
            Log.e("getOpenWeather", "getForecast(): Exception in getting data from OpenWeather.");
            return null;
        }

    }

}
