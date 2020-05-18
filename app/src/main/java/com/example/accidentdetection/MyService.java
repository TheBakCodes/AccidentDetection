package com.example.accidentdetection;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.widget.Toast;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyService extends Service implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor sensor;
    public FirebaseDatabase database = FirebaseDatabase.getInstance();
    public DatabaseReference myRef = database.getReference("message");


    // CountDownTimer timer;
   //  Boolean flag= false;



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
        myRef.setValue("Stop hela");
     //   if(flag) {
      //      timer.cancel();
      //      Toast.makeText(this, "timer cancel hela", Toast.LENGTH_SHORT).show();
      //      flag =false;
      //  }

        Toast.makeText(this, "Stop", Toast.LENGTH_LONG).show();


    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER)
        {
            double total=Math.sqrt(Math.pow(event.values[0],2)+Math.pow(event.values[1],2)+Math.pow(event.values[2],2));

            if(total>=40.00)
            {


                // timer = new CountDownTimer(30000, 1000) {

                  //  public void onTick(long millisUntilFinished) {
                  //      Toast.makeText(MyService.this, "seconds remaining: " + millisUntilFinished / 1000, Toast.LENGTH_SHORT).show();
                        //countdowntv.setText("seconds remaining: " + millisUntilFinished / 1000);

                   // }



                  //  public void onFinish() {

                       // countdowntv.setText("done!");
                      //  Toast.makeText(MyService.this, "Done", Toast.LENGTH_SHORT).show();




                        Intent dialogIntent = new Intent(this, MyActivity.class);
                        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(dialogIntent);





                        myRef.setValue("Accident");

                        Toast.makeText(MyService.this, "Accident", Toast.LENGTH_LONG).show();

                  //  }

             //   }.start();
            //    flag =true;



               /* cancelbut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        timer.cancel();
                    }
                });

*/

            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
