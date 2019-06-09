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
import com.mpreventos.admin.adapter.ProductoAdapter;
import com.mpreventos.admin.helper.FirebaseHelper;
import com.mpreventos.admin.model.Producto;
import com.mpreventos.admin.utils.Funciones;

import java.util.ArrayList;

public class ProductoLista extends AppCompatActivity {

    private static final String PRODUCTO_CHILD = "productos";
    private DatabaseReference db;
    private FirebaseHelper firebaseHelper;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private ProductoAdapter productoAdapter;
    private ArrayList<Producto> productoLista = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto_lista);

        setTitle(PRODUCTO_CHILD);
        db = FirebaseDatabase.getInstance().getReference();

        recyclerView = findViewById(R.id.recyclerProducto);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        obtenerProductos();

        fab = findViewById(R.id.fabProducto);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: HACER ACTIVITY DETALLES
                Intent intent = new Intent(v.getContext(), ProductoAdd.class);
                startActivity(intent);
            }
        });

    }

    private void obtenerProductos() {
        try {
            db.child(PRODUCTO_CHILD).getRef().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        productoLista.clear();

                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            //TODO: CAMBIAR TODOS A GETVALUE
                            if (ds.child("id").getValue() != null) {
                                String id = ds.child("id").getValue().toString();
                                String nombre = Funciones.asignarValor(ds.child("nombre").getValue().toString());
                                String imgUrl = Funciones.asignarValor(ds.child("imgUrl").getValue().toString());
                                String descripcion = Funciones.asignarValor(ds.child("descripcion").getValue().toString());
                                productoLista.add(new Producto(id, nombre, descripcion, imgUrl));
                            }
                            productoAdapter = new ProductoAdapter(R.layout.itemlist_producto, productoLista, getApplicationContext());
                            recyclerView.setAdapter(productoAdapter);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } catch (Exception ex) {
            //TODO: MENSAJE POR SI OCURRE UN ERROR
        }
    }
}
