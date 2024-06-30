package com.equipo1.prueba3.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.equipo1.prueba3.R;
import com.equipo1.prueba3.adapters.DetailedDailyAdapter;
import com.equipo1.prueba3.models.DetailedDailyModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class DetailedDailyMealActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<DetailedDailyModel> detailedDailyModelList;
    DetailedDailyAdapter dailyAdapter;
    ImageView imageView;
    FloatingActionButton botonAgregar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_daily_meal);

        Intent intent = getIntent();
        String type = intent.getStringExtra("type");

        recyclerView = findViewById(R.id.detailed_rec);
        imageView = findViewById(R.id.detailed_img);

        botonAgregar = findViewById(R.id.boton_agregar);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        detailedDailyModelList = new ArrayList<>();
        dailyAdapter = new DetailedDailyAdapter(detailedDailyModelList,this);
        recyclerView.setAdapter(dailyAdapter);

        if(type != null && type.equalsIgnoreCase("breakfast")){
            imageView.setImageResource(R.drawable.desayunos);
            detailedDailyModelList.add(new DetailedDailyModel(R.drawable.desayuno1, "Tamales", "Tamales de pollo y chancho","4.2","2.00","6:00 a 10:00"));
            detailedDailyModelList.add(new DetailedDailyModel(R.drawable.desayuno2, "Pan con torreja", "Torreja clasica","4.3","2.00","6:00 a 10:00"));
            detailedDailyModelList.add(new DetailedDailyModel(R.drawable.desayuno3, "Pan con relleno", "Pan con Sangrecita de pollo","4.5","1.50","6:00 a 10:00"));
            detailedDailyModelList.add(new DetailedDailyModel(R.drawable.desayuno4, "Pan con chicharron", "Pan con el mejor chicharron","4.9","3.00","6:00 a 10:00"));
            detailedDailyModelList.add(new DetailedDailyModel(R.drawable.desayuno5, "Sandwich de pavo", "Pavo horneado","4.1","3.50","6:00 a 10:00"));
            detailedDailyModelList.add(new DetailedDailyModel(R.drawable.desayuno6, "Mixto caliente", "Mixto clasico de jamon ingles, queso y mantequilla","3.9","3.5","6:00 a 10:00"));
            detailedDailyModelList.add(new DetailedDailyModel(R.drawable.desayuno7, "Pan con Salchicha huachana", "Salchicha con huevo revuelto","4.7","2.50","6:00 a 10:00"));
            detailedDailyModelList.add(new DetailedDailyModel(R.drawable.desayuno8, "Sandiwch de filete de pollo", "Pan con filete de pollo fresco","4.1","4.00","6:00 a 10:00"));
            dailyAdapter.notifyDataSetChanged();
        }

        if(type != null && type.equalsIgnoreCase("lunch")){
            imageView.setImageResource(R.drawable.almuerzos);
            detailedDailyModelList.add(new DetailedDailyModel(R.drawable.almuerzo1, "Lomo saltado", "Lomo saltado clasico","4.2","14.00","13:00 a 16:00"));
            detailedDailyModelList.add(new DetailedDailyModel(R.drawable.almuerzo2, "Ají de gallina", "Aji de fallina clasico","4.3","12.00","13:00 a 16:00"));
            detailedDailyModelList.add(new DetailedDailyModel(R.drawable.almuerzo3, "Arroz con pollo", "Arroz con pollo criollo","4.1","11.00","13:00 a 16:00"));
            detailedDailyModelList.add(new DetailedDailyModel(R.drawable.almuerzo4, "Tallarines verdes", "Tallarines con bistec","4.9","10.00","13:00 a 16:00"));
            detailedDailyModelList.add(new DetailedDailyModel(R.drawable.almuerzo5, "Arroz chaufa", "Chaufa clasico","4.5","12.00","13:00 a 16:00"));
            dailyAdapter.notifyDataSetChanged();
        }

        if(type != null && type.equalsIgnoreCase("dinner")){
            imageView.setImageResource(R.drawable.cenas);
            detailedDailyModelList.add(new DetailedDailyModel(R.drawable.cena1, "Chuleta de cerdo", "Chuleta de cerdo grill con ensalada","4.1","18.90","18:00 a 22:00"));
            detailedDailyModelList.add(new DetailedDailyModel(R.drawable.cena2, "Ensala Cesar", "Ensalda con filete de pollo fresco en trozos","4.4","18.90","18:00 a 22:00"));
            detailedDailyModelList.add(new DetailedDailyModel(R.drawable.cena3, "Ensalda palteada", "Ensalda con filete de pollo fresco en trozos, jamon, ingles","4.6","18.90","18:00 a 22:00"));
            detailedDailyModelList.add(new DetailedDailyModel(R.drawable.cena4, "Pollo a la plancha", "Filete de pollo fresco a la plancha con ensalda fresca","4.9","14.90","18:00 a 22:00"));
            detailedDailyModelList.add(new DetailedDailyModel(R.drawable.cena5, "Costillas estilo tamal", "Porcion de costillas de cerdo acompañado con porcion de camote","4.2","22.90","18:00 a 22:00"));
            dailyAdapter.notifyDataSetChanged();
        }

        if(type != null && type.equalsIgnoreCase("sweets")){
            imageView.setImageResource(R.drawable.postres);
            detailedDailyModelList.add(new DetailedDailyModel(R.drawable.postre1, "Cheese cake de maracuyá", "A base de galleta y fruta fresca","4.2","9.90","8:00 a 22:00"));
            detailedDailyModelList.add(new DetailedDailyModel(R.drawable.postre2, "Pastel de choclo dulce", "A base de maiz de choclo y pasas","4.4","9.90","8:00 a 22:00"));
            detailedDailyModelList.add(new DetailedDailyModel(R.drawable.postre3, "Pastel de acelga", "Pastel relleno de acelgas","4.1","9.90","8:00 a 22:00"));
            detailedDailyModelList.add(new DetailedDailyModel(R.drawable.postre4, "Pastel de choclo saldado", "Hecho con choclo fresco desgranado","4.7","9.90","8:00 a 22:00"));
            detailedDailyModelList.add(new DetailedDailyModel(R.drawable.postre5, "Picarones", "Picarones clasicos","4.4","9.90","8:00 a 22:00"));
            dailyAdapter.notifyDataSetChanged();
        }

        if(type != null && type.equalsIgnoreCase("coffee")){
            imageView.setImageResource(R.drawable.bebidas);
            detailedDailyModelList.add(new DetailedDailyModel(R.drawable.bebida1, "Chicha morada", "Bebida de maiz morado peruano con limon","4.2","4.00","6:00 to 22:00"));
            detailedDailyModelList.add(new DetailedDailyModel(R.drawable.bebida2, "Chocolate peruano", "Chocolate peruano selecto","4.3","4.00","6:00 to 22:00"));
            detailedDailyModelList.add(new DetailedDailyModel(R.drawable.bebida3, "Cafe peruano", "Cafe peruano selecto","4.1","4.00","6:00 to 22:00"));
            detailedDailyModelList.add(new DetailedDailyModel(R.drawable.bebida4, "Jugo de maracuya", "Maracuya fresca","4.6","4.00","6:00 to 22:00"));
            detailedDailyModelList.add(new DetailedDailyModel(R.drawable.bebida5, "Fresa con lecha", "Fresas frescas con leche ","4.8","5.00","6:00 to 22:00"));
            dailyAdapter.notifyDataSetChanged();
        }

        botonAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}