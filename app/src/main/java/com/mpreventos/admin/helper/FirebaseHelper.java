package com.mpreventos.admin.helper;

import androidx.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mpreventos.admin.model.Categoria;
import com.mpreventos.admin.model.Producto;
import com.mpreventos.admin.model.Tematica;
import java.util.ArrayList;

public class FirebaseHelper {

    private static final String TAG = "spinner";
    private DatabaseReference db;
    private DatabaseReference db2;
    private Boolean estado;

    //constructor firebasehelper
    public FirebaseHelper(DatabaseReference db) {
        this.db = db;
        db2 = FirebaseDatabase.getInstance().getReference();
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


    public void eventosTematicas(Tematica tematca, String nombreEvento) {
        db2 = db.getRoot();
        db2.child("eventoTematicas").child(nombreEvento).child(tematca.getId()).setValue(true);
    }

    public void TematicaCategora(Categoria categoria, String nombreCategoria) {
        db2 = db.getRoot();
        db2.child("tematicaCategorias").child(nombreCategoria).child(categoria.getId())
            .setValue(true);
    }

    public void CategoriaProducto(Producto producto, String nombreProducto) {
        db2 = db.getRoot();
        db2.child("categoriaProducto").child(nombreProducto).child(producto.getId()).setValue(true);
    }

    //PARA CONSEGUUIR LOS ELEMENTOS  AQUI DEBES EN VEZ DE AGREGARLO A UNA LISTA PASARLO A UN ADAPTER
    public void EventosNombre(final FirebaseEventosCallback firebaseEventosCallback) {
        final ArrayList<String> itemList = new ArrayList<>();

        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                itemList.clear();
                itemList.add(0, "Seleccione una opción...");
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String nombre = ds.child("nombre").getValue().toString();
                    itemList.add(nombre);
                }
                firebaseEventosCallback.onCallback(itemList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //DEBES UTIlizar el db.orederBychild("id").equealTo(st)
    public void TematicasNombre(final FirebaseEventosCallback firebaseEventosCallback, ArrayList<String> lista) {
        final ArrayList<String> itemList = new ArrayList<>();
        itemList.clear();
        for (String st :
                lista) {
            db.orderByChild("id").equalTo(st).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String nombre = ds.child("nombre").getValue().toString();
                        itemList.add(nombre);
                    }
                    firebaseEventosCallback.onCallback(itemList);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }


            });


        }


    }

    public interface FirebaseEventosCallback {
        void onCallback(ArrayList<String> lista);
    }



}

