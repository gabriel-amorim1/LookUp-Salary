package com.example.tcc_final;

import com.google.firebase.database.IgnoreExtraProperties;

public class Dados {
    private String email;
    private String senha;
    private String nome;
    private String sobrenome;
    private String salarioBruto;
    private String horasDiarias;


    public Dados() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Dados(String email, String senha, String nome, String sobrenome, String salarioBruto, String horasDiarias){
        this.email = email;
        this.senha = senha;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.salarioBruto = salarioBruto;
        this.horasDiarias = horasDiarias;

    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public String getNome() {
        return nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public String getSalarioBruto() {
        return salarioBruto;
    }

    public String getHorasDiarias() {
        return horasDiarias;
    }

}
