package br.com.controleite.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import br.com.controleite.R;
import br.com.controleite.util.DateUtils;

import java.util.List;

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

        holder.date.setText(DateUtils.dateToString(context, production.getData()));

        holder.itemView.setOnClickListener(view -> onClickListener.onClickProduction(holder, position));

    }

    @Override
    public int getItemCount() {
        return this.productions != null ? this.productions.size() : 0;
    }

    public static class ProductionsViewHolder extends RecyclerView.ViewHolder {

        public TextView liters;
        public TextView date;
        public View view;

        public ProductionsViewHolder(View view) {
            super(view);
            this.view = view;
            this.liters = view.findViewById(R.id.tvLiters);
            this.date = view.findViewById(R.id.tvProdDate);
        }

    }

}
