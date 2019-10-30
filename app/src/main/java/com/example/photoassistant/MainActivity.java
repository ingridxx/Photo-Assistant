package com.example.photoassistant;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

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
    public static ArrayList<ListItem> lens_al = new ArrayList<>();
    public static ArrayList<ListItem> body_al = new ArrayList<>();
    public static String currFrag = "sun";
    public static String prevFrag;
    public static Stack fragmentStack = new Stack();

    public static ListItem[] Slot1 = new ListItem[2];
    public static ListItem[] Slot2 = new ListItem[2];
    public static ListItem[] Slot3 = new ListItem[2];
    public static ListItem[] Slot4 = new ListItem[2];
    public static int WhichSlot;
    ListItem[] cacheArray = null;
    public static ArrayList<ListItem[]> currBody;

    public static void addSlot(int whichSlot, ListItem[] li) {

        switch (whichSlot) {
            case 1:
                Slot1 = li;
                break;
            case 2:
                Slot2 = li;
                break;
            case 3:
                Slot3 = li;
                break;
            case 4:
                Slot4 = li;
                break;
        }

    }
       public static ListItemBody getBodySlot(int whichSlot) {
           ListItemBody retSlot = null;
           if (resolveSlot(whichSlot)[0] != null) {
            retSlot = (ListItemBody) resolveSlot(whichSlot)[0];
            retSlot.toString();
        }
           return retSlot;
    }

    public static ListItemLens getLensSlot(int whichSlot) {

        ListItemLens retSlot = null;
        if (resolveSlot(whichSlot)[1] != null) {
            retSlot = (ListItemLens) resolveSlot(whichSlot)[1];
            retSlot.toString();
        }
        return retSlot;
    }

    private static ListItem[] resolveSlot(int whichSlot) {

        switch (whichSlot) {
            case 1:
                return Slot1;
            case 2:
                return Slot2;
            case 3:
                return Slot3;
            case 4:
                return Slot4;
        }
        return null;

    }

    public ArrayList<String> permissionsToRequest;
    public ArrayList<String> permissionsRejected = new ArrayList<>();
    public ArrayList<String> permissions = new ArrayList<>();
    public final static int ALL_PERMISSIONS_RESULT = 101;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    GPS gps=new GPS(MainActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
        permissionsToRequest = findUnAskedPermissions(permissions);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }

        new ProcessDataFromArrays().execute();

        currBody = new ArrayList<>();


        bodyButton = findViewById(R.id.bodyButton);
        bodyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                changeFragBody();

            }
        });

//        lensButton = findViewById(R.id.lensButton);
//        lensButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                fragmentStack.push(new Lens());
//                getSupportFragmentManager().beginTransaction().replace(R.id.fl,new Lens()).commit();
//
//            }
//        });
        weatherButton = findViewById(R.id.weatherButton);
        weatherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentStack.push(new Weather());
                getSupportFragmentManager().beginTransaction().replace(R.id.fl,new Weather()).commit();
            }
        });
        calcButton = findViewById(R.id.calcButton);
        calcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentStack.push(new Calculator());
                getSupportFragmentManager().beginTransaction().replace(R.id.fl,new Calculator()).commit();
            }
        });
        sunButton = findViewById(R.id.sunButton);
        sunButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentStack.push(new Sun());
                getSupportFragmentManager().beginTransaction().replace(R.id.fl,new Sun()).commit();
            }
        });


        if(fragmentStack.empty()) {fragmentStack.push(new Sun());getSupportFragmentManager().beginTransaction().replace(R.id.fl,new Sun()).commit();}
        //getSupportFragmentManager().beginTransaction().replace(R.id.fl,new Sun()).commit();

    }



    @Override
    public void onBackPressed() {
        Log.d("myDebugTag", "onBackPressed:");
        fragmentStack.pop();
        if(!fragmentStack.empty())getSupportFragmentManager().beginTransaction().replace(R.id.fl,(Fragment)fragmentStack.peek()).commit();
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


    private void setOverlayResources() {
        TextView tvTS = findViewById(R.id.tv_ImageButtonTS);
        TextView tvTE = findViewById(R.id.tv_ImageButtonTE);
        TextView tvBS = findViewById(R.id.tv_ImageButtonBS);
        TextView tvBE = findViewById(R.id.tv_ImageButtonBE);


        if (Slot1[0] != null) {
            tvTS.setText(Slot1[0].getPartName());
        } else {
            tvTS.setText("Not Selected");
        }

        if (Slot2[0] != null) {
            tvTE.setText(Slot2[0].getPartName());
        } else {
            tvTE.setText("Not Selected");
        }

        if (Slot3[0] != null) {
            tvBS.setText(Slot3[0].getPartName());
        } else {
            tvBS.setText("Not Selected");
        }

        if (Slot4[0] != null) {
            tvBE.setText(Slot4[0].getPartName());
        } else {
            tvBE.setText("Not Selected");
        }

    }

    public void changeFragBody() {
        prevFrag = currFrag;
        currFrag = "body";

        ImageButton ib_topStart = findViewById(R.id.imageButtonTopStart);
        ImageButton ib_topEnd = findViewById(R.id.imageButtonTopEnd);
        ImageButton ib_bottomStart = findViewById(R.id.imageButtonBottomStart);
        ImageButton ib_bottomEnd = findViewById(R.id.imageButtonBottomEnd);


        setOverlayResources();

        final View view = findViewById(R.id.body_selection_overlay_include);
        view.setVisibility(View.VISIBLE);
        ib_topStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.INVISIBLE);
                if (Slot1[0] == null) {
                    WhichSlot =1;
                    getSupportFragmentManager().beginTransaction().add(R.id.fl, new Body(Slot1, 1)).addToBackStack("body").commit();
                }
            }
        });
        ib_topStart.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (Slot1 != null) {
                    cacheArray = Slot1.clone();
                    Slot1[0] = null;
                    Slot1[1] = null;
                    setOverlayResources();

                    Snackbar.make(v, "Item Deleted", Snackbar.LENGTH_LONG).setAction("Undo?", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Slot1 = cacheArray.clone();
                            setOverlayResources();
                            cacheArray[0] = null;
                            cacheArray[1] = null;

                        }

                    }).show();
                }
                return true;
            }
        });
        ib_topEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.INVISIBLE);
                if (Slot2[0] == null) {
                    WhichSlot =2;
                    getSupportFragmentManager().beginTransaction().add(R.id.fl, new Body(Slot2, 2)).addToBackStack("body").commit();
                }
            }
        });
        ib_bottomStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.INVISIBLE);
                if (Slot3[0] == null) {
                    WhichSlot =3;
                    getSupportFragmentManager().beginTransaction().add(R.id.fl, new Body(Slot3, 3)).addToBackStack("body").commit();
                }
            }
        });
        ib_bottomEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.INVISIBLE);
                if (Slot4[0] == null) {
                    WhichSlot =4;
                    getSupportFragmentManager().beginTransaction().add(R.id.fl, new Body(Slot4, 4)).addToBackStack("body").commit();
                }
            }
        });
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
            super.onPostExecute(listItems);
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

                    Log.d("debug", "ProcessArrays: " + i);
                    i++;
                }

            } catch (IOException e) {

                e.printStackTrace();

            }

            return retArray;
        }

    }


}
