package com.example.tcc_final;

import com.google.firebase.database.IgnoreExtraProperties;

public class HorasTrabalhadas {
    public String entrada;
    public String intervalo;
    public String saidaIntervalo;
    public String saida;
    public String totalHoras;
    public String totalMinutos;
    public String totalDia;

    public HorasTrabalhadas() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public HorasTrabalhadas(String entrada, String intervalo, String saidaIntervalo, String saida, String totalHoras, String totalMinutos, String totalDia) {
        this.entrada = entrada;
        this.intervalo = intervalo;
        this.saidaIntervalo = saidaIntervalo;
        this.saida = saida;
        this.totalHoras = totalHoras;
        this.totalMinutos = totalMinutos;
        this.totalDia = totalDia;
    }


}
