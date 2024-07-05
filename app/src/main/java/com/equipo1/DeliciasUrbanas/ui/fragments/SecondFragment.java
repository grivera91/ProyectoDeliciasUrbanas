package com.equipo1.DeliciasUrbanas.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.equipo1.DeliciasUrbanas.R;
import com.equipo1.DeliciasUrbanas.ui.adapters.FeaturedAdapter;
import com.equipo1.DeliciasUrbanas.ui.adapters.FeaturedVerAdapter;
import com.equipo1.DeliciasUrbanas.data.models.FeaturedModel;
import com.equipo1.DeliciasUrbanas.data.models.FeaturedVerModel;

import java.util.ArrayList;
import java.util.List;

public class SecondFragment extends Fragment {

    //Featured Hor RecyclerView
    List<FeaturedModel> featuredModelsList;
    RecyclerView recyclerView;
    FeaturedAdapter featuredAdapter;

    //Featured Ver RecyclerView
    List<FeaturedVerModel> featuredVerModelsList;
    RecyclerView recyclerView2;
    FeaturedVerAdapter featuredVerAdapter;

    public SecondFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_second, container, false);

        //Featured Hor RecyclerView
        recyclerView = view.findViewById(R.id.featured_hor_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        featuredModelsList = new ArrayList<>();

        featuredModelsList.add(new FeaturedModel(R.drawable.almuerzo1,"Lomo saltado","Lomo saltado clásico"));
        featuredModelsList.add(new FeaturedModel(R.drawable.almuerzo6,"Ceviche clásico","Ceviche clásico peruano"));
        featuredModelsList.add(new FeaturedModel(R.drawable.almuerzo4,"Tallarines verdes","Tallarines verdes con bistec de res"));

        featuredAdapter = new FeaturedAdapter(featuredModelsList);
        recyclerView.setAdapter(featuredAdapter);

        //Featured Ver RecyclerView
        recyclerView2 = view.findViewById(R.id.featured_ver_rec);
        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        featuredVerModelsList = new ArrayList<>();

        featuredVerModelsList.add(new FeaturedVerModel(R.drawable.cena5, "Costillas estilo tamal","Porcion de costillas de cerdo acompañadas con","4.2","18:00 a 22:00"));
        featuredVerModelsList.add(new FeaturedVerModel(R.drawable.desayuno2, "Pan con torreja","Panm con torreja clásica","4.3","06:00 a 10:00"));
        featuredVerModelsList.add(new FeaturedVerModel(R.drawable.bebida4, "Jugo de maracuyá","Maracuyá fresca","4.6","06:00 a 22:00"));
        featuredVerModelsList.add(new FeaturedVerModel(R.drawable.cena3, "Ensalda palteada","Ensalada con filte de pollo y frescas verduras","4.6","18:00 a 22:00"));

        featuredVerAdapter = new FeaturedVerAdapter(featuredVerModelsList);
        recyclerView2.setAdapter(featuredVerAdapter);

        return view;
    }
}