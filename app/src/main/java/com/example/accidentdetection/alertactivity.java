package com.example.accidentdetection;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
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

public class alertactivity extends AppCompatActivity {
    TextView countdowntv;
    CountDownTimer timer;
    Button cancelbut ;
    public FirebaseDatabase database = FirebaseDatabase.getInstance();
    public DatabaseReference myRef,myRefambu,myRefAge,myRefBloodgr,lat,lon,name,relmob1,puid,accheck,myRefdname,myRefduid,myRefaccident;
    FirebaseAuth mAuth;
    FirebaseUser user;
    String TAG="AlertActivity";
    String Duid="";

    MediaPlayer mediaPlayer;
    //int maxVolume = 50;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alertactivity);
        dialog = new ProgressDialog(this); // this = YourActivity
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Loading");
        dialog.setMessage("Loading. Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        mediaPlayer =MediaPlayer.create(this, R.raw.siren);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        countdowntv = (TextView)findViewById(R.id.countdowntv);
        cancelbut = (Button)findViewById(R.id.cancelbut);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        myRef= database.getReference("Users/"+user.getUid()+"/DUID");
        myRefduid=database.getReference("Users/"+user.getUid()+"/DUID");
        myRefdname=database.getReference("Users/"+user.getUid()+"/DName");
        myRefaccident=database.getReference("Users/"+user.getUid()+"/Accident");
        myRefdname=database.getReference("Users/"+user.getUid()+"/DName");
        cancelbut.setVisibility(View.VISIBLE);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue().toString();
                Duid=value;
                Toast.makeText(alertactivity.this, value, Toast.LENGTH_SHORT).show();
                myRefambu=database.getReference("Ambulances/"+Duid);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });





        final CountDownTimer timer = new CountDownTimer(40000, 1000) {

            public void onTick(long millisUntilFinished) {
                countdowntv.setText( String.valueOf(millisUntilFinished / 1000));
                if( millisUntilFinished / 1000==20)
                {
                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // This method is called once with the initial value and again
                            // whenever data at this location is updated.
                            String value = dataSnapshot.getValue().toString();
                            Duid=value;
                            Toast.makeText(alertactivity.this, value, Toast.LENGTH_SHORT).show();

                            Log.d(TAG, "Value is: " + value);
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            Log.w(TAG, "Failed to read value.", error.toException());
                        }
                    });
                }
            }

            public void onFinish() {
                countdowntv.setText("Help Is Coming!");
                Toast.makeText(alertactivity.this, "Sarigala timer", Toast.LENGTH_SHORT).show();
                myRefambu=database.getReference("Ambulances/"+Duid+"/AccCheck");
               // myRefa=database.getReference("Ambulances/"+Duid);
                myRefambu.setValue("1");
                mediaPlayer.stop();
                mediaPlayer.release();
                cancelbut.setVisibility(View.INVISIBLE);
                //dialog.show();





            }

        }.start();



        cancelbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                timer.cancel();

                mediaPlayer.stop();
                mediaPlayer.release();
                dialog.show();
                //myRefambu=database.getReference("Ambulances/"+Duid);
                myRefAge=database.getReference("Ambulances/"+Duid+"/PAge");
                myRefBloodgr=database.getReference("Ambulances/"+Duid+"/PBloodGr");
                lat=database.getReference("Ambulances/"+Duid+"/PLat");
                lon=database.getReference("Ambulances/"+Duid+"/PLon");
                name=database.getReference("Ambulances/"+Duid+"/PName");
                relmob1=database.getReference("Ambulances/"+Duid+"/PRelMob1");
                puid=database.getReference("Ambulances/"+Duid+"/PUID");
                if(Duid.length()==28) {
                    myRefAge.setValue("N/A");
                    name.setValue("N/A");
                    lat.setValue("N/A");
                    lon.setValue("N/A");
                    myRefBloodgr.setValue("N/A");
                    relmob1.setValue("N/A");
                    puid.setValue("N/A");
                }
                myRefdname.setValue("N/A");
                myRefduid.setValue("N/A");
                myRefaccident.setValue("N/A");
                dialog.dismiss();
                Intent intent=new Intent(alertactivity.this,MainActivity.class);
                startActivity(intent);



            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
    }
}
