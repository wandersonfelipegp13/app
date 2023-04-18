package com.example.myapplication.model;

import java.util.Date;
import java.util.Objects;

public class Animal {

    private String identificacao;
    private String foto;
    private String raca;
    private double peso;
    private String genero;
    private boolean produzindo;
    private Date dataNascimento;

    public Animal() {
    }

    public Animal(String identificacao, String foto, String raca, double peso, String genero, boolean produzindo, Date dataNascimento) {
        this.setIdentificacao(identificacao);
        this.setFoto(foto);
        this.setRaca(raca);
        this.setPeso(peso);
        this.setGenero(genero);
        this.setProduzindo(produzindo);
        this.setDataNascimento(dataNascimento);
    }

    public String getIdentificacao() {
        return identificacao;
    }

    public void setIdentificacao(String identificacao) {
        this.identificacao = identificacao;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getRaca() {
        return raca;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public boolean isProduzindo() {
        return produzindo;
    }

    public void setProduzindo(boolean produzindo) {
        this.produzindo = produzindo;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Animal animal = (Animal) o;
        return identificacao.equals(animal.identificacao);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identificacao);
    }

    @Override
    public String toString() {
        return "Animal{" +
                "identificacao='" + identificacao + '\'' +
                ", foto='" + foto + '\'' +
                ", raca='" + raca + '\'' +
                ", peso=" + peso +
                ", genero='" + genero + '\'' +
                ", produzindo=" + produzindo +
                ", dataNascimento=" + dataNascimento +
                '}';
    }

}
