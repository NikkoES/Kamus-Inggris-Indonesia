package io.github.nikkoes.kamusinggrisindonesia.model;

import java.io.Serializable;

public class Kamus implements Serializable {

    private int id;
    private String kata;
    private String deskripsi;

    public Kamus(String kata, String deskripsi) {
        this.kata = kata;
        this.deskripsi = deskripsi;
    }

    public int getId() {
        return id;
    }

    public String getKata() {
        return kata;
    }

    public String getDeskripsi() {
        return deskripsi;
    }
}
