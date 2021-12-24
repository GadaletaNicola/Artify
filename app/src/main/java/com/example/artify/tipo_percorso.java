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
        Intent contextIntent = getIntent();

        Intent switcher = new Intent(this,ListaZone.class);
        switcher.putExtra("MuseoCliccato",contextIntent.getStringExtra("MuseoCliccato"));
        switcher.putExtra("TipoPercorso", btnClicked.getText());
        switcher.putExtra("preMenuScelta",contextIntent.getStringExtra("preMenuScelta"));
        startActivity(switcher);
    }

    public void tipoPercorsoSwitchToHome(View view){
        Intent switcher = new Intent(this,HomePage.class);
        startActivity(switcher);
        finish();
    }
}