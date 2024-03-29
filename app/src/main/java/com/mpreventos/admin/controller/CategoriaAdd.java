package com.mpreventos.admin.controller;

import static com.mpreventos.admin.utils.Constantes.CATEGORIAS_CHILD;
import static com.mpreventos.admin.utils.Constantes.LOADING_IMG;
import static com.mpreventos.admin.utils.Constantes.REQUEST_IMAGE;
import static com.mpreventos.admin.utils.Constantes.TEMATICAS_CATEGORIAS;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.mpreventos.admin.utils.DialogAlertSpinner;
import com.mpreventos.admin.utils.DialogLoader;
import com.mpreventos.admin.utils.Funciones;
import com.mpreventos.admin.utils.Spinnerloaders;
import java.util.ArrayList;

public class CategoriaAdd extends AppCompatActivity {

  private String relacionAntigua;
  String spinnerElement;
  private DatabaseReference mDataBase;
  private ImageView imagenCategoria;
  private EditText nombreCategoria;
  private FirebaseHelper firebaseHelper;
  private Categoria categoria;
  private String imgUrl;
  private String id;
  private Button modButton;
  private Uri uri;
  private String nombre;
  private DialogLoader dialogLoader;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_categoria_add);

    mDataBase = FirebaseDatabase.getInstance().getReference(CATEGORIAS_CHILD);

    nombreCategoria = findViewById(R.id.editNombreCategoria);
    imagenCategoria = findViewById(R.id.imgCategoria);
    modButton = findViewById(R.id.btAddCategoria);
    final Spinner spinnerEvento = findViewById(R.id.spinnerCategoriaEvento);
    final Spinner spinnerTematica = findViewById(R.id.spinnerCategoriaTematica);

    if (getIntent() != null && getIntent().getExtras() != null) {

      id = getIntent().getStringExtra("id");
      setTitle("modificar categoría");

      modButton.setText(R.string.modificar);
      mDataBase.child(id).addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
          nombreCategoria.setText(dataSnapshot.child("nombre").getValue().toString());
          imgUrl = dataSnapshot.child("imgUrl").getValue().toString();
          Funciones funciones = new Funciones();
          funciones.setImg(imgUrl, imagenCategoria, getApplicationContext());
          spinnerElement = dataSnapshot.child("tematica").getValue().toString();
          relacionAntigua = spinnerElement;
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
      });
    } else {
      setTitle("Agregar categoría");
    }

    final Spinnerloaders spinnerloaders = new Spinnerloaders(getBaseContext());
    spinnerloaders.adapterEventos((adapter, listaEventos) -> {
      spinnerEvento.setAdapter(adapter);

      spinnerEvento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
          String item = parent.getItemAtPosition(position).toString();
          if (!item.equals("Seleccione una opción...")) {
            spinnerTematica.setAdapter(null);
            if (listaEventos.get(position - 1).getNombre().equals(item)) {
              getItemName(listaEventos.get(position - 1).getId());

              spinnerloaders
                  .adapterTematicas((adapter2, listaTematicas) -> {
                    spinnerTematica.setAdapter(adapter2);

                    spinnerTematica
                        .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                          @Override
                          public void onItemSelected(AdapterView<?> parent1, View view1,
                              int position1,
                              long id1) {
                            String item1 = parent1.getItemAtPosition(position1).toString();
                            if (!item1.equals("Seleccione una opción...")) {
                              if (listaTematicas.get(position1).getNombre().equals(item1)) {
                                getItemName(listaTematicas.get(position1).getId());
                              }
                            }
                          }

                          @Override
                          public void onNothingSelected(AdapterView<?> parent1) {

                          }
                        });
                  }, nombre);


            }
          } else {
            ArrayList<String> lista = new ArrayList<>();
            lista.add("Seleccione una opción...");
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_list_item_1, lista);
            spinnerTematica.setAdapter(adapter);
          }


        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
      });
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
        if (Funciones.validarTexto(nombreCategoria.getText().toString())) {
          nombreCategoria.setError("El nombre no debe estar vacío");
          break;
        } else if (Funciones.validarTexto(nombre) || nombre.equals("Seleccione una opción...")
            || nombre.equals("No se encontró ninguna temática")) {
          DialogAlertSpinner dialogAlertSpinner = new DialogAlertSpinner(CategoriaAdd.this);
          dialogAlertSpinner.DialogCreator();
          break;
        } else {

          dialogLoader = new DialogLoader(this);
          dialogLoader.CreateDialog();
          dialogLoader.ShowDialog();

          firebaseHelper = new FirebaseHelper(mDataBase);
          if (modButton.getText().toString().toLowerCase().equals("modificar")) {
            categoria = new Categoria(id, nombreCategoria.getText().toString(), imgUrl, nombre);

          } else {
            id = firebaseHelper.getIdkey();
            categoria = new Categoria(id, nombreCategoria.getText().toString(), LOADING_IMG,
                nombre);

            Boolean estado = firebaseHelper.guardarDatosFirebase(categoria, id);

            if (!estado) {
              ErrorMensaje();
            } else if (uri == null && modButton.getText().toString().toLowerCase()
                .equals("modificar")) {
              firebaseHelper.AddRelacion(categoria.getId(), nombre, TEMATICAS_CATEGORIAS);
              firebaseHelper
                  .EliminarRelacion(categoria.getId(), relacionAntigua, TEMATICAS_CATEGORIAS);
              SuccesMensaje();
              finish();
            } else if (modButton.getText().toString().toLowerCase().equals("agregar")) {
              firebaseHelper.AddRelacion(categoria.getId(), nombre, TEMATICAS_CATEGORIAS);
              SuccesMensaje();
              finish();
            } else {

              final StorageReference storageReference = FirebaseStorage.getInstance()
                  .getReference(CATEGORIAS_CHILD).child(categoria.getId());
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
                      categoria = new Categoria(id, nombreCategoria.getText().toString(),
                          downloadUri.toString(), nombre);
                      firebaseHelper.guardarDatosFirebase(categoria, categoria.getId());
                      firebaseHelper.AddRelacion(categoria.getId(), nombre, TEMATICAS_CATEGORIAS);
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
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQUEST_IMAGE) {
      if (resultCode == RESULT_OK) {
        if (data != null) {
          uri = data.getData();
          imagenCategoria.setImageURI(uri);
          Toast.makeText(this, "La imagen se cargo correctamente", Toast.LENGTH_LONG)
              .show();
        }
      }

    } else {
      Toast.makeText(this, "La imagen no se pudo cargar", Toast.LENGTH_LONG).show();
    }
  }

  private void SuccesMensaje() {
    Toast.makeText(this, "Categoria agregada exitosamente", Toast.LENGTH_SHORT).show();
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
}
