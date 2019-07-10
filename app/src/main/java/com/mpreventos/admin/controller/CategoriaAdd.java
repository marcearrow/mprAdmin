package com.mpreventos.admin.controller;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mpreventos.admin.R;
import com.mpreventos.admin.helper.FirebaseHelper;
import com.mpreventos.admin.helper.StorageHelper;
import com.mpreventos.admin.model.Categoria;
import com.mpreventos.admin.utils.Funciones;
import com.mpreventos.admin.utils.Spinnerloaders;

public class CategoriaAdd extends AppCompatActivity {

    private static final String LOADING_IMAGE_URL = "https://firebasestorage.googleapis.com/v0/b/mprfirebase-7753b.appspot.com/o/logo-mpr-decoracion.png?alt=media&token=49548e12-c035-4995-be4d-f38be90bbc06";
    private static final String CATEGORIAS_CHILD = "categorias";
    private static final int REQUEST_IMAGE = 2;
    private static final String TAG = "spinner";
    private DatabaseReference mDataBase;
    private ImageView imagenCategoria;
    private EditText nombreCategoria;
    private EditText descripcionCategoria;
    private FirebaseHelper firebaseHelper;
    private Categoria categoria;
    private String imgUrl;
    private String id;
    private Button modButton;
    private Boolean estado;
    private Uri uri;
    private String nombre;
    Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria_add);

        mDataBase = FirebaseDatabase.getInstance().getReference(CATEGORIAS_CHILD);

        nombreCategoria = findViewById(R.id.editNombreCategoria);
        descripcionCategoria = findViewById(R.id.editDescripcionCategoria);
        imagenCategoria = findViewById(R.id.imgCategoria);
        modButton = findViewById(R.id.btAddCategoria);
        final Spinner spinnerEvento = findViewById(R.id.spinnerCategoriaEvento);
        final Spinner spinnerTematica = findViewById(R.id.spinnerCategoriaTematica);

        if (getIntent() != null && getIntent().getExtras() != null) {

            id = getIntent().getStringExtra("id");
            setTitle("modificar categoria");

            modButton.setText(R.string.modificar);
            mDataBase.child(id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    nombreCategoria.setText(dataSnapshot.child("nombre").getValue().toString());
                    descripcionCategoria.setText(dataSnapshot.child("categoria").getValue().toString());
                    imgUrl = dataSnapshot.child("imgUrl").getValue().toString();
                    Funciones funciones = new Funciones();
                    funciones.setImg(imgUrl, imagenCategoria, getApplicationContext());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            setTitle("Agregar categoria");
        }
        final Spinnerloaders spinnerloaders = new Spinnerloaders(getBaseContext());
        spinnerloaders.adapterEventos(new Spinnerloaders.SpinnerAdaperCallbackEventos() {
            @Override
            public void callbackEventos(ArrayAdapter<String> adapter) {
                spinnerEvento.setAdapter(adapter);
                spinnerEvento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String item = parent.getItemAtPosition(position).toString();
                        getItemName(item);

                        spinnerloaders.adapterTematicas(new Spinnerloaders.SpinnerAdapterCallbackTematicas() {
                            @Override
                            public void callbackTematicas(ArrayAdapter<String> adapter2) {

                                spinnerTematica.setAdapter(adapter2);
                                Log.d(TAG, "final del callback en categoria add ");
                            }
                        }, nombre);

                        spinnerTematica.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                String item = parent.getItemAtPosition(position).toString();
                                getItemName(item);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

        });

    }

    private void getItemName(String item) {
        nombre = item;
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
                if (modButton.getText().toString().toLowerCase().equals("modificar")) {
                    categoria = new Categoria(id, nombreCategoria.getText().toString(), descripcionCategoria.getText().toString(), imgUrl);

                } else {
                    id = firebaseHelper.getIdkey();
                    categoria = new Categoria(id, nombreCategoria.getText().toString(), descripcionCategoria.getText().toString(), LOADING_IMAGE_URL);
                }

                estado = firebaseHelper.guardarDatosFirebase(categoria, id);


                if (estado && uri != null) {
                    final StorageReference storageReference = FirebaseStorage.getInstance().getReference(CATEGORIAS_CHILD).child(categoria.getId());
                    StorageHelper storageHelper = new StorageHelper(storageReference);
                    UploadTask task = storageHelper.uploadImage(uri);

                    Task<Uri> urlTask = task.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
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
                                categoria = new Categoria(id, nombreCategoria.getText().toString(), descripcionCategoria.getText().toString(), downloadUri.toString());
                                firebaseHelper.guardarDatosFirebase(categoria, categoria.getId());

                            }
                            //TODO POR ERROR
                        }
                    });
                    break;
                }
                firebaseHelper.TematicaCategora(categoria, nombre);
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

    private void ShowDialog() {
        if (dialog != null) {
            dialog.show();
        }
    }

    private void DismmisDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    private void CreateDialog() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog);
        dialog.setCancelable(false);
    }

}
