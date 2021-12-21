package com.example.artify;

public class Opera {
    private String id = "";
    private String img = "";
    private String titolo = "";
    private String zona = "";
    private String descrizione = "";
    private int voto = 0;

    public Opera(){}


    public void setImg(String img) {
        this.img = img;
    }

    public void setVoto(int voto) {
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




    public String getImg() {
        return img;
    }

    public int getVoto() {
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
}
