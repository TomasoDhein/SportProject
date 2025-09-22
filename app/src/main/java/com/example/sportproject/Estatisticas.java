package com.example.sportproject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Estatisticas {
    int total = 0;
    HashMap<String, Integer> mapa = new HashMap<>();
    List<String> respostas = new ArrayList<>();

    // Array de esportes fixo
    private String[] esportes = {
            "Não pratica esporte", "Futebol", "Críquete", "Hóquei", "Volei",
            "Basquete", "Tênis", "Corrida", "Salto em altura", "Salto em distância",
            "Arremesso de dardo", "Arremesso de peso", "Handebol", "Boxe", "Tênis de mesa"
    };

    public void adicionarResposta(String esporte, String nomeCompleto) {
        total++;
        mapa.put(esporte, mapa.getOrDefault(esporte, 0) + 1);
        respostas.add(nomeCompleto + " → " + esporte);
    }

    public int getQtd(String esporte) {
        return mapa.getOrDefault(esporte, 0);
    }

    public double getPercentual(String esporte) {
        if (total == 0) return 0;
        return (getQtd(esporte) * 100.0) / total;
    }

    public List<String> getRespostas() {
        return respostas;
    }

    // ✅ Novo método para pegar todos os esportes
    public List<String> getEsportes() {
        return Arrays.asList(esportes);
    }
}
