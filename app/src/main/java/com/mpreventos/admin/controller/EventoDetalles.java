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
import com.mpreventos.admin.model.Evento;
import com.mpreventos.admin.utils.ImageLoader;

public class EventoDetalles extends AppCompatActivity {

    private static final String EVENTOS_CHILD = "eventos";
    private static final int REQUEST_IMAGE = 2;
    private Uri uri;
    private String id;
    private EditText nombreEvento;
    private ImageView imagenEvento;
    private DatabaseReference mDatabase;
    private String imgUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento_detalles);
        setTitle("Modificar Evento");

        id = getIntent().getStringExtra("id");
        nombreEvento = findViewById(R.id.editNombreEventoDetalles);
        imagenEvento = findViewById(R.id.imgEventoDetalles);

        mDatabase = FirebaseDatabase.getInstance().getReference(EVENTOS_CHILD).child(id);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nombreEvento.setText(dataSnapshot.child("nombre").getValue().toString());
                imgUrl = dataSnapshot.child("imgUrl").getValue().toString();
                setImg(imgUrl);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }


    public void onClicEventoDetalles(View view) {
        switch (view.getId()) {
            case R.id.btCancelarEventoDetalles:
                finish();
                break;
            case R.id.btSelectImagenDetalles:

                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_IMAGE);

                break;
            case R.id.btEventoDetalles:
                modificarDatos();
                break;

        }
    }

    private void modificarDatos() {
        //falta modificar storage
        Evento evento = new Evento(id, nombreEvento.getText().toString(), imgUrl);
        mDatabase.setValue(evento);
    }

    private void setImg(String imgUrl) {
        if (imgUrl != null) {
            try {
                ImageLoader imageLoader = new ImageLoader(getApplicationContext());
                imageLoader.setImgWithGlide(imgUrl, imagenEvento);
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
                    imagenEvento.setImageURI(uri);
                    Toast.makeText(this, "La imagen se cargo correctamente", Toast.LENGTH_LONG).show();
                }
            }

        } else Toast.makeText(this, "La imagen no se pudo cargar", Toast.LENGTH_LONG).show();
    }
}
