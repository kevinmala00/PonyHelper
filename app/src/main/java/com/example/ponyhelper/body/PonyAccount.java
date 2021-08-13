package com.example.ponyhelper.body;

import androidx.annotation.NonNull;

public class PonyAccount {
    String Username;
    String Email;
    String Nome;
    String Cognome;
    String DataNascita;
    String Password;
    int AccessCode;

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

    public String getDataNascita() {
        return DataNascita;
    }

    public void setDataNascita(String dataNascita) {
        DataNascita = dataNascita;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public int getAccessCode() {
        return AccessCode;
    }

    public void setAccessCode(int accessCode) {
        AccessCode = accessCode;
    }

    @NonNull
    @Override
    public String toString() {
        return "Account{" +
                "Username='" + Username + '\'' +
                ", Nome='" + Nome + '\'' +
                ", Cognome='" + Cognome + '\'' +
                ", DataNascita='" + DataNascita + '\'' +
                ", Password='" + Password + '\'' +
                ", AccessCode=" + AccessCode +
                '}';
    }
}
