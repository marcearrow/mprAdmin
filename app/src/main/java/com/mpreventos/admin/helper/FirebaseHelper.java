package com.mpreventos.admin.helper;

import androidx.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mpreventos.admin.model.Categoria;
import com.mpreventos.admin.model.Evento;
import com.mpreventos.admin.model.Producto;
import com.mpreventos.admin.model.Tematica;
import java.util.ArrayList;

public class FirebaseHelper {

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
  public Boolean EliminarNodoFirebase(String key) {
    if (db == null) {
      estado = false;
    } else {
      try {
        db.child(key).removeValue();
        estado = true;
      } catch (DatabaseException ex) {
        ex.getStackTrace();
        estado = false;
      }
    }
    return estado;
  }


  public void EventosTematicas(Tematica tematca, String nombreEvento) {
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


  public void EventosNombre(final FirebaseEventosListaCallback firebaseEventosListaCallback) {
    final ArrayList<Evento> itemList = new ArrayList<>();

    db.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

        itemList.clear();
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
          String nombre = ds.child("nombre").getValue().toString();
          String id = ds.child("id").getValue().toString();
          String imgUrl = ds.child("imgUrl").getValue().toString();
          Evento evento = new Evento(id, nombre, imgUrl);
          itemList.add(evento);
        }
        firebaseEventosListaCallback.onCallback(itemList);
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    });
  }

  public void TematicasNombre(final FirebaseTematicasListaCallback firebaseTematicasListaCallback,
      ArrayList<String> lista) {
    final ArrayList<Tematica> tematicas = new ArrayList<>();
    tematicas.clear();
    for (String st :
        lista) {
      db.orderByChild("id").equalTo(st).addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

          for (DataSnapshot ds : dataSnapshot.getChildren()) {
            String nombre = ds.child("nombre").getValue().toString();
            String id = ds.child("id").getValue().toString();
            String imgUrl = ds.child("imgUrl").getValue().toString();
            Tematica tematica = new Tematica(id, nombre, imgUrl);
            tematicas.add(tematica);
          }
          firebaseTematicasListaCallback.onCallback(tematicas);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
      });
    }
  }

  public void CategoriasNombre(
      final FirebaseCategoriasListaCallback firebaseCategoriasListaCallback,
      ArrayList<String> lista) {
    final ArrayList<Categoria> categorias = new ArrayList<>();
    categorias.clear();
    for (String st :
        lista) {
      db.orderByChild("id").equalTo(st).addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

          for (DataSnapshot ds : dataSnapshot.getChildren()) {
            String nombre = ds.child("nombre").getValue().toString();
            String id = ds.child("id").getValue().toString();
            String imgUrl = ds.child("imgUrl").getValue().toString();
            String descripcion = ds.child("descripcion").getValue().toString();
            Categoria categoria = new Categoria(id, nombre, descripcion, imgUrl);
            categorias.add(categoria);
          }
          firebaseCategoriasListaCallback.onCallback(categorias);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
      });
    }
  }

  public interface FirebaseEventosListaCallback {

    void onCallback(ArrayList<Evento> listaDeEventos);
  }

  public interface FirebaseTematicasListaCallback {

    void onCallback(ArrayList<Tematica> listaDeTematicas);
  }

  public interface FirebaseCategoriasListaCallback {

    void onCallback(ArrayList<Categoria> listaDeCategorias);
  }


}

