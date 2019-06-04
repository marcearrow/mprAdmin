package com.mpreventos.admin.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mpreventos.admin.R;
import com.mpreventos.admin.helper.FirebaseHelper;
import com.mpreventos.admin.helper.StorageHelper;
import com.mpreventos.admin.model.Evento;

public class EventoAdd extends AppCompatActivity {


    private static final String EVENTOS_CHILD = "eventos";
    private static final int REQUEST_IMAGE = 2;
    private static final String LOADING_IMAGE_URL = "https://firebasestorage.googleapis.com/v0/b/mprfirebase-7753b.appspot.com/o/logo-mpr-decoracion.png?alt=media&token=49548e12-c035-4995-be4d-f38be90bbc06";
    DatabaseReference mDataBase;
    ImageView imagenEvento;
    EditText nombreEvento;
    FirebaseHelper firebaseHelper;
    Evento evento;
    Uri uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento_add);

        mDataBase = FirebaseDatabase.getInstance().getReference(EVENTOS_CHILD);
        nombreEvento = (EditText) findViewById(R.id.editNombreEvento);
        imagenEvento = (ImageView) findViewById(R.id.imgEvento);

    }

    public void onClickAddEvento(View view) {
        switch (view.getId()) {
            case R.id.btCancelarEvento:
                finish();
                break;
            case R.id.btSelectImagen:

                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_IMAGE);

                break;
            case R.id.btAddEvento:
                final FirebaseHelper firebaseHelper = new FirebaseHelper(mDataBase);
                final String id = firebaseHelper.getIdkey();
                Evento tempEvento = new Evento(id, nombreEvento.getText().toString(), LOADING_IMAGE_URL);
                Boolean estado = firebaseHelper.guardarDatosFirebase(tempEvento, id);

                if (estado == true && uri != null) {
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference(EVENTOS_CHILD).child(id);
                    StorageHelper storageHelper = new StorageHelper(storageReference);
                    estado = storageHelper.uploadImage(uri);

                    if (estado = true) {
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                uptade(uri, id);
                            }
                        });

                        //TODO FALTA MOSTRAR AVISO DE QUE SE AGREGO EXITOSAMENTE
                    }

                }

                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    uri = data.getData();
                    imagenEvento.setImageURI(uri);
                    Toast.makeText(this, "La imagen se cargo correctamente", Toast.LENGTH_LONG).show();
                }
            }

        } else Toast.makeText(this, "La imagen no se pudo cargar", Toast.LENGTH_LONG).show();
    }

    public void uptade(Uri uri, String id) {
        evento = new Evento(id, nombreEvento.getText().toString(), uri.toString());
        firebaseHelper.guardarDatosFirebase(evento, evento.getId());

    }
}
