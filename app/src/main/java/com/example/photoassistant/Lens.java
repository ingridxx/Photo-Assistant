package com.example.photoassistant;

import android.content.SharedPreferences;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

public class Lens extends Fragment{

    private RecyclerView recyclerView;
    private RecycleAdapterLens ra;
    private Spinner lens_spinner;
    private static ArrayAdapter<String> arrayAdapterString;
    private static ArrayList<String> favouritesString;
    private static ArrayList<ListItemLens> favourites;
    private SharedPreferences.Editor editor;
    private static String LENS_TAG = "LENS_SAVE_TAG";
    private SharedPreferences mPrefs;




    public Lens() {
        // Required empty public constructor
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        View rootView = inflater.inflate(R.layout.fragment_lens, container, false);
        return rootView;

    }

    public static void updateArrayAdapter(){

        arrayAdapterString.notifyDataSetChanged();

    }
    public static void addLensToArrays(ListItemLens lensToAdd){




        if(favourites.size() < 5 ){
            favourites.add(0,lensToAdd);
            favouritesString.add(0,lensToAdd.getPartName());
        } else {
            favourites.add(0,lensToAdd);
            favouritesString.add(0,lensToAdd.getPartName());
            favouritesString.remove(5);
            favourites.remove(5);

        }

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        favourites = new ArrayList<ListItemLens>();
        favouritesString = new ArrayList<String>();
        mPrefs = getActivity().getSharedPreferences(LENS_TAG,0);
        Gson gson = new Gson();
        ListItemLens obj;
        Log.d("DebuggingTag", mPrefs.getString("MyObject0","null"));


        if (mPrefs.getString("MyObject0","null") != "null"){
            int tempCounter = 0;
            String keyValue = "MyObject" + tempCounter;
            Log.d("DebuggingTag", "inside if loop");
            while (mPrefs.getString(keyValue,"null")!="null"){
                String json = mPrefs.getString(keyValue, "null");
                keyValue = "MyObject" + tempCounter;
                tempCounter++;
                obj = gson.fromJson(json, ListItemLens.class);
                Log.d("DebuggingTag", "inside while loop --- " + obj.getPartName());

                addLensToArrays(obj);

            }

        }

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        Gson gson = new Gson();
        editor = getActivity().getSharedPreferences(LENS_TAG,0).edit();
        for (int i = 0; i < favourites.size(); i++) {
            String json = gson.toJson(favourites.get(i)); // myObject - instance of MyObject
            editor.putString("MyObject"+ i, json);
        }

        editor.apply();
        super.onDestroy();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        lens_spinner = view.findViewById(R.id.spinner1);
        arrayAdapterString = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1,favouritesString);
        //arrayAdapter = new ArrayAdapter<ListItemLens>(view.getContext(), android.R.layout.simple_list_item_1,favourites);
        lens_spinner.setAdapter(arrayAdapterString);
        updateArrayAdapter();
        recyclerView = (RecyclerView) view.findViewById(R.id.lens_rv);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        // use a linear layout manager
        RecyclerView.LayoutManager loutmn = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(loutmn);
        ra = new RecycleAdapterLens(getContext(),MainActivity.lens_al);
        recyclerView.setAdapter(ra);
        ra.notifyDataSetChanged();

        lens_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                //   tv.setText(parent.getItemAtPosition(position).toString()); //this is your selected item
                Toast.makeText(getContext(), parent.getItemAtPosition(position).toString(), Toast.LENGTH_LONG).show();

            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        super.onViewCreated(view, savedInstanceState);

    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.search_bar, menu);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.search_bar));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                return true;

            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (false);//(newText.length() == 0 || newText.isEmpty()){}
                else {

                    ra.getFilter().filter(newText.toLowerCase().trim());
                }
                return true;
            }
        });
    }


}
