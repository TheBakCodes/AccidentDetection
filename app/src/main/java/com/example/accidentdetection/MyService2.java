package com.example.accidentdetection;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.ContentValues.TAG;

public class MyService2 extends Service {
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    public DatabaseReference myRefDUID;
    FirebaseAuth mAuth;
    FirebaseUser user;
    public FirebaseDatabase database = FirebaseDatabase.getInstance();
    public MyService2() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        myRefDUID=database.getReference("Users/"+user.getUid()+"/DUID");
        sharedpreferences = getSharedPreferences("Accident", Context.MODE_PRIVATE);
        editor= sharedpreferences.edit();

        myRefDUID.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                editor.putString("DUID", value);
                editor.commit();
                Toast.makeText(MyService2.this, value, Toast.LENGTH_SHORT).show();
                //Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }


        });

    }
}
