package com.equipo1.DeliciasUrbanas.ui.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.equipo1.DeliciasUrbanas.R;
import com.equipo1.DeliciasUrbanas.data.models.Usuario;
import com.equipo1.DeliciasUrbanas.ui.adapters.UsuarioAdministradorAdapter;
import com.equipo1.DeliciasUrbanas.ui.viewmodel.UsuarioViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class AdministradorUsuarioActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    private UsuarioAdministradorAdapter adapter;
    private List<Usuario> usuarioList;
    private UsuarioViewModel usuarioViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrador_usuario);

        mAuth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.recyclerView_usuarios);
        usuarioList = new ArrayList<>();
        adapter = new UsuarioAdministradorAdapter(usuarioList, new UsuarioAdministradorAdapter.OnUsuarioClickListener() {
            @Override
            public void onEliminarClick(Usuario usuario) {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                String usuarioLogeado = currentUser.getUid();
                String usuarioEnviado = usuario.getUsuarioId();

                if (!usuarioLogeado.equals(usuarioEnviado)) {
                    usuarioViewModel.eliminarUsuario(usuario);
                } else {
                    Toast.makeText(AdministradorUsuarioActivity.this, "No se puede eliminar el usuario logeado.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        usuarioViewModel = new ViewModelProvider(this).get(UsuarioViewModel.class);
        usuarioViewModel.getUsuarios().observe(this, usuarios -> {
            usuarioList.clear();
            usuarioList.addAll(usuarios);
            adapter.notifyDataSetChanged();
        });
    }
}
