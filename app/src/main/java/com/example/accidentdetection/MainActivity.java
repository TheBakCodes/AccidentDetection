package com.example.accidentdetection;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor sensor;
    private Button btstart,btstop;
    TextView x,y,z,totaltv;
    Button accibutton;

    public FirebaseDatabase database = FirebaseDatabase.getInstance();
    public DatabaseReference myRef = database.getReference("message");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        x=(TextView)findViewById(R.id.textView2);
        y=(TextView)findViewById(R.id.textView);
        z=(TextView)findViewById(R.id.textView3);
        btstart=(Button)findViewById(R.id.btstart);
        btstop=(Button)findViewById(R.id.btstop);
        accibutton = (Button)findViewById(R.id.accibutton) ;
        totaltv=(TextView)findViewById(R.id.textView4);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener((SensorEventListener) this, sensor,SensorManager.SENSOR_DELAY_FASTEST );

        accibutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.setValue("Accident");

                //Toast.makeText(this, "Accident", Toast.LENGTH_LONG).show();
            }
        });


        btstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startkaro();
            }
        });

       startkaro();

        btstop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopkaro();
            }
        });
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");
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
            x.setText("x:"+event.values[0]);
            y.setText("y:"+event.values[1]);
            z.setText("z:"+event.values[2]);
            double total=Math.sqrt(Math.pow(event.values[0],2)+Math.pow(event.values[1],2)+Math.pow(event.values[2],2));

            totaltv.setText("Total:"+total);

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
}

