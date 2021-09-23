package com.example.ponyhelper.body;

import androidx.annotation.NonNull;

import java.util.Objects;

public class Costo {
    String meseAnno;
    double costoCarburante;
    double consumoMedio;

    public Costo() {
    }

    public Costo(String meseAnno, double costoCarburante, double consumoMedio) {
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
        Costo costo = (Costo) o;
        return Double.compare(costo.costoCarburante, costoCarburante) == 0 && Double.compare(costo.consumoMedio, consumoMedio) == 0 && Objects.equals(meseAnno, costo.meseAnno);
    }

    @Override
    public int hashCode() {
        return Objects.hash(meseAnno, costoCarburante, consumoMedio);
    }

    @NonNull
    @Override
    public String toString() {
        return "Costo{" +
                "meseAnno='" + meseAnno + '\'' +
                ", costoCarburante=" + costoCarburante +
                ", consumoMedio=" + consumoMedio +
                '}';
    }
}
