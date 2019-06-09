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
import com.mpreventos.admin.adapter.CategoriaAdapter;
import com.mpreventos.admin.helper.FirebaseHelper;
import com.mpreventos.admin.model.Categoria;

import java.util.ArrayList;

public class CategoriaLista extends AppCompatActivity {

    private static final String CATEGORIA_CHILD = "categorias";
    private DatabaseReference db;
    private FirebaseHelper firebaseHelper;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private CategoriaAdapter categoriaAdapter;
    private ArrayList<Categoria> categoriaLista = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria_lista);
        setTitle(CATEGORIA_CHILD);
        db = FirebaseDatabase.getInstance().getReference();


        recyclerView = findViewById(R.id.recyclerCategoria);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        obtenerCategorias();
        fab = findViewById(R.id.fabCategoria);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CategoriaAdd.class);
                startActivity(intent);
            }
        });

    }

    private void obtenerCategorias() {
        try {
            db.child(CATEGORIA_CHILD).getRef().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        categoriaLista.clear();

                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            if (ds.child("id").getValue() != null) {
                                String nombre = ds.child("nombre").getValue().toString();
                                String imgUrl = ds.child("imgUrl").getValue().toString();
                                String descripcion = ds.child("descripcion").getValue().toString();
                                String id = ds.child("id").getValue().toString();
                                categoriaLista.add(new Categoria(id, nombre, descripcion, imgUrl));
                            }
                            categoriaAdapter = new CategoriaAdapter(R.layout.itemlist_categoria, categoriaLista, getApplicationContext());
                            recyclerView.setAdapter(categoriaAdapter);
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
