package com.equipo1.prueba3.adapters;

import android.content.Context;

import androidx.annotation.NonNull;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.equipo1.prueba3.R;
import com.equipo1.prueba3.util.SpinnerItem;

import java.util.List;

public class SpinnerAdapter extends ArrayAdapter<SpinnerItem> {
    public SpinnerAdapter(@NonNull Context context, @NonNull List<SpinnerItem> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_item, parent, false);
        }

        ImageView imageViewIcon = convertView.findViewById(R.id.imageViewIcon);
        TextView textViewName = convertView.findViewById(R.id.textViewName);

        SpinnerItem currentItem = getItem(position);
        if (currentItem != null) {
            imageViewIcon.setImageResource(currentItem.getDrawableResId());
            textViewName.setText(currentItem.getText());
        }

        return convertView;
    }
}
