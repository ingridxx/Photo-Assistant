package com.example.photoassistant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
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
import java.util.Stack;


import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;


public class MainActivity extends AppCompatActivity {
    public static final int h = 10;
    public final static int ALL_PERMISSIONS_RESULT = 101;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    public static ArrayList<ListItem> lens_al = new ArrayList<>();
    public static ArrayList<ListItem> body_al = new ArrayList<>();
    public static Stack fragmentStack = new Stack();
    private static Activity activity;
    public ArrayList<String> permissionsToRequest;
    public ArrayList<String> permissionsRejected = new ArrayList<>();
    public ArrayList<String> permissions = new ArrayList<>();
    Button bodyButton;
    Button weatherButton;
    Button calcButton;
    Button sunButton;
    GPS gps = new GPS(MainActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;
        new ProcessDataFromArrays().execute();

        Intelligence.setLens(new ListItemLens("Lens", new String[]{"Lens", "50", "50", "2.8", "2.8", "22", "22"}));
        Intelligence.setBody(new ListItemBody("Body", new String[]{"Body", "50", "36", "24", "DSLR", "Sample"}));

        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);
        permissions.add(CAMERA);
        permissionsToRequest = findUnAskedPermissions(permissions);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }

        handleButtons();

    }

    private void handleButtons() {

        bodyButton = findViewById(R.id.bodyButton);
        bodyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentStack.push(new BodySelector());
                getSupportFragmentManager().beginTransaction().replace(R.id.fl, new BodySelector()).commit();
            }
        });

        weatherButton = findViewById(R.id.weatherButton);
        weatherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fragmentStack.push(new Weather());
                getSupportFragmentManager().beginTransaction().replace(R.id.fl, new Weather()).commit();

            }
        });

        calcButton = findViewById(R.id.calcButton);
        calcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calculator.clearWait();
                if (!BodySelector.empty()) {
                    fragmentStack.push(new Calculator());
                    getSupportFragmentManager().beginTransaction().replace(R.id.fl, new Calculator()).commit();
                } else{
                    Toast.makeText(getApplicationContext(), "Please Select a Lens First!", Toast.LENGTH_SHORT).show();
                    fragmentStack.push(new BodySelector());
                    getSupportFragmentManager().beginTransaction().replace(R.id.fl, new BodySelector()).commit();
                }
            }

        });
        sunButton = findViewById(R.id.sunButton);
        sunButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentStack.push(new Sun());
                getSupportFragmentManager().beginTransaction().replace(R.id.fl, new Sun()).commit();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Log.d("myDebugTag", "onBackPressed:");
        fragmentStack.pop();
        if (!fragmentStack.empty())
            getSupportFragmentManager().beginTransaction().replace(R.id.fl, (Fragment) fragmentStack.peek()).commit();
        else System.exit(1);
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


    @SuppressLint("StaticFieldLeak")
    private class ProcessDataFromArrays extends AsyncTask<ArrayList<ListItem>, Integer, ArrayList<ListItem>> {
        @Override
        protected ArrayList<ListItem> doInBackground(ArrayList<ListItem>... arrayLists) {
            ListItem lens = null;
            ListItemBody body = null;

            body_al.clear();
            body_al.addAll(ProcessArrays(body, ListItemBody.resourceID, "Body"));
            lens_al.clear();
            lens_al.addAll(ProcessArrays(lens, ListItemLens.resourceID, "Lens"));

            return lens_al;

        }

        @Override
        protected void onPostExecute(ArrayList<ListItem> listItems) {
            //Slots[1][1] = lens_al.get(0);
            //Slots[1][0] = body_al.get(0);
            super.onPostExecute(listItems);
            if (fragmentStack.empty()) {
                fragmentStack.push(new Sun());
                getSupportFragmentManager().beginTransaction().replace(R.id.fl, new Sun()).commit();
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        public ArrayList<ListItem> ProcessArrays(ListItem listItem, int resourceID, String typeOfFactoryClass) {

            String temp_line;
            String[] temp_arr;
            ListItem tempListItem = listItem;
            ArrayList<ListItem> retArray = new ArrayList<>();
            int i = 0;
            try {
                InputStream is = getResources().openRawResource(resourceID);
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(is, Charset.forName("UTF-8"))
                );

                while ((temp_line = br.readLine()) != null) {

                    temp_arr = temp_line.split(",");

                    tempListItem = ListItemFactoryClass.getListItemInstance(typeOfFactoryClass, temp_arr);

                    retArray.add(tempListItem);

                    i++;
                }

            } catch (IOException e) {

                e.printStackTrace();

            }

            return retArray;
        }

    }




}
