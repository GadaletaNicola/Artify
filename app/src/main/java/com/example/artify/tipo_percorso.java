package com.example.artify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class tipo_percorso extends AppCompatActivity {
    private Button btnClicked = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipo_percorso);
    }


    public void switchToListaZone(View view){
        btnClicked = findViewById(view.getId());

        Intent switcher = new Intent(this,ListaZone.class).putExtra("TipoPercorso", btnClicked.getText().toString());
        switcher.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(switcher);
    }
}