package com.mpreventos.admin.utils;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mpreventos.admin.helper.FirebaseHelper;

public class Spinnerloaders {

    Context context;

    public Spinnerloaders(Context context) {
        this.context = context;
    }

    public ArrayAdapter setSpinnerEventos() {
        DatabaseReference eventos = FirebaseDatabase.getInstance().getReference("eventos");
        FirebaseHelper helperEventos = new FirebaseHelper(eventos);
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, helperEventos.eventosNombre());
        return arrayAdapter;
    }

    //TODO: TRAER DATOS DEPENDIENDO De la relacion que existe entre un elemnto y otro
    public ArrayAdapter setSpinnerTematicas() {
        DatabaseReference tematicas = FirebaseDatabase.getInstance().getReference("tematicas");
        FirebaseHelper helperTematicas = new FirebaseHelper(tematicas);
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, helperTematicas.eventosNombre());
        return arrayAdapter;
    }

    public ArrayAdapter setSpinnerCategorias() {
        DatabaseReference categorias = FirebaseDatabase.getInstance().getReference("categorias");
        FirebaseHelper helperCategorias = new FirebaseHelper(categorias);
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, helperCategorias.categoriaNombre());
        return arrayAdapter;
    }
}
