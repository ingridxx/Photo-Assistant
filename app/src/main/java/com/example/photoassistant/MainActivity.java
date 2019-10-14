package com.example.photoassistant;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
    public static ArrayList<ListItemLens> lens_al = new ArrayList<ListItemLens>();
    public static ArrayList<ListItemBody> body_al = new ArrayList<ListItemBody>();
    public static ArrayList<ListItemCombination> combination_al = new ArrayList<ListItemCombination>();
    public static String currFrag = "sun";
    public static String prevFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new ProcessDataFromArrays().execute(lens_al);

        bodyButton = findViewById(R.id.bodyButton);
        bodyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prevFrag = currFrag;
                currFrag = "body";
                getSupportFragmentManager().beginTransaction().replace(R.id.fl,new Body()).commit();
            }
        });

        lensButton = findViewById(R.id.lensButton);
        lensButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prevFrag = currFrag;
                currFrag = "lens";

                getSupportFragmentManager().beginTransaction().replace(R.id.fl,new Lens()).commit();

            }
        });
        weatherButton = findViewById(R.id.weatherButton);
        weatherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prevFrag = currFrag;
                currFrag = "weather";

                getSupportFragmentManager().beginTransaction().replace(R.id.fl,new Weather()).commit();
            }
        });
        calcButton = findViewById(R.id.calcButton);
        calcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prevFrag = currFrag;
                currFrag = "calc";

                getSupportFragmentManager().beginTransaction().add(R.id.fl,new Calculator()).addToBackStack("calc_fragment").commit();
            }
        });
        sunButton = findViewById(R.id.sunButton);
        sunButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prevFrag = currFrag;
                currFrag = "sun";
                getSupportFragmentManager().beginTransaction().replace(R.id.fl,new Sun()).commit();
            }
        });

        ProcessBodyData();
        ProcessCombinationData();


    }

    @Override
    public void onBackPressed() {
        Log.d("myDebugTag", "onBackPressed:");



        // Check if that Fragment is currently visible
        boolean myFragXwasVisible = false;
        try{
            Calculator myFragment = (Calculator) getSupportFragmentManager().findFragmentByTag("calc_fragment");
            myFragXwasVisible = myFragment.isVisible();

        } catch (Exception e) {

            e.printStackTrace();
        }


        // Let the Activity pop the BackStack as normal


        // If it was your particular Fragment that was visible...
        if (myFragXwasVisible) {
         //   switch (prevFrag) {
                Log.d("myDebugTag", "FragVisible");

//               case "weather":
//                   prevFrag = currFrag;
//                   currFrag = "weather";
//                   getSupportFragmentManager().beginTransaction().replace(R.id.fl,new Weather()).commit();
//                   break;
//               case "sun":
//                   prevFrag = currFrag;
//                   currFrag = "sun";
//                   getSupportFragmentManager().beginTransaction().replace(R.id.fl,new Sun()).commit();
//                   break;
//               case "body":
//                   prevFrag = currFrag;
//                   currFrag = "body";
//                   getSupportFragmentManager().beginTransaction().replace(R.id.fl,new Body()).commit();
//                   break;
//               case "lens":
//                   prevFrag = currFrag;
//                   currFrag = "lens";
//                   getSupportFragmentManager().beginTransaction().replace(R.id.fl,new Lens()).commit();
//                   break;
//
//           }
          //  }
            super.onBackPressed();
        }
    }

    private class ProcessDataFromArrays extends AsyncTask <ArrayList<ListItemLens>,Integer,ArrayList<ListItemLens>>{
        @Override
        protected ArrayList<ListItemLens> doInBackground(ArrayList<ListItemLens>... arrayLists) {
            ListItemLens lens;
            InputStream is = getResources().openRawResource(R.raw.lens);
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(is, Charset.forName("UTF-8"))
            );

            String temp_line = "";
            String[] temp_arr;

            try {
                while ((temp_line = br.readLine()) != null){
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
            return lens_al;


        }

        @Override
        protected void onPostExecute(ArrayList<ListItemLens> listItemLens) {
            super.onPostExecute(listItemLens);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }
    public void ProcessLensData(){
        //Lens lens = new Lens();



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
