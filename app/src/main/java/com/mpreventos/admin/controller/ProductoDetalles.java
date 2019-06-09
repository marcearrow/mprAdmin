package com.mpreventos.admin.controller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mpreventos.admin.R;
import com.mpreventos.admin.helper.FirebaseHelper;
import com.mpreventos.admin.model.Producto;
import com.mpreventos.admin.utils.Funciones;

public class ProductoDetalles extends AppCompatActivity {

    private static final String PRODUCTOS_CHILD = "productos";
    private static final int REQUEST_IMAGE = 2;
    private static final String LOADING_IMAGE_URL = "https://firebasestorage.googleapis.com/v0/b/mprfirebase-7753b.appspot.com/o/logo-mpr-decoracion.png?alt=media&token=49548e12-c035-4995-be4d-f38be90bbc06";
    DatabaseReference mDataBase;
    ImageView imagenProducto;
    EditText nombreProducto;
    EditText descripcionProducto;
    FirebaseHelper firebaseHelper;
    Producto temProducto;
    String id;
    String imgUrl;
    Uri uri;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto_detalles);

        id = getIntent().getStringExtra("id");
        mDataBase = FirebaseDatabase.getInstance().getReference(PRODUCTOS_CHILD).child(id);
        nombreProducto = findViewById(R.id.editNombreProductoDetalles);
        descripcionProducto = findViewById(R.id.editDescripcionProductoDetalles);
        imagenProducto = findViewById(R.id.imageViewProductoDetalles);
        spinner = findViewById(R.id.spinnerEventoProducto);

        mDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nombreProducto.setText(dataSnapshot.child("nombre").getValue().toString());
                descripcionProducto.setText(dataSnapshot.child("descripcion").getValue().toString());
                imgUrl = dataSnapshot.child("imgUrl").getValue().toString();
                Funciones funciones = new Funciones();
                funciones.setImg(imgUrl, imagenProducto, getApplicationContext());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void onClickProductosDetalles(View view) {
        switch (view.getId()) {
            case R.id.btCancelarProductoDetalles:
                finish();
                break;
            case R.id.btSelectImagenProductosDetalles:
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_IMAGE);
                break;
            case R.id.btModProductoDetalles:
                temProducto = new Producto(id, nombreProducto.getText().toString(), descripcionProducto.getText().toString(), imgUrl);
                firebaseHelper = new FirebaseHelper(mDataBase);
                firebaseHelper.guardarDatosFirebase(temProducto, null);
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
