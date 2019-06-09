package com.mpreventos.admin.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mpreventos.admin.R;
import com.mpreventos.admin.adapter.TematicaAdapter;
import com.mpreventos.admin.helper.FirebaseHelper;
import com.mpreventos.admin.model.Tematica;

import java.util.ArrayList;

public class TematicaLista extends AppCompatActivity {
    private static final String TEMATICA_CHILD = "tematicas";
    private DatabaseReference db;
    private FirebaseHelper firebaseHelper;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private TematicaAdapter tematicaAdapter;

    private ArrayList<Tematica> tematicaLista = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tematica_lista);
        setTitle(TEMATICA_CHILD);
        db = FirebaseDatabase.getInstance().getReference();

        recyclerView = findViewById(R.id.recyclerTematica);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        obtenerTematicas();
        fab = findViewById(R.id.fabTematica);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), TematicaAdd.class);
                startActivity(intent);
            }
        });

    }

    private void obtenerTematicas() {
        try {
            db.child(TEMATICA_CHILD).getRef().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        tematicaLista.clear();

                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            if (ds.child("id").getValue() != null) {
                                String nombre = ds.child("nombre").getValue().toString();
                                String imgUrl = ds.child("imgUrl").getValue().toString();
                                String id = ds.child("id").getValue().toString();
                                tematicaLista.add(new Tematica(id, nombre, imgUrl));
                            }
                            tematicaAdapter = new TematicaAdapter(R.layout.itemlist_tematica, tematicaLista, getApplicationContext());
                            recyclerView.setAdapter(tematicaAdapter);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } catch (Exception ex) {
            //catch exception
        }
    }
}
