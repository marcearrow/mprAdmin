package com.mpreventos.admin.helper;

import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;

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


}

