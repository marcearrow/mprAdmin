package com.mpreventos.admin.helper;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class StorageHelper {
    private static final String TAG = "StorageHelper";
    private Uri imgurl;
    private StorageReference storageReference;
    private String url;
    Uri downloadUri;
    private UploadTask uploadTask;
    Task<Uri> sjk;

    public StorageHelper(StorageReference storageReference) {
        this.storageReference = storageReference;
    }


    public Uri uploadImage(Uri uri) {
        if (uri == null) {
            url = null;
        } else {
            try {
                uploadTask = storageReference.putFile(uri);

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }// Continue with the task to get the download URL

                        return storageReference.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();

                        }
                        //TODO POR ERROR
                    }
                });
            } catch (Exception ex) {
                Log.d(TAG, ex.getMessage());
            }
        }
        return imgurl;
    }
}
