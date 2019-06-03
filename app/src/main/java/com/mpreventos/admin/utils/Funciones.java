package com.mpreventos.admin.utils;

import android.text.TextUtils;

public class Funciones {

    private static boolean estado;

    public static boolean validarTexto(String texto) {
        estado = !TextUtils.isEmpty(texto);
        return !estado;
    }
}
