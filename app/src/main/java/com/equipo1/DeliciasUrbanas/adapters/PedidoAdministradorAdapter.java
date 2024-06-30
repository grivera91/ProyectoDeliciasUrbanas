package com.equipo1.DeliciasUrbanas.adapters;

import static com.equipo1.DeliciasUrbanas.adapters.PedidoAdapter.formatDateAndTime;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.content.ContextCompat;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.equipo1.DeliciasUrbanas.R;
import com.equipo1.DeliciasUrbanas.models.PedidoModel;

import java.util.List;

public class PedidoAdministradorAdapter extends RecyclerView.Adapter<PedidoAdministradorAdapter.PedidoAdministradorViewHolder> {
    private List<PedidoModel> pedidos;
    private OnPedidoClickListener listener;
    Context context;

    public PedidoAdministradorAdapter(List<PedidoModel> pedidos, OnPedidoClickListener listener, Context context) {
        this.pedidos = pedidos;
        this.listener = listener;
        this.context = context;
    }
    public PedidoAdministradorAdapter(List<PedidoModel> pedidos, OnPedidoClickListener listener) {
        this.pedidos = pedidos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PedidoAdministradorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pedido_administrador, parent, false);
        context = view.getContext();
        return new PedidoAdministradorViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull PedidoAdministradorViewHolder holder, int position) {
        PedidoModel pedido = pedidos.get(position);
        String nombreCompleto = pedido.getUsuarioNombres() + " " + pedido.getUsuarioApellidos();
        String fechaCorta = formatDateAndTime(pedido.getFecha());


        holder.tvNombreUsuario.setText(nombreCompleto);
        holder.tvFecha.setText(fechaCorta);
        holder.tvEstado.setText(pedidos.get(position).getEstado());

        switch (pedido.getEstado()){
            case "preparando":
                Glide.with(holder.itemView.getContext()).asGif().load(R.drawable.pedido_preparando).into(holder.ivPedidoImagen);
                holder.tvEstado.setTextColor(ContextCompat.getColor(context, R.color.estado_preaparando));
                break;
            case "listo":
                Glide.with(holder.itemView.getContext()).asGif().load(R.drawable.pedido_listo).into(holder.ivPedidoImagen);
                holder.tvEstado.setTextColor(ContextCompat.getColor(context, R.color.estado_listo));
                break;
            case "finalizado":
                Glide.with(holder.itemView.getContext()).asGif().load(R.drawable.pedido_finalizado).into(holder.ivPedidoImagen);
                holder.tvEstado.setTextColor(ContextCompat.getColor(context, R.color.estado_finalizado));
                break;
            default:
                // Handle unexpected state
                holder.ivPedidoImagen.setImageResource(R.drawable.baseline_fastfood_24);
                holder.tvEstado.setTextColor(Color.BLACK);
                break;
        }
        holder.bind(pedido, listener);
    }

    @Override
    public int getItemCount() {
        return pedidos.size();
    }

    public static class PedidoAdministradorViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNombreUsuario, tvFecha, tvEstado;
        private Button btnPreparando, btnListo, btnFinalizado;
        private ImageView ivPedidoImagen;

        public PedidoAdministradorViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreUsuario = itemView.findViewById(R.id.tvNombreUsuario);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvEstado = itemView.findViewById(R.id.tvEstado);
            ivPedidoImagen = itemView.findViewById(R.id.ivPedidoImagen);
            btnPreparando = itemView.findViewById(R.id.btnPreparando);
            btnListo = itemView.findViewById(R.id.btnListo);
            btnFinalizado = itemView.findViewById(R.id.btnFinalizado);
        }

        public void bind(final PedidoModel pedido, final OnPedidoClickListener listener) {
            tvNombreUsuario.setText(pedido.getUsuarioNombres() + " " + pedido.getUsuarioApellidos());
            tvFecha.setText(pedido.getFecha().toString());
            tvEstado.setText(pedido.getEstado());
            btnPreparando.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onPreparandoClick(pedido);
                }
            });
            btnListo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onListoClick(pedido);
                }
            });
            btnFinalizado.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onFinalizadoClick(pedido);
                }
            });
        }
    }

    public interface OnPedidoClickListener {
        void onPreparandoClick(PedidoModel pedido);
        void onListoClick(PedidoModel pedido);
        void onFinalizadoClick(PedidoModel pedido);
    }
}