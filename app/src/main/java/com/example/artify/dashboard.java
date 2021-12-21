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

import com.google.android.material.navigation.NavigationView;

public class dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_dashboard);

            drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            setNavigationViewListener();
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