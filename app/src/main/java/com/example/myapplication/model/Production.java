package com.example.myapplication.model;

import java.util.Date;
import java.util.Objects;

public class Production {

    private String id;
    private double litros;
    private Date data;

    public Production() {
    }

    public Production(String id, double litros, Date data) {
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

    public double getLitros() {
        return litros;
    }

    public void setLitros(double litros) {
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

    @Override
    public String toString() {
        return "Production{" +
                "id='" + id + '\'' +
                ", litros=" + litros +
                ", data=" + data +
                '}';
    }

}
