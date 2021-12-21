package com.example.artify;

public class User {
    private String name;
    private String surname;
    private String username;
    private String img="";
    private int punti=0;
    private String stato="offline";

    public void setUsername(String username) {
        this.username = username;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public String getSurname() {
        return surname;
    }

    public String getName() {
        return name;
    }

    public String getPunti()
    {
        return String.valueOf(punti);
    }

    public String getStato()
    {
        return stato;
    }

    public String getImg()
    {
        return img;
    }
}
