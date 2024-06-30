package com.equipo1.DeliciasUrbanas.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.equipo1.DeliciasUrbanas.R;
import com.equipo1.DeliciasUrbanas.models.Producto;
import com.equipo1.DeliciasUrbanas.models.HomeVerModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class HomeVerAdapter extends RecyclerView.Adapter<HomeVerAdapter.ViewHolder>{

    private BottomSheetDialog bottomSheetDialog;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    Context context;
    ArrayList<HomeVerModel> list;
    private FirebaseFirestore db;
    String colleccionUsuarios, collecionCarrito;

    public HomeVerAdapter(Context context, ArrayList<HomeVerModel> list) {
        this.context = context;
        this.list = list;
        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.home_vertical_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final String mName = list.get(position).getName();
        final String mTiming = list.get(position).getTiming();
        final String mRating = list.get(position).getRating();
        final String mPrice = list.get(position).getPrice();
        final int mImage = list.get(position).getImage();

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        holder.imageView.setImageResource(list.get(position).getImage());
        holder.name.setText(list.get(position).getName());
        holder.timing.setText(list.get(position).getTiming());
        holder.rating.setText(list.get(position).getRating());
        holder.price.setText(list.get(position).getPrice());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetTheme);

                View sheetView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_layout, null);
                sheetView.findViewById(R.id.add_to_cart).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Producto producto = new Producto();
                        producto.setNombre(mName);
                        producto.setCantidad(1);
                        producto.setImagen(mImage);
                        producto.setPrecio(Double.parseDouble(mPrice));

                        agregarAlCarrito(producto);
                        bottomSheetDialog.dismiss();
                    }
                });

                ImageView bottomImg =  sheetView.findViewById(R.id.bottom_img);
                TextView bottomName =  sheetView.findViewById(R.id.bottom_name);
                TextView bottomPrice =  sheetView.findViewById(R.id.bottom_price);
                TextView bottomRating =  sheetView.findViewById(R.id.bottom_rating);

                bottomImg.setImageResource(mImage);
                bottomName.setText(mName);
                bottomPrice.setText(mPrice);
                bottomRating.setText(mRating);

                bottomSheetDialog.setContentView(sheetView);
                bottomSheetDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name, timing, rating, price;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.ver_img);
            name = itemView.findViewById(R.id.name);
            timing = itemView.findViewById(R.id.timing);
            rating = itemView.findViewById(R.id.rating);
            price = itemView.findViewById(R.id.price);
        }
    }

    private void agregarAlCarrito(Producto producto)
    {
        mUser = mAuth.getCurrentUser();
        if (mUser != null) {
            colleccionUsuarios = "usuarios";
            collecionCarrito = "carrito";

            FirebaseUser currentUser = mAuth.getCurrentUser();
            String productId = producto.getNombre();
            db.collection(colleccionUsuarios)
                    .document(currentUser.getUid())
                    .collection(collecionCarrito)
                    .document(productId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            // El producto existe, actualizar la cantidad y el precio total
                            Producto productoExistente = documentSnapshot.toObject(Producto.class);
                            int nuevaCantidad = productoExistente.getCantidad() + producto.getCantidad();
                            double nuevoPrecioTotal = nuevaCantidad * producto.getPrecio();

                            productoExistente.setCantidad(nuevaCantidad);
                            productoExistente.setPrecio(nuevoPrecioTotal);

                            db.collection(colleccionUsuarios)
                                    .document(currentUser.getUid())
                                    .collection(collecionCarrito)
                                    .document(productId)
                                    .set(productoExistente)
                                    .addOnSuccessListener(aVoid -> Toast.makeText(context, "Carrito actualizado", Toast.LENGTH_SHORT).show())
                                    .addOnFailureListener(e -> Toast.makeText(context, "Error al actualizar producto", Toast.LENGTH_SHORT).show());
                        } else {
                            // El producto no existe, calcular el precio total y agregarlo nuevo
                            double precioTotalInicial = producto.getCantidad() * producto.getPrecio(); // Precio total basado en la cantidad inicial
                            producto.setPrecio(precioTotalInicial); // Ajustamos el precio total en el objeto producto

                            db.collection(colleccionUsuarios)
                                    .document(currentUser.getUid())
                                    .collection(collecionCarrito)
                                    .document(productId)
                                    .set(producto)
                                    .addOnSuccessListener(aVoid -> Toast.makeText(context, "Producto añadido al carrito", Toast.LENGTH_SHORT).show())
                                    .addOnFailureListener(e -> Toast.makeText(context, "Error al agregar producto", Toast.LENGTH_SHORT).show());
                        }
                    })
                    .addOnFailureListener(e -> Toast.makeText(context, "Error al acceder al carrito", Toast.LENGTH_SHORT).show());
        }
    }
}
