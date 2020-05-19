package com.example.accidentdetection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FIllDetails extends AppCompatActivity {
    Button btenter;
    EditText etname,etage,etbloodgr,etrelmob1;
    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference myRefUser;
    String latitude, longitude;
    SharedPreferences sharedpreferences;
    private GpsTracker gpsTracker;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_f_ill_details);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        sharedpreferences = getSharedPreferences("Accident", Context.MODE_PRIVATE);
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        getLocation();
        btenter=(Button)findViewById(R.id.btenter);
        etage=(EditText)findViewById(R.id.etage);
        etbloodgr=(EditText)findViewById(R.id.etbloodgr);
        etrelmob1=(EditText)findViewById(R.id.etrelmob1);
        etname=(EditText)findViewById(R.id.etname);
        myRefUser= database.getReference("Users/"+user.getUid());
        btenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
                myRefUser.child("Name").setValue(etname.getText().toString());
                myRefUser.child("Age").setValue(etage.getText().toString());
                myRefUser.child("Accident").setValue(0);
                myRefUser.child("UserLat").setValue(latitude);
                myRefUser.child("UserLon").setValue(longitude);
                myRefUser.child("DName").setValue("N/A");
                myRefUser.child("DUID").setValue("N/A");
                myRefUser.child("PreAcc").setValue("0");
                myRefUser.child("BloodGr").setValue(etbloodgr.getText().toString());
                myRefUser.child("RelMob1").setValue(etrelmob1.getText().toString());
                SharedPreferences.Editor editor = sharedpreferences.edit();

                editor.putInt("stage", 2);
                editor.commit();
                Intent intent=new Intent(FIllDetails.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
    public void getLocation(){
        gpsTracker = new GpsTracker(this);
        if(gpsTracker.canGetLocation()){
            latitude=String.valueOf(gpsTracker.getLatitude());
            longitude=String.valueOf(gpsTracker.getLongitude());
           // tvlatitude.setText(String.valueOf(latitude));
            //tvlongitude.setText(String.valueOf(longitude));

        }else{
            gpsTracker.showSettingsAlert();
        }
    }
}
