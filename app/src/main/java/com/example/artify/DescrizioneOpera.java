package com.example.artify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

public class DescrizioneOpera extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference rootPath;
    private final String ROOTNODE="opere/";
    private ImageButton share;
    private ImageButton game;
    private ImageButton voto;
    private Opera opera;

    private ImageView imgOpera;
    private TextView nomeOpera;
    private TextView autoreOpera;
    private TextView stileOpera;
    private TextView dimensioniOpera;
    private TextView dataCreazione;
    private TextView descrizioneOpera;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private FirebaseAuth mAuth;

    private HashMap<String, Object> opere=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descrizione_opera);
        Intent contextIntent = getIntent();

        //database
        database = FirebaseDatabase.getInstance("https://artify-2ead0-default-rtdb.europe-west1.firebasedatabase.app/");
        rootPath = database.getReference(ROOTNODE);

        opere=new HashMap<>();

        imgOpera=(ImageView) findViewById(R.id.imgOpera);
        nomeOpera = (TextView) findViewById(R.id.nomeOpera);
        autoreOpera = (TextView) findViewById(R.id.autoreOpera);
        stileOpera = (TextView) findViewById(R.id.stileOpera);
        dimensioniOpera = (TextView) findViewById(R.id.dimensioniOpera);
        dataCreazione = (TextView) findViewById(R.id.dataOpera);
        descrizioneOpera = (TextView) findViewById(R.id.descrizioneOpera);
        game = (ImageButton) findViewById(R.id.game);
        share = (ImageButton) findViewById(R.id.share);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user == null) {
            game.setVisibility(View.INVISIBLE);
        }

        rootPath.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                initOpera(snapshot, contextIntent);
                initUI(opera);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), R.string.ReadDbError, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Il metodo inizializza l'opera da visualizzare nell'activity
     * @param snapshot: snapshot del db
     * @param contextIntent: intent contenente l'opera selezionata dall'utente
     */
    public void initOpera(DataSnapshot snapshot, Intent contextIntent){
        for(DataSnapshot sn : snapshot.getChildren()){
            Opera tmpOpera=new Opera();

            String id=sn.getKey();
            tmpOpera.setId(id);

            String titolo=sn.getValue(Opera.class).getTitolo();
            tmpOpera.setTitolo(titolo);

            String zona=sn.getValue(Opera.class).getZona();
            tmpOpera.setZona(zona);

            float voto=sn.getValue(Opera.class).getVoto();
            tmpOpera.setVoto(voto);

            String descrizione=sn.getValue(Opera.class).getDescrizione();
            tmpOpera.setDescrizione(descrizione);

            String urlImg=sn.getValue(Opera.class).getImg();
            tmpOpera.setImg(urlImg);

            String autore = sn.getValue(Opera.class).getAutore();
            tmpOpera.setAutore(autore);

            String dimensione = sn.getValue(Opera.class).getDimensione();
            tmpOpera.setDimensione(dimensione);

            String data = sn.getValue(Opera.class).getData();
            tmpOpera.setData(data);

            String stile = sn.getValue(Opera.class).getStile();
            tmpOpera.setStile(stile);

            int numeroVoti = sn.getValue(Opera.class).getNumeroVoti();
            tmpOpera.setNumeroVoti(numeroVoti);

            if(id.equals(contextIntent.getStringExtra("idOpera"))){
                opera=tmpOpera;
            }
        }
    }

    /**
     * Il metodo inizializza l'interfaccia dell'activity con i dati inerenti l'opera selezionata
     * @param opera: Opera selezionata dall'utente
     */
    public void initUI(Opera opera){
        StorageReference storageRf=(FirebaseStorage.getInstance().getReferenceFromUrl(opera.getImg()));
        storageRf.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext()).load(uri.toString()).into(imgOpera);
            }
        });

        nomeOpera.setText(opera.getTitolo());
        autoreOpera.setText(opera.getAutore());
        stileOpera.setText(opera.getStile());
        dimensioniOpera.setText(opera.getDimensione());
        dataCreazione.setText(opera.getData());
        descrizioneOpera.setText(opera.getDescrizione());
    }

    /**
     * Il metodo permette l'accesso all'activity dedicata al minigioco
     * @param view: view di riferimento
     */
    public void game(View view){
        Intent goPuzzle = new Intent(DescrizioneOpera.this, Puzzlegame.class);
        goPuzzle.putExtra("URLOpera", opera.getImg());
        goPuzzle.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(goPuzzle);
    }

    /**
     * il metodo permette la condivisione di info tramite app di comunicazione esterne (Whatsapp,etc)
     * @param view: view di riferimento
     */
    public void share(View view){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        sendIntent.putExtra(Intent.EXTRA_TEXT,"Ciao, ho visitato l'opera "+opera.getTitolo()+"! Scarica Artify da Google Play!!");

        Intent shareIntent = Intent.createChooser(sendIntent,null);
        startActivity(shareIntent);
        Intent i=new Intent();
    }

    /**
     * Metodo per creare il Popup per la valutazione delle opere
     * @param view: view di riferimento
     */
    public void createNewVoteDialog(View view){
        dialogBuilder= new AlertDialog.Builder(this);
        final View voteView=getLayoutInflater().inflate(R.layout.popup, null);

        Button submit=voteView.findViewById(R.id.submitVote);
        RatingBar ratingBar=voteView.findViewById(R.id.ratingBar);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int numeroVoti = opera.getNumeroVoti();
                float voto = ratingBar.getRating();
                float votoPrec = opera.getVoto();

                numeroVoti++;
                opera.setNumeroVoti(numeroVoti);
                opera.setVoto((voto + votoPrec)/2);

                DatabaseReference operaReference=rootPath.child(opera.getId());
                opere.put("numeroVoti", opera.getNumeroVoti());
                opere.put("voto", opera.getVoto());
                operaReference.updateChildren(opere);
                dialog.dismiss();
            }
        });

        dialogBuilder.setView(voteView);
        dialog=dialogBuilder.create();
        dialog.show();
    }
}
