package com.example.myapplication.model;

import java.util.Date;
import java.util.Objects;

public class Care {

    private String id;
    private Date data;
    private String descricao;
    private String medicamentos;

    public Care() {
    }

    public Care(String id, Date data, String descricao, String medicamentos) {
        this.id = id;
        this.data = data;
        this.descricao = descricao;
        this.medicamentos = medicamentos;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getMedicamentos() {
        return medicamentos;
    }

    public void setMedicamentos(String medicamentos) {
        this.medicamentos = medicamentos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Care care = (Care) o;
        return id.equals(care.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
