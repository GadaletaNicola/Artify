package com.example.artify;

public class Opera {
    private String id = "";
    private String img = "";
    private String titolo = "";
    private String zona = "";
    private String descrizione = "";
    private String stile = "";
    private String Dimensione = "";
    private String Data = "";
    private String Autore = "";
    private float voto = 0;
    private int numeroVoti = 0;

    public Opera(){}


    public void setImg(String img) {
        this.img = img;
    }

    public void setVoto(float voto) {
        this.voto = voto;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public void setAutore(String autore) { Autore = autore; }

    public void setData(String data) { Data = data; }

    public void setDimensione(String dimensione) { Dimensione = dimensione; }

    public void setStile(String stile) { this.stile = stile; }

    public void setNumeroVoti(int numero) { numeroVoti = numero; }


    public String getImg() {
        return img;
    }

    public float getVoto() {
        return voto;
    }

    public String getZona() {
        return zona;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public String getId() {
        return id;
    }

    public String getTitolo() {
        return titolo;
    }

    public String getAutore() { return Autore; }

    public String getData() { return Data; }

    public String getDimensione() { return Dimensione; }

    public String getStile() { return stile; }

    public int getNumeroVoti() { return numeroVoti; }
}
