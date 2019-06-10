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
import com.mpreventos.admin.controller.TematicaAdd;
import com.mpreventos.admin.model.Tematica;
import com.mpreventos.admin.utils.ImageLoader;

import java.util.ArrayList;

public class TematicaAdapter extends RecyclerView.Adapter<TematicaAdapter.viewHolderTematicas> {
    private int resource;
    private ArrayList<Tematica> temacaLista;
    private Context context;

    public TematicaAdapter(int resource, ArrayList<Tematica> temacaLista, Context context) {
        this.resource = resource;
        this.temacaLista = temacaLista;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolderTematicas onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new viewHolderTematicas(view);

    }

    @Override
    public void onBindViewHolder(@NonNull viewHolderTematicas holder, int position) {
        Tematica tematica = temacaLista.get(position);
        final String idTematica = tematica.getId();
        holder.textView.setText(tematica.getNombre());
        if (tematica.getImgUrl() == null) {
            holder.imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.logompr));
        } else {
            try {
                ImageLoader imageLoader = new ImageLoader(context);
                imageLoader.setImgWithGlide(tematica.getImgUrl(), holder.imageView);
            } catch (Exception ex) {
                Toast.makeText(context, "Ocurrio un error", Toast.LENGTH_SHORT).show();
                //holder.imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.logompr));
            }

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), TematicaAdd.class);
                    intent.putExtra("id", idTematica);
                    v.getContext().startActivity(intent);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return temacaLista.size();
    }

    class viewHolderTematicas extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;
        CardView cardView;

        private viewHolderTematicas(@NonNull View itemView) {
            super(itemView);

            this.imageView = itemView.findViewById(R.id.imageCardTematica);
            this.textView = itemView.findViewById(R.id.cardTextNameTematica);
            this.cardView = itemView.findViewById(R.id.cardTematica);

        }
    }
}
