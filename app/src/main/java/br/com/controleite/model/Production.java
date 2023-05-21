package br.com.controleite.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Date;
import java.util.Objects;

public class Production implements Parcelable {

    private Double litros;
    private Date data;

    public Production() {
    }

    public Production(Double litros, Date data) {
        this.litros = litros;
        this.data = data;
    }

    protected Production(Parcel in) {
        if (in.readByte() == 0) {
            litros = null;
        } else {
            litros = in.readDouble();
        }
        if (in.readByte() == 0) {
            data = null;
        } else {
            data = new Date(in.readLong());
        }
    }

    public static final Creator<Production> CREATOR = new Creator<Production>() {
        @Override
        public Production createFromParcel(Parcel in) {
            return new Production(in);
        }

        @Override
        public Production[] newArray(int size) {
            return new Production[size];
        }
    };

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

    @NonNull
    @Override
    public String toString() {
        return "Production{" +
                "litros=" + litros +
                ", data=" + data +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        if (litros == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(litros);
        }
        if (data == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(data.getTime());
        }
    }
}
