package com.equipo1.DeliciasUrbanas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.equipo1.DeliciasUrbanas.activities.AdministradorActivity;
import com.equipo1.DeliciasUrbanas.activities.LoginActivity;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.equipo1.DeliciasUrbanas.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.Nullable;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private Button logOutButton;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private DrawerLayout drawer;
    private TextView nombreMenu, correoMenu;
    private ImageView imagenMenu;

    @Override
    public void onStart(){
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            SetearDatos(currentUser);
        }else{
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,
                R.id.nav_daily_meal,
                R.id.nav_favourite,
                R.id.nav_my_cart,
                R.id.nav_my_pedido,
                R.id.nav_my_profile,
                R.id.main_dayli_meals,
                R.id.nav_my_about)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isDarkMode = sharedPreferences.getBoolean("dark_mode", false);
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        logOutButton = findViewById(R.id.logOutButton);

        View headerView = navigationView.getHeaderView(0);
        nombreMenu = headerView.findViewById(R.id.textViewNombre);
        correoMenu = headerView.findViewById(R.id.textViewEmail);
        imagenMenu =headerView.findViewById(R.id.imageView_menu_foto_perfil);

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                logOutUser(mUser);
            }
        });
    }
    private void logOutUser(FirebaseUser mUser)
    {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(MainActivity.this, "Cerro sesión correctamente", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void SetearDatos(FirebaseUser currentUser)
    {
        String usuarioId = currentUser.getUid();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("usuarios")
                .whereEqualTo("usuarioId", usuarioId)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.w("Firestore", "Error al escuchar cambios.", error);
                            Toast.makeText(MainActivity.this, "Error al obtener datos del usuario.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        for (QueryDocumentSnapshot document : value) {
                            if (document.exists()) {
                                String nombres = document.getString("nombres");
                                String apellidos = document.getString("apellidos");
                                String email = document.getString("correo");
                                String genero = document.getString("genero");

                                String nombreCompleto = nombres + " " + apellidos;

                                // Actualiza las vistas con los datos del usuario
                                nombreMenu.setText(nombreCompleto);
                                correoMenu.setText(email);

                                if (genero != null && !genero.isEmpty()) {
                                    asignarImagen(genero);
                                }
                            }
                        }
                    }
                });
    }

    private void asignarImagen(String genero){

        switch (genero){
            case "Masculino":
                imagenMenu.setImageResource(R.drawable.man);
                break;

            case "Femenino":
                imagenMenu.setImageResource(R.drawable.woman);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_admistrador) {
            Intent intent = new Intent(MainActivity.this, AdministradorActivity.class);
            startActivity(intent);
            return true;
        }else if(id == R.id.action_tema){
            toggleTheme();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void toggleTheme() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isDarkMode = sharedPreferences.getBoolean("dark_mode", false);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            editor.putBoolean("dark_mode", false);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            editor.putBoolean("dark_mode", true);
        }
        editor.apply();
        recreate();
    }
}