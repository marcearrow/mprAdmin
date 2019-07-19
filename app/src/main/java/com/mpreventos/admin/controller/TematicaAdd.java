package com.mpreventos.admin.controller;

import static com.mpreventos.admin.utils.Constantes.EVENTOS_TEMATICAS;
import static com.mpreventos.admin.utils.Constantes.LOADING_IMG;
import static com.mpreventos.admin.utils.Constantes.REQUEST_IMAGE;
import static com.mpreventos.admin.utils.Constantes.TEMATICAS_CHILD;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
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
import com.mpreventos.admin.model.Evento;
import com.mpreventos.admin.model.Tematica;
import com.mpreventos.admin.utils.DialogAlertSpinner;
import com.mpreventos.admin.utils.DialogLoader;
import com.mpreventos.admin.utils.Funciones;
import com.mpreventos.admin.utils.Spinnerloaders;
import java.util.ArrayList;

public class TematicaAdd extends AppCompatActivity {


  String spinnerElement;
  ArrayList<Evento> listaEventosSpinner;
  private Uri uri;
  private DatabaseReference mDataBase;
  private ImageView imagenTematica;
  private EditText nombreTematica;
  private FirebaseHelper firebaseHelper;
  private Tematica tematica;
  private String imgUrl;
  private String id;
  private Button modButton;
  private Spinner spinner;
  private String nombre;
  private DialogLoader dialogLoader = null;
  private String relacionAntigua;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_tematica_add);

    ActionBar actionBar = getSupportActionBar();

    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
      actionBar.setDisplayShowHomeEnabled(true);
    }

    mDataBase = FirebaseDatabase.getInstance().getReference(TEMATICAS_CHILD);
    mDataBase.keepSynced(true);
    nombreTematica = findViewById(R.id.editNombreTematica);
    imagenTematica = findViewById(R.id.imgTematica);
    modButton = findViewById(R.id.btAddTematica);
    spinner = findViewById(R.id.spinnerTematicaEvento);

    final Spinnerloaders spinnerloaders = new Spinnerloaders(getBaseContext());
    spinnerloaders.adapterEventos((adapter, listaEventos) -> {
      listaEventosSpinner = listaEventos;
      spinner.setAdapter(adapter);
      spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
          String item = parent.getItemAtPosition(position).toString();
          if (!item.equals("Seleccione una opción...") && !item.equals("")) {
            if (listaEventos.get(position - 1).getNombre().equals(item)) {
              getItemName(listaEventos.get(position - 1).getId());
            } else {
              ErrorMensaje();
            }
          }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }

      });
      if (getIntent() != null && getIntent().getExtras() != null) {

        id = getIntent().getStringExtra("id");
        setTitle("Modificar temática");

        modButton.setText(R.string.modificar);
        mDataBase.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
              nombreTematica.setText(dataSnapshot.child("nombre").getValue().toString());
              imgUrl = dataSnapshot.child("imgUrl").getValue().toString();
              Funciones funciones = new Funciones();
              funciones.setImg(imgUrl, imagenTematica, getApplicationContext());
              spinnerElement = dataSnapshot.child("evento").getValue().toString();
              relacionAntigua = spinnerElement;

              int i = 0;
              for (Evento evento :
                  listaEventosSpinner) {
                i++;
                if (evento.getId().equals(spinnerElement)) {
                  spinner.setSelection(i);
                }
              }
            }


          }

          @Override
          public void onCancelled(@NonNull DatabaseError databaseError) {

          }
        });


      } else {
        setTitle("Agregar temática");
      }

    });

  }

  private void getItemName(String item) {
    nombre = item;
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

        if (Funciones.validarTexto(nombreTematica.getText().toString()) && Funciones
            .validarTexto(nombre)) {
          nombreTematica.setError("El nombre no puede estar vacío");
          DialogAlertSpinner dialogAlertSpinner = new DialogAlertSpinner(TematicaAdd.this);
          dialogAlertSpinner.DialogCreator();
          break;
        } else if (Funciones.validarTexto(nombreTematica.getText().toString())) {
          nombreTematica.setError("El nombre no puede estar vacío");
          break;
        } else if (Funciones.validarTexto(nombre) || nombre.equals("Seleccione una opción...")) {
          DialogAlertSpinner dialogAlertSpinner = new DialogAlertSpinner(TematicaAdd.this);
          dialogAlertSpinner.DialogCreator();
          break;
        } else {

          dialogLoader = new DialogLoader(this);
          dialogLoader.CreateDialog();
          dialogLoader.ShowDialog();

          firebaseHelper = new FirebaseHelper(mDataBase);
          if (modButton.getText().toString().toLowerCase().equals("modificar")) {
            tematica = new Tematica(id, nombreTematica.getText().toString(), imgUrl, nombre);

          } else {
            id = firebaseHelper.getIdkey();
            tematica = new Tematica(id, nombreTematica.getText().toString(), LOADING_IMG,
                nombre);
          }

          Boolean estado = firebaseHelper.guardarDatosFirebase(tematica, id);

          if (!estado) {
            ErrorMensaje();
          } else if (uri == null && modButton.getText().toString().toLowerCase()
              .equals("modificar")) {
            firebaseHelper.AddRelacion(tematica.getId(), nombre, EVENTOS_TEMATICAS);
            firebaseHelper.EliminarRelacion(tematica.getId(), relacionAntigua, EVENTOS_TEMATICAS);
            SuccesMensaje();
            finish();
          } else if (modButton.getText().toString().toLowerCase().equals("agregar")) {
            firebaseHelper.AddRelacion(tematica.getId(), nombre, "eventoTematicas");
            SuccesMensaje();
            finish();
          } else {
            final StorageReference storageReference = FirebaseStorage.getInstance()
                .getReference(TEMATICAS_CHILD).child(tematica.getId());
            StorageHelper storageHelper = new StorageHelper(storageReference);
            UploadTask task = storageHelper.uploadImage(uri);

            Task<Uri> urlTask = task
                .continueWithTask(task1 -> {
                  if (!task1.isSuccessful()) {
                    ErrorMensaje();
                    throw task1.getException();
                  }// Continue with the task to get the download URL
                  return storageReference.getDownloadUrl();
                }).addOnCompleteListener(task12 -> {
                  if (task12.isSuccessful()) {

                    Uri downloadUri = task12.getResult();
                    tematica = new Tematica(id, nombreTematica.getText().toString(),
                        downloadUri.toString(), nombre);
                    firebaseHelper.guardarDatosFirebase(tematica, tematica.getId());
                    firebaseHelper.AddRelacion(tematica.getId(), nombre, "eventoTematicas");
                    SuccesMensaje();
                    finish();
                  } else {
                    ErrorMensaje();
                  }
                });
          }
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
          Toast.makeText(this, "La imagen se cargo correctamente", Toast.LENGTH_LONG)
              .show();
        }
      }

    } else {
      Toast.makeText(this, "La imagen no se pudo cargar", Toast.LENGTH_LONG).show();
    }
  }

  private void SuccesMensaje() {
    if (modButton.getText().toString().toLowerCase().equals("modificar")) {
      Toast.makeText(this, "Temática modificada exitosamente", Toast.LENGTH_SHORT).show();
    } else {
      Toast.makeText(this, "Temática agregada exitosamente", Toast.LENGTH_SHORT).show();
    }
    dialogLoader.DismisDialog();
  }

  private void ErrorMensaje() {
    Toast.makeText(this, "Ocurrio un error", Toast.LENGTH_SHORT).show();
    dialogLoader.DismisDialog();
  }


  @Override
  public void finish() {
    if (dialogLoader != null) {
      dialogLoader.DismisDialog();
    }
    super.finish();
  }

  @Override
  public boolean onSupportNavigateUp() {
    onBackPressed();
    return true;
  }

}

