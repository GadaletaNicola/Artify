package com.example.artify;

public class Zone {
    private String nomeZona="";
    private String img = "";
    private String nomeMuseo="";

    public Zone(){

    }



    public void setImg(String img) {
        this.img = img;
    }

    public void setNomeZona(String nomeZona) {
        this.nomeZona = nomeZona;
    }
    public void setNomeMuseo(String nomeMuseo) {
        this.nomeMuseo = nomeMuseo;
    }


    public String getImg() {
        return img;
    }

    public String getNomeZona() {
        return nomeZona;
    }

    public String getNomeMuseo() {
        return nomeMuseo;
    }


}
