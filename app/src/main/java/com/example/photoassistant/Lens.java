package com.example.photoassistant;

import android.content.pm.ActivityInfo;
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

import java.util.ArrayList;

public class Lens extends Fragment {

    private static ArrayAdapter<String> arrayAdapterString;
    ListItem slot[];
    private RecyclerView recyclerView;
    private RecyclerAdapterListItem ra;
    private Spinner lens_spinner;
    private ArrayList<String> favouritesString;
    private ArrayList<ListItem> favourites;
    private ArrayList<ListItem> arrayToSort;
    private int currentSlot;


    public Lens() {
        // Required empty public constructor
    }
//    public Lens(ListItem[] slot) {
//        arrayToSort = MainActivity.lens_al;
//        this.slot = slot;
//        // Required empty public constructor
//    }

    public Lens(ListItem[] slot, ArrayList<ListItem> lia, int whichSlot) {

        this.slot = slot;
        Log.d("debugone", "onItemSelected:" + slot.length);
        arrayToSort = lia;
        currentSlot = whichSlot;
        // Required empty public constructor
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
            increasedi++;
            favouritesString.add(i, favourites.get(i).getPartName());
            slot[increasedi] = favourites.get(i);
        }
        arrayAdapterString.notifyDataSetChanged();

    }


    public void addLensToArrays(ListItem lensToAdd) {

        if (!favourites.contains(lensToAdd)) {
            if (favourites.size() < 5) {
                favourites.add(0, lensToAdd);
            } else {
                favourites.add(0, lensToAdd);
                favourites.remove(5);
            }
        }

    }

    public void swapSpinnerItemToStart(ListItem lensToAdd, int position) {

        System.out.println("Lens " + lensToAdd.toString() + " : Pos " + position);

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        favourites = new ArrayList<ListItem>();
        favouritesString = new ArrayList<String>();
        //Gson gson = new Gson();
        //ListItemLens obj;

        super.onCreate(savedInstanceState);
    }


    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

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

        ra = new RecyclerAdapterListItem(getContext(), arrayToSort, new RecyclerViewOnClickListener() {
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
                int where;
                if (slot[1] != null)
                    for (int i = 1; i < slot.length; i++) {
                        String ret = parent.getItemAtPosition(position).toString();
                        Log.d("debugme", "onItemSelected: " + ret + "");
                        Log.d("debugme", "onItemSelected: " + slot[i] + "");

                        if (slot[i].getPartName().equals(ret)) {
                            where = i;
                            ListItem newItem = slot[i];
                            ListItem tempItem;
                            tempItem = slot[1];
                            slot[1] = newItem;
                            slot[i] = tempItem;
                            updateArrayAdapter();
                        }

                    }



            }

            public void onNothingSelected(AdapterView<?> parent) {

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

                if (false) ;//(newText.length() == 0 || newText.isEmpty()){}
                else {

                    ra.getFilter().filter(newText.toLowerCase().trim());
                }
                return true;
            }
        });
    }


}
