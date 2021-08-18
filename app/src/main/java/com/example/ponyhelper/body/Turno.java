package com.example.ponyhelper.body;

import androidx.annotation.NonNull;

import java.sql.Time;
import java.util.Date;


/**
 * @author kevin
 *
 * rappresenta un turno di un pony
 */

public class Turno {
    /**
     * rappresenta la data del turno
     */
    Date data;

    /**
     * rappresenta l'ora di inizio del turno
     */
    Time oraInizio;

    /**
     * rappresenta l'ora di fine del turno
     */
    Time oraFine;

    public Turno() {
    }

    public Turno(Date data, Time oraInizio, Time oraFine) {
        this.data = data;
        this.oraInizio = oraInizio;
        this.oraFine = oraFine;
    }

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

    @NonNull
    @Override
    public String toString() {
        return "Turno{" +
                "data=" + data +
                ", oraInizio=" + oraInizio +
                ", oraFine=" + oraFine +
                '}';
    }
}
