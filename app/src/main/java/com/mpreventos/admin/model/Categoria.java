package com.mpreventos.admin.model;

public class Categoria {

  //atributos Categoria
  private String id;
  private String nombre;
  private String imgUrl;
  private String tematica;

  //constructor Categoria

  public Categoria(String id, String nombre, String imgUrl, String tematica) {
    this.id = id;
    this.nombre = nombre;

    this.imgUrl = imgUrl;
    this.tematica = tematica;
  }

  //getter y setter Categoria

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

  public String getTematica() {
    return tematica;
  }

  public void setTematica(String tematica) {
    this.tematica = tematica;
  }
}
