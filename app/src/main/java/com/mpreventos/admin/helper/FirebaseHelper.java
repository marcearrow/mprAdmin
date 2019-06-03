package com.mpreventos.admin.helper;

import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;

public class FirebaseHelper {

    private DatabaseReference db;
    private Boolean estado;

    public FirebaseHelper(DatabaseReference db) {
        this.db = db;
    }

    //Guardar datos en Firebase
    public Boolean guardarFirebase(Class clase) {

        if (clase == null) {
            estado = false;
            return estado;
        } else {
            try {
                db.child(clase.getName()).setValue(clase);
                estado = true;
            } catch (DatabaseException ex) {
                ex.printStackTrace();
                estado = false;
            }
        }
        return estado;
    }

    //Generar idKey
    public String idkey() {
        return db.push().getKey();
    }

    //Remover item
    public Boolean EliminarFirebase(String nodo, String key) {
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

