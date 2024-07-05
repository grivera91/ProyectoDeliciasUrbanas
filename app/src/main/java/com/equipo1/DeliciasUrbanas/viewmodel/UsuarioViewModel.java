package com.equipo1.DeliciasUrbanas.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.equipo1.DeliciasUrbanas.data.models.Usuario;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class UsuarioViewModel extends ViewModel {
    private MutableLiveData<List<Usuario>> usuarios;
    private FirebaseFirestore db;

    public UsuarioViewModel() {
        usuarios = new MutableLiveData<>();
        db = FirebaseFirestore.getInstance();
        loadUsuarios();
    }

    public LiveData<List<Usuario>> getUsuarios() {
        return usuarios;
    }

    private void loadUsuarios() {
        db.collection("usuarios")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Usuario> tempList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Usuario usuario = document.toObject(Usuario.class);
                            usuario.setUsuarioId(document.getId());
                            tempList.add(usuario);
                        }
                        usuarios.setValue(tempList);
                    } else {
                        Log.w("Firestore", "Error al obtener documentos.", task.getException());
                    }
                });
    }

    public void eliminarUsuario(Usuario usuario) {
        db.collection("usuarios")
                .document(usuario.getUsuarioId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    List<Usuario> currentList = usuarios.getValue();
                    if (currentList != null) {
                        currentList.remove(usuario);
                        usuarios.setValue(new ArrayList<>(currentList));
                    }
                })
                .addOnFailureListener(e -> Log.w("Firestore", "Error al eliminar usuario", e));
    }
}
