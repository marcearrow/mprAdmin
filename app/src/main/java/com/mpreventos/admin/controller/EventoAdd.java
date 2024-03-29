package com.mpreventos.admin.controller;

import static com.mpreventos.admin.utils.Constantes.EVENTOS_CHILD;
import static com.mpreventos.admin.utils.Constantes.LOADING_IMG;
import static com.mpreventos.admin.utils.Constantes.REQUEST_IMAGE;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.mpreventos.admin.utils.DialogLoader;
import com.mpreventos.admin.utils.Funciones;

public class EventoAdd extends AppCompatActivity {


  private DatabaseReference mDataBase;
  private ImageView imagenEvento;
  private EditText nombreEvento;
  private FirebaseHelper firebaseHelper;
  private Evento tempEvento;
  private String imgUrl;
  private String id;
  private Button modButton;
  private Uri uri;
  private DialogLoader dialogLoader;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_evento_add);

    ActionBar actionBar = getSupportActionBar();

    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
      actionBar.setDisplayShowHomeEnabled(true);
    }

    dialogLoader = new DialogLoader(this);
    mDataBase = FirebaseDatabase.getInstance().getReference(EVENTOS_CHILD);
    mDataBase.keepSynced(true);

    nombreEvento = findViewById(R.id.editNombreEvento);
    imagenEvento = findViewById(R.id.imgEvento);
    modButton = findViewById(R.id.btAddEvento);

    if (getIntent() != null && getIntent().getExtras() != null) {

      id = getIntent().getStringExtra("id");
      setTitle("Modificar evento");

      modButton.setText(R.string.modificar);
      mDataBase.child(id).addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
          nombreEvento.setText(dataSnapshot.child("nombre").getValue().toString());
          imgUrl = dataSnapshot.child("imgUrl").getValue().toString();
          Funciones funciones = new Funciones();
          funciones.setImg(imgUrl, imagenEvento, getApplicationContext());
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
      });
    } else {
      setTitle("Agregar evento");
    }

  }

  public void onClickAddEvento(View view) {
    switch (view.getId()) {
      case R.id.btCancelarEvento:
        finish();
        break;
      case R.id.btSelectImagen:

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE);

        break;
      case R.id.btAddEvento:

        if (Funciones.validarTexto(nombreEvento.getText().toString())) {
          nombreEvento.setError("El nombre no puede estar vacío");
          break;
        } else {

          dialogLoader = new DialogLoader(this);
          dialogLoader.CreateDialog();
          dialogLoader.ShowDialog();

          firebaseHelper = new FirebaseHelper(mDataBase);

          if (modButton.getText().toString().toLowerCase().equals("modificar")) {
            tempEvento = new Evento(id, nombreEvento.getText().toString(), imgUrl);

          } else {
            id = firebaseHelper.getIdkey();
            tempEvento = new Evento(id, nombreEvento.getText().toString(), LOADING_IMG);
          }

          Boolean estado = firebaseHelper.guardarDatosFirebase(tempEvento, id);

          if (!estado) {
            ErrorMensaje();
          } else if (uri == null) {
            SuccesMensaje();
            finish();
          } else {
            final StorageReference storageReference = FirebaseStorage.getInstance()
                .getReference(EVENTOS_CHILD).child(tempEvento.getId());
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
                    if (downloadUri != null) {
                      tempEvento = new Evento(id, nombreEvento.getText().toString(),
                          downloadUri.toString());
                    }
                    firebaseHelper.guardarDatosFirebase(tempEvento, tempEvento.getId());
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

  private void SuccesMensaje() {
    if (modButton.getText().toString().toLowerCase().equals("modificar")) {
      Toast.makeText(this, "Evento modificado exitosamente", Toast.LENGTH_SHORT).show();
    } else {
      Toast.makeText(this, "Evento agregado exitosamente", Toast.LENGTH_SHORT).show();
    }

    dialogLoader.DismisDialog();
  }

  private void ErrorMensaje() {
    Toast.makeText(this, "Ocurrio un error", Toast.LENGTH_SHORT).show();
    dialogLoader.DismisDialog();
  }


  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQUEST_IMAGE) {
      if (resultCode == RESULT_OK) {
        if (data != null) {
          uri = data.getData();
          imagenEvento.setImageURI(uri);
          Toast.makeText(this, "La imagen se cargo correctamente", Toast.LENGTH_LONG)
              .show();
        }
      }

    } else {
      Toast.makeText(this, "La imagen no se pudo cargar", Toast.LENGTH_LONG).show();
    }
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
