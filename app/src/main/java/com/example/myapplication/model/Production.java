package com.example.myapplication.model;

import androidx.annotation.NonNull;

import java.util.Date;
import java.util.Objects;

public class Production {

    private String id;
    private Double litros;
    private Date data;

    public Production() {
    }

    public Production(String id, Double litros, Date data) {
        this.id = id;
        this.litros = litros;
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @NonNull
    @Override
    public String toString() {
        return "Production{" +
                "id='" + id + '\'' +
                ", litros=" + litros +
                ", data=" + data +
                '}';
    }

}
