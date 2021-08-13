package com.example.ponyhelper.body;

import android.media.Image;

import androidx.annotation.NonNull;

import java.util.Date;

public class Destinazione {
    Indirizzo indirizzo;
    Date dataUltimaModifica;
    double longitudine;
    double latitudine;
    String note;
    Image foto;

    /**
     * costruttore di destinazione, genera un'istanza vuota di destinazione
     */
    public Destinazione() {
        super();
    }

    /**
     * costruttore di destinazione, acccetta tre parametri indirizzo, latitudine  e longitudine
     * @param indirizzo rappresenta l'indirizzo della destinazione
     * @param longitudine rappresenta la longitudine della destinazione
     * @param latitudine rappresenta la latitudine della destinazione
     */
    public Destinazione(Indirizzo indirizzo, double longitudine, double latitudine) {
        super();
        this.indirizzo = indirizzo;
        this.longitudine = longitudine;
        this.latitudine = latitudine;
    }

    /**
     * getter di indirizzo
     * @return ritorna l'indirizzo della destinazionE
     */
    public Indirizzo getIndirizzo() {
        return indirizzo;
    }

    public Date getDataUltimaModifica() {
        return dataUltimaModifica;
    }

    public void setDataUltimaModifica(Date dataUltimaModifica) {
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

    /**
     * getter della foto
     * @return ritorna la foto relativa a una destinazione
     */
    public Image getFoto() {
        return foto;
    }

    /**
     * setter di foto, permette di settare la foto di una desrinazione
     * @param foto foto relativa alla destinazione
     */
    public void setFoto(Image foto) {
        this.foto = foto;
    }

    @NonNull
    @Override
    public String toString() {
        return "Destinazione{" +
                "indirizzo=" + indirizzo +
                ", dataUltimaModifica=" + dataUltimaModifica +
                ", longitudine=" + longitudine +
                ", latitudine=" + latitudine +
                ", note='" + note + '\'' +
                ", foto=" + foto +
                '}';
    }
}
