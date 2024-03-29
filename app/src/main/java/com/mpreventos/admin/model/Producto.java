package com.mpreventos.admin.model;

public class Producto {

  //atributos Producto
  private String id;
  private String nombre;
  private String descripcion;
  private String imgUrl;
  private String categoria;

  //constructor Producto
  public Producto(String id, String nombre, String descripcion, String imgUrl, String categoria) {
    this.id = id;
    this.nombre = nombre;
    this.descripcion = descripcion;
    this.imgUrl = imgUrl;
    this.categoria = categoria;
  }

  //getter y setter Producto
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

  public String getDescripcion() {
    return descripcion;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public String getImgUrl() {
    return imgUrl;
  }

  public void setImgUrl(String imgUrl) {
    this.imgUrl = imgUrl;
  }

  public String getCategoria() {
    return categoria;
  }

  public void setCategoria(String categoria) {
    this.categoria = categoria;
  }
}
