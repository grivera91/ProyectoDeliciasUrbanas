package com.equipo1.DeliciasUrbanas.ui.profile;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.equipo1.DeliciasUrbanas.R;
import com.equipo1.DeliciasUrbanas.activities.LocationActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class ProfileFragment extends Fragment {

    private static final int REQUEST_CODE_LOCATION = 1;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    private EditText usuarioFechaNacimiento;
    private TextView usuarioNombres, usuarioApellidos, usuarioCorreo, usuadioDNI, usuarioTelefono, usuarioDireccion;
    private Spinner comboSpinner;
    private Button buttonAccion;
    private ImageButton buttonGetAddress;
    String usuarioGenero;
    int selectedItemPosition;
    ImageView perfilUsuario;
    Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        context = root.getContext();

        perfilUsuario = root.findViewById(R.id.imageView_imagen_perfil);
        usuarioNombres = root.findViewById(R.id.editText_nombres);
        usuarioApellidos = root.findViewById(R.id.editText_apellidos);
        usuarioCorreo = root.findViewById(R.id.editText_correo);
        usuadioDNI = root.findViewById(R.id.editText_dni);
        usuarioFechaNacimiento = root.findViewById(R.id.editText_fecha_nacimiento);
        comboSpinner = root.findViewById(R.id.spinner_genero);
        usuarioDireccion = root.findViewById(R.id.editText_direccion);
        usuarioTelefono = root.findViewById(R.id.editText_telefono);
        buttonAccion = root.findViewById(R.id.buttom_editar_perfil);
        buttonGetAddress = root.findViewById(R.id.btnGetAddress);

        cargarDatos();

        usuarioFechaNacimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.combo_genero, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        comboSpinner.setAdapter(adapter);

        comboSpinner.setSelection(0, false);

        comboSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    usuarioGenero = parent.getItemAtPosition(position).toString();
                    selectedItemPosition = comboSpinner.getSelectedItemPosition();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getActivity(), "Debe elegir un valor en el combo genero", Toast.LENGTH_SHORT).show();
            }
        });

        buttonAccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarCambios();
                cargarDatos();
            }
        });

        buttonGetAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LocationActivity.class);
                intent.putExtra("address", usuarioDireccion.getText().toString());
                startActivityForResult(intent, REQUEST_CODE_LOCATION);
            }
        });

        return root;
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                (view, year1, month1, dayOfMonth) -> usuarioFechaNacimiento.setText(String.format("%d/%d/%d", dayOfMonth, month1 + 1, year1)),
                year, month, day);

        datePickerDialog.show();
    }

    private void cargarDatos() {
        String usuarioId = currentUser.getUid();
        db = FirebaseFirestore.getInstance();
        db.collection("usuarios")
                .document(usuarioId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            usuarioNombres.setText(document.getString("nombres"));
                            usuarioApellidos.setText(document.getString("apellidos"));
                            usuarioCorreo.setText(document.getString("correo"));
                            usuadioDNI.setText(document.getString("dni"));
                            usuarioFechaNacimiento.setText(document.getString("fechaNacimiento"));
                            usuarioTelefono.setText(document.getString("telefono"));
                            usuarioGenero = document.getString("genero");
                            usuarioDireccion.setText(document.getString("direccion"));

                            if (usuarioGenero != null) {
                                selectItemByText(usuarioGenero);
                                asignarImagen(usuarioGenero);
                            }
                        }
                    }
                });
    }

    private void guardarCambios() {

        String usuarioId = currentUser.getUid();

        if (selectedItemPosition == 0) {
            Toast.makeText(getActivity(), "Debe elegir un valor en el combo genero", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("usuarios").document(usuarioId)
                .update(
                        "fechaNacimiento", usuarioFechaNacimiento.getText().toString(),
                        "telefono", usuarioTelefono.getText().toString(),
                        "genero", usuarioGenero,
                        "direccion", usuarioDireccion.getText().toString()
                )
                .addOnSuccessListener(aVoid -> Toast.makeText(getActivity(), "Perfil actualizado correctamente.", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(getActivity(), "Ocurrio un error al guardar.", Toast.LENGTH_SHORT).show());
    }

    @SuppressWarnings("unchecked")
    private void selectItemByText(String textToSelect) {
        ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) comboSpinner.getAdapter();
        int position = adapter.getPosition(textToSelect);
        if (position >= 0) {
            comboSpinner.setSelection(position);
        }
    }

    private void asignarImagen(String usuarioGenero) {
        switch (usuarioGenero) {
            case "Masculino":
                perfilUsuario.setImageResource(R.drawable.man);
                break;

            case "Femenino":
                perfilUsuario.setImageResource(R.drawable.woman);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_LOCATION && resultCode == AppCompatActivity.RESULT_OK) {
            if (data != null) {
                String address = data.getStringExtra("address");
                usuarioDireccion.setText(address);
            }
        }
    }
}