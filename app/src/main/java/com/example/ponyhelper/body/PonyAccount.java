package com.example.ponyhelper.body;

import androidx.annotation.NonNull;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PonyAccount account = (PonyAccount) o;
        return Objects.equals(Username, account.Username) && Objects.equals(Email, account.Email) && Objects.equals(Nome, account.Nome) && Objects.equals(Cognome, account.Cognome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Username, Email, Nome, Cognome);
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
