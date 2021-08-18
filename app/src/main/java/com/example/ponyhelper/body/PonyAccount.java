package com.example.ponyhelper.body;

import androidx.annotation.NonNull;

public class PonyAccount {
    String Username;
    String Email;
    String Nome;
    String Cognome;

    public PonyAccount() {
    }

    public PonyAccount(String username, String email, String nome, String cognome) {
        Username = username;
        Email = email;
        Nome = nome;
        Cognome = cognome;
    }


    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public String getCognome() {
        return Cognome;
    }

    public void setCognome(String cognome) {
        Cognome = cognome;
    }

    @NonNull
    @Override
    public String toString() {
        return "Account{" +
                "Username='" + Username + '\'' +
                ", Nome='" + Nome + '\'' +
                ", Cognome='" + Cognome + '\'' +
                '}';
    }
}
