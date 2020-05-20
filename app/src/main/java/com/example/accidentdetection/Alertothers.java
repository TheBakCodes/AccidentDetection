package com.example.accidentdetection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.ContentValues.TAG;

public class Alertothers extends AppCompatActivity {
    TextView countdowntv;
    public FirebaseDatabase database = FirebaseDatabase.getInstance();
    public DatabaseReference myRef,myRefambu,myRefAge,myRefBloodgr,lat,lon,name,relmob1,puid,accheck,myRefdname,myRefduid,myRefaccident;
    FirebaseAuth mAuth;
    FirebaseUser user;
    Button cancelbut ;
    String Duid="";
    SharedPreferences sharedpreferences;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alertothers);
        sharedpreferences = getSharedPreferences("Accident", Context.MODE_PRIVATE);
        dialog = new ProgressDialog(this); // this = YourActivity
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        countdowntv = (TextView)findViewById(R.id.tvcountdown);
        cancelbut = (Button)findViewById(R.id.btcancel);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        //myRef= database.getReference("Users/"+user.getUid()+"/DUID");
        myRefduid=database.getReference("Users/"+user.getUid()+"/DUID");
        myRefdname=database.getReference("Users/"+user.getUid()+"/DName");
        myRefaccident=database.getReference("Users/"+user.getUid()+"/Accident");
        myRefdname=database.getReference("Users/"+user.getUid()+"/DName");
        //myRefDUID=database.getReference("Users/"+user.getUid()+"/DUID");
        cancelbut.setVisibility(View.VISIBLE);
        Duid=sharedpreferences.getString("DUID", "0");
       // cancelbut.setVisibility(View.INVISIBLE);
        myRefambu=database.getReference("Ambulances/"+Duid+"/AccCheck");
        // myRefa=database.getReference("Ambulances/"+Duid);
        myRefambu.setValue("1");
        countdowntv.setText("Thank You For Helping.");

        //cancelbut.setVisibility(View.INVISIBLE);

        cancelbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.show();
                dialog.setTitle("Cancelling");
                dialog.setMessage("Please wait...");
                Duid=sharedpreferences.getString("DUID", "0");
                if(Duid.length()>=10)
                {
                    //myRefambu=database.getReference("Ambulances/"+Duid);
                    myRefAge=database.getReference("Ambulances/"+Duid+"/PAge");
                    myRefBloodgr=database.getReference("Ambulances/"+Duid+"/PBloodGr");
                    lat=database.getReference("Ambulances/"+Duid+"/PLat");
                    lon=database.getReference("Ambulances/"+Duid+"/PLon");
                    name=database.getReference("Ambulances/"+Duid+"/PName");
                    relmob1=database.getReference("Ambulances/"+Duid+"/PRelMob1");
                    puid=database.getReference("Ambulances/"+Duid+"/PUID");
                    // if(Duid.length()==28) {
                    myRefAge.setValue("N/A");
                    name.setValue("N/A");
                    lat.setValue("N/A");
                    lon.setValue("N/A");
                    myRefBloodgr.setValue("N/A");
                    relmob1.setValue("N/A");
                    puid.setValue("N/A");
                    // }
                    myRefdname.setValue("N/A");
                    myRefduid.setValue("N/A");
                    myRefaccident.setValue("N/A");
                    dialog.dismiss();
                    Intent intent=new Intent(Alertothers.this,MainActivity.class);
                    startActivity(intent);

                }
                else
                {

                    myRefduid.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // This method is called once with the initial value and again
                            // whenever data at this location is updated.
                            String value = dataSnapshot.getValue().toString();
                            Toast.makeText(Alertothers.this, value, Toast.LENGTH_SHORT).show();
                             Duid=value;
                            if(Duid.length()>=10)
                            {
                                //myRefambu=database.getReference("Ambulances/"+Duid);
                                myRefAge=database.getReference("Ambulances/"+Duid+"/PAge");
                                myRefBloodgr=database.getReference("Ambulances/"+Duid+"/PBloodGr");
                                lat=database.getReference("Ambulances/"+Duid+"/PLat");
                                lon=database.getReference("Ambulances/"+Duid+"/PLon");
                                name=database.getReference("Ambulances/"+Duid+"/PName");
                                relmob1=database.getReference("Ambulances/"+Duid+"/PRelMob1");
                                puid=database.getReference("Ambulances/"+Duid+"/PUID");
                                // if(Duid.length()==28) {
                                myRefAge.setValue("N/A");
                                name.setValue("N/A");
                                lat.setValue("N/A");
                                lon.setValue("N/A");
                                myRefBloodgr.setValue("N/A");
                                relmob1.setValue("N/A");
                                puid.setValue("N/A");
                                // }
                                myRefdname.setValue("N/A");
                                myRefduid.setValue("N/A");
                                myRefaccident.setValue("N/A");
                                dialog.dismiss();
                                Intent intent=new Intent(Alertothers.this,MainActivity.class);
                                startActivity(intent);

                            }
                            //Log.d(TAG, "Value is: " + value);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.w(TAG, "Failed to read value.", databaseError.toException());
                        }


                    });
                }




            }
        });
    }
}
