package com.example.artify;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class dashboard extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private String userId;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


            mDatabase = FirebaseDatabase.getInstance().getReference();
            userId = FirebaseAuth.getInstance().getUid();
            mDatabase.child("users").child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                String computedString;

                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
                        TextView textView = (TextView) findViewById(R.id.dash_mailIns_tw);
                        textView.setText((String) task.getResult().child("email").getValue());
                        textView.setTypeface(null, Typeface.BOLD);
                        textView = (TextView) findViewById(R.id.dash_nameIns_tw);
                        computedString = (String) task.getResult().child("name").getValue() + " " + (String) task.getResult().child("surname").getValue();
                        textView.setText(computedString);
                        textView.setTypeface(null, Typeface.BOLD);
                        textView = (TextView) findViewById(R.id.dash_pointsReached_tw);
                        textView.setText(task.getResult().child("punti").getValue().toString());
                        textView.setTypeface(null, Typeface.BOLD);
                    } else {
                        Toast.makeText(dashboard.this, R.string.ReadDbError, Toast.LENGTH_LONG).show();
                    }
                }
            });

            //Handle click on home
            ImageButton iB = (ImageButton) findViewById(R.id.homeButton);
            iB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(dashboard.this, HomePage.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();
                }
         });
     }
}