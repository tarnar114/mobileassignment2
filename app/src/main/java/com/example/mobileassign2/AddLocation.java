package com.example.mobileassign2;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class AddLocation extends MainActivity {
    EditText longitude,latitude,address;
    Button add;
    @Override
    //on create constructor
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_location);

        longitude=findViewById(R.id.longitudeAdd);
        latitude=findViewById(R.id.latitudeAdd);
        add=findViewById(R.id.addLocation);

        add.setOnClickListener(view-> {
            try {
                addPressed();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    //onclick to add locations based on their longitude and latitude
    void addPressed() throws IOException {
        SQLHelper db=new SQLHelper(AddLocation.this);
        db.addLocation(Double.parseDouble(longitude.getText().toString().trim()),
                Double.parseDouble(latitude.getText().toString().trim()));

        startActivity(new Intent(AddLocation.this,MainActivity.class));
    }
}
