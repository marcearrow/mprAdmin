package com.mpreventos.admin.controller;

import static com.mpreventos.admin.utils.Constantes.CATEGORIAS_CHILD;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mpreventos.admin.R;
import com.mpreventos.admin.adapter.CategoriaAdapter;
import com.mpreventos.admin.helper.FirebaseHelper;

public class CategoriaLista extends AppCompatActivity {

  private DatabaseReference db;
  private RecyclerView recyclerView;
  private CategoriaAdapter categoriaAdapter;
  private ProgressBar loader;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_categoria_lista);
    ActionBar actionBar = getSupportActionBar();

    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
      actionBar.setDisplayShowHomeEnabled(true);
    }

    setTitle("Categorías");
    db = FirebaseDatabase.getInstance().getReference();

    loader = findViewById(R.id.progressBar3);
    recyclerView = findViewById(R.id.recyclerCategoria);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));

    ObtenerCategorias();
    FloatingActionButton fab = findViewById(R.id.fabCategoria);
    fab.setOnClickListener(v -> {
      Intent intent = new Intent(v.getContext(), CategoriaAdd.class);
      startActivity(intent);
    });

  }

  private void ObtenerCategorias() {
    try {
      db = db.child(CATEGORIAS_CHILD);
      FirebaseHelper firebaseHelper = new FirebaseHelper(db);
      firebaseHelper.ListaCategorias(listaDeCategorias -> {
        categoriaAdapter = new CategoriaAdapter(R.layout.itemlist_categoria,
            listaDeCategorias, CategoriaLista.this, db);
        recyclerView.setAdapter(categoriaAdapter);

        if (recyclerView.getAdapter() != null && listaDeCategorias.size() > 0) {
          loader.setVisibility(View.GONE);
        }
      });
    } catch (Exception ignored) {

    }
  }


  @Override
  public boolean onSupportNavigateUp() {
    onBackPressed();
    return true;
  }

}
