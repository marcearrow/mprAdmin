package com.mpreventos.admin.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

public class Funciones {

    private static boolean estado;
    private String error = "";

    public static boolean validarTexto(String texto) {
        estado = TextUtils.isEmpty(texto);
        return estado;
    }

  public void setImg(String imgUrl, ImageView imageView, Context context) {
        if (imgUrl != null) {
            try {
                ImageLoader imageLoader = new ImageLoader(context);
                imageLoader.setImgWithGlide(imgUrl, imageView);
            } catch (Exception ignored) {

            }
        }
    }

    public static String asignarValor(String texto) {
        String valor = "";
        if (!validarTexto(texto)) {
            valor = texto;
        }
        return valor;
    }
}
