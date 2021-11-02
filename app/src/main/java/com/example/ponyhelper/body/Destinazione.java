package com.example.ponyhelper.body;

import androidx.annotation.NonNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Destinazione implements Comparable<Destinazione>{
    Indirizzo indirizzo;
    LocalDate dataUltimaModifica;
    LocalTime oraUltimaModifica;
    double mancia;
    double longitudine;
    double latitudine;
    String note;

    /**
     * costruttore di destinazione, genera un'istanza vuota di destinazione
     */
    public Destinazione() {
        super();
    }

    public Destinazione(Indirizzo indirizzo, LocalDate dataUltimaModifica, LocalTime oraUltimaModifica, double mancia, double longitudine, double latitudine, String note) {
        this.indirizzo = indirizzo;
        this.dataUltimaModifica = dataUltimaModifica;
        this.oraUltimaModifica = oraUltimaModifica;
        this.mancia = mancia;
        this.longitudine = longitudine;
        this.latitudine = latitudine;
        this.note = note;
    }

    public LocalTime getOraUltimaModifica() {
        return oraUltimaModifica;
    }

    public void setOraUltimaModifica(LocalTime oraUltimaModifica) {
        this.oraUltimaModifica = oraUltimaModifica;
    }

    /**
     * getter di indirizzo
     * @return ritorna l'indirizzo della destinazionE
     */
    public Indirizzo getIndirizzo() {
        return indirizzo;
    }

    public LocalDate getDataUltimaModifica() {
        return dataUltimaModifica;
    }

    public void setDataUltimaModifica(LocalDate dataUltimaModifica) {
        this.dataUltimaModifica = dataUltimaModifica;
    }

    /**
     * setter di indirizzo, permette di settare l'indirizzo di una destinazione
     * @param indirizzo l'indirizzo di una destinazione
     */
    public void setIndirizzo(Indirizzo indirizzo) {
        this.indirizzo = indirizzo;
    }

    /**
     * getter di longitudine
     * @return ritorna la longitudine di una destinazione
     */
    public double getLongitudine() {
        return longitudine;
    }

    /**
     * setter di longitudine, permette di settare la longitudine di una destinazione
     * @param longitudine longitudine della destinazione
     */
    public void setLongitudine(double longitudine) {
        this.longitudine = longitudine;
    }

    /**
     * getter di latitudine
     * @return ritorna la latitudine della destinazione
     */
    public double getLatitudine() {
        return latitudine;
    }

    /**
     * setter di latitudine, permette di settare la latitudine di una destinazione
     * @param latitudine latitudine della destinazione
     */
    public void setLatitudine(double latitudine) {
        this.latitudine = latitudine;
    }

    /**
     * getter delle note
     * @return ritorna le note relative a una destinazione
     */
    public String getNote() {
        return note;
    }

    /**
     * setter di note, permette di settare le note di una destinazione
     * @param note note della destinazione
     */
    public void setNote(String note) {
        this.note = note;
    }




    public double getMancia() {
        return mancia;
    }

    public void setMancia(double mancia) {
        this.mancia = mancia;
    }

    @NonNull
    @Override
    public String toString() {
        return "Destinazione{" +
                "indirizzo=" + indirizzo +
                ", dataUltimaModifica=" + dataUltimaModifica.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) +
                ", longitudine=" + longitudine +
                ", latitudine=" + latitudine +
                ", note='" + note + '\'' +
                '}';
    }

    /**
     * ordinati prima in basae alla data di ultima modifica poi in base all'ora di ultima modifica
     * poi in ordine  di indirizzo
     * @param d1 destinazione con cui deve avvenire il confronto
     * @return positivo se segue, negativo se precede, 0 se uguali
     */
    @Override
    public int compareTo(Destinazione d1) {
        if(dataUltimaModifica.isBefore(d1.getDataUltimaModifica())){
            return -1;
        }else if(dataUltimaModifica.isAfter(d1.getDataUltimaModifica())){
            return  1;
        }else {
            if(oraUltimaModifica.isBefore(d1.getOraUltimaModifica())){
                return -1;
            }else if(oraUltimaModifica.isAfter(d1.getOraUltimaModifica())){
                return 1;
            }else{
                return indirizzo.compareTo(d1.getIndirizzo());
            }
        }
    }
}
