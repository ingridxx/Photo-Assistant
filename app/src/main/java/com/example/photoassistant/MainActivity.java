package com.example.photoassistant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    public static final int h = 10;
    Button bodyButton;
    Button lensButton;
    Button weatherButton;
    Button calcButton;
    Button sunButton;
    public static ArrayList<ListItemLens> lens_al = new ArrayList<>();
    public static ArrayList<ListItemBody> body_al = new ArrayList<>();
    public static ArrayList<ListItemCombination> combination_al = new ArrayList<>();
    static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bodyButton = findViewById(R.id.bodyButton);
        bodyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fl, new Body()).commit();
            }
        });

        lensButton = findViewById(R.id.lensButton);
        lensButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fl, new Lens()).commit();

            }
        });
        weatherButton = findViewById(R.id.weatherButton);
        weatherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fl, new Weather()).commit();
            }
        });
        calcButton = findViewById(R.id.calcButton);
        calcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fl, new Calculator()).commit();
                Calculator c = new Calculator();
            }
        });
        sunButton = findViewById(R.id.sunButton);
        sunButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fl, new Sun()).commit();
            }
        });

        ProcessLensData();
        ProcessBodyData();
        ProcessCombinationData();
    }

    static void toaster(@NonNull String text)
    {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public void ProcessLensData(){
        //Lens lens = new Lens();

        ListItemLens lens;
        InputStream is = getResources().openRawResource(R.raw.lens);
        BufferedReader br = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8"))
        );

        String temp_line = "";
        String[] temp_arr;

        try {
            while ((temp_line = br.readLine()) != null){
                Log.d("myActivity", "line " + temp_line);
                temp_arr = temp_line.split(",");
                if(temp_arr.length==7)
                {
                    temp_arr = Arrays.copyOf(temp_arr, 8);
                    temp_arr[7] = String.valueOf(Float.valueOf(temp_arr[2])/2000);
                }
                if(temp_arr.length==8)
                {
                    lens = new ListItemLens(temp_arr[0], temp_arr[1], temp_arr[2], temp_arr[3], temp_arr[4], temp_arr[5], temp_arr[6], temp_arr[7]);
                    lens_al.add(lens);
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void ProcessBodyData(){
        //Lens lens = new Lens();

        ListItemBody body;
        InputStream is = getResources().openRawResource(R.raw.mount);
        BufferedReader br = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8"))
        );

        String temp_line = "";
        String[] temp_arr;

        try {
            while ((temp_line = br.readLine()) != null){
                Log.d("myActivity", "body " + temp_line);
                temp_arr = temp_line.split(",");
                body = new ListItemBody(temp_arr[0], temp_arr[1], temp_arr[2], temp_arr[3], temp_arr[4], temp_arr[5]);
                body_al.add(body);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void ProcessCombinationData(){
        //Lens lens = new Lens();

        ListItemCombination combination;
        InputStream is = getResources().openRawResource(R.raw.combination);
        BufferedReader br = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8"))
        );

        String temp_line = "";
        String[] temp_arr;

        try {
            while ((temp_line = br.readLine()) != null){
                Log.d("myActivity", "combo " + temp_line);
                temp_arr = temp_line.split(",");
                if(temp_arr.length==2)
                {
                    combination = new ListItemCombination(temp_arr[0], temp_arr[1]);
                    combination_al.add(combination);
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
