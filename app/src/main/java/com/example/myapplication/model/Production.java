package com.example.myapplication.model;

import androidx.annotation.NonNull;

import java.util.Date;
import java.util.Objects;

public class Production {

    private Double litros;
    private Date data;

    public Production() {
    }

    public Production(Double litros, Date data) {
        this.litros = litros;
        this.data = data;
    }

    public Double getLitros() {
        return litros;
    }

    public void setLitros(Double litros) {
        this.litros = litros;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Production that = (Production) o;
        return litros.equals(that.litros) && data.equals(that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(litros, data);
    }

    @Override
    public String toString() {
        return "Production{" +
                "litros=" + litros +
                ", data=" + data +
                '}';
    }

}
