package com.example.artify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;


public class ListaZone extends AppCompatActivity{
    private ArrayList<Zone> zone = null;
    private final String ROUTES_PATH = "zone/";
    private RecyclerView ListaZone = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_zone);

        Intent contextIntent = getIntent();
        ListaZone = findViewById(R.id.ListaZoneMuseo);
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance("https://artify-2ead0-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference routesPath = rootNode.getReference(ROUTES_PATH);


        routesPath.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                initRv(snapshot,contextIntent);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),R.string.ReadDbError,Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initRv(DataSnapshot snapshot,Intent contextIntent){
        int i=0;
        for (DataSnapshot sn : snapshot.getChildren()) {
            Zone zone = new Zone();

            String nomeZona = sn.getKey();
            zone.setNomeZona(nomeZona);

            String urlImg = sn.getValue(Zone.class).getImg();
            zone.setImg(urlImg);

            String tipoPercorso = sn.getValue(Zone.class).getTipo();
            zone.setTipo(tipoPercorso);

            String nomeMuseo = sn.getValue(Zone.class).getMuseo();
            zone.setMuseo(nomeMuseo);

            if(tipoPercorso.equals(contextIntent.getStringExtra("TipoPercorso"))){
                com.example.artify.ListaZone.this.zone.add(i, zone);
            }

            i++;
        }

        listadapter adapter = new listadapter(zone, getApplicationContext(),contextIntent);
        ListaZone.setAdapter(adapter);
        ListaZone.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    @Override
    protected void onStart() {
        super.onStart();
        zone = new ArrayList<>();
    }


    public void listaZoneSwitchToHome(View view){
        //to do
    }
}
