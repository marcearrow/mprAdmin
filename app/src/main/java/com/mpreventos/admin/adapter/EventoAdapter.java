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
import com.google.firebase.database.DatabaseReference;
import com.mpreventos.admin.R;
import com.mpreventos.admin.controller.EventoAdd;
import com.mpreventos.admin.model.Evento;
import com.mpreventos.admin.utils.DialogAlertDelete;
import com.mpreventos.admin.utils.ImageLoader;
import java.util.ArrayList;

public class EventoAdapter extends RecyclerView.Adapter<EventoAdapter.viewHolderEventos> {

    private int resource;
    private ArrayList<Evento> eventoLista;
    private Context context;
    private DatabaseReference ds;

    public EventoAdapter(int resource, ArrayList<Evento> eventoLista, Context context,
        DatabaseReference ds) {
        this.resource = resource;
        this.eventoLista = eventoLista;
        this.context = context;
        this.ds = ds;
    }

    @NonNull
    @Override
    public viewHolderEventos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new viewHolderEventos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final viewHolderEventos holder, int position) {
        Evento evento = eventoLista.get(position);
        final String idevento = evento.getId();
        holder.textView.setText(evento.getNombre());
        if (evento.getImgUrl() == null) {
            holder.imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.logompr));
        } else {
            try {
                ImageLoader imageLoader = new ImageLoader(context);
                imageLoader.setImgWithGlide(evento.getImgUrl(), holder.imageView);
            } catch (Exception ex) {
                Toast.makeText(context, "Ocurrio un error", Toast.LENGTH_SHORT).show();
                holder.imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.logompr));
            }

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), EventoAdd.class);
                    intent.putExtra("id", idevento);
                    v.getContext().startActivity(intent);
                }
            });

            holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    DialogAlertDelete dialogAlertDelete = new DialogAlertDelete(context,
                        "este evento", ds, evento.getId());
                    dialogAlertDelete.CreateDeleteDialog();
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return eventoLista.size();
    }

    class viewHolderEventos extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;
        CardView cardView;

        private viewHolderEventos(@NonNull View itemView) {
            super(itemView);

            this.imageView = itemView.findViewById(R.id.imageCardEvent);
            this.textView = itemView.findViewById(R.id.cardTextName);
            this.cardView = itemView.findViewById(R.id.cardEvent);

        }
    }

}
