package com.example.artify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuPreScelta extends AppCompatActivity {
    private Button btnClicked;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_pre_scelta);
    }

    public void switchToTipoPercorso(View view){
        btnClicked = findViewById(view.getId());

        Intent switcher = new Intent(this,tipo_percorso.class);
        switcher.putExtra("preMenuScelta",btnClicked.getText());
        startActivity(switcher);
    }

    public void switchToListaOpere(View view){
        Intent switcher = new Intent(this, ListaOpere.class);
        switcher.putExtra("preMenuScelta","");
        startActivity(switcher);
    }

    public void MenuPreSceltaToHome(View view){
        //to do
    }
}