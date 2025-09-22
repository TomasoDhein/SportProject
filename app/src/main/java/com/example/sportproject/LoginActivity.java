package com.example.sportproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText edtUsuario, edtSenha;
    Button btnLogin, btnSobre;

    final String USUARIO = "admin";
    final String SENHA = "123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUsuario = findViewById(R.id.edtUsuario);
        edtSenha = findViewById(R.id.edtSenha);
        btnLogin = findViewById(R.id.btnLogin);
        btnSobre = findViewById(R.id.btnSobre);

        btnLogin.setOnClickListener(v -> {
            String u = edtUsuario.getText().toString();
            String s = edtSenha.getText().toString();

            if(u.equals(USUARIO) && s.equals(SENHA)){
                Intent i = new Intent(LoginActivity.this, PesquisaActivity.class);
                startActivity(i);
            } else {
                Toast.makeText(this, "Usuário ou senha incorretos!", Toast.LENGTH_SHORT).show();
            }
        });

        btnSobre.setOnClickListener(v -> {
            Intent i = new Intent(LoginActivity.this, SobreActivity.class);
            startActivity(i);
        });
    }
}