package com.example.sportproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class FinalizarActivity extends AppCompatActivity {

    private TextView txtResultadoFinal;
    private Button btnVoltarPesquisa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalizar); // XML separado só para finalizar

        txtResultadoFinal = findViewById(R.id.txtResultadoFinal);
        btnVoltarPesquisa = findViewById(R.id.btnVoltarPesquisa);

        mostrarResultados();

        btnVoltarPesquisa.setOnClickListener(v -> {
            // Zera os dados da pesquisa antes de voltar
            PesquisaActivity.estatisticas = new Estatisticas();

            // Volta para a PesquisaActivity
            Intent intent = new Intent(FinalizarActivity.this, PesquisaActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }

    private void mostrarResultados() {
        if (PesquisaActivity.estatisticas.total == 0) {
            txtResultadoFinal.setText("Nenhuma resposta registrada.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("📊 Resultado Final da Pesquisa\n\n");
        sb.append("Total de respostas: ").append(PesquisaActivity.estatisticas.total).append("\n\n");

        for (String esporte : PesquisaActivity.estatisticas.getEsportes()) {
            int qtd = PesquisaActivity.estatisticas.getQtd(esporte);
            double perc = PesquisaActivity.estatisticas.getPercentual(esporte);
            sb.append(esporte).append(": ").append(qtd)
                    .append(" (").append(String.format("%.1f", perc)).append("%)\n");
        }

        txtResultadoFinal.setText(sb.toString());
    }
}
