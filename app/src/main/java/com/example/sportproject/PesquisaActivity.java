package com.example.sportproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class PesquisaActivity extends AppCompatActivity {

    EditText edtNome, edtSobrenome, edtEmail, edtNascimento;
    Spinner spnEsporte;
    Button btnEnviar, btnVoltarLogin, btnVerRespostas, btnFinalizar;
    TextView txtEstatisticas;

    String[] esportes = {
            "Não pratica esporte", "Futebol", "Críquete", "Hóquei", "Volei", "Basquete", "Tênis",
            "Corrida", "Salto em altura", "Salto em distância", "Arremesso de dardo",
            "Arremesso de peso", "Handebol", "Boxe", "Tênis de mesa"
    };

    // Estatísticas compartilhadas entre as telas
    static Estatisticas estatisticas = new Estatisticas();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisa);

        // Ligação com layout
        edtNome = findViewById(R.id.edtNome);
        edtSobrenome = findViewById(R.id.edtSobrenome);
        edtEmail = findViewById(R.id.edtEmail);
        edtNascimento = findViewById(R.id.edtNascimento);
        spnEsporte = findViewById(R.id.spnEsporte);
        btnEnviar = findViewById(R.id.btnEnviar);
        btnVoltarLogin = findViewById(R.id.btnVoltarLogin);
        btnVerRespostas = findViewById(R.id.btnVerRespostas);
        btnFinalizar = findViewById(R.id.btnFinalizar);
        txtEstatisticas = findViewById(R.id.txtEstatisticas);

        // Preenche spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, esportes);
        spnEsporte.setAdapter(adapter);

        // Máscara automática para data de nascimento (dd/MM/yyyy)
        edtNascimento.addTextChangedListener(new TextWatcher() {
            private String current = "";
            private String ddmmyyyy = "DDMMYYYY";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d]", "");
                    String cleanC = current.replaceAll("[^\\d]", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) sel++;

                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8) {
                        clean = clean + ddmmyyyy.substring(clean.length());
                    } else {
                        clean = clean.substring(0, 8);
                    }

                    clean = String.format("%s/%s/%s",
                            clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = Math.max(sel, 0);
                    current = clean;
                    edtNascimento.setText(current);
                    edtNascimento.setSelection(Math.min(sel, current.length()));
                }
            }
        });

        // Botão ENVIAR
        btnEnviar.setOnClickListener(v -> {
            String nome = edtNome.getText().toString().trim();
            String sobrenome = edtSobrenome.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String nascimento = edtNascimento.getText().toString().trim();
            String esporte = spnEsporte.getSelectedItem().toString();

            // Valida campos
            if (nome.isEmpty() || sobrenome.isEmpty() || email.isEmpty() || nascimento.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Valida nome e sobrenome (somente letras e acentos, sem números/símbolos)
            if (!nome.matches("^[a-zA-ZÀ-ÿ ]+$") || !sobrenome.matches("^[a-zA-ZÀ-ÿ ]+$")) {
                Toast.makeText(this, "Nome e sobrenome não podem conter números ou símbolos!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Valida email
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "E-mail inválido!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Valida data
            if (nascimento.length() != 10) {
                Toast.makeText(this, "Data inválida!", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                int dia = Integer.parseInt(nascimento.substring(0, 2));
                int mes = Integer.parseInt(nascimento.substring(3, 5));
                int ano = Integer.parseInt(nascimento.substring(6, 10));

                if (dia <= 0 || mes <= 0 || ano <= 0 || mes > 12) {
                    Toast.makeText(this, "Data inválida!", Toast.LENGTH_SHORT).show();
                    return;
                }

                int[] diasPorMes = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
                if ((ano % 4 == 0 && ano % 100 != 0) || (ano % 400 == 0)) diasPorMes[1] = 29;

                if (dia > diasPorMes[mes - 1]) {
                    Toast.makeText(this, "Data inválida!", Toast.LENGTH_SHORT).show();
                    return;
                }

            } catch (Exception e) {
                Toast.makeText(this, "Erro na data!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Verifica nome completo único
            String nomeCompleto = nome + " " + sobrenome;
            if (estatisticas.getRespostas().contains(nomeCompleto)) {
                Toast.makeText(this, "Nome já registrado!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Adiciona resposta
            estatisticas.adicionarResposta(esporte, nomeCompleto);
            atualizarEstatisticas();
            Toast.makeText(this, "Resposta enviada!", Toast.LENGTH_SHORT).show();

            // Limpa campos
            edtNome.setText("");
            edtSobrenome.setText("");
            edtEmail.setText("");
            edtNascimento.setText("");
            spnEsporte.setSelection(0);
        });

        // Botão VOLTAR LOGIN
        btnVoltarLogin.setOnClickListener(v -> {
            Intent i = new Intent(PesquisaActivity.this, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        });

        // Botão VER RESPOSTAS
        btnVerRespostas.setOnClickListener(v -> {
            final EditText senhaInput = new EditText(this);
            senhaInput.setHint("Digite a senha");

            int padding = (int) (16 * getResources().getDisplayMetrics().density);
            senhaInput.setPadding(padding, padding, padding, padding);

            new AlertDialog.Builder(this)
                    .setTitle("Acesso Restrito")
                    .setMessage("Digite a senha para ver as respostas:")
                    .setView(senhaInput)
                    .setPositiveButton("OK", (dialog, which) -> {
                        String senha = senhaInput.getText().toString();
                        if (senha.equals("admin123")) {
                            mostrarTodasRespostas();
                        } else {
                            Toast.makeText(this, "Senha incorreta!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });

        // Botão FINALIZAR PESQUISA
        btnFinalizar.setOnClickListener(v -> {
            Intent i = new Intent(PesquisaActivity.this, FinalizarActivity.class);
            startActivity(i);
        });
    }

    // Atualiza estatísticas
    private void atualizarEstatisticas() {
        StringBuilder sb = new StringBuilder();
        sb.append("Total de respostas: ").append(estatisticas.total).append("\n\n");

        for (String e : esportes) {
            int qtd = estatisticas.getQtd(e);
            double perc = estatisticas.getPercentual(e);
            sb.append(e).append(": ").append(qtd)
                    .append(" (").append(String.format("%.1f", perc)).append("%)\n");
        }

        txtEstatisticas.setText(sb.toString());
    }

    // Mostra todas as respostas
    private void mostrarTodasRespostas() {
        StringBuilder sb = new StringBuilder();
        for (String r : estatisticas.getRespostas()) {
            sb.append(r).append("\n");
        }

        new AlertDialog.Builder(this)
                .setTitle("Todas as Respostas")
                .setMessage(sb.length() > 0 ? sb.toString() : "Nenhuma resposta ainda.")
                .setPositiveButton("Fechar", null)
                .show();
    }
}
