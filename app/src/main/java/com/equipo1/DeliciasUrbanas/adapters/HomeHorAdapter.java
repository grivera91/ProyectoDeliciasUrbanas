package com.equipo1.DeliciasUrbanas.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.equipo1.DeliciasUrbanas.R;
import com.equipo1.DeliciasUrbanas.models.HomeHorModel;
import com.equipo1.DeliciasUrbanas.models.HomeVerModel;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class HomeHorAdapter extends RecyclerView.Adapter<HomeHorAdapter.ViewHolder> {

    UpdateVerticalRec updateVerticalRec;
    Activity activity;
    ArrayList<HomeHorModel> homeHorModelsList;
    ArrayList<HomeVerModel> homeVerModelsList;

    boolean check = true;
    boolean select = true;
    int row_index = -1;

    public HomeHorAdapter(UpdateVerticalRec updateVerticalRec, Activity activity, ArrayList<HomeHorModel> homeHorList, ArrayList<HomeVerModel> homeVerList) {
        this.updateVerticalRec = updateVerticalRec;
        this.activity = activity;
        this.homeHorModelsList = homeHorList;
        this.homeVerModelsList = homeVerList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.home_horizontal_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imageView.setImageResource(homeHorModelsList.get(position).getImageIcono());
        holder.name.setText(homeHorModelsList.get(position).getName());

        if(check) {
            if(!homeVerModelsList.isEmpty())
            {
                ArrayList<HomeVerModel> homeVerModels  = new ArrayList<>(homeVerModelsList
                        .stream()
                        .filter(homeVerModel -> homeVerModel.getTipo().equals("1"))
                        .collect(Collectors.toList()));
                updateVerticalRec.callback(position, homeVerModels);
            }
            check = false;
        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                row_index = position;
                notifyDataSetChanged();

                if(homeVerModelsList.isEmpty()) {
                    return;
                }

                if(position == 0){
                    /*
                    ArrayList<HomeVerModel> homeVerModels = new ArrayList<>();
                    homeVerModels.add(new HomeVerModel(R.drawable.icecream1,"Ice Cream 1","10:30 - 23:00","4.9","Min - $35","1"));
                    homeVerModels.add(new HomeVerModel(R.drawable.icecream2,"Ice Cream 2","10:30 - 23:00","4.9","Min - $35","1"));
                    homeVerModels.add(new HomeVerModel(R.drawable.icecream3,"Ice Cream 4","10:30 - 23:00","4.9","Min - $35","1"));
                    homeVerModels.add(new HomeVerModel(R.drawable.icecream4,"Ice Cream 4","10:30 - 23:00","4.9","Min - $35","1"));
                    */
                    ArrayList<HomeVerModel> homeVerModels  = new ArrayList<>(homeVerModelsList
                            .stream()
                            .filter(homeVerModel -> homeVerModel.getTipo().equals("1"))
                            .collect(Collectors.toList()));

                    updateVerticalRec.callback(position, homeVerModels);
                }
                else if(position == 1){
                    /*
                    ArrayList<HomeVerModel> homeVerModels = new ArrayList<>();
                    homeVerModels.add(new HomeVerModel(R.drawable.sandwich1,"Sandwich 1","10:30 - 23:00","4.9","Min - $35","2"));
                    homeVerModels.add(new HomeVerModel(R.drawable.sandwich2,"Sandwich 2","10:30 - 23:00","4.9","Min - $35","2"));
                    homeVerModels.add(new HomeVerModel(R.drawable.sandwich3,"Sandwich 4","10:30 - 23:00","4.9","Min - $35","2"));
                    homeVerModels.add(new HomeVerModel(R.drawable.sandwich4,"Sandwich 4","10:30 - 23:00","4.9","Min - $35","2"));
                    */
                    ArrayList<HomeVerModel> homeVerModels  = new ArrayList<>(homeVerModelsList
                            .stream()
                            .filter(homeVerModel -> homeVerModel.getTipo().equals("2"))
                            .collect(Collectors.toList()));
                    updateVerticalRec.callback(position, homeVerModels);
                }
                else if(position == 2){
                    /*
                    ArrayList<HomeVerModel> homeVerModels = new ArrayList<>();
                    homeVerModels.add(new HomeVerModel(R.drawable.burger1,"Burger 1","10:30 - 23:00","4.9","Min - $35","3"));
                    homeVerModels.add(new HomeVerModel(R.drawable.burger2,"Burger 2","10:30 - 23:00","4.9","Min - $35","3"));
                    homeVerModels.add(new HomeVerModel(R.drawable.burger4,"Burger 4","10:30 - 23:00","4.9","Min - $35","3"));

                     */
                    ArrayList<HomeVerModel> homeVerModels  = new ArrayList<>(homeVerModelsList
                            .stream()
                            .filter(homeVerModel -> homeVerModel.getTipo().equals("3"))
                            .collect(Collectors.toList()));
                    updateVerticalRec.callback(position, homeVerModels);
                }
                else if (position == 3){
                    /*
                    ArrayList<HomeVerModel> homeVerModels = new ArrayList<>();
                    homeVerModels.add(new HomeVerModel(R.drawable.pizza1,"Pizza 1","10:30 - 23:00","4.9","Min - $35","4"));
                    homeVerModels.add(new HomeVerModel(R.drawable.pizza2,"Pizza 2","10:30 - 23:00","4.9","Min - $35","4"));
                    homeVerModels.add(new HomeVerModel(R.drawable.pizza3,"Pizza 3","10:30 - 23:00","4.9","Min - $35","4"));
                    homeVerModels.add(new HomeVerModel(R.drawable.pizza4,"Pizza 4","10:30 - 23:00","4.9","Min - $35","4"));

                     */
                    ArrayList<HomeVerModel> homeVerModels  = new ArrayList<>(homeVerModelsList
                            .stream()
                            .filter(homeVerModel -> homeVerModel.getTipo().equals("4"))
                            .collect(Collectors.toList()));
                    updateVerticalRec.callback(position, homeVerModels);
                }
                else if(position == 4){
                    /*
                    ArrayList<HomeVerModel> homeVerModels = new ArrayList<>();
                    homeVerModels.add(new HomeVerModel(R.drawable.fries1,"Fries 1","10:30 - 23:00","4.9","Min - $35","5"));
                    homeVerModels.add(new HomeVerModel(R.drawable.fries2,"Fries 2","10:30 - 23:00","4.9","Min - $35","5"));
                    homeVerModels.add(new HomeVerModel(R.drawable.fries3,"Fries 3","10:30 - 23:00","4.9","Min - $35","5"));
                    homeVerModels.add(new HomeVerModel(R.drawable.fries4,"Fries 4","10:30 - 23:00","4.9","Min - $35","5"));
                     */
                    ArrayList<HomeVerModel> homeVerModels  = new ArrayList<>(homeVerModelsList
                            .stream()
                            .filter(homeVerModel -> homeVerModel.getTipo().equals("5"))
                            .collect(Collectors.toList()));
                    updateVerticalRec.callback(position, homeVerModels);
                }
            }
        });

        if(select){
            if(position == 0){
                holder.cardView.setBackgroundResource(R.drawable.change_bg);
                Glide.with(holder.itemView.getContext()).asGif().load(homeHorModelsList.get(position).getImageAnimada()).into(holder.imageView);
                select = false;
            }
        }
        else{
            if(row_index == position){
                holder.cardView.setBackgroundResource(R.drawable.change_bg);
                Glide.with(holder.itemView.getContext()).asGif().load(homeHorModelsList.get(position).getImageAnimada()).into(holder.imageView);
            }else{
                holder.cardView.setBackgroundResource(R.drawable.default_bg);
            }
        }
    }

    @Override
    public int getItemCount() {
        return homeHorModelsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.hor_img);
            name = itemView.findViewById(R.id.hor_text);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
