package com.example.projetodontpadentrega2;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class NewUserActivity extends AppCompatActivity {

    private EditText loginNovoUsuarioEditText;
    private EditText senhaNovoUsuarioEditText;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.projetodontpadentrega2.R.layout.activity_new_user);
        loginNovoUsuarioEditText = findViewById(com.example.projetodontpadentrega2.R.id.loginNovoUsuarioEditText);
        senhaNovoUsuarioEditText = findViewById(com.example.projetodontpadentrega2.R.id.senhaNovoUsuarioEditText);
        mAuth = FirebaseAuth.getInstance();
    }

    public void criarNovoUsuario(View view) {
        String login = loginNovoUsuarioEditText.getText().toString();
        String senha = senhaNovoUsuarioEditText.getText().toString();
        if (loginNovoUsuarioEditText.getText() != null && !loginNovoUsuarioEditText.getText().toString().isEmpty()
                && senhaNovoUsuarioEditText.getText() != null && !senhaNovoUsuarioEditText.getText().toString().isEmpty())
        {
            Task<AuthResult> task = mAuth.createUserWithEmailAndPassword(login, senha);
            task.addOnSuccessListener(authResult -> {
                Toast.makeText(NewUserActivity.this, getString(android.R.string.ok), Toast.LENGTH_SHORT).show();
                finish();
            });
            task.addOnFailureListener(e -> {
                e.printStackTrace();
                Toast.makeText(NewUserActivity.this, "Algo deu errado no cadastro. Por favor tente novamente mais tarde.", Toast.LENGTH_SHORT).show();
            });
        }else{
            Toast.makeText(this, "Usuário ou senha incorretos. Tente novamente.", Toast.LENGTH_SHORT).show();
        }
    }
}

