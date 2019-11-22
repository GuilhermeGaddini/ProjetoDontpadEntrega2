package com.example.projetodontpadentrega2;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText loginEditText;
    private EditText senhaEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.projetodontpad2.R.layout.activity_main);
        loginEditText = findViewById(com.example.projetodontpadentrega2.R.id.loginEditText);
        senhaEditText = findViewById(com.example.projetodontpadentrega2.R.id.senhaEditText);
        mAuth = FirebaseAuth.getInstance();
    }

    public void irParaCadastro(View view) {
        Intent intent = new Intent(this, NewUserActivity.class);
        startActivity(intent);
    }

    public void fazerLogin(View view) {
        String login = loginEditText.getEditableText().toString();
        String senha = senhaEditText.getEditableText().toString();
        mAuth.signInWithEmailAndPassword(login,senha).addOnSuccessListener((authResult) -> {startActivity (new Intent (this, ChatActivity.class));})
                .addOnFailureListener((exception) -> {
                    exception.printStackTrace();
                    Toast.makeText(this, "Usuário ou senha não conferem. Por favor verifique.",Toast.LENGTH_SHORT).show();
                });
    }
}
