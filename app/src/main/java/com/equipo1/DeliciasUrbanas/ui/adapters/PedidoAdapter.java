package com.equipo1.DeliciasUrbanas.ui.adapters;

import android.content.Context;
import android.icu.text.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.equipo1.DeliciasUrbanas.R;
import com.equipo1.DeliciasUrbanas.data.models.PedidoModel;
import com.equipo1.DeliciasUrbanas.data.models.Producto;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PedidoAdapter extends RecyclerView.Adapter<PedidoAdapter.ViewHolder> {
    Context context;
    List<PedidoModel> listaPedidos;
    private List<String> dataList;

    public PedidoAdapter(Context context, List<PedidoModel> listaPedidos) {
        this.context = context;
        this.listaPedidos = listaPedidos;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.pedido_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PedidoModel pedido = listaPedidos.get(position);
        String fechaCorta = formatDateAndTime(pedido.getFecha());
        String estado = pedido.getEstado();

        switch (estado){
            case "preparando":
                holder.estadoPedido.setTextColor(ContextCompat.getColor(context, R.color.estado_preaparando));
                break;
            case "listo":
                holder.estadoPedido.setTextColor(ContextCompat.getColor(context, R.color.estado_listo));
                break;
            case "finalizado":
                holder.estadoPedido.setTextColor(ContextCompat.getColor(context, R.color.estado_finalizado));
                break;
            default:
                break;
        }

        holder.fechaPedido.setText(fechaCorta);
        holder.importePedido.setText(String.valueOf(pedido.getImporteTotal()));
        holder.estadoPedido.setText(Character.toUpperCase(estado.charAt(0)) + estado.substring(1));

        switch (pedido.getEstado()){
            case "preparando":
                //holder.imagenEstadoPedido.setImageResource(R.drawable.preparando_animacion);
                Glide.with(holder.itemView.getContext()).asGif().load(R.drawable.pedido_preparando).into(holder.imagenEstadoPedido);
                break;
            case "listo":
                Glide.with(holder.itemView.getContext()).asGif().load(R.drawable.pedido_listo).into(holder.imagenEstadoPedido);
                break;
            case "finalizado":
                Glide.with(holder.itemView.getContext()).asGif().load(R.drawable.pedido_finalizado).into(holder.imagenEstadoPedido);
                break;
            default:
                break;
        }

        holder.cantidadPedido.setText(String.valueOf((int)pedido.getTotal()));

        holder.itemCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetDialog(pedido);
            }
        });
    }

    private void showBottomSheetDialog(PedidoModel dataPedido) {
        View bottomSheetView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_layout2, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(bottomSheetView);

        RecyclerView bottomSheetRecyclerView = bottomSheetView.findViewById(R.id.bottom_sheet_recycler_view);
        bottomSheetRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        List<Producto> detailDataList = dataPedido.getProductos();

        DetalleAdapter detailAdapter = new DetalleAdapter(detailDataList, context);
        bottomSheetRecyclerView.setAdapter(detailAdapter);

        bottomSheetDialog.show();
    }

    @Override
    public int getItemCount() {
        return listaPedidos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout itemCarrito;
        TextView fechaPedido, importePedido, cantidadPedido, estadoPedido;
        ImageView imagenEstadoPedido;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fechaPedido = itemView.findViewById(R.id.fecha_pedido);
            importePedido = itemView.findViewById(R.id.pedido_importe);
            cantidadPedido = itemView.findViewById(R.id.cantidad_pedido);
            imagenEstadoPedido = itemView.findViewById(R.id.imagen_estado_Pedido);
            estadoPedido = itemView.findViewById(R.id.estado_pedido);
            itemCarrito = itemView.findViewById(R.id.linear_item_carrito);
        }
    }

    public static String formatDateAndTime(Date date) {

        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.getDefault());
        return dateFormat.format(date);
    }
}
