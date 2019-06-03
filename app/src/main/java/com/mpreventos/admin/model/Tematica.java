package com.mpreventos.admin.model;

public class Tematica {

    //atributos Tematica
    private String id;
    private String nombre;
    private String urlImg;

    //constructor Tematica
    public Tematica(String id, String nombre, String urlImg) {
        this.id = id;
        this.nombre = nombre;
        this.urlImg = urlImg;
    }

    //getter y setters Tematica
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUrlImg() {
        return urlImg;
    }

    public void setUrlImg(String urlImg) {
        this.urlImg = urlImg;
    }

}
