package com.example.artify;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class PuzzleCompleted extends AppCompatActivity {
    private String URL;
    private ImageView img;

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

        int points;
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
    }

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

    public void backToOpera(View view) {
        Intent goList = new Intent(PuzzleCompleted.this, ListaOpere.class);
        startActivity(goList);
    }
}
