package com.example.photoassistant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final int h = 10;
    Button bodyButton;
    Button lensButton;
    Button weatherButton;
    Button calcButton;
    Button sunButton;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.lens_rv);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

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
                getSupportFragmentManager().beginTransaction().replace(R.id.fl,new weather()).commit();
            }
        });
        calcButton = findViewById(R.id.calcButton);
        calcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fl,new Calculator()).commit();
            }
        });
        sunButton = findViewById(R.id.sunButton);
        sunButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fl,new sun()).commit();
            }
        });




    }

    public void LoadLenses(){

        ListItemLens tempLens = null; // use to set array into a temp item
        ArrayList<ListItemLens> ar = new ArrayList<>();
        int i=0;
        //for each row in cvs
        ar.add(new ListItemLens("10","1000","cannon Lens"));
        while (i < ar.size()) {

            ar.add(new ListItemLens(ar.get(i).getMinZoom(), ar.get(i).getMaxZoom(), ar.get(i).getPartName()));
            i++;
            if (i==1000){
                break;
            }
        }
        adapter = new RecycleAdapterLens(ar);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

}
