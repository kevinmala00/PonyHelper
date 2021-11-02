package com.example.ponyhelper.body;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

public class Entrata implements Comparable<Entrata>{
    LocalDate data;
    LocalTime oraInizio;
    LocalTime oraFine;
    LocalTime oreTot;
    double entrate;
    double mancia;
    double km;
    String note;


    public Entrata(LocalDate data, LocalTime oraInizio, LocalTime oraFine, LocalTime oreTot, double entrate, double mancia, double km, String note) {
        this.data = data;
        this.oraInizio = oraInizio;
        this.oraFine = oraFine;
        this.oreTot = oreTot;
        this.entrate = entrate;
        this.mancia = mancia;
        this.km = km;
        this.note = note;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getOraInizio() {
        return oraInizio;
    }

    public void setOraInizio(LocalTime oraInizio) {
        this.oraInizio = oraInizio;
    }

    public LocalTime getOraFine() {
        return oraFine;
    }

    public void setOraFine(LocalTime oraFine) {
        this.oraFine = oraFine;
    }

    public LocalTime getOreTot() {
        return oreTot;
    }

    public void setOreTot(LocalTime oreTot) {
        this.oreTot = oreTot;
    }

    public double getEntrate() {
        return entrate;
    }

    public void setEntrate(double entrate) {
        this.entrate = entrate;
    }

    public double getMancia() {
        return mancia;
    }

    public void setMancia(double mancia) {
        this.mancia = mancia;
    }

    public double getKm() {
        return km;
    }

    public void setKm(double km) {
        this.km = km;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public int compareTo(Entrata e1) {
        if(data.isBefore(e1.data)){
            return -1;
        }else if(data.isAfter(e1.data)){
            return 1;
        }else{
            return 0;
        }
    }
}
