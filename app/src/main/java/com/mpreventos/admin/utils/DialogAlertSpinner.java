package com.mpreventos.admin.utils;

import android.content.Context;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.mpreventos.admin.R;


public class DialogAlertSpinner {

  Context context;
  String title;
  String mensaje;

  public DialogAlertSpinner(Context context) {
    this.context = context;
    this.title = "Error";
    this.mensaje = "Por favor seleccione un elemento de la lista ";
  }

  public void DialogCreator() {
    MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(context,
        R.style.AlertDialogTheme);
    dialogBuilder.setTitle(title);
    dialogBuilder.setMessage(mensaje);
    dialogBuilder.setPositiveButton("ACEPTAR", null);

    dialogBuilder.show();
  }
}
