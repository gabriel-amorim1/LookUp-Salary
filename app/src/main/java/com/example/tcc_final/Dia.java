package com.example.tcc_final;

import java.io.Serializable;

public class Dia implements Serializable {
    private String dia;
    private String horas;

    //informações secundárias
    private String entrada;
    private String intervalo;
    private String voltaIntervalo;
    private String saida;
    private String total;

    public Dia(String dia, String horas){
        this.dia = dia;
        this.horas = horas;
    }

    public String getDia() {
        return dia;
    }

    public String getHoras() {
        return horas;
    }

    public String getEntrada() {
        return entrada;
    }

    public void setEntrada(String entrada) {
        this.entrada = entrada;
    }

    public String getIntervalo() {
        return intervalo;
    }

    public void setIntervalo(String intervalo) {
        this.intervalo = intervalo;
    }

    public String getVoltaIntervalo() {
        return voltaIntervalo;
    }

    public void setVoltaIntervalo(String voltaIntervalo) {
        this.voltaIntervalo = voltaIntervalo;
    }

    public String getSaida() {
        return saida;
    }

    public void setSaida(String saida) {
        this.saida = saida;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
