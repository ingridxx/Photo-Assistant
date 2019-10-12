package com.example.photoassistant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;

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

    public ArrayList<String> permissionsToRequest;
    public ArrayList<String> permissionsRejected = new ArrayList<>();
    public ArrayList<String> permissions = new ArrayList<>();
    public final static int ALL_PERMISSIONS_RESULT = 101;
   GPS gps=new GPS(MainActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);
        permissionsToRequest = findUnAskedPermissions(permissions);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }

        bodyButton = findViewById(R.id.bodyButton);
        bodyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fl,new Body()).commit();
            }
        });



        lensButton = findViewById(R.id.lensButton);
        lensButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fl,new Lens()).commit();

            }
        });
        weatherButton = findViewById(R.id.weatherButton);
        weatherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fl,new Weather()).commit();
            }
        });
        calcButton = findViewById(R.id.calcButton);
        calcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fl,new Calculator()).commit();
                Calculator c = new Calculator();
            }
        });
        sunButton = findViewById(R.id.sunButton);
        sunButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fl,new Sun()).commit();
            }
        });

        ProcessLensData();
        ProcessBodyData();
        ProcessCombinationData();
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

    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (String perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }

                }

                break;
        }

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gps.stopListener();
    }

}
