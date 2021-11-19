package com.example.mobileassign2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;

public class UpdateLocation extends MainActivity{
    EditText address,longitude,latitude;
    Button update;
    Intent intent;
    @Override
    //oncreate constructor for views, layouts and onclick for updating
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_location);
        intent=getIntent();
        longitude=findViewById(R.id.longitudeUpdate);
        latitude=findViewById(R.id.latitudeUpdate);
        update=findViewById(R.id.updateLocation);
        longitude.setText(intent.getStringExtra("longitude"));
        latitude.setText(intent.getStringExtra("latitude"));


        update.setOnClickListener(view-> {
            try {
                onUpdate();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }
    //update event
    void onUpdate() throws IOException {
        SQLHelper db=new SQLHelper(UpdateLocation.this);
        db.updateLocation(Double.parseDouble(longitude.getText().toString().trim()),
                Double.parseDouble(latitude.getText().toString().trim()),
                intent.getStringExtra("id"));

        startActivity(new Intent(UpdateLocation.this,MainActivity.class));
    }
}
