package com.example.mobileassign2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import objects.Location;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    SQLHelper db;
    ArrayList<Location> availableLocations;
    recyclerView LocationRecycler;


    @Override
    //oncreate constructor for layout and views, creates data display for locations and sets adapter
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton add=(ImageButton) findViewById(R.id.addLocation);
        add.setOnClickListener(view->startActivity(new Intent(MainActivity.this,AddLocation.class)));

        recyclerView=findViewById(R.id.main_content);
        db=new SQLHelper(MainActivity.this);
        availableLocations=new ArrayList<>();
        displayData();

        LocationRecycler=new recyclerView(MainActivity.this,availableLocations);
        recyclerView.setAdapter(LocationRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));


    }
    void displayData(){
        Cursor cursor =db.readAll();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "no data.", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                availableLocations.add(new Location(cursor.getString(0),cursor.getInt(1),cursor.getInt(2),cursor.getString(3)));
            }
        }
    }
}