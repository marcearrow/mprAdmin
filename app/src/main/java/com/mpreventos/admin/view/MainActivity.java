package com.mpreventos.admin.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mpreventos.admin.R;

public class MainActivity extends AppCompatActivity {

    //variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAuth = FirebaseAuth.getInstance();

        //buscar si existe una sesion iniciada
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        if (mFirebaseUser == null) {
            //si no se encuentra usuario abrir login
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        }

        //asignar titulo a la toolbar
        setTitle("MPR eventos");

    }

    //Crear menu de opciones
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    //asignar funciones a cada opcion
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //buscar id de la opcion
        switch (item.getItemId()) {
            //cerrar sesion
            case R.id.sign_out_menu:
                mFirebaseAuth.signOut();
                //cerrar activity e ir a login
                startActivity(new Intent(this, SignInActivity.class));
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //metodo para abrir las diferentes listas
    public void onClickMainActivity(View view) {
        switch (view.getId()) {
            case R.id.menuTematicas:
                startActivity(new Intent(this, TematicaLista.class));

                break;
            case R.id.menuEventos:
                startActivity(new Intent(this, EventoLista.class));

                break;
            case R.id.menuProductos:
                startActivity(new Intent(this, ProductoLista.class));

                break;
            case R.id.menuCategorias:
                startActivity(new Intent(this, CategoriaLista.class));

                break;
        }
    }

}
