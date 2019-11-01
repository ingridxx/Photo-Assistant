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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;


public class Body extends Fragment {
    private Button LensButton;
    private static RecyclerAdapterListItem adapter;
    private static ListItem[] slot;
    private ListItem[] tempSlot = new ListItem[6];
    private static int whichSlot;
    public Body() {
        // Required empty public constructor
    }

    public Body(ListItem[] i,int whichSlot) {
        slot = i;
        this.whichSlot = whichSlot;
    }

    public Body(ListItem body,int whichSlot) {
        this.whichSlot = whichSlot;
        new AsyncTaskLoadPossibleLenses().execute(slot[0]);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        View rootView = inflater.inflate(R.layout.fragment_body, container, false);
        return rootView;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 1. get a reference to recyclerView
        final TextView tv_slot = view.findViewById(R.id.tv_slot);
        tv_slot.setText("Slot " + whichSlot);
        final TextView tv_slot_selected = view.findViewById(R.id.tv_selected_slot);

        RecyclerView recyclerView = view.findViewById(R.id.body_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new RecyclerAdapterListItem(getContext(), MainActivity.body_al,  new RecyclerViewOnClickListener() {
            @Override
            public void onItemClick(ListItem item) {
                Toast.makeText(getContext(), "Item Clicked", Toast.LENGTH_LONG).show();
                tv_slot_selected.setText(item.getPartName());
                tempSlot[0] = item;
            }
        });
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        LensButton = getActivity().findViewById(R.id.lens_select_button);
        LensButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tempSlot[0] !=null) {
                    new AsyncTaskLoadPossibleLenses().execute(tempSlot[0]);
                }
            }
        });
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }


    public void startLensFragment(ArrayList<ListItem> listItems){

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fl,new Lens(tempSlot,listItems,whichSlot)).addToBackStack("lens").commit();

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

                    adapter.getFilter().filter(newText.toLowerCase().trim());
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
            startLensFragment(listItems);
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
