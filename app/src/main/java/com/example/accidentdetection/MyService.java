package com.example.accidentdetection;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.ContentValues.TAG;

public class MyService extends Service implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor sensor;
    public FirebaseDatabase database = FirebaseDatabase.getInstance();
    public DatabaseReference myRef,myRef1,myRefDUID;
    FirebaseAuth mAuth;
    FirebaseUser user;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    Context context=this;
    String ACTION_START_SERVICE="com.example.accidentdetection";

    // CountDownTimer timer;
   //  Boolean flag= false;

    static final int NOTIFICATION_ID = 543;

    public static boolean isServiceRunning = false;

    public MyService() {


    }



    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
        //return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "Start", Toast.LENGTH_LONG).show();

        System.out.println("Service aya");
        //startServiceWithNotification();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        myRef= database.getReference("Users/"+user.getUid()+"/Accident");
        myRef1= database.getReference("Users/"+user.getUid()+"/PreAcc");
        myRefDUID=database.getReference("Users/"+user.getUid()+"/DUID");
        sharedpreferences = getSharedPreferences("Accident", Context.MODE_PRIVATE);
        editor= sharedpreferences.edit();
        // Read from the database
       /* myRefDUID.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                editor.putString("DUID", value);
                //Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }


        });*/
        try {
            sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener((SensorEventListener) this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
        }
        catch (Exception e)
        {
            System.out.println("Service Error:"+e);
        }




    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(this);
        /*Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, Restartter.class);
        this.sendBroadcast(broadcastIntent);*/
        //isServiceRunning = false;


    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER)
        {
            double total=Math.sqrt(Math.pow(event.values[0],2)+Math.pow(event.values[1],2)+Math.pow(event.values[2],2));

            if(total>=60.00)
            {

                myRef.setValue("1");
                myRef1.setValue("1");
                Intent dialogIntent = new Intent(this, alertactivity.class);
                dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(dialogIntent);




            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


}
