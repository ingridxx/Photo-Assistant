package com.example.photoassistant;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import androidx.fragment.app.Fragment;

/**
 * static class used to establish connnection with the sunrise sunset api.
 */

public class getSunriseSunsetAPI extends Fragment {

    /**
     * static method which gets the weather data from the API
     *
     * @param longitude location data which is used to interface with the API
     * @param latitude location data which is used to interface with the API
     * @return
     */
    public static JSONObject  getSunriseSunset(double longitude, double  latitude) {
        String url = "https://api.sunrise-sunset.org/json?" + "lat=" + latitude + "&lng=" + longitude + "&date=today";
        Log.e("getSunAPI", "url "+url);

        try {
            URL obj = new URL(url);
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

            if(con.getResponseCode() == 200){
                Log.e("getSunAPI", "Error in getting data.");
            }
            return data;
        } catch(Exception e) {
            Log.e("e", "e"+e.toString());
            return null;
        }
    }
}

