package com.mpreventos.admin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.mpreventos.admin.R;
import com.mpreventos.admin.model.Evento;
import com.mpreventos.admin.utils.imageLoader;

import java.util.ArrayList;

public class EventoAdapter extends RecyclerView.Adapter<EventoAdapter.viewHolderEventos> {

    private int resource;
    private ArrayList<Evento> eventoLista;
    private Context context;

    public EventoAdapter(int resource, ArrayList<Evento> eventoLista, Context context) {
        this.resource = resource;
        this.eventoLista = eventoLista;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolderEventos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new viewHolderEventos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolderEventos holder, int position) {
        Evento evento = eventoLista.get(position);
        holder.textView.setText(evento.getNombre());
        if (evento.getImgUrl() == null) {
            holder.imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.logompr));
        } else {
            try {
                imageLoader.setImgWithGlide(evento.getImgUrl(), holder.imageView);
            } catch (Exception ex) {
                Toast.makeText(context, "Ocurrio un error", Toast.LENGTH_SHORT).show();
                holder.imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.logompr));
            }

        }
    }

    @Override
    public int getItemCount() {
        return eventoLista.size();
    }

    class viewHolderEventos extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;

        public viewHolderEventos(@NonNull View itemView) {
            super(itemView);

            this.imageView = itemView.findViewById(R.id.imageCardEvent);
            this.textView = itemView.findViewById(R.id.cardTextName);

        }
    }
}
