package com.mpreventos.admin.controller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mpreventos.admin.R;
import com.mpreventos.admin.helper.FirebaseHelper;
import com.mpreventos.admin.helper.StorageHelper;
import com.mpreventos.admin.model.Evento;
import com.mpreventos.admin.model.Tematica;

public class TematicaAdd extends AppCompatActivity {
    private static final String LOADING_IMAGE_URL = "https://firebasestorage.googleapis.com/v0/b/mprfirebase-7753b.appspot.com/o/logo-mpr-decoracion.png?alt=media&token=49548e12-c035-4995-be4d-f38be90bbc06";
    private static final String TEMATICAS_CHILD = "tematicas";
    private static final int REQUEST_IMAGE = 2;
    private Uri uri;
    DatabaseReference mDataBase;
    ImageView imagenTematica;
    EditText nombreTematica;
    FirebaseHelper firebaseHelper;
    Tematica tematica;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tematica_add);

        mDataBase = FirebaseDatabase.getInstance().getReference(TEMATICAS_CHILD);
        nombreTematica = findViewById(R.id.editNombreTematica);
        imagenTematica = findViewById(R.id.imgTematica);

    }

    public void onClickAddTematica(View view) {
        switch (view.getId()) {
            case R.id.btCancelarTematica:
                finish();
                break;
            case R.id.btSelectImagenTematica:
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_IMAGE);
                break;
            case R.id.btAddTematica:
                final FirebaseHelper firebaseHelper = new FirebaseHelper(mDataBase);
                final String id = firebaseHelper.getIdkey();
                Evento tempTematica = new Evento(id, nombreTematica.getText().toString(), LOADING_IMAGE_URL);
                Boolean estado = firebaseHelper.guardarDatosFirebase(tempTematica, id);

                if (estado && uri != null) {
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference(TEMATICAS_CHILD).child(id);
                    StorageHelper storageHelper = new StorageHelper(storageReference);
                    //Uri uris = storageHelper.uploadImage(uri);

                    break;
                }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    uri = data.getData();
                    imagenTematica.setImageURI(uri);
                    Toast.makeText(this, "La imagen se cargo correctamente", Toast.LENGTH_LONG).show();
                }
            }

        } else Toast.makeText(this, "La imagen no se pudo cargar", Toast.LENGTH_LONG).show();
    }
}

