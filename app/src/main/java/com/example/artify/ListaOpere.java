package com.example.artify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListaOpere extends AppCompatActivity {
    private ArrayList<Opera> opere = null;
    private FirebaseDatabase rootNode = null;
    private final String ROUTES_PATH = "opere/";
    private RecyclerView ListaOpere = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_opere);

        Intent contextIntent = getIntent();
        ListaOpere = findViewById(R.id.ListaOpereMuseo);
        rootNode = FirebaseDatabase.getInstance("https://artify-2ead0-default-rtdb.europe-west1.firebasedatabase.app/");
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
            Opera opera = new Opera();

            String id = sn.getKey();
            opera.setId(id);

            String titolo = sn.getValue(Opera.class).getTitolo();
            opera.setTitolo(titolo);

            String zona = sn.getValue(Opera.class).getZona();
            opera.setZona(zona);

            int voto = sn.getValue(Opera.class).getVoto();
            opera.setVoto(voto);

            String descrizione = sn.getValue(Opera.class).getDescrizione();
            opera.setDescrizione(descrizione);

            String urlImg = sn.getValue(Opera.class).getImg();
            opera.setImg(urlImg);

            if(zona.equals(contextIntent.getStringExtra("ZonaCliccata"))){
                com.example.artify.ListaOpere.this.opere.add(i,opera);
            }else if(contextIntent.getStringExtra("preMenuScelta").isEmpty()){
                com.example.artify.ListaOpere.this.opere.add(i,opera);
            }

            i++;
        }

        lista_opere_adapter adapter = new lista_opere_adapter(opere, getApplicationContext(),contextIntent);
        ListaOpere.setAdapter(adapter);
        ListaOpere.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    @Override
    protected void onStart() {
        super.onStart();
        opere = new ArrayList<>();

    }

    public void listaOpereSwitchToHome(View view){
        //to do
    }
}