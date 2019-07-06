package com.mpreventos.admin.utils;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mpreventos.admin.helper.FirebaseHelper;

import java.util.ArrayList;

public class Spinnerloaders {

    private static final String TAG = "spinnerloader";
    Context context;
    ArrayAdapter<String> adapter;

    public Spinnerloaders(Context context) {
        this.context = context;
    }

    public void adapterEventos(final SpinnerAdaperCallbackEventos spinnerAdaperCallbackEventos) {
        DatabaseReference eventos = FirebaseDatabase.getInstance().getReference("eventos");
        FirebaseHelper helperEventos = new FirebaseHelper(eventos);

        helperEventos.EventosNombre(new FirebaseHelper.FirebaseEventosCallback() {
            @Override
            public void onCallback(ArrayList<String> lista) {
                adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, lista);
                spinnerAdaperCallbackEventos.callbackEventos(adapter);
            }
        });
    }

    // PARA MANEJAR LAS RELACIONES DEBES UTILIZAR ALGO ASI
    public void adapterTematicas(final SpinnerAdapterCallbackTematicas spinnerAdapterCallbackTematicas, String evento) {
        DatabaseReference eventosTematicas = FirebaseDatabase.getInstance().getReference("eventoTematicas").child(evento);
        DatabaseReference tematicas = FirebaseDatabase.getInstance().getReference("tematicas");

        //final FirebaseHelper helperEventosTematicas= new FirebaseHelper(eventosTematicas);
        final FirebaseHelper firebaseHelperTematicas = new FirebaseHelper(tematicas);

        eventosTematicas.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                ArrayList<String> lista = new ArrayList<>();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds :
                            dataSnapshot.getChildren()) {
                        //TODO: hacer que solo tome valores verdaderos
                        lista.add(ds.getKey());

                        // Log.d(TAG, "onDataChange: " + "ds.valu= " + ds.getValue()) ;
                    }

                    //Log.d(TAG, "onDataChange: lista sieze"+ lista.size());

                    if (lista.size() > 0) {
                        firebaseHelperTematicas.TematicasNombre(new FirebaseHelper.FirebaseEventosCallback() {
                            @Override
                            public void onCallback(ArrayList<String> lista) {
                                //    Log.d(TAG,"Llamada de callback" + dataSnapshot.toString());
                                adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, lista);
                                spinnerAdapterCallbackTematicas.callbackTematicas(adapter);
                            }
                        }, lista);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void adapterCategorias(final SpinnerAdapterCallbackCategorias spinnerAdapterCallbackCategorias, String tematica) {
        DatabaseReference tematicasCategorias = FirebaseDatabase.getInstance().getReference("tematicaCategorias").child(tematica);
        DatabaseReference categorias = FirebaseDatabase.getInstance().getReference("categorias");

        final FirebaseHelper firebaseHelperCategorias = new FirebaseHelper(categorias);

        tematicasCategorias.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                ArrayList<String> lista = new ArrayList<>();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds :
                            dataSnapshot.getChildren()) {
                        //TODO: hacer que solo tome valores verdaderos
                        lista.add(ds.getKey());

                        Log.d(TAG, "onDataChange: " + "ds.valu= " + ds.getValue());
                    }

                    Log.d(TAG, "onDataChange: lista sieze" + lista.size());
                    if (lista.size() > 0) {
                        firebaseHelperCategorias.TematicasNombre(new FirebaseHelper.FirebaseEventosCallback() {
                            @Override
                            public void onCallback(ArrayList<String> lista) {
                                Log.d(TAG, "Llamada de callback" + dataSnapshot.toString());
                                adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, lista);
                                spinnerAdapterCallbackCategorias.callbackCategorias(adapter);
                            }
                        }, lista);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    public interface SpinnerAdaperCallbackEventos {
        void callbackEventos(ArrayAdapter<String> adapter);

    }

    public interface SpinnerAdapterCallbackTematicas {
        void callbackTematicas(ArrayAdapter<String> adapter);
    }

    public interface SpinnerAdapterCallbackCategorias {
        void callbackCategorias(ArrayAdapter<String> adapter);
    }
}
