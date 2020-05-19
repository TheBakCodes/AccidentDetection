package com.example.accidentdetection;


import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Context;

import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor sensor;
    private Button btstart,btstop;
    Button accibutton;
    FirebaseAuth mAuth;
    FirebaseUser user;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    public FirebaseDatabase database = FirebaseDatabase.getInstance();
    public DatabaseReference myRef ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        myRef= database.getReference("Users/"+user.getUid()+"/Accident");
        setContentView(R.layout.activity_main);
        btstart=(Button)findViewById(R.id.btstart);
        btstop=(Button)findViewById(R.id.btstop);
        accibutton = (Button)findViewById(R.id.accibutton) ;

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener((SensorEventListener) this, sensor,SensorManager.SENSOR_DELAY_FASTEST );
        //startService(new Intent(this, MyService.class));
        accibutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.setValue("1");

                //Toast.makeText(this, "Accident", Toast.LENGTH_LONG).show();
            }
        });
        startkaro();
        if(!isMyServiceRunning(MyService.class))
        btstart.setVisibility(View.INVISIBLE);
        else
        btstart.setVisibility(View.INVISIBLE);
        btstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startkaro();
                //myRef.setValue("1");
                btstart.setVisibility(View.INVISIBLE);
                btstop.setVisibility(View.VISIBLE);

            }
        });



        btstop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopkaro();
               // myRef.setValue("0");
                btstart.setVisibility(View.VISIBLE);
                btstop.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void startkaro()
    {
        startService(new Intent(this, MyService.class));
    }
    public void stopkaro()
    {
        stopService(new Intent(this, MyService.class));
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER)
        {

            double total=Math.sqrt(Math.pow(event.values[0],2)+Math.pow(event.values[1],2)+Math.pow(event.values[2],2));



        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);

    }

    public boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, MyService.class));
    }
}


