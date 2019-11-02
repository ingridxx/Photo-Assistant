package com.example.photoassistant;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Lens extends Fragment {

    private static ArrayAdapter<String> arrayAdapterString;
    ListItem slot[];
    private RecyclerView recyclerView;
    private RecyclerAdapterListItem ra;
    private Spinner lens_spinner;
    private ArrayList<String> favouritesString;
    private ArrayList<ListItem> favourites;
    private ListItem bodyToSort;
    private ArrayList<ListItem> itemsToDisplay;
    int currentPosSelcted;
    private int currentSlot;
    private View thisView;


    public Lens() {
        // Required empty public constructor
    }
//    public Lens(ListItem[] slot) {
//        arrayToSort = MainActivity.lens_al;
//        this.slot = slot;
//        // Required empty public constructor
//    }

    public Lens(ListItem[] slot, ListItemBody li, int whichSlot) {

        this.slot = slot;
        bodyToSort = li;
        currentSlot = whichSlot;
        // Required empty public constructor
    }

    private void checkSavedLenses() {
        ListItem tempItem;
        for(int i =1; i < 1+BodySelector.howManySavedLenses;i++){
            tempItem = BodySelector.getLensSlot(currentSlot,i);
            if (tempItem != null){
                System.out.println(tempItem.toString());
                favourites.add(tempItem);
                updateArrayAdapter();
            }

        }

    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_lens, container, false);
        return rootView;

    }

    public void updateArrayAdapter() {

        favouritesString.clear();
        int increasedi = 1;
        for (int i = 0; i < favourites.size(); i++) {

            favouritesString.add(i, favourites.get(i).getPartName());
            Log.d("SlotDebug", "updateArrayAdapter: " + slot[increasedi] + " length - " + slot.length + " i - " + i);
            slot[increasedi] = favourites.get(i);
            increasedi++;
        }
        arrayAdapterString.notifyDataSetChanged();

//        for (int i =0 ;i < favourites.size();i++){
//            if (favourites.get(i) !=null){
//                Log.d("aaaa", "addLensToArrays: index " + i + " - " + favourites.get(i).toString());
//            }
//        }
    }


    public void addLensToArrays(ListItem lensToAdd) {

        Log.d("addLensToArray", "" + favourites.size());
        if (!favourites.contains(lensToAdd)) {
            if (favourites.size() < 5) {
                Log.d("addLensToArray", "< 5");
                favourites.add(0, lensToAdd);
            } else {
                Log.d("addLensToArray", "MORE THAN 5");
                favourites.add(0, lensToAdd);
                favourites.remove(5);

            }
        }

    }

    private void swapSpinnerItemToStart(int position) {

        if (slot[1] != null && slot[2] != null && position!=0) { // if the first spots arent empty

            Collections.swap(favourites,position,0);
            lens_spinner.getSelectedItem();
            updateArrayAdapter();

        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        favourites = new ArrayList<ListItem>();
        favouritesString = new ArrayList<String>();
        //Gson gson = new Gson();
        //ListItemLens obj;
        checkSavedLenses();
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onDestroy() {

        swapSpinnerItemToStart(currentPosSelcted);

        for(int i =0; i<BodySelector.howManySavedLenses;i++){

            BodySelector.Slots[currentSlot][i].getPartName();

        }

        super.onDestroy();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        thisView = view;
        itemsToDisplay = new ArrayList<>();
        new AsyncTaskLoadPossibleLenses().execute(bodyToSort);

        super.onViewCreated(view, savedInstanceState);
    }

    private void InitiateUI(@NonNull View view) {
        lens_spinner = view.findViewById(R.id.spinner1);
        arrayAdapterString = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1, favouritesString);
        //arrayAdapter = new ArrayAdapter<ListItemLens>(view.getContext(), android.R.layout.simple_list_item_1,favourites);
        lens_spinner.setAdapter(arrayAdapterString);
        updateArrayAdapter();
        recyclerView = (RecyclerView) view.findViewById(R.id.lens_rv);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        // use a linear layout manager
        RecyclerView.LayoutManager loutmn = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(loutmn);

        final Button doneButton = getActivity().findViewById(R.id.doneButton);
        if (favourites != null) doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ppp", "onClick: Lens Done Button");
                BodySelector.addSlot(currentSlot, slot);
                MainActivity.fragmentStack.pop();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fl, new BodySelector()).commit();
                //getActivity().getSupportFragmentManager().popBackStack("lens", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                //getActivity().getSupportFragmentManager().popBackStack("body", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });
        else {
            doneButton.setVisibility(View.INVISIBLE);
        }

        ra = new RecyclerAdapterListItem(getContext(), itemsToDisplay, new RecyclerViewOnClickListener() {
            @Override
            public void onItemClick(ListItem item) {

                doneButton.setVisibility(View.VISIBLE);
                addLensToArrays(item);
                updateArrayAdapter();

            }
        });

        recyclerView.setAdapter(ra);
        ra.notifyDataSetChanged();


        lens_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentPosSelcted = position;
                System.out.println(" THE CURRENT ITEM" + favourites.get(position).toString());
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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

                if (false) ;//(newText.length() == 0 || newText.isEmpty()){}
                else {

                    ra.getFilter().filter(newText.toLowerCase().trim());
                }
                return true;
            }
        });

    }


    @SuppressLint("StaticFieldLeak")
    public class AsyncTaskLoadPossibleLenses extends AsyncTask<ListItem, Integer, ArrayList<ListItem>> {


        @Override
        protected void onPostExecute(ArrayList<ListItem> listItems) {
            Log.d("proc1", "onPostExecute: ");
            itemsToDisplay.addAll(listItems);
            InitiateUI(thisView);
        }

        @Override
        protected ArrayList<ListItem> doInBackground(ListItem... BodyListItem) {
            ArrayList<ListItem> returnArray = new ArrayList<>();
            String temp_line;
            String[] temp_arr;

            try {
                InputStream is = getResources().openRawResource(ListItemCombination.resourceID);
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(is, Charset.forName("UTF-8"))
                );
                while ((temp_line = br.readLine()) != null) {
                    if (temp_line.contains(BodyListItem[0].getPartName())) {
                        temp_arr = temp_line.split(",");
                        returnArray.add(ListItemFactoryClass.getListItemInstance("Combo", temp_arr));
                    }

                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return returnArray;
        }
    }


}
