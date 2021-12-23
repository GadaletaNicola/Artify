package com.example.artify;

public class Zone {
    private String nomeZona="";
    private String img = "";
    private String tipo ="";
    private String museo ="";

    public Zone(){}

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setMuseo(String museo) {
        this.museo = museo;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setNomeZona(String nomeZona) {
        this.nomeZona = nomeZona;
    }


    public String getTipo() {
        return tipo;
    }

    public String getImg() {
        return img;
    }

    public String getNomeZona() {
        return nomeZona;
    }

    public String getMuseo() {
        return museo;
    }
}
