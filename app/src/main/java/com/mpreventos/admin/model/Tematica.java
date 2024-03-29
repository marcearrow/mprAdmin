package com.mpreventos.admin.model;

public class Tematica {

  //atributos Tematica
  private String id;
  private String nombre;
  private String imgUrl;
  private String evento;

  //constructor Tematica
  public Tematica(String id, String nombre, String imgUrl, String evento) {
    this.id = id;
    this.nombre = nombre;
    this.imgUrl = imgUrl;
    this.evento = evento;
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

  public String getImgUrl() {
    return imgUrl;
  }

  public void setImgUrl(String imgUrl) {
    this.imgUrl = imgUrl;
  }

  public String getEvento() {
    return evento;
  }

  public void setEvento(String evento) {
    this.evento = evento;
  }
}
