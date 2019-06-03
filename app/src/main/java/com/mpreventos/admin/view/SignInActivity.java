package com.mpreventos.admin.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mpreventos.admin.R;
import com.mpreventos.admin.utils.Funciones;

public class SignInActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText mEmail;
    private EditText mPassword;
    Button sigin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();

        //buscar boton en vista
        sigin = findViewById(R.id.sigInButton);
        mEmail = findViewById(R.id.edtSignInEmail);
        mPassword = findViewById(R.id.edtSignInPassword);

        //asignar un evento al presionarlo
        sigin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarSesion(mEmail.getText().toString(), mPassword.getText().toString());
            }
        });
    }

    public void iniciarSesion(String email, String password) {
        if (Funciones.validarTexto(email) || Funciones.validarTexto(password)) {
            mEmail.setError("Debe ingresar un correo");
            mPassword.setError("Contraseña incorrecta");
        } else {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();

                        closeActivity();
                    } else {
                        Toast.makeText(SignInActivity.this, R.string.signIngFallido, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void closeActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
