package com.mpreventos.admin.controller;

import static com.mpreventos.admin.utils.Constantes.PRODUCTOS_CHILD;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mpreventos.admin.R;
import com.mpreventos.admin.adapter.ProductoAdapter;
import com.mpreventos.admin.helper.FirebaseHelper;


public class ProductoLista extends AppCompatActivity {

  private DatabaseReference db;
  private RecyclerView recyclerView;
  private ProductoAdapter productoAdapter;
  private ProgressBar loader;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_producto_lista);

    ActionBar actionBar = getSupportActionBar();

    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
      actionBar.setDisplayShowHomeEnabled(true);
    }

    loader = findViewById(R.id.progressBar4);
    setTitle("Productos");
    db = FirebaseDatabase.getInstance().getReference();

    recyclerView = findViewById(R.id.recyclerProducto);
    recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

    ObtenerProductos();

    FloatingActionButton fab = findViewById(R.id.fabProducto);
    fab.setOnClickListener(v -> {

      Intent intent = new Intent(v.getContext(), ProductoAdd.class);
      startActivity(intent);
    });

  }

  private void ObtenerProductos() {
    try {
      db = db.child(PRODUCTOS_CHILD);
      FirebaseHelper firebaseHelper = new FirebaseHelper(db);
      firebaseHelper.ListaProductos(listaDeProductos -> {
        productoAdapter = new ProductoAdapter(R.layout.itemlist_producto,
            listaDeProductos, ProductoLista.this, db);
        recyclerView.setAdapter(productoAdapter);

        if (recyclerView.getAdapter() != null && listaDeProductos.size() > 0) {
          loader.setVisibility(View.GONE);
        }
      });


    } catch (Exception ex) {
      //TODO: MENSAJE POR SI OCURRE UN ERROR
    }
  }

  @Override
  public boolean onSupportNavigateUp() {
    onBackPressed();
    return true;
  }
}
