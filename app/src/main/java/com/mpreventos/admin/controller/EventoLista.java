package com.mpreventos.admin.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mpreventos.admin.R;
import com.mpreventos.admin.adapter.EventoAdapter;
import com.mpreventos.admin.helper.FirebaseHelper;
import com.mpreventos.admin.model.Evento;

import java.util.ArrayList;

public class EventoLista extends AppCompatActivity {

    private static final String EVENT_CHILD = "eventos";
    private DatabaseReference db;
    private FirebaseHelper firebaseHelper;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    public EventoAdapter eventoAdapter;

    private ArrayList<Evento> eventoLista = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento_lista);
        db = FirebaseDatabase.getInstance().getReference();
        setTitle(R.string.eventos);

        recyclerView = findViewById(R.id.recyclerEvento);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        obtenerEventos();
        fab = findViewById(R.id.fabEvento);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(), EventoAdd.class);
                startActivity(intent);
            }
        });

    }

    public void obtenerEventos() {
        try {
            db.child(EVENT_CHILD).getRef().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        eventoLista.clear();

                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            if (ds.child("id").getValue() != null) {
                                String nombre = ds.child("nombre").getValue().toString();
                                String imgUrl = ds.child("imgUrl").getValue().toString();
                                String id = ds.child("id").getValue().toString();
                                eventoLista.add(new Evento(id, nombre, imgUrl));
                            }

                            eventoAdapter = new EventoAdapter(R.layout.itemlist_evento, eventoLista, getApplicationContext());
                            recyclerView.setAdapter(eventoAdapter);
                        }
                    }//TODO: AGERGAR UN ELSE
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } catch (Exception ex) {
            // Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show();
        }
    }

    //TODO: RECICLERVIEW OR ADAPTER STOP
}
