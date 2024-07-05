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

public class FirstFragment extends Fragment {

    //Featured Hor RecyclerView
    List<FeaturedModel> featuredModelsList;
    RecyclerView recyclerView;
    FeaturedAdapter featuredAdapter;

    //Featured Ver RecyclerView
    List<FeaturedVerModel> featuredVerModelsList;
    RecyclerView recyclerView2;
    FeaturedVerAdapter featuredVerAdapter;

    public FirstFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_first, container, false);

        //Featured Hor RecyclerView
        recyclerView = view.findViewById(R.id.featured_hor_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        featuredModelsList = new ArrayList<>();

        featuredModelsList.add(new FeaturedModel(R.drawable.fav1,"Ensala de frutas","Ensala con fresa, kiwi y moras"));
        featuredModelsList.add(new FeaturedModel(R.drawable.fav2,"Hamburguesa royal","Hamburguesa con tocino y huevo"));
        featuredModelsList.add(new FeaturedModel(R.drawable.fav3,"Tallarines veganos","Tallarines con champiñones"));

        featuredAdapter = new FeaturedAdapter(featuredModelsList);
        recyclerView.setAdapter(featuredAdapter);

        //Featured Ver RecyclerView
        recyclerView2 = view.findViewById(R.id.featured_ver_rec);
        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        featuredVerModelsList = new ArrayList<>();

        featuredVerModelsList.add(new FeaturedVerModel(R.drawable.ver1, "Snack de frutos secos","Pecanas, fresas, pasas","4.5","08:00 a 22:00"));
        featuredVerModelsList.add(new FeaturedVerModel(R.drawable.ver2, "Huevo frito","Huevo a la inglesa","4.1","08:00 a 22:00"));
        featuredVerModelsList.add(new FeaturedVerModel(R.drawable.ver3, "Sandwich de frutas","Pan de model, platano y moras","4.5","08:00 a 22:00"));
        featuredVerModelsList.add(new FeaturedVerModel(R.drawable.ver4, "Ensalda rusa","Ensalda de papa, zanahora y arvejas","4.8","08:00 a 22:00"));

        featuredVerAdapter = new FeaturedVerAdapter(featuredVerModelsList);
        recyclerView2.setAdapter(featuredVerAdapter);

        return view;
    }
}