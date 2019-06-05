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
import com.mpreventos.admin.model.Categoria;

public class CategoriaAdd extends AppCompatActivity {

    private static final String CATEGORIAS_CHILD = "categorias";
    private static final int REQUEST_IMAGE = 2;
    private static final String LOADING_IMAGE_URL = "https://firebasestorage.googleapis.com/v0/b/mprfirebase-7753b.appspot.com/o/logo-mpr-decoracion.png?alt=media&token=49548e12-c035-4995-be4d-f38be90bbc06";
    DatabaseReference mDataBase;
    ImageView imagenCategoria;
    EditText nombreCategoria;
    EditText descripcionCategoria;
    FirebaseHelper firebaseHelper;
    Categoria tempCategoria;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria_add);

        setTitle(CATEGORIAS_CHILD);
        mDataBase = FirebaseDatabase.getInstance().getReference(CATEGORIAS_CHILD);

        nombreCategoria = findViewById(R.id.editNombreCategoria);
        descripcionCategoria = findViewById(R.id.editDescripcionCategoria);
        imagenCategoria = findViewById(R.id.imgCategoria);
    }

    public void onClickAddCategoria(View view) {
        switch (view.getId()) {
            case R.id.btCancelarCategoria:
                finish();
                break;
            case R.id.btSelectImagenCategoria:

                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_IMAGE);

                break;
            case R.id.btAddCategoria:

                firebaseHelper = new FirebaseHelper(mDataBase);
                final String id = firebaseHelper.getIdkey();
                tempCategoria = new Categoria(id, nombreCategoria.getText().toString(), descripcionCategoria.getText().toString(), LOADING_IMAGE_URL);
                Boolean estado = firebaseHelper.guardarDatosFirebase(tempCategoria, id);

                if (estado && uri != null) {
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference(CATEGORIAS_CHILD).child(id);
                    StorageHelper storageHelper = new StorageHelper(storageReference);
                    //storageHelper.uploadImage(uri);
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
                    imagenCategoria.setImageURI(uri);
                    Toast.makeText(this, "La imagen se cargo correctamente", Toast.LENGTH_LONG).show();
                }
            }

        } else Toast.makeText(this, "La imagen no se pudo cargar", Toast.LENGTH_LONG).show();
    }

}
