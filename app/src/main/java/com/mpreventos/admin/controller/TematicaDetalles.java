package com.mpreventos.admin.controller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
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
import com.mpreventos.admin.model.Tematica;
import com.mpreventos.admin.utils.Funciones;

import java.util.ArrayList;

public class TematicaDetalles extends AppCompatActivity {

    private static final String TEMATICAS_CHILD = "tematicas";
    private static final int REQUEST_IMAGE = 2;
    private static final String LOG = "logSpinner";
    private Uri uri;
    DatabaseReference mDataBase;
    ImageView imagenTematica;
    EditText nombreTematica;
    FirebaseHelper firebaseHelper;
    String id;
    String imgUrl;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tematica_detalles);

        id = getIntent().getStringExtra("id");
        mDataBase = FirebaseDatabase.getInstance().getReference(TEMATICAS_CHILD).child(id);
        nombreTematica = findViewById(R.id.editNombreTematicaDetalles);
        imagenTematica = findViewById(R.id.imgTematicaDetalles);
        spinner = findViewById(R.id.spinnerTematicaDetalles);
        mDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nombreTematica.setText(dataSnapshot.child("nombre").getValue().toString());
                imgUrl = dataSnapshot.child("imgUrl").getValue().toString();
                Funciones funciones = new Funciones();
                funciones.setImg(imgUrl, imagenTematica, getApplicationContext());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        spinner.setAdapter(new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, eventos()));
    }

    public void onClickTematicaDetalles(View view) {
        switch (view.getId()) {
            case R.id.btCancelarTematicaDetalles:
                finish();
                break;
            case R.id.btSelectImagenTematicaDetalles:

                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_IMAGE);

                break;
            case R.id.btModTematicaDetalles:
                Tematica tematica = new Tematica(id, nombreTematica.getText().toString(), imgUrl);
                firebaseHelper = new FirebaseHelper(mDataBase);
                firebaseHelper.guardarDatosFirebase(tematica, null);

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
                    imagenTematica.setImageURI(uri);
                    Toast.makeText(this, "La imagen se cargo correctamente", Toast.LENGTH_LONG).show();
                }
            }

        } else Toast.makeText(this, "La imagen no se pudo cargar", Toast.LENGTH_LONG).show();
    }

    private ArrayList<String> eventos() {
        final ArrayList<String> eventos = new ArrayList<>();
        mDataBase = FirebaseDatabase.getInstance().getReference();
        mDataBase.child("eventos").getRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                eventos.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.child("id").getValue() != null) {
                        String nombre = ds.child("nombre").getValue().toString();
                        eventos.add(nombre);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });
        return eventos;
    }


}
