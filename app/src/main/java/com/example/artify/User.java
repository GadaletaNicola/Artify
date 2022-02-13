package com.example.artify;

public class User {
    private String name;
    private String surname;
    private String username;
    private String email;
    private String img="";
    private String punti;
    private String stato="offline";
    private String key;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPunti(String punti) {
        this.punti = punti;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setEmail(String email) {
        this.email = email;
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
        return punti;
    }

    public String getStato()
    {
        return stato;
    }

    public String getImg()
    {
        return img;
    }

    public String getKey() {
        return key;
    }

    public String getEmail() {
        return email;
    }
}
