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
import com.mpreventos.admin.model.Producto;

public class ProductoAdd extends AppCompatActivity {

    private static final String PRODUCTOS_CHILD = "productos";
    private static final int REQUEST_IMAGE = 2;
    private static final String LOADING_IMAGE_URL = "https://firebasestorage.googleapis.com/v0/b/mprfirebase-7753b.appspot.com/o/logo-mpr-decoracion.png?alt=media&token=49548e12-c035-4995-be4d-f38be90bbc06";
    DatabaseReference mDataBase;
    ImageView imagenProducto;
    EditText nombreProducto;
    EditText descripcionProducto;
    FirebaseHelper firebaseHelper;
    Producto temProducto;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto_add);

        mDataBase = FirebaseDatabase.getInstance().getReference(PRODUCTOS_CHILD);
        nombreProducto = findViewById(R.id.editNombreProducto);
        descripcionProducto = findViewById(R.id.editDescripcionProducto);
        imagenProducto = findViewById(R.id.imageViewProducto);


    }

    public void onClickAddProductos(View view) {
        switch (view.getId()) {
            case R.id.btCancelarProducto:
                finish();
                break;
            case R.id.btSelectImagenProductos:

                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_IMAGE);
                break;

            case R.id.btAddProducto:
                firebaseHelper = new FirebaseHelper(mDataBase);
                final String id = firebaseHelper.getIdkey();
                temProducto = new Producto(id, nombreProducto.getText().toString(), descripcionProducto.getText().toString(), LOADING_IMAGE_URL);
                Boolean estado = firebaseHelper.guardarDatosFirebase(temProducto, id);


                if (estado && uri != null) {
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference(PRODUCTOS_CHILD).child(id);
                    StorageHelper storageHelper = new StorageHelper(storageReference);
                    //storageHelper.uploadImage(uri);

                }
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    uri = data.getData();
                    imagenProducto.setImageURI(uri);
                    Toast.makeText(this, "La imagen se cargo correctamente", Toast.LENGTH_LONG).show();
                }
            }

        } else Toast.makeText(this, "La imagen no se pudo cargar", Toast.LENGTH_LONG).show();
    }
}
