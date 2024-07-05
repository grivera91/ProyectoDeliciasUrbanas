package com.equipo1.DeliciasUrbanas.ui.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import androidx.activity.OnBackPressedCallback;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.equipo1.DeliciasUrbanas.R;
import com.equipo1.DeliciasUrbanas.ui.adapters.HomeHorAdapter;
import com.equipo1.DeliciasUrbanas.ui.adapters.HomeVerAdapter;
import com.equipo1.DeliciasUrbanas.util.UpdateVerticalRec;
import com.equipo1.DeliciasUrbanas.data.models.HomeHorModel;
import com.equipo1.DeliciasUrbanas.data.models.HomeVerModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements UpdateVerticalRec {

    Context context;
    private FirebaseAuth mAuth;
    RecyclerView homeHorizontalRec, homeVerticalRec;
    ArrayList<HomeHorModel> homeHorModelList;
    HomeHorAdapter homeHorAdapter;
    ArrayList<HomeVerModel> homeVerModelList;
    HomeVerAdapter homeVerAdapter;
    TextView nombreUsuarioHome;
    private boolean fromButton;

    public static HomeFragment newInstance(boolean fromButton) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putBoolean("fromButton", fromButton);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            fromButton = getArguments().getBoolean("fromButton", false);
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home,container,false);

        homeHorizontalRec = view.findViewById(R.id.home_hor_rec);
        homeVerticalRec = view.findViewById(R.id.home_ver_rec);
        context = getContext();

        mAuth = FirebaseAuth.getInstance();
        nombreUsuarioHome = view.findViewById(R.id.editTextMainMenu);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        homeHorModelList = new ArrayList<>();
        db.collection("listaMenuHorizontal")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String image = document.getString("image");
                                String name = document.getString("name");
                                String tipo = document.getString("tipo");
                                String gif = document.getString("gif");

                                int imageIcono = getDrawableId(context, image);
                                int imageAnimada = getDrawableId(context, gif);

                                HomeHorModel homeHorModel = new HomeHorModel(imageIcono, name, tipo, imageAnimada);
                                homeHorModelList.add(homeHorModel);
                            }
                            homeHorAdapter.notifyDataSetChanged();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Manejar el error
                        Log.w("Firestore", "Error al obtener documentos.", e);
                        String mensaje = e.getMessage();
                    }
                });

        homeVerModelList = new ArrayList<>();
        db.collection("listaMenuVertical")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String image = document.getString("image");
                                String name = document.getString("name");
                                String timing = document.getString("timing");
                                String rating = document.getString("rating");
                                String price = document.getString("price");
                                String tipo = document.getString("tipo");
                                int ImageId = getDrawableId(context, image);

                                if (ImageId != 0) {
                                    HomeVerModel homeVerModel = new HomeVerModel(ImageId, name, timing, rating, price, tipo);
                                    homeVerModelList.add(homeVerModel);
                                } else {
                                    Log.e("Firestore", "Recurso drawable no encontrado para la imagen: " + image);
                                }
                            }
                            homeVerAdapter.notifyDataSetChanged();
                        }
                    }
                });

        homeHorAdapter = new HomeHorAdapter(this, getActivity(), homeHorModelList, homeVerModelList);
        homeHorizontalRec.setAdapter(homeHorAdapter);
        homeHorizontalRec.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        homeHorizontalRec.setHasFixedSize(true);
        homeHorizontalRec.setNestedScrollingEnabled(false);

        //Vertical
        homeVerAdapter = new HomeVerAdapter(getActivity(),homeVerModelList);
        homeVerticalRec.setAdapter(homeVerAdapter);
        homeVerticalRec.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (fromButton) {
                    // Si el fragmento fue abierto desde el botón, omite la validación
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .remove(HomeFragment.this)
                            .commit();
                    getActivity().findViewById(R.id.app_bar_main).setVisibility(View.VISIBLE);
                } else {
                    // Muestra el diálogo de confirmación
                    new AlertDialog.Builder(requireContext())
                            .setMessage("¿Estás seguro de que deseas salir de la aplicación?")
                            .setCancelable(false)
                            .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    requireActivity().finish();
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                }
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            SetearDatos(currentUser);
        }
    }

    @Override
    public void callback(int position, ArrayList<HomeVerModel> list) {

        homeVerAdapter = new HomeVerAdapter(getContext(), list);
        homeVerAdapter.notifyDataSetChanged();
        homeVerticalRec.setAdapter(homeVerAdapter);
    }

    private void SetearDatos(FirebaseUser currentUser)
    {
        String usuarioId = currentUser.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("usuarios")
                .whereEqualTo("usuarioId",usuarioId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String nombres = document.getString("nombres");
                                String nombreMenuPrincipal = "Hola " + nombres + "!";
                                nombreUsuarioHome.setText(nombreMenuPrincipal);
                            }
                        }
                    }
                });

    }
    private int getDrawableId(Context context, String imageName) {
        return context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
    }
}