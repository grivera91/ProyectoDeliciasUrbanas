package com.equipo1.DeliciasUrbanas.activities;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.equipo1.DeliciasUrbanas.MainActivity;
import com.equipo1.DeliciasUrbanas.R;
import com.equipo1.DeliciasUrbanas.adapters.UsuarioAdministradorAdapter;
import com.equipo1.DeliciasUrbanas.models.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdministradorUsuarioActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private UsuarioAdministradorAdapter adapter;
    private List<Usuario> usuarioList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrador_usuario);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.recyclerView_usuarios);
        usuarioList = new ArrayList<>();
        adapter = new UsuarioAdministradorAdapter(usuarioList, new UsuarioAdministradorAdapter.OnUsuarioClickListener() {
            @Override
            public void onEliminarClick(Usuario usuario) {

                FirebaseUser currentUser = mAuth.getCurrentUser();
                String usuarioLogeado = currentUser.getUid();
                String usuarioEnviado = usuario.getUsuarioId();

                if(!usuarioLogeado.equals(usuarioEnviado)){
                    eliminarUsuario(usuario);
                }else{
                    Toast.makeText(AdministradorUsuarioActivity.this, "No se puede eliminar el usuario logeado.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        obtenerUsuarios();
    }

    private void obtenerUsuarios(){
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
                                String usuarioCorreo = document.getString("correo");

                                Usuario usuario = document.toObject(Usuario.class);
                                usuario.setUsuarioId(usuarioId);
                                usuario.setNombres(usuarioNombres);
                                usuario.setApellidos(usuarioApellidos);
                                usuario.setCorreo(usuarioCorreo);
                                usuarioList.add(usuario);
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.w("Firestore", "Error al obtener documents.", task.getException());
                        }
                    }
                });
    }

    private void eliminarUsuario(Usuario usuario){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String usuarioLogeado = currentUser.getUid();
        String usuarioEnviado = usuario.getUsuarioId();

        if (!usuarioLogeado.equals(usuarioEnviado)) {
            DocumentReference usuariooRef = db.collection("usuarios")
                    .document(usuario.getUsuarioId());

            usuariooRef.delete()
                    .addOnSuccessListener(aVoid -> {
                        usuarioList.remove(usuario);
                        adapter.notifyDataSetChanged();
                    })
                    .addOnFailureListener(e -> Log.w(TAG, "Error al eliminar pedido", e));
        }
    }
}