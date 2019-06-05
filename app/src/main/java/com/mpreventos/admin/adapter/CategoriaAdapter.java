package com.mpreventos.admin.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.mpreventos.admin.R;
import com.mpreventos.admin.controller.CategoriaDetalles;
import com.mpreventos.admin.model.Categoria;
import com.mpreventos.admin.utils.ImageLoader;

import java.util.ArrayList;

public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaAdapter.viewHolderCategoria> {

    private int resource;
    private ArrayList<Categoria> categoriaLista;
    private Context context;

    public CategoriaAdapter(int resource, ArrayList<Categoria> categoriaLista, Context context) {
        this.resource = resource;
        this.categoriaLista = categoriaLista;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolderCategoria onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new viewHolderCategoria(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolderCategoria holder, int position) {
        Categoria categoria = categoriaLista.get(position);
        final String idCategoria = categoria.getId();
        holder.textViewNombre.setText(categoria.getNombre());
        holder.textViewwDescipcion.setText(categoria.getDescripcion());
        if (categoria.getImgUrl() == null) {
            holder.imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.logompr));
        } else {
            try {
                ImageLoader imageLoader = new ImageLoader(context);
                imageLoader.setImgWithGlide(categoria.getImgUrl(), holder.imageView);
            } catch (Exception ex) {

                Toast.makeText(context, "Ocurrio un error", Toast.LENGTH_SHORT).show();
            }

        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CategoriaDetalles.class);
                intent.putExtra("id", idCategoria);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoriaLista.size();
    }

    class viewHolderCategoria extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textViewwDescipcion;
        TextView textViewNombre;
        CardView cardView;

        private viewHolderCategoria(@NonNull View itemView) {
            super(itemView);


            this.imageView = itemView.findViewById(R.id.cardImagenCategoria);
            this.cardView = itemView.findViewById(R.id.cardCategoria);
            this.textViewNombre = itemView.findViewById(R.id.cardTextNameCategoria);
            this.textViewwDescipcion = itemView.findViewById(R.id.cardDescripcionCategoria);

        }
    }
}
