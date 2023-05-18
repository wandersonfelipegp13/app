package com.example.myapplication.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Date;
import java.util.Objects;

public class Animal implements Parcelable {

    private String identificacao;
    private String foto;
    private String raca;
    private Double peso;
    private String genero;
    private boolean produzindo;
    private Date dataNascimento;

    public Animal() {
    }

    public Animal(String identificacao, String foto, String raca, Double peso, String genero, boolean produzindo, Date dataNascimento) {
        this.setIdentificacao(identificacao);
        this.setFoto(foto);
        this.setRaca(raca);
        this.setPeso(peso);
        this.setGenero(genero);
        this.setProduzindo(produzindo);
        this.setDataNascimento(dataNascimento);
    }

    protected Animal(Parcel in) {
        identificacao = in.readString();
        foto = in.readString();
        raca = in.readString();
        if (in.readByte() == 0) {
            peso = null;
        } else {
            peso = in.readDouble();
        }
        genero = in.readString();
        produzindo = in.readByte() != 0;
        if (in.readByte() == 0) {
            dataNascimento = null;
        } else {
            dataNascimento = new Date(in.readLong());
        }
    }

    public static final Creator<Animal> CREATOR = new Creator<Animal>() {
        @Override
        public Animal createFromParcel(Parcel in) {
            return new Animal(in);
        }

        @Override
        public Animal[] newArray(int size) {
            return new Animal[size];
        }
    };

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

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
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

    @NonNull
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(identificacao);
        parcel.writeString(foto);
        parcel.writeString(raca);
        if (peso == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(peso);
        }
        parcel.writeString(genero);
        parcel.writeByte((byte) (produzindo ? 1 : 0));
        if (dataNascimento == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(dataNascimento.getTime());
        }
    }

}
