package com.example.artify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.HashMap;

public class tipo_percorso extends AppCompatActivity {
    private Button btnClicked = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipo_percorso);
    }

    // da commentare
    public void switchToListaZone(View view){
        btnClicked = findViewById(view.getId());

        Intent intent = new Intent(this,ListaZone.class).putExtra("clickedBtn",btnClicked.getText().toString());
        startActivity(intent);
    }
}