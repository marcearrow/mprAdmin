package com.mpreventos.admin.controller;

import static com.mpreventos.admin.utils.Constantes.TEMATICAS_CHILD;

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
import com.mpreventos.admin.adapter.TematicaAdapter;
import com.mpreventos.admin.helper.FirebaseHelper;

public class TematicaLista extends AppCompatActivity {

  private DatabaseReference db;
  private RecyclerView recyclerView;
  private TematicaAdapter tematicaAdapter;
  private ProgressBar loader;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_tematica_lista);

    ActionBar actionBar = getSupportActionBar();

    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
      actionBar.setDisplayShowHomeEnabled(true);
    }
    setTitle("Temáticas");
    db = FirebaseDatabase.getInstance().getReference();

    loader = findViewById(R.id.progressBar2);

    recyclerView = findViewById(R.id.recyclerTematica);
    recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

    ObtenerTematicas();
    FloatingActionButton fab = findViewById(R.id.fabTematica);
    fab.setOnClickListener(v -> {
      Intent intent = new Intent(v.getContext(), TematicaAdd.class);
      startActivity(intent);

    });

  }

  private void ObtenerTematicas() {
    try {
      db = db.child(TEMATICAS_CHILD);
      FirebaseHelper firebaseHelper = new FirebaseHelper(db);
      firebaseHelper.ListaTematicas(listaDeTematicas -> {
        tematicaAdapter = new TematicaAdapter(R.layout.itemlist_tematica,
            listaDeTematicas, TematicaLista.this, db);
        recyclerView.setAdapter(tematicaAdapter);

        if (recyclerView.getAdapter() != null && listaDeTematicas.size() > 0) {
          loader.setVisibility(View.GONE);
        }

      });

    } catch (Exception ex) {
      //catch exception
    }
  }

  @Override
  public boolean onSupportNavigateUp() {
    onBackPressed();
    return true;
  }
}
