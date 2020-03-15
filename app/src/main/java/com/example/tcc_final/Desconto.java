package com.example.tcc_final;

import java.io.Serializable;

public class Desconto implements Serializable {

    private String nome;
    private String valor;

    public Desconto(String nome, String valor){
        this.nome = nome;
        this.valor = valor;
    }

    public String getNome() {
        return nome;
    }

    public String getValor() {
        return valor;
    }
}
