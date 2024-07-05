package com.equipo1.DeliciasUrbanas.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.equipo1.DeliciasUrbanas.data.models.PedidoModel;
import com.equipo1.DeliciasUrbanas.data.models.Producto;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CartViewModel extends ViewModel {
    private MutableLiveData<List<Producto>> _productos;
    private MutableLiveData<Double> _total;

    public CartViewModel() {
        _productos = new MutableLiveData<>();
        _total = new MutableLiveData<>(0.0);
    }

    public LiveData<List<Producto>> getProductos() {
        return _productos;
    }

    public LiveData<Double> getTotal() {
        return _total;
    }

    public void cargarCarrito() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("usuarios")
                    .document(currentUser.getUid())
                    .collection("carrito")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            List<Producto> carrito = new ArrayList<>();
                            double importeTotalPedido = 0.0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Producto producto = document.toObject(Producto.class);
                                importeTotalPedido += producto.getPrecio();
                                carrito.add(producto);
                            }
                            _productos.setValue(carrito);
                            _total.setValue(importeTotalPedido);
                        }
                    });
        }
    }

    public void eliminarProductoDeFirestore(Producto producto, int position) {
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
                        List<Producto> currentList = _productos.getValue();
                        if (currentList != null) {
                            currentList.remove(position);
                            _productos.setValue(currentList);
                            double nuevoTotal = 0.0;
                            for (Producto p : currentList) {
                                nuevoTotal += p.getPrecio();
                            }
                            _total.setValue(nuevoTotal);
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Maneja el error
                    });
        }
    }

    public void registrarPedido(FirebaseUser currentUser, List<Producto> list, double importeTotalPedido) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String pedidoId = db.collection("usuarios").document().getId(); // Genera un ID único
        PedidoModel pedido = new PedidoModel(pedidoId, new Date(), list.size(), "preparando", list, importeTotalPedido);

        db.collection("usuarios")
                .document(currentUser.getUid())
                .collection("pedidos")
                .document(pedidoId)
                .set(pedido)
                .addOnSuccessListener(aVoid -> {
                    for (Producto producto : list) {
                        db.collection("usuarios")
                                .document(currentUser.getUid())
                                .collection("pedidos")
                                .document(pedidoId)
                                .collection("productos")
                                .document(producto.getNombre())
                                .set(producto);
                    }
                    // Aquí puedes agregar una callback para actualizar la UI o mostrar un mensaje
                })
                .addOnFailureListener(e -> {
                    // Maneja el error
                });
    }

    public void limpiarCarrito(FirebaseUser currentUser) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("usuarios")
                .document(currentUser.getUid())
                .collection("carrito")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Producto producto = document.toObject(Producto.class);
                            db.collection("usuarios")
                                    .document(currentUser.getUid())
                                    .collection("carrito")
                                    .document(producto.getNombre())
                                    .delete();
                        }
                        _productos.setValue(new ArrayList<>());
                        _total.setValue(0.0);
                    }
                });
    }

    public void actualizarTotal(double nuevoTotal) {
        _total.setValue(nuevoTotal);
    }
}
