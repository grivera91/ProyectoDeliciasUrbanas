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

public class ThirdFragment extends Fragment {

    //Featured Hor RecyclerView
    List<FeaturedModel> featuredModelsList;
    RecyclerView recyclerView;
    FeaturedAdapter featuredAdapter;

    //Featured Ver RecyclerView
    List<FeaturedVerModel> featuredVerModelsList;
    RecyclerView recyclerView2;
    FeaturedVerAdapter featuredVerAdapter;

    public ThirdFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_third, container, false);

        //Featured Hor RecyclerView
        recyclerView = view.findViewById(R.id.featured_hor_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        featuredModelsList = new ArrayList<>();

        featuredModelsList.add(new FeaturedModel(R.drawable.postre6,"Helado de menta y licor","Helado natural de menta y ron"));
        featuredModelsList.add(new FeaturedModel(R.drawable.bebida6,"Chicha de jora","Chicha de jora casera peruana"));
        featuredModelsList.add(new FeaturedModel(R.drawable.pizza5,"Pïzza lomo saltado","Masa de pizza con lomo saltado clasico"));

        featuredAdapter = new FeaturedAdapter(featuredModelsList);
        recyclerView.setAdapter(featuredAdapter);

        //Featured Ver RecyclerView
        recyclerView2 = view.findViewById(R.id.featured_ver_rec);
        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        featuredVerModelsList = new ArrayList<>();

        featuredVerModelsList.add(new FeaturedVerModel(R.drawable.almuerzo7, "Chaufa amazónico","Arroz chaufa con cecina de cerdo","5.0","12:00 a 22:00"));
        featuredVerModelsList.add(new FeaturedVerModel(R.drawable.almuerzo8, "Causa acevichada","Cusa de ceviche clasico peruano","5.0","12:00 a 22:00"));
        featuredVerModelsList.add(new FeaturedVerModel(R.drawable.bebida7, "Cerveza artesanal de cannabis","Cerveza artesanal hecha a abse de cannabis","5.0","12:00 a 22:00"));
        featuredVerModelsList.add(new FeaturedVerModel(R.drawable.almuerzo9, "Sopa ramen","Sopa ramen clásica","5.0","12:00 a 22:00"));

        featuredVerAdapter = new FeaturedVerAdapter(featuredVerModelsList);
        recyclerView2.setAdapter(featuredVerAdapter);

        return view;
    }
}