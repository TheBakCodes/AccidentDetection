package com.example.accidentdetection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
    public DatabaseReference myRef,myRefambu ;
    FirebaseAuth mAuth;
    FirebaseUser user;
    String TAG="AlertActivity";
    String Duid="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alertactivity);
        countdowntv = (TextView)findViewById(R.id.countdowntv);
        cancelbut = (Button)findViewById(R.id.cancelbut);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        myRef= database.getReference("Users/"+user.getUid()+"/DUID");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue().toString();
                Duid=value;
                Toast.makeText(alertactivity.this, value, Toast.LENGTH_SHORT).show();
                myRefambu=database.getReference("Ambulances/"+Duid+"/AccCheck");
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
                countdowntv.setText("seconds remaining: " + millisUntilFinished / 1000);
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
                            myRefambu=database.getReference("Ambulances/"+Duid+"/AccCheck");
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
                countdowntv.setText("done!");
                Toast.makeText(alertactivity.this, "Sarigala timer", Toast.LENGTH_SHORT).show();
                myRefambu.setValue("1");


            }

        }.start();



        cancelbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                timer.cancel();
                Intent intent=new Intent(alertactivity.this,MainActivity.class);
                startActivity(intent);

            }
        });
    }
}
