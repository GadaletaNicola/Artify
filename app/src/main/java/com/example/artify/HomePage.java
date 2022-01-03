package com.example.artify;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ArrayList<Museo> musei = new ArrayList<Museo>();
    private FirebaseDatabase rootNode;
    private final String ROOT_PATH = "musei/";
    private RecyclerView listaMusei;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FirebaseAuth mAuth;
    private RelativeLayout rLLogout;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        listaMusei = findViewById(R.id.ListaMusei);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        rootNode = FirebaseDatabase.getInstance("https://artify-2ead0-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference rootPath = rootNode.getReference(ROOT_PATH);


        //Handle click in drawer menu
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        setNavigationViewListener();

        //Handle click on logout
        rLLogout = (RelativeLayout) findViewById(R.id.logout_layout);
        rLLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(HomePage.this, "Disconnesso!", Toast.LENGTH_LONG).show();
                Intent i = new Intent(HomePage.this, login.class);
                startActivity(i);
                finish();
            }
        });

        rootPath.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                initRv(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), R.string.ReadDbError,Toast.LENGTH_LONG).show();
            }
        });

        hideItem();
    }


    private void initRv(DataSnapshot snapshot){

        for (DataSnapshot sn : snapshot.getChildren()) {
            Museo museo = new Museo();

            String nomeMuseo = sn.getKey();
            museo.setNome(nomeMuseo);

            String urlImg = sn.getValue(Museo.class).getImg();
            museo.setImg(urlImg);

            HomePage.this.musei.add(museo);

        }

        lista_musei_adapter adapter = new lista_musei_adapter(musei, getApplicationContext());
        listaMusei.setAdapter(adapter);
        listaMusei.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent i;
        switch (item.getItemId()) {

            case R.id.login_menu: {
                i = new Intent(this, login.class);
                startActivity(i);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }

            case R.id.dashboard_menu: {
                i = new Intent(this, dashboard.class);
                startActivity(i);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        }
        return true;
    }


    private void setNavigationViewListener() {
        navigationView = (NavigationView) findViewById(R.id.side_menu);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void hideItem()
    {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.login_menu).setVisible(true);
        }else{
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.login_menu).setVisible(false);
            rLLogout.setVisibility(View.VISIBLE);
        }
    }

}