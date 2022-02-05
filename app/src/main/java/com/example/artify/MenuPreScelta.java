package com.example.artify;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MenuPreScelta extends AppCompatActivity {
    private Button btnClicked;
    Intent contextIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_pre_scelta);
        contextIntent = getIntent();
    }

    /**
     * il metodo permette lo switch tra l'activity corrente e quella che gestisce il tipo di percorso
     * @param view: view di riferimento
     */
    public void switchToTipoPercorso(View view){
        btnClicked = findViewById(view.getId());


        Intent switcher = new Intent(this,tipo_percorso.class);
        switcher.putExtra("preMenuScelta",btnClicked.getText());
        switcher.putExtra("MuseoCliccato",contextIntent.getStringExtra("MuseoCliccato"));
        switcher.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(switcher);
    }

    /**
     * il metodo permette lo switch tra l'activity corrente e quella che gestisce la lista delle opere
     * @param view: view di riferimento
     */
    public void switchToListaOpere(View view){
        Intent switcher = new Intent(this, ListaOpere.class);
        switcher.putExtra("preMenuScelta","");
        switcher.putExtra("MuseoCliccato",contextIntent.getStringExtra("MuseoCliccato"));
        switcher.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(switcher);

    }

    /**
     * il metodo permette lo switch tra l'activity corrente e quella che gestisce la homepage
     * @param view: view di riferimento
     */
    public void MenuPreSceltaToHome(View view){
        Intent switcher = new Intent(this,HomePage.class);
        switcher.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(switcher);
        finish();
    }
}