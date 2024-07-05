package com.equipo1.DeliciasUrbanas.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.equipo1.DeliciasUrbanas.R;
import com.equipo1.DeliciasUrbanas.data.models.Usuario;

import java.util.List;

public class UsuarioAdministradorAdapter extends RecyclerView.Adapter<UsuarioAdministradorAdapter.UsuarioAdministradorViewHolder> {
    private List<Usuario> usuarios;
    private OnUsuarioClickListener listener;
    Context context;

    public UsuarioAdministradorAdapter(List<Usuario> usuarios, OnUsuarioClickListener listener, Context context) {
        this.usuarios = usuarios;
        this.listener = listener;
        this.context = context;
    }

    public UsuarioAdministradorAdapter(List<Usuario> usuarios, OnUsuarioClickListener listener) {
        this.usuarios = usuarios;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UsuarioAdministradorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_usuario_administrador, parent, false);
        context = view.getContext();
        return new UsuarioAdministradorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuarioAdministradorViewHolder holder, int position) {
        Usuario usuario = usuarios.get(position);
        String nombreCompleto = usuario.getNombres() + " " + usuario.getApellidos();

        holder.tvNombreUsuario.setText(nombreCompleto);
        holder.tvCorreoUsuario.setText(usuarios.get(position).getCorreo());

        switch (usuario.getGenero()) {
            case "Masculino":
                holder.ivUsuarioImagen.setImageResource(R.drawable.man);
                break;
            case "Femenino":
                holder.ivUsuarioImagen.setImageResource(R.drawable.woman);
                break;
            default:
                holder.ivUsuarioImagen.setImageResource(R.drawable.usuario_neutro);
                break;
        }
        holder.bind(usuario, listener);
    }

    @Override
    public int getItemCount() {
        return usuarios.size();
    }

    public static class UsuarioAdministradorViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNombreUsuario, tvCorreoUsuario;
        private Button btnEliminar;
        private ImageView ivUsuarioImagen;

        public UsuarioAdministradorViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreUsuario = itemView.findViewById(R.id.tvNombreUsuario_usuario);
            tvCorreoUsuario = itemView.findViewById(R.id.tvCorreoUsuario_usuario);
            ivUsuarioImagen = itemView.findViewById(R.id.ivUsuarioImagen);
            btnEliminar = itemView.findViewById(R.id.btnEliminar_usuario);
        }

        public void bind(final Usuario usuario, final OnUsuarioClickListener listener) {
            tvNombreUsuario.setText(usuario.getNombres() + " " + usuario.getApellidos());
            tvCorreoUsuario.setText(usuario.getCorreo());
            btnEliminar.setOnClickListener(v -> listener.onEliminarClick(usuario));
        }
    }

    public interface OnUsuarioClickListener {
        void onEliminarClick(Usuario usuario);
    }
}
