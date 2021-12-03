package com.example.artify;

import java.util.ArrayList;

public class Percorso {
    private int voto = 0;
    private String nomePercorso="";
    private String img = "";
    private String tipoPercorso = "";


    public Percorso(){

    }

    public void setVoto(int voto) {
        this.voto = voto;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setNomePercorso(String nomePercorso) {
        this.nomePercorso = nomePercorso;
    }

    public void setTipoPercorso(String tipoPercorso) {
        this.tipoPercorso = tipoPercorso;
    }



    public int getVoto() {
        return voto;
    }

    public String getImg() {
        return img;
    }

    public String getNomePercorso() {
        return nomePercorso;
    }

    public String getTipoPercorso() {
        return tipoPercorso;
    }
}
