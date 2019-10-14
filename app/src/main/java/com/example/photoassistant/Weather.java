package com.example.photoassistant;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Handler;

import com.squareup.picasso.Picasso;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONObject;


public class Weather extends Fragment {
    TextView location;
    TextView nowTemp;
    ImageView weatherIcon;
    TextView Description;
    TextView hum;
    TextView psi;
    TextView Hour1;
    TextView Hour1Temp;
    TextView Hour2;
    TextView Hour2Temp;
    TextView Hour3;
    TextView Hour3Temp;
    ImageView H1Icon;
    ImageView H2Icon;
    ImageView H3Icon;


    Handler handler;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weather, container, false);

        handler = new Handler();

        location = (TextView) view.findViewById(R.id.Location);
        Description = (TextView) view.findViewById(R.id.Description);
        weatherIcon = (ImageView) view.findViewById(R.id.WeatherIcon);
        nowTemp = (TextView) view.findViewById(R.id.currentTemp);
        psi = (TextView) view.findViewById(R.id.PSI);
        hum = (TextView) view.findViewById(R.id.Humidity);
        Hour1 = (TextView) view.findViewById(R.id.Hour1);
        Hour2 = (TextView) view.findViewById(R.id.Hour2);
        Hour3 = (TextView) view.findViewById(R.id.Hour3);
        Hour1Temp = (TextView) view.findViewById(R.id.H1Temp);
        Hour2Temp = (TextView) view.findViewById(R.id.H2Temp);
        Hour3Temp = (TextView) view.findViewById(R.id.H3Temp);
        H1Icon = (ImageView) view.findViewById(R.id.H1Icon);
        H2Icon = (ImageView) view.findViewById(R.id.H2Icon);
        H3Icon = (ImageView) view.findViewById(R.id.H3Icon);

        // Check Internet Connectivity?



        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        updateWeather();
        super.onViewCreated(view, savedInstanceState);
    }

    private void updateWeather(){
        new Thread(){
            public void run(){
                final JSONObject currentTemp =getWeatherAPI.getWeather("air-temperature"); //currentTemp
                final JSONObject OWForecast =getOpenWeather.getForecast(); //description, icon
                final JSONObject PSI =getWeatherAPI.getWeather("psi"); //PSI
                final JSONObject Humidity =getWeatherAPI.getWeather("24-hour-weather-forecast"); //humidity


                if (currentTemp == null || OWForecast == null || PSI == null || Humidity == null) {
                    handler.post(new Runnable() {
                        public void run() {
                            Log.e("weather", "updateWeather(): Error retrieving data.");
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        public void run() {
                            renderWeatherScreen(currentTemp, OWForecast, PSI, Humidity);
                        }
                    });
                }

            }

        }.start();
    }

    private void renderWeatherScreen(JSONObject currentTemp, JSONObject OWForecast, JSONObject PSI, JSONObject Humidity){
        try {
            // Initialize data streams
            //JSONArray currentTempData = currentTemp.getJSONArray("items").getJSONObject(0).getJSONArray("readings");
            int currentTempData = currentTemp.getJSONArray("items").getJSONObject(0).getJSONArray("readings").getJSONObject(1).getInt("value"); // index 1 for Clementi Road
            JSONArray forecastData = OWForecast.getJSONArray("list");
            int PSIData = PSI.getJSONArray("items").getJSONObject(0).getJSONObject("readings").getJSONObject("psi_twenty_four_hourly").getInt("national");
            int humidityData = Humidity.getJSONArray("items").getJSONObject(0).getJSONObject("general").getJSONObject("relative_humidity").getInt("high");


            // Current Weather Description
            Description.setText(forecastData.getJSONObject(0).getJSONArray("weather").getJSONObject(0).getString("description").trim());
            // Current Weather icon
            getWeatherIcon(forecastData.getJSONObject(0).getJSONArray("weather").getJSONObject(0).getString("icon"));

            // Current Temperature, Humidity, and PSI
            nowTemp.setText(currentTempData + "℃");
            hum.setText("Humidity: " + humidityData + "%");
            psi.setText("PSI: " + PSIData);

            // 3h forecast temperatures + icons
            int hours[] = new int[3];
            int tempVals[] = new int[3];
            String icons[] = new String[3];
            int diff, data_d, k;
            int index = 0;

            // Not sure if this part is necessary. I think it'd be fine if I just parse
            // 1, 2, 3 for the next 9h forecast...
            Calendar c = Calendar.getInstance();
            int x = c.get(Calendar.HOUR_OF_DAY);
            int d = c.get(Calendar.DAY_OF_MONTH);

            for (int j = 1; j < 4; j++) {
                hours[j - 1] = x + (j * 3);

                if (hours[j - 1] > 24) {
                    hours[j - 1] = hours[j - 1] - 24;
                }
            }
            for (k = 0; k < 10; k++) {
                diff = Integer.parseInt(forecastData.getJSONObject(k).getString("dt_txt").substring(11, 13)) - x; //time difference (hours)
                data_d = Integer.parseInt(forecastData.getJSONObject(k).getString("dt_txt").substring(8, 10)); //day
                if (diff > 0 && data_d == d) { // to avoid problems when time is 00:00:00, but date = next day. (midnight)
                    break;
                }
            }
            for (int l = k; l < (k + 3); l++) {
                tempVals[index] = forecastData.getJSONObject(l).getJSONObject("main").getInt("temp");
                icons[index] = forecastData.getJSONObject(l).getJSONArray("weather").getJSONObject(0).getString("icon");
                index++;
            }
            // 3h forecast temperature
            Hour1Temp.setText("" + tempVals[0] + "℃");
            Hour2Temp.setText("" + tempVals[1] + "℃");
            Hour3Temp.setText("" + tempVals[2] + "℃");

            // 3h forecast icons
            getHourlyIcon(icons[0], H1Icon);
            getHourlyIcon(icons[1], H2Icon);
            getHourlyIcon(icons[2], H3Icon);

            // Time values:
            setTimeStrings();

            // Location (hardcoded for now)
            location.setText("Singapore");

        } catch (Exception e) {
            Log.e("MainActivity", "renderWeatherScreen(): One or more fields not found in the JSON data.");
        }
    }



    private void setTimeStrings() {
//        Instant instant = Instant.now();
//        ZoneId z = ZoneId.of("Asia/Singapore");
//        ZonedDateTime zdt = instant.atZone(z);
//        Calendar c = Calendar.getInstance(zdt);
        TimeZone tz = TimeZone.getTimeZone("GMT+8:00");
        Calendar c = Calendar.getInstance(tz);
        int x = c.get(Calendar.HOUR_OF_DAY);
        int y; // 1 = PM, 0 = AM.
        String hours[] = new String[4];
        int hour;

        for (int j = 1; j < 4; j++) {
            hour = x + (j * 3);

            if (hour > 11 && hour < 24) {
                if (hour != 12) {
                    hour = hour % 12;
                }
                y = 1;
            } else {
                if (hour > 24) {
                    hour = hour - 24;
                } else if (hour == 24) {
                    hour = 12;
                }
                y = 0;
            }

            if (y == 0) {
                hours[j] = Integer.toString(hour) + "AM";
            } else {
                hours[j] = Integer.toString(hour) + "PM";
            }
            Hour1.setText(hours[1]);
            Hour2.setText(hours[2]);
            Hour3.setText(hours[3]);
        }
    }

    private void getWeatherIcon(String icon) {
        String iconUrl = "https://openweathermap.org/img/wn/" + icon + "@2x.png"; //large icon for current
        Picasso.get().load(iconUrl).into(weatherIcon); //add picasso to build.gradle
    }

    private void getHourlyIcon(String icon, ImageView hourly) {
        Log.d("DebugTag", "getHourlyIcon: " + icon);
        String iconUrl = "https://openweathermap.org/img/wn/" + icon + "@2x.png"; //small icons for forecast
        Picasso.get().load(iconUrl).into(hourly);

    }

}
