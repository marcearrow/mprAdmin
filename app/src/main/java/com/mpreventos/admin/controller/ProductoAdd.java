package com.mpreventos.admin.controller;

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
import com.mpreventos.admin.model.Producto;
import com.mpreventos.admin.model.Tematica;
import com.mpreventos.admin.utils.DialogAlertSpinner;
import com.mpreventos.admin.utils.DialogLoader;
import com.mpreventos.admin.utils.Funciones;
import com.mpreventos.admin.utils.Spinnerloaders;
import java.util.ArrayList;

public class ProductoAdd extends AppCompatActivity {

  private static final String LOADING_IMAGE_URL = "https://firebasestorage.googleapis.com/v0/b/mprfirebase-7753b.appspot.com/o/logo-mpr-decoracion.png?alt=media&token=49548e12-c035-4995-be4d-f38be90bbc06";
  private static final String PRODUCTOS_CHILD = "productos";
  private static final int REQUEST_IMAGE = 2;
  private DatabaseReference mDataBase;
  private ImageView imagenProducto;
  private EditText nombreProducto;
  private EditText descripcionProducto;
  private FirebaseHelper firebaseHelper;
  private Producto producto;
  private String imgUrl;
  private String id;
  private Button modButton;
  private Boolean estado;
  private DialogLoader dialogLoader;
  private Uri uri;
  private String nombre;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_producto_add);

    mDataBase = FirebaseDatabase.getInstance().getReference(PRODUCTOS_CHILD);
    nombreProducto = findViewById(R.id.editNombreProducto);
    descripcionProducto = findViewById(R.id.editDescripcionProducto);
    imagenProducto = findViewById(R.id.imageViewProducto);
    modButton = findViewById(R.id.btAddProducto);
    final Spinner spinnerEvento = findViewById(R.id.spinnerProductoEvento);
    final Spinner spinnerTematica = findViewById(R.id.spinnerProductoTematica);
    final Spinner spinnerCategoria = findViewById(R.id.spinnerProductoCategoria);

    if (getIntent() != null && getIntent().getExtras() != null) {

      id = getIntent().getStringExtra("id");
      setTitle("modificar producto");

      modButton.setText(R.string.modificar);
      mDataBase.child(id).addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
          nombreProducto.setText(dataSnapshot.child("nombre").getValue().toString());
          imgUrl = dataSnapshot.child("imgUrl").getValue().toString();
          descripcionProducto.setText(dataSnapshot.child("descripcion").getValue().toString());
          Funciones funciones = new Funciones();
          funciones.setImg(imgUrl, imagenProducto, getApplicationContext());
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
      });
    } else {
      setTitle("Agregar producto");
    }

    //TODO: MODIFICAR PRODUCTO Y LOS SPINNER CORRESPONDIENTES ADEMAS DE LUEGO INTENTAR OPTIMIZAR
    final Spinnerloaders spinnerloaders = new Spinnerloaders(this);
    spinnerloaders.adapterEventos(new Spinnerloaders.SpinnerAdaperCallbackEventos() {

      @Override
      public void callbackEventos(ArrayAdapter<String> adapter, ArrayList<Evento> eventoArrayList) {
        spinnerEvento.setAdapter(adapter);
        spinnerEvento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
          @Override
          public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String item = parent.getItemAtPosition(position).toString();
            getItemName(item);

            spinnerTematica.setAdapter(null);
            spinnerCategoria.setAdapter(null);
            if (!nombre.equals("Seleccione una opción...")) {
              spinnerloaders
                  .adapterTematicas(new Spinnerloaders.SpinnerAdapterCallbackTematicas() {

                    @Override
                    public void callbackTematicas(ArrayAdapter<String> adapter2,
                        ArrayList<Tematica> listaTematicas) {
                      spinnerTematica.setAdapter(adapter2);
                    }
                  }, nombre);

              spinnerTematica
                  .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view,
                        int position, long id) {
                      String item = parent.getItemAtPosition(position).toString();
                      getItemName(item);
                      if (!nombre.equals("Seleccione una opción...")) {
                       /* spinnerloaders.adapterCategorias(
                            new Spinnerloaders.SpinnerAdapterCallbackCategorias() {
                              @Override
                              public void callbackCategorias(
                                  ArrayAdapter<String> adapter) {
                                spinnerCategoria.setAdapter(adapter);
                              }
                            }, nombre);*/
                      }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                  });
            } else {
              ArrayList<String> lista = new ArrayList<>();
              lista.add("Seleccione una opción...");
              ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                  android.R.layout.simple_list_item_1, lista);
              spinnerTematica.setAdapter(adapter);
              spinnerCategoria.setAdapter(adapter);
            }
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

        if (Funciones.validarTexto(nombreProducto.getText().toString()) && Funciones
            .validarTexto(descripcionProducto.getText().toString())) {
          nombreProducto.setError("El nombre no debe estar vacío");
          descripcionProducto.setError("La descripción no puede estar vacía ");
          break;
        } else if (Funciones.validarTexto(descripcionProducto.getText().toString())) {
          descripcionProducto.setError("La descripción no puede estar vacía ");
          break;
        } else if (Funciones.validarTexto(nombreProducto.getText().toString())) {
          nombreProducto.setError("El nombre no debe estar vacío");
          break;
        } else if (Funciones.validarTexto(nombre) || nombre.equals("Seleccione una opción...")
            || nombre.equals("No se encontró ninguna categoría")) {
          DialogAlertSpinner dialogAlertSpinner = new DialogAlertSpinner(ProductoAdd.this);
          dialogAlertSpinner.DialogCreator();
          break;
        } else {

          dialogLoader = new DialogLoader(this);
          dialogLoader.CreateDialog();
          dialogLoader.ShowDialog();

          firebaseHelper = new FirebaseHelper(mDataBase);
          if (modButton.getText().toString().toLowerCase().equals("modificar")) {
            producto = new Producto(id, nombreProducto.getText().toString(),
                descripcionProducto.getText().toString(), imgUrl);

          } else {
            id = firebaseHelper.getIdkey();
            producto = new Producto(id, nombreProducto.getText().toString(),
                descripcionProducto.getText().toString(), LOADING_IMAGE_URL);
          }

          estado = firebaseHelper.guardarDatosFirebase(producto, id);

          if (!estado) {
            ErrorMensaje();
          } else if (uri == null) {
            firebaseHelper.CategoriaProducto(producto, nombre);
            SuccesMensaje();
            finish();
          } else {
            final StorageReference storageReference = FirebaseStorage.getInstance()
                .getReference(PRODUCTOS_CHILD).child(producto.getId());
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
                      producto = new Producto(id, nombreProducto.getText().toString(),
                          descripcionProducto.getText().toString(), downloadUri.toString());
                      firebaseHelper.guardarDatosFirebase(producto, producto.getId());
                      firebaseHelper.CategoriaProducto(producto, nombre);
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

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQUEST_IMAGE) {
      if (resultCode == RESULT_OK) {
        if (data != null) {
          uri = data.getData();
          imagenProducto.setImageURI(uri);
          Toast.makeText(this, "La imagen se cargo correctamente", Toast.LENGTH_LONG)
              .show();
        }
      }

    } else {
      Toast.makeText(this, "La imagen no se pudo cargar", Toast.LENGTH_LONG).show();
    }
  }

  private void SuccesMensaje() {
    Toast.makeText(this, "Producto agregado exitosamente", Toast.LENGTH_SHORT).show();
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
