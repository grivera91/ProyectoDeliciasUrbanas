package com.equipo1.DeliciasUrbanas.ui.activities;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.equipo1.DeliciasUrbanas.R;
import com.equipo1.DeliciasUrbanas.ui.adapters.PedidoAdministradorAdapter;
import com.equipo1.DeliciasUrbanas.data.models.PedidoModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdministradorPedidoActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private PedidoAdministradorAdapter adapter;
    private List<PedidoModel> pedidoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrador_pedido);

        db = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.recyclerView_pedidos);
        pedidoList = new ArrayList<>();
        adapter = new PedidoAdministradorAdapter(pedidoList, new PedidoAdministradorAdapter.OnPedidoClickListener() {
            @Override
            public void onPreparandoClick(PedidoModel pedido) {
                cambiarEstadoPedido(pedido, "preparando");
            }

            @Override
            public void onListoClick(PedidoModel pedido) {
                cambiarEstadoPedido(pedido, "listo");
            }

            @Override
            public void onFinalizadoClick(PedidoModel pedido) {
                cambiarEstadoPedido(pedido, "finalizado");
            }

            @Override
            public void onEliminarClick(PedidoModel pedido) {
                eliminarPedido(pedido);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        obtenerPedidos();
    }

    private void obtenerPedidos() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("usuarios")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String usuarioId = document.getId();
                                String usuarioNombres = document.getString("nombres");
                                String usuarioApellidos = document.getString("apellidos");

                                db.collection("usuarios")
                                        .document(usuarioId)
                                        .collection("pedidos")
                                        .orderBy("fecha", Query.Direction.DESCENDING)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot pedidoDoc : task.getResult()) {
                                                        try {
                                                            PedidoModel pedido = pedidoDoc.toObject(PedidoModel.class);
                                                            pedido.setUsuarioId(usuarioId);
                                                            pedido.setUsuarioNombres(usuarioNombres);
                                                            pedido.setUsuarioApellidos(usuarioApellidos);
                                                            pedidoList.add(pedido);
                                                        } catch (Exception e) {
                                                            Log.w("Firestore", "Error deserializar el Pedido", e);
                                                        }
                                                    }
                                                    adapter.notifyDataSetChanged();
                                                } else {
                                                    Log.w("Firestore", "Error al obtener documentoss.", task.getException());
                                                }
                                            }
                                        });
                            }
                        } else {
                            Log.w("Firestore", "Error al obtener documents.", task.getException());
                        }
                    }
                });
    }

    private void cambiarEstadoPedido(PedidoModel pedido, String nuevoEstado) {
        pedido.setEstado(nuevoEstado);
        DocumentReference pedidoRef = db.collection("usuarios")
                .document(pedido.getUsuarioId())
                .collection("pedidos")
                .document(pedido.getPedidoId());

        pedidoRef.update("estado", nuevoEstado)
                .addOnSuccessListener(aVoid -> {
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Log.w(TAG, "Error al actualizar pedido", e));

        adapter.notifyDataSetChanged();
    }

    private void eliminarPedido(PedidoModel pedido) {
        DocumentReference pedidoRef = db.collection("usuarios")
                .document(pedido.getUsuarioId())
                .collection("pedidos")
                .document(pedido.getPedidoId());

        pedidoRef.delete()
                .addOnSuccessListener(aVoid -> {
                    pedidoList.remove(pedido);
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Log.w(TAG, "Error al eliminar pedido", e));
    }
}
