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

        email=findViewById(R.id.email);
        password=findViewById(R.id.password);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            // to do
        }
    }

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
                        Toast.makeText(login.this, "Accesso effettuato!", Toast.LENGTH_LONG).show();
                        // to do
                    } else {
                        Toast.makeText(login.this, "Accesso non andato a buon fine!", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    public void switchToSignUp(View view){
        Intent intent = new Intent(this, sign_up.class);
        startActivity(intent);
    }
}