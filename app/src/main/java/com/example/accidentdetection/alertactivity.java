package com.example.accidentdetection;

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
    SharedPreferences sharedpreferences;
    MediaPlayer mediaPlayer;
    //int maxVolume = 50;
    ProgressDialog dialog;
    int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alertactivity);
        sharedpreferences = getSharedPreferences("Accident", Context.MODE_PRIVATE);
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
        Duid=sharedpreferences.getString("DUID", "0");
        cancelbut.setVisibility(View.INVISIBLE);




        final CountDownTimer timer = new CountDownTimer(40000, 1000) {

            public void onTick(long millisUntilFinished) {
                countdowntv.setText( String.valueOf(millisUntilFinished / 1000));
                Duid=sharedpreferences.getString("DUID", "0");
                if(Duid.length()>=10&&i==0)
                {
                    cancelbut.setVisibility(View.VISIBLE);
                    i=1;
                }
               /* if( millisUntilFinished / 1000==20)
                {

                }*/
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
        myRefaccident.setValue("N/A");

    }
}
