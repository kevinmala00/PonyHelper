package com.example.ponyhelper.body;

import androidx.annotation.NonNull;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
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
    LocalDate data;

    /**
     * rappresenta l'ora di inizio del turno
     */
    LocalTime oraInizio;

    /**
     * rappresenta l'ora di fine del turno
     */
    LocalTime oraFine;

    public Turno() {
    }

    public Turno(LocalDate data, LocalTime oraInizio, LocalTime oraFine) {
        this.data = data;
        this.oraInizio = oraInizio;
        this.oraFine = oraFine;
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
}
