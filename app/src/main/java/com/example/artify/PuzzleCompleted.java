package com.example.artify;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
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

public class PuzzleCompleted extends AppCompatActivity {
    private String URL;
    private ImageView img;
    private FirebaseDatabase database;
    private DatabaseReference rootPath;
    private final String ROOTNODE="users/";
    private FirebaseAuth mAuth;
    private HashMap<String, Object> users=null;
    private User user = new User();
    private FirebaseUser firebaseUser;
    private int points;
    private boolean f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.puzzle_completed);


        TextView SecondsTextView = (TextView) findViewById(R.id.seconds);
        TextView EarnedPointsTextView = (TextView) findViewById(R.id.earned_points);

        Intent intent = getIntent();
        String chronometer = intent.getStringExtra("Chronometer");
        String movesString = intent.getStringExtra("Moves");
        URL = intent.getStringExtra("URLOp");
        img = (ImageView) findViewById(R.id.imageCompleted);
        setImage();

        int movesInt = Integer.parseInt(movesString);
        String [] timeParts = chronometer.split(":");

        int hours = 0;
        int minutes = 0;
        int seconds = 0;

        switch (timeParts.length) {
            case 1:
                //only seconds
                seconds = Integer.parseInt(timeParts[0]);
                break;
            case 2:
                //minutes, seconds
                minutes = Integer.parseInt(timeParts[0]);
                seconds = Integer.parseInt(timeParts[1]);
                break;
            case 3:
                //hours, minutes, seconds
                hours = Integer.parseInt(timeParts[0]);
                minutes = Integer.parseInt(timeParts[1]);
                seconds = Integer.parseInt(timeParts[2]);
                break;
        }

        seconds = seconds + (minutes * 60) + (hours * 3600); //Number of seconds spent to complete the puzzle
        //Calculating Points [ The number of Moves decide the BasePoints; Points = BasePoints - Seconds ]
        if(movesInt <= 30) { //30 or less moves
            points = 2000; //BasePoints

            if(seconds < points) {
                points -= seconds;

                if(points < 100)
                    points = 100; //BasePoints if points became less than 100

            } else points = 100;

        } else if(movesInt <= 60) { //60 or less moves
            points = 1000; //BasePoints

            if(seconds < points) {
                points -= seconds;

                if(points < 100)
                    points = 100; //BasePoints if points became less than 100

            } else points = 100;

        } else if(movesInt <= 90) { //90 or less moves
            points = 500; //BasePoints

            if(seconds < points) {
                points -= seconds;

                if(points < 100)
                    points = 100; //BasePoints if points became less than 100

            } else points = 100;
        } else points = 100; //more than 90 moves

        EarnedPointsTextView.setText(Integer.toString(points));
        SecondsTextView.setText(chronometer);


        users = new HashMap<>();
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        database = FirebaseDatabase.getInstance("https://artify-2ead0-default-rtdb.europe-west1.firebasedatabase.app/");
        rootPath = database.getReference(ROOTNODE);
        f = false;
        rootPath.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                initUser(snapshot);
                setPunti();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * Funzione per il setting dei punti nel DB
     */
    private void setPunti() {
        if(f == false) {
            DatabaseReference userReference = rootPath.child(firebaseUser.getUid());
            users.put("punti", user.getPunti() + points);
            userReference.updateChildren(users);
            f = true;
        }
    }

    /**
     * Funzione per l'inizializzazione dell'utente
     * @param snapshot: snapshot del DB
     */
    private void initUser(DataSnapshot snapshot) {
        for (DataSnapshot sn : snapshot.getChildren()) {

            String key = sn.getKey();

            String email = sn.getValue(User.class).getEmail();

            String name = sn.getValue(User.class).getName();

            String surname = sn.getValue(User.class).getSurname();

            String username = sn.getValue(User.class).getUsername();

            String img = sn.getValue(User.class).getImg();

            int punti = sn.getValue(User.class).getPunti();

            String stato = sn.getValue(User.class).getStato();

            if(key.equals(firebaseUser.getUid())) {
                user.setEmail(email);

                user.setName(name);

                user.setSurname(surname);

                user.setUsername(username);

                user.setKey(key);

                user.setPunti(punti);
            }
        }
    }

    /**
     * Funzione per il setting dell'immagine da risolvere
     */
    private void setImage() {
        Puzzlegame.gsRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

            @Override
            public void onSuccess(Uri uri) {
                Glide.with(PuzzleCompleted.this).load(uri.toString()).into(img);
            }
        }).addOnFailureListener(new OnFailureListener() {

            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PuzzleCompleted.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * funzione per tornare indietro all'opera
     * @param view: view di riferimento
     */
    public void backToOpera(View view) {
        finish();
    }
}