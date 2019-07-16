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
import com.mpreventos.admin.controller.ProductoAdd;
import com.mpreventos.admin.model.Producto;
import com.mpreventos.admin.utils.ImageLoader;
import java.util.ArrayList;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.viewHolderProducto> {

  private int resource;
  private ArrayList<Producto> productoLista;
  private Context context;

  public ProductoAdapter(int resource, ArrayList<Producto> productoLista, Context context) {
    this.resource = resource;
    this.productoLista = productoLista;
    this.context = context;
  }

  @NonNull
  @Override
  public viewHolderProducto onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
    return new viewHolderProducto(view);
  }

  @Override
  public void onBindViewHolder(@NonNull viewHolderProducto holder, int position) {
    Producto producto = productoLista.get(position);
    final String idproducto = producto.getId();
    holder.textView.setText(producto.getNombre());
    if (producto.getImgUrl() == null) {
      holder.imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.logompr));
    } else {

      try {
        ImageLoader imageLoader = new ImageLoader(context);
        imageLoader.setImgWithGlide(producto.getImgUrl(), holder.imageView);
      } catch (Exception ex) {
        Toast.makeText(context, "Ocurrio un error", Toast.LENGTH_SHORT).show();
      }

      holder.cardView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          Intent intent = new Intent(v.getContext(), ProductoAdd.class);
          intent.putExtra("id", idproducto);
          v.getContext().startActivity(intent);
        }
      });
    }
  }

  @Override
  public int getItemCount() {
    return productoLista.size();
  }

  class viewHolderProducto extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView textView;
    CardView cardView;

    private viewHolderProducto(@NonNull View itemView) {
      super(itemView);

      this.imageView = itemView.findViewById(R.id.imageCardProducto);
      this.textView = itemView.findViewById(R.id.cardTextNameProducto);
      this.cardView = itemView.findViewById(R.id.cardProducto);
    }
  }
}
