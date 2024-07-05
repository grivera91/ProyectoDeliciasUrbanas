package com.equipo1.DeliciasUrbanas.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.equipo1.DeliciasUrbanas.data.models.PedidoModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class PedidoViewModel extends ViewModel {
    private MutableLiveData<List<PedidoModel>> pedidos;
    private FirebaseFirestore db;

    public PedidoViewModel() {
        pedidos = new MutableLiveData<>();
        db = FirebaseFirestore.getInstance();
        loadPedidos();
    }

    public LiveData<List<PedidoModel>> getPedidos() {
        return pedidos;
    }

    private void loadPedidos() {
        db.collection("usuarios")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<PedidoModel> tempList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String usuarioId = document.getId();
                            String usuarioNombres = document.getString("nombres");
                            String usuarioApellidos = document.getString("apellidos");

                            db.collection("usuarios")
                                    .document(usuarioId)
                                    .collection("pedidos")
                                    .orderBy("fecha", Query.Direction.DESCENDING)
                                    .get()
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            for (QueryDocumentSnapshot pedidoDoc : task1.getResult()) {
                                                try {
                                                    PedidoModel pedido = pedidoDoc.toObject(PedidoModel.class);
                                                    pedido.setUsuarioId(usuarioId);
                                                    pedido.setUsuarioNombres(usuarioNombres);
                                                    pedido.setUsuarioApellidos(usuarioApellidos);
                                                    tempList.add(pedido);
                                                } catch (Exception e) {
                                                    Log.w("Firestore", "Error deserializar el Pedido", e);
                                                }
                                            }
                                            pedidos.setValue(tempList);
                                        } else {
                                            Log.w("Firestore", "Error al obtener documents.", task1.getException());
                                        }
                                    });
                        }
                    } else {
                        Log.w("Firestore", "Error al obtener documents.", task.getException());
                    }
                });
    }

    public void cambiarEstadoPedido(PedidoModel pedido, String nuevoEstado) {
        pedido.setEstado(nuevoEstado);
        db.collection("usuarios")
                .document(pedido.getUsuarioId())
                .collection("pedidos")
                .document(pedido.getPedidoId())
                .update("estado", nuevoEstado)
                .addOnSuccessListener(aVoid -> {
                    // Notify observers of the updated list
                    List<PedidoModel> currentList = pedidos.getValue();
                    if (currentList != null) {
                        pedidos.setValue(new ArrayList<>(currentList));
                    }
                })
                .addOnFailureListener(e -> Log.w("Firestore", "Error al actualizar pedido", e));
    }

    public void eliminarPedido(PedidoModel pedido) {
        db.collection("usuarios")
                .document(pedido.getUsuarioId())
                .collection("pedidos")
                .document(pedido.getPedidoId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    List<PedidoModel> currentList = pedidos.getValue();
                    if (currentList != null) {
                        currentList.remove(pedido);
                        pedidos.setValue(new ArrayList<>(currentList));
                    }
                })
                .addOnFailureListener(e -> Log.w("Firestore", "Error al eliminar pedido", e));
    }
}
