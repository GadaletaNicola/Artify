package com.example.artify;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
private DrawerLayout drawerLayout;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_dashboard);
            //Handle click in drawer menu
            drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            setNavigationViewListener();
            //Handle click on logout
            RelativeLayout rl = (RelativeLayout)findViewById(R.id.logout_layout);
            rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseAuth.getInstance().signOut();
                    Toast.makeText(dashboard.this, "Disconnesso!", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(dashboard.this, login.class);
                    startActivity(i);
                }
            });
        }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent i;



        switch (item.getItemId()) {

            case R.id.path_menu: {
                i = new Intent(this, tipo_percorso.class);
                startActivity(i);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        }
        return true;
    }

    private void setNavigationViewListener() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.side_menu);
        navigationView.setNavigationItemSelectedListener(this);
    }
}