package com.mpreventos.admin.helper;

import android.net.Uri;

import com.google.firebase.storage.StorageReference;

public class StorageHelper {

    private StorageReference storageReference;
    private Boolean estado;

    public StorageHelper(StorageReference storageReference) {
        this.storageReference = storageReference;
    }

    //TODO: MODIFICAR PATH DEL ARCHIVO Y DE LA BD
    public Boolean uploadImage(Uri uri) {
        if (uri == null) {
            estado = false;
        } else {
            try {
                storageReference.putFile(uri);
                estado = true;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return estado;
    }
}
