package com.example.tugasakhir;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;



public class SendMessage extends AppCompatActivity {

    private static final long START_TIME_IN_MILLIS = 5000;

    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;
    private CountDownTimer mCountDownTimer;


    DatabaseHelper mDatabasehelper = new DatabaseHelper(this);

    private static final int REQUEST_LOCATION = 1;
    double longitude;
    double latitude;
    LocationManager locationManager;
    int n = 1;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);



        Cursor data = mDatabasehelper.getData();
        getLocation();
        while(data.moveToNext()){

            //get the value from the database in column 1
            //then add it to the ArrayList
            sendEmail(data.getString(1));
            Toast.makeText(this, data.getString(1), Toast.LENGTH_SHORT).show();
        }

        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;

            }
            @Override
            public void onFinish() { //if timer ends
                Intent myIntent = new Intent(SendMessage.this, MainActivity.class);
                SendMessage.this.startActivity(myIntent);
            }
        }.start();



    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(
                SendMessage.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                SendMessage.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (locationGPS != null) {
                double lat = locationGPS.getLatitude();
                double longi = locationGPS.getLongitude();
                latitude = lat;
                longitude = longi;
            } else {
                Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected void sendEmail(String Emailto) {

        //Getting content for email
        String email = Emailto;
        String subject = "Fall Detected";
        String message = "Fall Detected from user.longitude :" + longitude + "      Latitude : " + latitude + ". google.com/maps/@"+latitude+","+longitude+",20z";

        //Creating SendMail object
        SendMail sm = new SendMail(this, email, subject, message);

        //Executing sendmail to send email
        sm.execute();
    }


}
