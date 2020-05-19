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
import android.widget.Toast;
import android.os.Bundle;
import android.view.View;

import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyService extends Service implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor sensor;
    public FirebaseDatabase database = FirebaseDatabase.getInstance();
    public DatabaseReference myRef,myRef1;
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
        //
        /*Toast.makeText(this, "Start", Toast.LENGTH_LONG).show();
        System.out.println("Service aya");
        try {
            sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener((SensorEventListener) this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
        }
        catch (Exception e)
        {
            System.out.println("Service Error:"+e);
        }*/
        throw new UnsupportedOperationException("Not yet implemented");
        //return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "Start", Toast.LENGTH_LONG).show();

      //  if(flag) {
      //      timer.cancel();
      //      Toast.makeText(this, "timer cancel hela", Toast.LENGTH_SHORT).show();
      //      flag =false;
       // }

        System.out.println("Service aya");
        //startServiceWithNotification();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        myRef= database.getReference("Users/"+user.getUid()+"/Accident");
        myRef1= database.getReference("Users/"+user.getUid()+"/PreAcc");
        sharedpreferences = getSharedPreferences("Accident", Context.MODE_PRIVATE);
        editor= sharedpreferences.edit();
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

            if(total>=40.00)
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

   /* void startServiceWithNotification() {
        if (isServiceRunning) return;
        isServiceRunning = true;

        Intent notificationIntent = new Intent(getApplicationContext(), MyActivity.class);
        notificationIntent.setAction(C.ACTION_MAIN);  // A string containing the action name
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent contentPendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.cosmitoicon);

        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setTicker(getResources().getString(R.string.app_name))
                .setContentText("App is Running")
                .setSmallIcon(R.drawable.cosmitoicon)
                .setLargeIcon(Bitmap.createScaledBitmap(icon, 128, 128, false))
                .setContentIntent(contentPendingIntent)
                .setOngoing(true)
//                .setDeleteIntent(contentPendingIntent)  // if needed
                .build();
        notification.flags = notification.flags | Notification.FLAG_NO_CLEAR;     // NO_CLEAR makes the notification stay when the user performs a "delete all" command
        startForeground(NOTIFICATION_ID, notification);
    }

    void stopMyService() {
        stopForeground(true);
        stopSelf();
        isServiceRunning = false;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getAction().equals(com.example.accidentdetection.Restartter.ACTION_START_SERVICE)) {
            startServiceWithNotification();
        }
        else stopMyService();
        return START_STICKY;
    }*/
}
