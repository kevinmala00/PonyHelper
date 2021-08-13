package com.example.ponyhelper.body;

import java.sql.Time;
import java.util.Date;

public class Entrate {
    Date data;
    Time oraInizio;
    Time oraFine;
    Time oreTot;
    double entrate;
    double mancia;
    double km;
    String note;

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Time getOraInizio() {
        return oraInizio;
    }

    public void setOraInizio(Time oraInizio) {
        this.oraInizio = oraInizio;
    }

    public Time getOraFine() {
        return oraFine;
    }

    public void setOraFine(Time oraFine) {
        this.oraFine = oraFine;
    }

    public Time getOreTot() {
        return oreTot;
    }

    public void setOreTot(Time oreTot) {
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
    public String toString() {
        return "Entrate{" +
                "data=" + data +
                ", oraInizio=" + oraInizio +
                ", oraFine=" + oraFine +
                ", oreTot=" + oreTot +
                ", entrate=" + entrate +
                ", mancia=" + mancia +
                ", km=" + km +
                ", note='" + note + '\'' +
                '}';
    }
}
