package com.example.artify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomePage extends AppCompatActivity {
    private ArrayList<Museo> musei = new ArrayList<Museo>();
    private FirebaseDatabase rootNode;
    private final String ROOT_PATH = "musei/";
    private RecyclerView listaMusei;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        listaMusei = findViewById(R.id.ListaMusei);
        rootNode = FirebaseDatabase.getInstance("https://artify-2ead0-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference rootPath = rootNode.getReference(ROOT_PATH);

        rootPath.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                initRv(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),R.string.ReadDbError,Toast.LENGTH_LONG).show();
            }
        });
    }


    private void initRv(DataSnapshot snapshot){

        for (DataSnapshot sn : snapshot.getChildren()) {
            Museo museo = new Museo();

            String nomeMuseo = sn.getKey();
            museo.setNome(nomeMuseo);

            String urlImg = sn.getValue(Museo.class).getImg();
            museo.setImg(urlImg);

            com.example.artify.HomePage.this.musei.add(museo);

        }

        lista_musei_adapter adapter = new lista_musei_adapter(musei, getApplicationContext());
        listaMusei.setAdapter(adapter);
        listaMusei.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }
}