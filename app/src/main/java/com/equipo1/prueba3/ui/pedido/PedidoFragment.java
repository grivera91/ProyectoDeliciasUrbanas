package com.equipo1.prueba3.ui.pedido;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.equipo1.prueba3.R;
import com.equipo1.prueba3.adapters.PedidoAdapter;
import com.equipo1.prueba3.models.PedidoModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PedidoFragment extends Fragment {

    List<PedidoModel> listaPedidos;
    PedidoAdapter pedidoAdapter;
    RecyclerView recyclerView;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    TextView textViewPedidosTotal;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_pedido, container, false);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        textViewPedidosTotal = root.findViewById(R.id.pedidos_totales);
        recyclerView = root.findViewById(R.id.pedido_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        listaPedidos = new ArrayList<>();

        cargarPedido();

        return  root;
    }

    private void cargarPedido(){
        String usuarioId = currentUser.getUid();
        db = FirebaseFirestore.getInstance();
        db.collection("usuarios")
                .document(usuarioId)
                .collection("pedidos")
                .orderBy("fecha", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.w("Firestore", "Ha ocurrido un error en el Listener.", error);
                            return;
                        }

                        if (value != null) {
                            List<PedidoModel> pedidos = new ArrayList<>();
                            for (QueryDocumentSnapshot document : value) {
                                PedidoModel pedido = document.toObject(PedidoModel.class);
                                pedidos.add(pedido);
                            }

                            listaPedidos = pedidos;
                            textViewPedidosTotal.setText(String.valueOf(listaPedidos.size()));

                            pedidoAdapter = new PedidoAdapter(getContext(), listaPedidos);
                            recyclerView.setAdapter(pedidoAdapter);
                            pedidoAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}
