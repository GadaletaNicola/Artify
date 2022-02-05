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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class sign_up extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseDatabase rootNode;
    private DatabaseReference reference;

    private EditText name=null;
    private EditText surname=null;
    private EditText email=null;
    private EditText username=null;
    private EditText password=null;

    private HashMap<String, String> userInfo=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name=findViewById(R.id.name);
        surname=findViewById(R.id.surname);
        email=findViewById(R.id.email);
        username=findViewById(R.id.username);
        password=findViewById(R.id.password);

        mAuth=FirebaseAuth.getInstance();

        rootNode=FirebaseDatabase.getInstance("https://artify-2ead0-default-rtdb.europe-west1.firebasedatabase.app/");
        reference= rootNode.getReference("users");
    }

    /**
     * Metodo utile per la creazione dell' utente da inserire nel db
     * @param view: view di riferimento
     */
    public void createUser(View view){
        String textName=name.getText().toString();
        String textSurname=surname.getText().toString();
        String textEmail=email.getText().toString();
        String textUsername=username.getText().toString();
        String textPassword=password.getText().toString();

        if(textName.isEmpty()) {
            name.setError(getText(R.string.error_name));
            name.requestFocus();
        } else if (textSurname.isEmpty()) {
            surname.setError(getText(R.string.error_surname));
            surname.requestFocus();
        }else if (textEmail.isEmpty()) {
            email.setError(getText(R.string.error_email));
            email.requestFocus();
        }else if (textUsername.isEmpty()) {
            username.setError(getText(R.string.error_username));
            username.requestFocus();
        }else if (textPassword.isEmpty()) {
            password.setError(getText(R.string.error_password));
            password.requestFocus();
        } else {
            mAuth.createUserWithEmailAndPassword(textEmail, textPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        FirebaseUser user=mAuth.getCurrentUser();
                        User tmpUser=new User();
                        tmpUser.setName(textName);
                        tmpUser.setSurname(textSurname);
                        tmpUser.setUsername(textUsername);
                        insertInDB(user, tmpUser);

                        Intent intent=new Intent(sign_up.this, login.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(sign_up.this, R.string.error_signUp, Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

    }

    /**
     * il metodo permette l'inserimento dell'utente nel db
     * @param userGiven: Utente da inserire nel db
     * @param tmpUser: Oggetto temporaneo contenente le info dell' utente da inserire
     */
    public void insertInDB(FirebaseUser userGiven, User tmpUser){
        setHashMap(userGiven, tmpUser);
        reference.child(userGiven.getUid()).setValue(userInfo);
    }

    /**
     * il metodo permette il setting dell'hashmap utile alla registrazione dell' utente nel db
     * @param userGiven:Utente da inserire nel db
     * @param tmpUser: Oggetto temporaneo contenente le info dell' utente da inserire
     */
    public void setHashMap(FirebaseUser userGiven, User tmpUser){
        userInfo=new HashMap<>();

        userInfo.putIfAbsent("surname", tmpUser.getSurname());
        userInfo.putIfAbsent("name", tmpUser.getName());
        userInfo.putIfAbsent("username", tmpUser.getUsername());
        userInfo.putIfAbsent("punti", tmpUser.getPunti());
        userInfo.putIfAbsent("immagine", tmpUser.getImg());
        userInfo.putIfAbsent("stato", tmpUser.getStato());
        userInfo.putIfAbsent("email", userGiven.getEmail());
    }

    /**
     * il metodo permette lo switch tra l'activity corrente e quella di login
     * @param view: view di riferimento
     */
    public void switchToLogIn(View view) {
        Intent intent=new Intent(this, login.class);
        startActivity(intent);
        finish();
    }
}