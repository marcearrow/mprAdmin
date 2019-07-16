package com.mpreventos.admin.helper;

import android.net.Uri;
import android.util.Log;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class StorageHelper {
    private static final String TAG = "StorageHelper";

  private StorageReference storageReference;
    private String url;

  private UploadTask uploadTask;


  public StorageHelper(StorageReference storageReference) {
        this.storageReference = storageReference;
    }


    public UploadTask uploadImage(Uri uri) {
        if (uri == null) {
            url = null;
        } else {
            try {
                uploadTask = storageReference.putFile(uri);


            } catch (Exception ex) {
                Log.d(TAG, ex.getMessage());
            }
        }
        return uploadTask;
    }
}
