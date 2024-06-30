package com.equipo1.DeliciasUrbanas.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.equipo1.DeliciasUrbanas.R;
import com.equipo1.DeliciasUrbanas.models.DetailedDailyModel;
import com.equipo1.DeliciasUrbanas.models.Producto;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class DetailedDailyAdapter extends RecyclerView.Adapter<DetailedDailyAdapter.ViewHolder> {

    List<DetailedDailyModel> list;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseFirestore db;
    private BottomSheetDialog bottomSheetDialog;
    Context context;
    String colleccionUsuarios, collecionCarrito;

    public DetailedDailyAdapter(List<DetailedDailyModel> list, Context context) {
        this.list = list;
        this.context = context;
        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.detailed_daily_meal_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        final String mName = list.get(position).getName();
        final String mPrice = list.get(position).getPrice();
        final String mRating = list.get(position).getRating();
        final String mDescription = list.get(position).getDescripcion();
        final String mTiming = list.get(position).getTiming();
        final int mImage = list.get(position).getImage();

        holder.imageView.setImageResource(list.get(position).getImage());
        holder.price.setText(list.get(position).getPrice());
        holder.name.setText(list.get(position).getName());
        holder.description.setText(list.get(position).getDescripcion());
        holder.timing.setText(list.get(position).getTiming());
        holder.rating.setText(list.get(position).getRating());

        holder.botonAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetTheme);

                View sheetView = LayoutInflater.from(context).inflate(R.layout.detailed_daily_meal_item, null);
                sheetView.findViewById(R.id.button_agregar_carrito).setOnClickListener(new View.OnClickListener() {
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

                ImageView bottomImg =  sheetView.findViewById(R.id.detailed_img);
                TextView bottomName =  sheetView.findViewById(R.id.detailed_name);
                TextView bottomPrice =  sheetView.findViewById(R.id.detailed_price);
                TextView bottomRating =  sheetView.findViewById(R.id.detailed_rating);
                TextView bottomDesc =  sheetView.findViewById(R.id.detailed_des);
                TextView bottomTiming =  sheetView.findViewById(R.id.detailed_timing);

                bottomImg.setImageResource(mImage);
                bottomName.setText(mName);
                bottomPrice.setText(mPrice);
                bottomRating.setText(mRating);
                bottomDesc.setText(mDescription);
                bottomTiming.setText(mTiming);

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
        TextView name, price, description, rating, timing;
        Button botonAgregar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.detailed_img);
            name = itemView.findViewById(R.id.detailed_name);
            price = itemView.findViewById(R.id.detailed_price);
            description = itemView.findViewById(R.id.detailed_des);
            rating = itemView.findViewById(R.id.detailed_rating);
            timing = itemView.findViewById(R.id.detailed_timing);
            botonAgregar = itemView.findViewById(R.id.button_agregar_carrito);
        }
    }
    private void agregarAlCarrito(Producto producto)
    {
        mUser = mAuth.getCurrentUser();
        if(mUser != null){

            colleccionUsuarios = "usuarios";
            collecionCarrito = "carrito";

            FirebaseUser currentUser = mAuth.getCurrentUser();
            db.collection(colleccionUsuarios)
                    .document(currentUser.getUid())
                    .collection(collecionCarrito)
                    .document(producto.getNombre()).set(producto)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(context,"Añadido al carrito",Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context,"Error al agregar producto",Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
