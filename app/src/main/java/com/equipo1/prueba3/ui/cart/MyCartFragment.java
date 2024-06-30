package com.equipo1.prueba3.ui.cart;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.equipo1.prueba3.R;
import com.equipo1.prueba3.adapters.CartAdapter;
import com.equipo1.prueba3.models.PedidoModel;
import com.equipo1.prueba3.models.Producto;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyCartFragment extends Fragment implements CartAdapter.CartAdapterCallback{
    List<Producto> list;
    CartAdapter cartAdapter;
    RecyclerView recyclerView;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    TextView importeTotal;
    Button realizarPedido;
    double importeTotalPedido;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_my_cart, container, false);


        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        importeTotal = root.findViewById(R.id.textViewPrecioTotal);
        realizarPedido = root.findViewById(R.id.buttonRealizarPedido);

        recyclerView = root.findViewById(R.id.cart_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        list = new ArrayList<>();

        cargarCarrito();

        realizarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarPedido();
                limpiarCarrito();
            }
        });

        return  root;
    }

    private void cargarCarrito(){
        String usuarioId = currentUser.getUid();
        db = FirebaseFirestore.getInstance();
        db.collection("usuarios")
                .document(usuarioId)
                .collection("carrito")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            importeTotalPedido = 0;

                            List<Producto> carrito = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Producto producto = document.toObject(Producto.class);
                                importeTotalPedido = importeTotalPedido + producto.getPrecio();
                                carrito.add(producto);
                            }

                            list = carrito;
                            importeTotal.setText(String.valueOf(importeTotalPedido));

                            cartAdapter = new CartAdapter(getContext(),list,MyCartFragment.this);
                            recyclerView.setAdapter(cartAdapter);
                            cartAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    private void registrarPedido(){

        String pedidoId = db.collection("usuarios").document().getId(); // Genera un ID único
        PedidoModel pedido = new PedidoModel(pedidoId, new Date(), list.size(), "preparando", list, importeTotalPedido);

        db.collection("usuarios")
                .document(currentUser.getUid())
                .collection("pedidos")
                .document(pedidoId)
                .set(pedido)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        for (Producto producto : list) {
                            db.collection("usuarios")
                                    .document(currentUser.getUid())
                                    .collection("pedidos")
                                    .document(pedidoId)
                                    .collection("productos")
                                    .document(producto.getNombre())
                                    .set(producto);
                        }
                        Toast.makeText(getContext(), "Pedido registrado correctamente", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (getContext() != null) {
                            Toast.makeText(getContext(), "Error al registrar pedido: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void limpiarCarrito(){
        db.collection("usuarios")
                .document(currentUser.getUid())
                .collection("carrito")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Producto producto = document.toObject(Producto.class);
                                String nombreProducto = producto.getNombre();
                                db.collection("usuarios")
                                        .document(currentUser.getUid())
                                        .collection("carrito")
                                        .document(producto.getNombre())
                                        .delete();

                            }

                            List<Producto> carrito = new ArrayList<>();
                            importeTotal.setText(String.valueOf(0));
                            list = carrito;
                            cartAdapter = new CartAdapter(getContext(),list);
                            recyclerView.setAdapter(cartAdapter);
                            cartAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }
    @Override
    public void onProductoEliminado(double nuevoTotal) {
        String total = String.valueOf(nuevoTotal);
        importeTotal.setText(String.valueOf(nuevoTotal));
    }
}