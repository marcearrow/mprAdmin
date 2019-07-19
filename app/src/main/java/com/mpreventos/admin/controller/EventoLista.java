package com.mpreventos.admin.controller;

import static com.mpreventos.admin.utils.Constantes.EVENTOS_CHILD;

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
import com.mpreventos.admin.adapter.EventoAdapter;
import com.mpreventos.admin.helper.FirebaseHelper;


public class EventoLista extends AppCompatActivity {

  private DatabaseReference db;
  private RecyclerView recyclerView;
  private EventoAdapter eventoAdapter;
  private ProgressBar loader;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_evento_lista);
    db = FirebaseDatabase.getInstance().getReference();
    db.keepSynced(true);
    ActionBar actionBar = getSupportActionBar();
    setTitle(R.string.eventos);

    loader = findViewById(R.id.progressBar1);
    recyclerView = findViewById(R.id.recyclerEvento);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));

    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
      actionBar.setDisplayShowHomeEnabled(true);
    }

    FloatingActionButton fab = findViewById(R.id.fabEvento);

    fab.setOnClickListener(view -> {

      Intent intent = new Intent(view.getContext(), EventoAdd.class);
      startActivity(intent);
    });

    ObtenerEventos();

  }

  private void ObtenerEventos() {
    db = db.child(EVENTOS_CHILD);
    FirebaseHelper firebaseHelper = new FirebaseHelper(db);
    try {
      firebaseHelper.ListaEventos(listaDeEventos -> {

        eventoAdapter = new EventoAdapter(R.layout.itemlist_evento, listaDeEventos,
            EventoLista.this, db);
        recyclerView.setAdapter(eventoAdapter);

        if (recyclerView.getAdapter() != null && listaDeEventos.size() > 0) {
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
