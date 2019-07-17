package com.mpreventos.admin.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.Toast;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mpreventos.admin.R;
import com.mpreventos.admin.helper.FirebaseHelper;
import com.mpreventos.admin.helper.StorageHelper;

public class DialogAlertDelete {

  private Context context;
  private String mensaje;
  private DatabaseReference ds;
  private String nombre;

  public DialogAlertDelete(Context context, String mensaje, DatabaseReference ds, String nombre) {
    this.context = context;
    this.mensaje = mensaje;
    this.ds = ds;
    this.nombre = nombre;
  }

  public void CreateDeleteDialog() {
    MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(context,
        R.style.AlertDialogTheme);
    dialogBuilder.setTitle("¿Estás Seguro?");
    dialogBuilder.setMessage("Deseas eliminar " + mensaje);
    dialogBuilder.setCancelable(false);

    dialogBuilder.setNegativeButton("ELIMINAR", new OnClickListener() {
      @Override
      public void onClick(DialogInterface dialogInterface, int i) {
        FirebaseHelper firebaseHelper = new FirebaseHelper(ds);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference()
            .child(ds.getKey()).child(nombre);

        StorageHelper storageHelper = new StorageHelper(storageReference);
        storageHelper.DeleteStorage();
        boolean estado = firebaseHelper.EliminarNodoFirebase(nombre, ds.getKey());

        if (estado) {
          Toast.makeText(context, "Eliminado exitosamente", Toast.LENGTH_SHORT).show();
        } else {
          Toast.makeText(context, "No se pudo eliminar", Toast.LENGTH_SHORT).show();
        }
      }
    });

    dialogBuilder.setNeutralButton("CANCELAR", null);
    dialogBuilder.show();
  }


}
