package br.com.controleite.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import br.com.controleite.R;
import br.com.controleite.constants.Constants;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class AnimalAdapter extends RecyclerView.Adapter<AnimalAdapter.AnimalsViewHolder> {

    private final List<Animal> animals;
    private final Context context;
    private final AnimalOnClickListener onClickListener;


    public interface AnimalOnClickListener {

        void onClickAnimal(AnimalsViewHolder holder, int idx);

    }

    public AnimalAdapter(List<Animal> animals, Context context, AnimalOnClickListener onClickListener) {
        this.animals = animals;
        this.context = context;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public AnimalsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_animal, parent, false);
        return new AnimalsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnimalsViewHolder holder, int position) {

        Animal animal = animals.get(position);
        holder.name.setText(animal.getIdentificacao());

        if (animal.getFoto() != null) {
            Glide
                    .with(context)
                    .load(Constants.STORAGE_IMAGES + animal.getFoto() + "?alt=media")
                    .centerCrop()
                    .into(holder.photo);
        }

        if (onClickListener != null) {
            holder.itemView.setOnClickListener(view -> onClickListener.onClickAnimal(holder, position));
        }

    }

    @Override
    public int getItemCount() {
        return this.animals != null ? this.animals.size() : 0;
    }

    public static class AnimalsViewHolder extends RecyclerView.ViewHolder {

        public ShapeableImageView photo;
        public TextView name;
        public View view;

        public AnimalsViewHolder(View view) {
            super(view);
            this.view = view;
            this.photo = view.findViewById(R.id.sivAnimalPhoto);
            this.name = view.findViewById(R.id.tvAnimalName);
        }

    }

}
