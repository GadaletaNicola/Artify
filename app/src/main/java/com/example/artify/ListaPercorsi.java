package com.example.artify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
public class ListaPercorsi extends AppCompatActivity {
    private ArrayList<Percorso> percorsi = null;
    private FirebaseDatabase rootNode = null;
    private final String ROUTES_PATH = "percorsi/";
    private RecyclerView ListaPercorsi = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_percorsi);

        ListaPercorsi = findViewById(R.id.ListaPerocrsi);

        rootNode = FirebaseDatabase.getInstance("https://artify-2ead0-default-rtdb.europe-west1.firebasedatabase.app/");

        DatabaseReference routesPath = rootNode.getReference(ROUTES_PATH);


        routesPath.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i=0;
                for (DataSnapshot sn : snapshot.getChildren()) {
                    Percorso percorso = new Percorso();
                    percorso.setNomePercorso(sn.getKey());
                    percorso.setImg(sn.getValue(Percorso.class).getImg());
                    percorso.setVoto(sn.getValue(Percorso.class).getVoto());
                    percorso.setTipoPercorso(sn.getValue(Percorso.class).getTipoPercorso());
                    percorsi.add(i,percorso);
                    i++;
                }
                listadapter adapter = new listadapter(percorsi,getApplicationContext());
                ListaPercorsi.setAdapter(adapter);
                ListaPercorsi.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


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
        percorsi = new ArrayList<>();
    }

}
