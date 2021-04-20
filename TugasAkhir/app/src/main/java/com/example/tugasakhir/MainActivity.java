package com.example.tugasakhir;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private SensorManager sensorManagerG;
    private Sensor accelerometer;
    private Sensor gyrometer;

    private boolean start;

    private int startstate =0;

    private Button buttonStart,testingButton;
    private Button buttonContact;

    private double Accm ,Wccm , minacc, maxacc, mingy, maxgy, deltaacc,deltagy,angular,minang,maxang,deltaang;


    private int Acccounter ,Wcccounter,foracc,forgyro ;

    private ArrayList<Double> AcceleroM = new ArrayList<Double>();
    private ArrayList<Double> GyroM = new ArrayList<Double>();
    private ArrayList<Double> AngM = new ArrayList<Double>();
    private ArrayList<Double> DeltaAccM = new ArrayList<Double>();
    private ArrayList<Double> DeltaGyM = new ArrayList<Double>();
    private ArrayList<Double> DeltaAng = new ArrayList<Double>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Acccounter =0;
        Wcccounter =0;

        buttonStart = findViewById(R.id.buttonStart);
        buttonContact = findViewById(R.id.buttonContact);



        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (startstate==0) {
                    initializeSensor();

                    startstate = 1;
                    buttonStart.setText("Pause");
                }
                else if(startstate == 1){
                    stopsensor();
                    startstate =2;
                    buttonStart.setText("Resume");
                }

                else if(startstate == 2){
                    initializeSensor();
                    startstate =1;
                    buttonStart.setText("Pause");
                }

            }
        });

        buttonContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, ContactActivity.class);
                MainActivity.this.startActivity(myIntent);
            }
        });

    }

    public void stopsensor(){
        sensorManager.unregisterListener((SensorEventListener) this);
        sensorManagerG.unregisterListener((SensorEventListener) this);
    }

    protected void initializeSensor(){
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        assert sensorManager != null;
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            // success! we have an accelerometer

            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener((SensorEventListener) this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        } else {
            // fail we dont have an accelerometer!
        }

        sensorManagerG = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        assert sensorManagerG != null;
        if (sensorManagerG.getDefaultSensor(Sensor.TYPE_GYROSCOPE) != null){

            gyrometer = sensorManagerG.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
            sensorManagerG.registerListener((SensorEventListener) this, gyrometer,SensorManager.SENSOR_DELAY_NORMAL);
        }

    }


    protected void onPause() {
        super.onPause();

    }




    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            Accm = Math.sqrt((Math.pow(event.values[0],2))+(Math.pow(event.values[1],2))+(Math.pow(event.values[2],2)));
            angular=Math.acos(event.values[1]/Accm)*180/Math.PI;
            AngM.add(angular);
            AcceleroM.add(Accm);
            Acccounter += 1;
            if (Acccounter >6) {
                minacc=Accm; maxacc=0;
                for (foracc=1; foracc<5; foracc++ ){
                    if (minacc>AcceleroM.get(Acccounter-foracc)){
                        minacc =AcceleroM.get(Acccounter-foracc);
                    };
                    if (maxacc<AcceleroM.get(Acccounter-foracc)){
                        maxacc= AcceleroM.get(Acccounter-foracc);
                    }
                }
                deltaacc=maxacc-minacc;
                DeltaAccM.add(deltaacc);

                minang=angular; maxang=angular;
                for (foracc=1; foracc<5; foracc++ ){
                    if (minang>AngM.get(Acccounter-foracc)){
                        minang =AngM.get(Acccounter-foracc);
                    };
                    if (maxang<AngM.get(Acccounter-foracc)){
                        maxang= AngM.get(Acccounter-foracc);
                    }
                }
                deltaang=maxang-minang;
                DeltaAng.add(deltaang);


            }


        }
        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            Wccm = Math.sqrt((Math.pow(event.values[0],2))+(Math.pow(event.values[1],2))+(Math.pow(event.values[2],2)));
            GyroM.add(Wccm);
            Wcccounter +=1;
            if (Wcccounter >6) {
                mingy=Wccm; maxgy=0;
                for (forgyro=1; forgyro<5; forgyro++ ){
                    if (mingy>GyroM.get(Wcccounter-forgyro)){
                        mingy =GyroM.get(Wcccounter-forgyro);
                    };
                    if (maxgy<GyroM.get(Wcccounter-forgyro)){
                        maxgy= GyroM.get(Wcccounter-forgyro);
                    }
                }
                deltagy=maxgy-mingy;
                DeltaGyM.add(deltagy);


            }
        }
        if(Wcccounter>6) {
//            if (Math.abs(Accm - AcceleroM.get(Acccounter - 2)) > 19) {
//                if (Math.abs(Wccm - GyroM.get(Wcccounter - 4)) > 1.5) {
//                    Intent myIntent = new Intent(MainActivity.this, FallDeteted.class);
//                    MainActivity.this.startActivity(myIntent);
//                }
//            }
            falldetection();
        }



    }

    public void falldetection (){
        if (DeltaAccM.get(Acccounter-7)>10 && DeltaGyM.get(Wcccounter-7)>2){
            if(DeltaAng.get(Acccounter-7)>40){

                Intent myIntent = new Intent(MainActivity.this, FallDeteted.class);
                MainActivity.this.startActivity(myIntent);
                stopsensor();
                finish();


            }
        }

    }



}
