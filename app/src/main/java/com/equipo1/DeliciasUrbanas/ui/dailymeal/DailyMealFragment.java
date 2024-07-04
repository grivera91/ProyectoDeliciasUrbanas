package com.equipo1.DeliciasUrbanas.ui.dailymeal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.equipo1.DeliciasUrbanas.R;
import com.equipo1.DeliciasUrbanas.adapters.DailyMealAdapter;
import com.equipo1.DeliciasUrbanas.models.DailyMealModel;

import java.util.ArrayList;
import java.util.List;

public class DailyMealFragment extends Fragment {

    RecyclerView recyclerView;
    List<DailyMealModel> dailyMealModel;
    DailyMealAdapter dailyMealAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_daily_meal,container,false);

        recyclerView = root.findViewById(R.id.daily_meal_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        dailyMealModel = new ArrayList<>();

        dailyMealModel.add(new DailyMealModel(R.drawable.desayunos,"Desayunos","","Desayunos peruanos","breakfast"));
        dailyMealModel.add(new DailyMealModel(R.drawable.almuerzos,"Almuerzos","","Platos criollos","lunch"));
        dailyMealModel.add(new DailyMealModel(R.drawable.cenas,"Cenas","","Lonchecito","dinner"));
        dailyMealModel.add(new DailyMealModel(R.drawable.postres,"Postres","","Los mejores Postres del Perú","sweets"));
        dailyMealModel.add(new DailyMealModel(R.drawable.bebidas,"Bebidas","","Variedad de bebidas","coffee"));

        dailyMealAdapter = new DailyMealAdapter(getContext(), dailyMealModel);
        recyclerView.setAdapter(dailyMealAdapter);
        dailyMealAdapter.notifyDataSetChanged();

        return root;
    }
}