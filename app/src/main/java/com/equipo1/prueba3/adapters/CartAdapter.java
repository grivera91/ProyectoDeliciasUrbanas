package com.equipo1.prueba3.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.equipo1.prueba3.R;
import com.equipo1.prueba3.models.Producto;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    Context context;
    List<Producto> list;
    public CartAdapterCallback callback;

    public CartAdapter(Context context, List<Producto> list) {
        this.context = context;
        this.list = list;
    }

    public CartAdapter(Context context, List<Producto> list, CartAdapterCallback callback) {
        this.context = context;
        this.list = list;
        this.callback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.mycart_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Producto producto = list.get(position);
        holder.imageView.setImageResource(list.get(position).getImagen());
        holder.name.setText(list.get(position).getNombre());
        holder.price.setText(String.valueOf(producto.getPrecio()));
        holder.cantidad.setText(String.valueOf(producto.getCantidad()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Confirmar eliminación");
                builder.setMessage("¿Estás seguro de que deseas eliminar el producto del carrito?");
                builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        eliminarProductoDeFirestore(producto, position);
                    }
                });
                builder.setNegativeButton("Cancelar", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name, rating, price, cantidad;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.detailed_img);
            name = itemView.findViewById(R.id.detailed_name);
            rating = itemView.findViewById(R.id.detailed_rating);
            price = itemView.findViewById(R.id.textView12);
            cantidad = itemView.findViewById(R.id.textView_cantidad_carrito);
        }
    }
    private void eliminarProductoDeFirestore(Producto producto, int position) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("usuarios")
                    .document(currentUser.getUid())
                    .collection("carrito")
                    .document(producto.getNombre())
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        list.remove(position);
                        notifyItemRemoved(position);
                        double nuevoTotal = 0;
                        for (Producto p : list) {
                            nuevoTotal = nuevoTotal + p.getPrecio();
                        }
                        callback.onProductoEliminado(nuevoTotal);
                        Toast.makeText(context, "Producto eliminado del carrito", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> Toast.makeText(context, "Error al eliminar el producto", Toast.LENGTH_SHORT).show());
        }
    }
    public interface CartAdapterCallback {
        void onProductoEliminado(double nuevoTotal);
    }
}