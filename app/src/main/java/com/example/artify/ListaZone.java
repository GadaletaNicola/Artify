package com.example.artify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;

// inserire l'onclick event listener
public class ListaZone extends AppCompatActivity{
    private ArrayList<Zone> zone = null;
    private FirebaseDatabase rootNode = null;
    private final String ROUTES_PATH = "zone/";
    private RecyclerView ListaZone = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_zone);

        Intent i = getIntent();


        ListaZone = findViewById(R.id.ListaZoneMuseo);

        rootNode = FirebaseDatabase.getInstance("https://artify-2ead0-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference routesPath = rootNode.getReference(ROUTES_PATH);
        routesPath.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i=0;
                for (DataSnapshot sn : snapshot.getChildren()) {
                    Zone zone = new Zone();
                    zone.setNomeZona(sn.getKey());
                    zone.setImg(sn.getValue(Zone.class).getImg());
                    com.example.artify.ListaZone.this.zone.add(i, zone);
                    i++;
                }
                listadapter adapter = new listadapter(zone, getApplicationContext());
                ListaZone.setAdapter(adapter);
                ListaZone.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),R.string.ReadDbError,Toast.LENGTH_LONG);
            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
        zone = new ArrayList<>();

    }


}
