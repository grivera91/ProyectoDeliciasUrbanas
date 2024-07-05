package com.equipo1.DeliciasUrbanas.ui.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.equipo1.DeliciasUrbanas.R;
import com.equipo1.DeliciasUrbanas.ui.adapters.PedidoAdministradorAdapter;
import com.equipo1.DeliciasUrbanas.data.models.PedidoModel;
import com.equipo1.DeliciasUrbanas.ui.viewmodel.PedidoViewModel;

import java.util.ArrayList;
import java.util.List;

public class AdministradorPedidoActivity extends AppCompatActivity {

    private PedidoViewModel pedidoViewModel;
    private RecyclerView recyclerView;
    private PedidoAdministradorAdapter adapter;
    private List<PedidoModel> pedidoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrador_pedido);

        recyclerView = findViewById(R.id.recyclerView_pedidos);
        pedidoList = new ArrayList<>();
        adapter = new PedidoAdministradorAdapter(pedidoList, new PedidoAdministradorAdapter.OnPedidoClickListener() {
            @Override
            public void onPreparandoClick(PedidoModel pedido) {
                pedidoViewModel.cambiarEstadoPedido(pedido, "preparando");
            }

            @Override
            public void onListoClick(PedidoModel pedido) {
                pedidoViewModel.cambiarEstadoPedido(pedido, "listo");
            }

            @Override
            public void onFinalizadoClick(PedidoModel pedido) {
                pedidoViewModel.cambiarEstadoPedido(pedido, "finalizado");
            }

            @Override
            public void onEliminarClick(PedidoModel pedido) {
                pedidoViewModel.eliminarPedido(pedido);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        pedidoViewModel = new ViewModelProvider(this).get(PedidoViewModel.class);
        pedidoViewModel.getPedidos().observe(this, pedidos -> {
            pedidoList.clear();
            pedidoList.addAll(pedidos);
            adapter.notifyDataSetChanged();
        });
    }
}
