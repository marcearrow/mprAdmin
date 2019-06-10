package com.mpreventos.admin.helper;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FirebaseHelper {

    private DatabaseReference db;
    private Boolean estado;

    //constructor firebasehelper
    public FirebaseHelper(DatabaseReference db) {
        this.db = db;
    }

    //Guardar datos en Firebase
    public Boolean guardarDatosFirebase(Object clase, String key) {

        if (clase == null) {
            estado = false;
            return estado;
        } else if (key == null) {
            try {
                db.setValue(clase);
                estado = true;
            } catch (DatabaseException ex) {
                ex.printStackTrace();
                estado = false;
            }
        } else {
            try {
                db.child(key).setValue(clase);
                estado = true;
            } catch (DatabaseException ex) {
                ex.printStackTrace();
                estado = false;
            }
        }
        return estado;
    }

    //Generar idKey
    public String getIdkey() {
        return db.push().getKey();
    }

    //Eliminar item
    public Boolean EliminarNodoFirebase(String nodo, String key) {
        if (nodo == null) {
            estado = false;
        } else {
            try {
                db.child(nodo).child(key).removeValue();
                estado = true;
            } catch (DatabaseException ex) {
                ex.getStackTrace();
                estado = false;
            }
        }
        return estado;
    }

    //Arraylist de los nombres de los eventos
    public ArrayList<String> eventosNombre() {
        final ArrayList<String> eventos = new ArrayList<>();
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                eventos.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String nombre = ds.child("nombre").getValue().toString();
                    eventos.add(nombre);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return eventos;
    }

    //Arraylist de los nombres de las Tematicas
    public ArrayList<String> tematicasNombre() {
        final ArrayList<String> tematicas = new ArrayList<>();
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tematicas.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String nombre = ds.child("nombre").getValue().toString();
                    tematicas.add(nombre);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return tematicas;
    }


    //Arraylist de los nombres de los eventos
    public ArrayList<String> categoriaNombre() {
        final ArrayList<String> categorias = new ArrayList<>();
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                categorias.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String nombre = ds.child("nombre").getValue().toString();
                    categorias.add(nombre);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return categorias;
    }
}

