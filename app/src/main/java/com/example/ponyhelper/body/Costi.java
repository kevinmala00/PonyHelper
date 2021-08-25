package com.example.ponyhelper.body;

import androidx.annotation.NonNull;

import java.util.Objects;

public class Costi {
    String meseAnno;
    double costoCarburante;
    double consumoMedio;

    public Costi() {
    }

    public Costi(String meseAnno, double costoCarburante, double consumoMedio) {
        this.meseAnno = meseAnno;
        this.costoCarburante = costoCarburante;
        this.consumoMedio = consumoMedio;
    }

    public String getMeseAnno() {
        return meseAnno;
    }

    public void setMeseAnno(String meseAnno) {
        this.meseAnno = meseAnno;
    }

    public double getCostoCarburante() {
        return costoCarburante;
    }

    public void setCostoCarburante(double costoCarburante) {
        this.costoCarburante = costoCarburante;
    }

    public double getConsumoMedio() {
        return consumoMedio;
    }

    public void setConsumoMedio(double consumoMedio) {
        this.consumoMedio = consumoMedio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Costi costi = (Costi) o;
        return Double.compare(costi.costoCarburante, costoCarburante) == 0 && Double.compare(costi.consumoMedio, consumoMedio) == 0 && Objects.equals(meseAnno, costi.meseAnno);
    }

    @Override
    public int hashCode() {
        return Objects.hash(meseAnno, costoCarburante, consumoMedio);
    }

    @NonNull
    @Override
    public String toString() {
        return "Costi{" +
                "meseAnno='" + meseAnno + '\'' +
                ", costoCarburante=" + costoCarburante +
                ", consumoMedio=" + consumoMedio +
                '}';
    }
}
