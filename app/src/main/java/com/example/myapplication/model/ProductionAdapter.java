package com.example.myapplication.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.util.DateComparator;
import com.google.android.material.imageview.ShapeableImageView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ProductionAdapter extends RecyclerView.Adapter<ProductionAdapter.ProductionsViewHolder> {

    private final List<Production> productions;
    private final Context context;
    private final ProductionOnClickListener onClickListener;


    public interface ProductionOnClickListener {

        void onClickProduction(ProductionsViewHolder holder, int idx);

    }

    public ProductionAdapter(List<Production> productions, Context context, ProductionOnClickListener onClickListener) {
        this.productions = productions;
        this.context = context;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ProductionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_production, parent, false);
        return new ProductionsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductionsViewHolder holder, int position) {

        Production production = productions.get(position);

        String liters = production.getLitros() + " " + context.getString(R.string.liters);
        holder.liters.setText(liters);

        if (DateComparator.isYesterday(production.getData())) {

            holder.date.setText(context.getString(R.string.yesterday));

        } else {

            DateFormat df;

            if (DateComparator.isToday(production.getData())) {

                df = new SimpleDateFormat("HH:mm", Locale.getDefault());

            } else {

                df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

            }

            holder.date.setText(df.format(production.getData().getTime()));

        }

        holder.itemView.setOnClickListener(view -> onClickListener.onClickProduction(holder, position));

    }

    @Override
    public int getItemCount() {
        return this.productions != null ? this.productions.size() : 0;
    }

    public static class ProductionsViewHolder extends RecyclerView.ViewHolder {

        public ShapeableImageView animalPhoto;
        public TextView animalName;
        public TextView liters;
        public TextView date;
        public View view;

        public ProductionsViewHolder(View view) {
            super(view);
            this.view = view;
            this.animalPhoto = (ShapeableImageView) view.findViewById(R.id.sivProdAnimalPhoto);
            this.animalName = (TextView) view.findViewById(R.id.tvProdAnimalName);
            this.liters = (TextView) view.findViewById(R.id.tvLiters);
            this.date = (TextView) view.findViewById(R.id.tvProdDate);
        }

    }

}
