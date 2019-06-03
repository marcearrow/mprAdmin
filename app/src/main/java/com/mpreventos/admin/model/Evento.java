package com.mpreventos.admin.model;

//model para los eventos
public class Evento {

    //atributos Evento
    private String id;
    private String nombre;
    private String imgUrl;

    //constructor de cada evento
    public Evento(String id, String nombre, String imgUrl) {
        this.id = id;
        this.nombre = nombre;
        this.imgUrl = imgUrl;
    }

    //getter y setters evento
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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
