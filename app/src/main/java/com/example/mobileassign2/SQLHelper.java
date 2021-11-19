package com.example.mobileassign2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class SQLHelper extends SQLiteOpenHelper {
    Geocoder geocoder;
    List<Address> addresses;
    private Context context;
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "LocationDB";
    private static final String TABLE_NAME = "LocationDB";
    private static final String COL_ID = "_id";
    private static final String COL_LONGITUDE = "longitude";
    private static final String COL_LATITUDE = "latitude";
    private static final String COL_ADDRESS="notes_subtitle";
    //upgrade db version
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(db);
    }
    //constructor
    public SQLHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        geocoder=new Geocoder(context, Locale.getDefault());
        this.context = context;
    }
    //oncreate constructor to init the table
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOCATION_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COL_ID + " INTEGER PRIMARY KEY," + COL_LONGITUDE + " DOUBLE,"
                + COL_LATITUDE +" DOUBLE,"
                + COL_ADDRESS + " TEXT" + ")";
        db.execSQL(CREATE_LOCATION_TABLE);
    }
    //add function for the table and geocodes the long/lat values
    void addLocation(double longitude,double latitude) throws IOException {
        String address = "no address",city="no city",country="no country",postalCode="no postalcode";
        SQLiteDatabase db=this.getReadableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(COL_LATITUDE,latitude);
        cv.put(COL_LONGITUDE,longitude);
        addresses=geocoder.getFromLocation(longitude,latitude,1);
        if (addresses!=null&&addresses.size()>0){
             address = addresses.get(0).getAddressLine(0);
             city = addresses.get(0).getLocality();
             country = addresses.get(0).getCountryName();
             postalCode = addresses.get(0).getPostalCode();
        }
        String localTxt=address+", "+country+", "+postalCode;
        cv.put(COL_ADDRESS,localTxt);
        long res=db.insert(TABLE_NAME,null,cv);
        if (res==-1){
            Toast.makeText(context,"failed",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context,"success",Toast.LENGTH_SHORT).show();
        }
    }
    //updates values in table
    void updateLocation(double longitude,double latitude,String id) throws IOException {
        String address = "no address",city="no city",country="no country",postalCode="no postalcode";
        SQLiteDatabase db=this.getReadableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(COL_LATITUDE,latitude);
        cv.put(COL_LONGITUDE,longitude);
        addresses=geocoder.getFromLocation(longitude,latitude,1);
        if (addresses!=null&&addresses.size()>0){
            address = addresses.get(0).getAddressLine(0);
            country = addresses.get(0).getCountryName();
            postalCode = addresses.get(0).getPostalCode();
        }
        String localTxt=address+", "+country+", "+postalCode;
        cv.put(COL_ADDRESS,localTxt);
        long res=db.update(TABLE_NAME,cv,"_id = ?",new String[]{id});
        if (res==-1){
            Toast.makeText(context,"failed",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context,"success",Toast.LENGTH_SHORT).show();
        }
    }
    //returns all values in table
    Cursor readAll(){
        String query="SELECT * FROM "+TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
            Log.d("cursor: ", String.valueOf(cursor));
        }
        return cursor;
    }
    //deletes location from table where it is a address
    void deleteLocation(String address){
        SQLiteDatabase db=this.getReadableDatabase();
        String[] strParams=new String[]{address};
        String DEL_LOCATION = "DELETE FROM " + TABLE_NAME + " WHERE " + COL_ADDRESS + " = ?";

        db.execSQL(DEL_LOCATION,strParams);

    }
}
