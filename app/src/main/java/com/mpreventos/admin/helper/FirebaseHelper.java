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
  public Boolean EliminarNodoFirebase(String key, String relacion, String idParent) {

    if (db == null) {
      estado = false;
    } else {
      try {

        String tablaRelacion1 = null;
        String tablaRelacion2 = null;

        switch (relacion) {
          case "eventos":
            tablaRelacion1 = "eventoTematicas";
            break;
          case "tematicas":
            tablaRelacion1 = "tematicaCategorias";
            tablaRelacion2 = "eventoTematicas";
            break;
          case "categorias":
            tablaRelacion1 = "categoriaProducto";
            tablaRelacion2 = "tematicaCategorias";
            break;
          case "productos":
            tablaRelacion2 = "categoriaProducto";
            break;
        }

        db.child(key).removeValue();
        if (tablaRelacion1 != null && tablaRelacion2 != null) {
          db.getRoot().child(tablaRelacion1).child(key).removeValue();
          EliminarRelacion(key, idParent, tablaRelacion2);

        } else if (tablaRelacion1 == null && tablaRelacion2 != null) {
          EliminarRelacion(key, idParent, tablaRelacion2);
        } else if (tablaRelacion1 != null) {
          db.getRoot().child(tablaRelacion1).child(key).removeValue();
        }

        estado = true;
      } catch (DatabaseException ex) {
        ex.getStackTrace();
        estado = false;
      }
    }
    return estado;
  }


  public void AddRelacion(String idKey, String nombreNodo, String relacion) {
    db2 = db.getRoot();
    db2.child(relacion).child(nombreNodo).child(idKey).setValue(true);
  }

  public void TematicaConFiltro(final FirebaseTematicasListaCallback firebaseTematicasListaCallback,
      String filtro) {
    final ArrayList<Tematica> tematicas = new ArrayList<>();
    tematicas.clear();

    db.orderByChild("evento").equalTo(filtro).addListenerForSingleValueEvent(
        new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
              for (DataSnapshot ds : dataSnapshot.getChildren()) {
                String nombre = ds.child("nombre").getValue().toString();
                String id = ds.child("id").getValue().toString();
                String imgUrl = ds.child("imgUrl").getValue().toString();
                String idEvento = ds.child("evento").getValue().toString();
                Tematica tematica = new Tematica(id, nombre, imgUrl, idEvento);
                tematicas.add(tematica);
              }
              firebaseTematicasListaCallback.onCallback(tematicas);
            }
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
            String idEvento = ds.child("evento").getValue().toString();
            Tematica tematica = new Tematica(id, nombre, imgUrl, idEvento);
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
            String idTematica = ds.child("tematica").getValue().toString();
            Categoria categoria = new Categoria(id, nombre, imgUrl, idTematica);
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


  //LISTAS
  public void ListaEventos(final FirebaseEventosListaCallback firebaseEventosListaCallback) {
    final ArrayList<Evento> itemList = new ArrayList<>();

    db.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

        itemList.clear();
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
          if (dataSnapshot.exists()) {
            String nombre = ds.child("nombre").getValue().toString();
            String id = ds.child("id").getValue().toString();
            String imgUrl = ds.child("imgUrl").getValue().toString();
            Evento evento = new Evento(id, nombre, imgUrl);
            itemList.add(evento);
          }

        }
        firebaseEventosListaCallback.onCallback(itemList);
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    });
  }


  public void ListaTematicas(final FirebaseTematicasListaCallback firebaseTematicasListaCallback) {

    final ArrayList<Tematica> itemList = new ArrayList<>();

    db.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        itemList.clear();
        for (DataSnapshot ds : dataSnapshot.getChildren()) {

          if (dataSnapshot.exists()) {
            String nombre = ds.child("nombre").getValue().toString();
            String id = ds.child("id").getValue().toString();
            String imgUrl = ds.child("imgUrl").getValue().toString();
            String evento = ds.child("evento").getValue().toString();
            itemList.add(new Tematica(id, nombre, imgUrl, evento));
          }

        }
        firebaseTematicasListaCallback.onCallback(itemList);
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    });

  }

  public void ListaCategorias(
      final FirebaseCategoriasListaCallback firebaseCategoriasListaCallback) {

    final ArrayList<Categoria> itemList = new ArrayList<>();

    db.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        itemList.clear();
        for (DataSnapshot ds : dataSnapshot.getChildren()) {

          if (dataSnapshot.exists()) {
            String nombre = ds.child("nombre").getValue().toString();
            String id = ds.child("id").getValue().toString();
            String imgUrl = ds.child("imgUrl").getValue().toString();
            String tematica = ds.child("tematica").getValue().toString();
            itemList.add(new Categoria(id, nombre, imgUrl, tematica));
          }

        }
        firebaseCategoriasListaCallback.onCallback(itemList);
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    });
  }

  public void ListaProductos(final FirebaseProductosListaCallback firebaseProductosListaCallback) {

    final ArrayList<Producto> itemList = new ArrayList<>();

    db.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        itemList.clear();
        for (DataSnapshot ds : dataSnapshot.getChildren()) {

          if (dataSnapshot.exists()) {
            String nombre = ds.child("nombre").getValue().toString();
            String id = ds.child("id").getValue().toString();
            String descripcion = ds.child("descripcion").getValue().toString();
            String imgUrl = ds.child("imgUrl").getValue().toString();
            String categoria = ds.child("categoria").getValue().toString();
            itemList.add(new Producto(id, nombre, descripcion, imgUrl, categoria));
          }

        }
        firebaseProductosListaCallback.onCallback(itemList);
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    });
  }

  public void EliminarRelacion(String id, String relacionAntigua, String relacion) {
    db.getRoot().child(relacion).child(relacionAntigua).child(id).removeValue();

  }

  public void BuscarPadre(String hijo, String relacion,
      FirebaseCallbackIdKey firebaseCallbackIdKey) {
    final int[] i = {0};
    final String[] key = new String[1];
    db2 = db2.getRoot().child(relacion);
    db2.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        for (DataSnapshot ds :
            dataSnapshot.getChildren()) {
          i[0]++;
          if (ds.child(hijo).exists()) {
            key[0] = ds.getKey();
          }
          firebaseCallbackIdKey.onCallback(key[0], i[0]);
        }
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    });
  }
  //CALLBACKS

  public interface FirebaseEventosListaCallback {

    void onCallback(ArrayList<Evento> listaDeEventos);
  }

  public interface FirebaseTematicasListaCallback {

    void onCallback(ArrayList<Tematica> listaDeTematicas);
  }

  public interface FirebaseCategoriasListaCallback {

    void onCallback(ArrayList<Categoria> listaDeCategorias);
  }

  public interface FirebaseProductosListaCallback {

    void onCallback(ArrayList<Producto> listaDeProductos);
  }

  public interface FirebaseCallbackIdKey {

    void onCallback(String idKey, int position);
  }

}

