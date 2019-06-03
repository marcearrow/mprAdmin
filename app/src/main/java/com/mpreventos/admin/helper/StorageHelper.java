package com.mpreventos.admin.helper;

import android.net.Uri;

import com.google.firebase.storage.StorageReference;

public class StorageHelper {

    private StorageReference storageReference;
    private Boolean estado;

    public StorageHelper(StorageReference storageReference) {
        this.storageReference = storageReference;
    }

    public Boolean uploadImage(Uri uri, String nodo) {
        if (uri == null) {
            estado = false;
        } else {
            try {
                storageReference.child(nodo).putFile(uri);
                estado = true;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return estado;
    }
}
