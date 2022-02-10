package com.example.artify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
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
    private String searchedText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_opere);

        ImageButton searchButton = (ImageButton)findViewById(R.id.searchButton);

        Intent contextIntent = getIntent();
        ListaOpere = findViewById(R.id.ListaOpereMuseo);
        rootNode = FirebaseDatabase.getInstance("https://artify-2ead0-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference routesPath = rootNode.getReference(ROUTES_PATH);

        routesPath.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                initRv(snapshot, contextIntent);
                searchButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        takeText();
                        opere.clear();
                        initRv(snapshot, contextIntent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), R.string.ReadDbError, Toast.LENGTH_LONG).show();
            }
        });


    }


    public void takeText(){
        EditText testo = (EditText) findViewById(R.id.searchedText);
        searchedText = testo.getText().toString();
    }


    /**
     * il metodo permette l'inizializzazione della recyclerView
     * @param snapshot: snapshot del database
     * @param contextIntent: Intent per la gestione di informazioni esterne
     */
    private void initRv(DataSnapshot snapshot, Intent contextIntent) {
        for (DataSnapshot sn : snapshot.getChildren()) {
            Opera opera = new Opera();

            String id = sn.getKey();
            opera.setId(id);

            String titolo = sn.getValue(Opera.class).getTitolo();
            opera.setTitolo(titolo);

            String zona = sn.getValue(Opera.class).getZona();
            opera.setZona(zona);

            float voto = sn.getValue(Opera.class).getVoto();
            opera.setVoto(voto);

            String descrizione = sn.getValue(Opera.class).getDescrizione();
            opera.setDescrizione(descrizione);

            String autore = sn.getValue(Opera.class).getAutore();
            opera.setAutore(autore);

            String dimensione = sn.getValue(Opera.class).getDimensione();
            opera.setDimensione(dimensione);

            String data = sn.getValue(Opera.class).getData();
            opera.setData(data);

            String stile = sn.getValue(Opera.class).getStile();
            opera.setStile(stile);

            String urlImg = sn.getValue(Opera.class).getImg();
            opera.setImg(urlImg);

            int numeroVoti = sn.getValue(Opera.class).getNumeroVoti();
            opera.setNumeroVoti(numeroVoti);

            if (zona.equals(contextIntent.getStringExtra("ZonaCliccata")) || titolo.equals(searchedText) ) {
                com.example.artify.ListaOpere.this.opere.add(opera);
            }
        }

        lista_opere_adapter adapter = new lista_opere_adapter(opere, getApplicationContext(), contextIntent);
        ListaOpere.setAdapter(adapter);
        ListaOpere.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    @Override
    protected void onStart() {
        super.onStart();
        opere = new ArrayList<>();

    }
}