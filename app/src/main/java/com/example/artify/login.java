package com.example.artify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class login extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText email;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        mAuth = FirebaseAuth.getInstance();


    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            Intent intent = new Intent(login.this, HomePage.class);
            startActivity(intent);
            finish();
        }
    }

    /**
     * Metodo per la gestione del login nell'applicazione
     * @param view: view di riferimento
     */
    public void Login(View view) {
        String textEmail = email.getText().toString();
        String textPassword = password.getText().toString();

        if (textEmail.isEmpty()) {
            email.setError(getText(R.string.error_email));
            email.requestFocus();
        } else if (textPassword.isEmpty()) {
            password.setError(getText(R.string.error_password));
            password.requestFocus();
        } else {
            mAuth.signInWithEmailAndPassword(textEmail, textPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(login.this, R.string.accessDone, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(login.this, HomePage.class);
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(login.this, R.string.accessFailure, Toast.LENGTH_LONG).show();

                    }
                }
            });
        }
    }

    /**
     * Il metodo gestisce lo switch tra l'activity login e l'activity di registrazione
     * @param view: view di riferimento
     */
    public void switchToSignUp(View view) {
        Intent intent = new Intent(this, sign_up.class);
        startActivity(intent);
        finish();
    }
}