package com.equipo1.DeliciasUrbanas.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.equipo1.DeliciasUrbanas.R;
import com.equipo1.DeliciasUrbanas.models.Producto;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

public class DetalleAdapter extends RecyclerView.Adapter<DetalleAdapter.DetailViewHolder>{
    private List<Producto> detailDataList;
    private BottomSheetDialog bottomSheetDialog;
    Context context;

    public DetalleAdapter(List<Producto> detailDataList, Context context) {
        this.detailDataList = detailDataList;
        this.context = context;;
    }

    @NonNull
    @Override
    public DetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_item_layout, parent, false);
        return new DetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailViewHolder holder, int position) {
        Producto detailData = detailDataList.get(position);

        holder.detalleNombreProducto.setText(detailData.getNombre());
        holder.detallePrecioProducto.setText(String.valueOf(detailData.getPrecio()));
        holder.detalleImagenProducto.setImageResource(detailDataList.get(position).getImagen());
    }

    @Override
    public int getItemCount() {
        return detailDataList.size();
    }

    public static class DetailViewHolder extends RecyclerView.ViewHolder {
        TextView detalleNombreProducto, detallePrecioProducto, detalleDescripcionProducto;
        ImageView detalleImagenProducto;

        public DetailViewHolder(@NonNull View itemView) {
            super(itemView);
            detalleNombreProducto = itemView.findViewById(R.id.detalle_nombre_producto);
            detallePrecioProducto = itemView.findViewById(R.id.detalle_precio_producto);
            detalleImagenProducto = itemView.findViewById(R.id.detalle_imagen_producto);
        }
    }
}
