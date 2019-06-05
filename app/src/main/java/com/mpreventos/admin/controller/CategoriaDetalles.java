package com.mpreventos.admin.controller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mpreventos.admin.R;
import com.mpreventos.admin.model.Categoria;
import com.mpreventos.admin.utils.ImageLoader;

public class CategoriaDetalles extends AppCompatActivity {

    private static final int REQUEST_IMAGE = 2;
    private final String CATEGORIA_CHILD = "categorias";
    private String id;
    private EditText tvNombre;
    private EditText tvDescripcion;
    private ImageView imagenCategoriaDetalles;
    private DatabaseReference mDatabase;
    private String imgUrl;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria_detalles);
        setTitle("Modificar Categoria");

        id = getIntent().getStringExtra("id");
        mDatabase = FirebaseDatabase.getInstance().getReference(CATEGORIA_CHILD).child(id);
        tvNombre = findViewById(R.id.editNombreCategoriaDetales);
        tvDescripcion = findViewById(R.id.editDescripcionCategoriaDetalles);
        imagenCategoriaDetalles = findViewById(R.id.imgCategoriaDetalles);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tvNombre.setText(dataSnapshot.child("nombre").getValue().toString());
                tvDescripcion.setText(dataSnapshot.child("descripcion").getValue().toString());
                imgUrl = dataSnapshot.child("imgUrl").getValue().toString();
                setImg(imgUrl);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


    public void onClickCategoriaDetalles(View view) {
        switch (view.getId()) {
            case R.id.btCancelarCategoriaDetalles:
                finish();
                break;
            case R.id.btSelectImagenCategoriaDetalles:

                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_IMAGE);

                break;
            case R.id.btModCategoriaDetalles:
                modificarDatos();
                break;

        }

    }

    private void setImg(String imgUrl) {
        if (imgUrl != null) {
            try {
                ImageLoader imageLoader = new ImageLoader(getApplicationContext());
                imageLoader.setImgWithGlide(imgUrl, imagenCategoriaDetalles);
            } catch (Exception ex) {
                Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show();

            }
        } else {
            Toast.makeText(this, "no se pudo cargar la imagen", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    uri = data.getData();
                    imagenCategoriaDetalles.setImageURI(uri);
                    Toast.makeText(this, "La imagen se cargo correctamente", Toast.LENGTH_LONG).show();
                }
            }

        } else Toast.makeText(this, "La imagen no se pudo cargar", Toast.LENGTH_LONG).show();
    }

    private void modificarDatos() {
        //falta modificar storage
        Categoria categoria = new Categoria(id, tvNombre.getText().toString(), tvDescripcion.getText().toString(), imgUrl);
        mDatabase.setValue(categoria);
    }
}
