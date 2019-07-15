package com.mpreventos.admin.controller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.mpreventos.admin.model.Evento;
import com.mpreventos.admin.utils.DialogLoader;
import com.mpreventos.admin.utils.Funciones;

public class EventoAdd extends AppCompatActivity {

  private static final String LOADING_IMAGE_URL = "https://firebasestorage.googleapis.com/v0/b/mprfirebase-7753b.appspot.com/o/logo-mpr-decoracion.png?alt=media&token=49548e12-c035-4995-be4d-f38be90bbc06";
  private static final String EVENTOS_CHILD = "eventos";
  private static final int REQUEST_IMAGE = 2;
  private DatabaseReference mDataBase;
  private ImageView imagenEvento;
  private EditText nombreEvento;
  private FirebaseHelper firebaseHelper;
  private Evento tempEvento;
  private String imgUrl;
  private String id;
  private Button modButton;
  private Boolean estado;
  private Uri uri;
  DialogLoader dialogLoader;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_evento_add);

    dialogLoader = new DialogLoader(this);
    mDataBase = FirebaseDatabase.getInstance().getReference(EVENTOS_CHILD);
    mDataBase.keepSynced(true);

    nombreEvento = findViewById(R.id.editNombreEvento);
    imagenEvento = findViewById(R.id.imgEvento);
    modButton = findViewById(R.id.btAddEvento);

    if (getIntent() != null && getIntent().getExtras() != null) {

      id = getIntent().getStringExtra("id");
      setTitle("modificar evento");

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
            tempEvento = new Evento(id, nombreEvento.getText().toString(), LOADING_IMAGE_URL);
          }

          estado = firebaseHelper.guardarDatosFirebase(tempEvento, id);

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
                .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                  @Override
                  public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task)
                      throws Exception {
                    if (!task.isSuccessful()) {
                      ErrorMensaje();
                      throw task.getException();

                    }// Continue with the task to get the download URL
                    return storageReference.getDownloadUrl();
                  }

                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                  @Override
                  public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {

                      Uri downloadUri = task.getResult();
                      tempEvento = new Evento(id, nombreEvento.getText().toString(),
                          downloadUri.toString());
                      firebaseHelper.guardarDatosFirebase(tempEvento, tempEvento.getId());
                      SuccesMensaje();
                      finish();
                    } else {
                      ErrorMensaje();
                    }
                  }
                });
          }
        }
    }
  }

  private void SuccesMensaje() {
    Toast.makeText(this, "Evento agregado exitosamente", Toast.LENGTH_SHORT).show();
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

}
