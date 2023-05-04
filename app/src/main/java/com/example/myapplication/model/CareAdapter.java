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

import java.util.List;

public class CareAdapter extends RecyclerView.Adapter<CareAdapter.CareViewHolder> {

    private final List<Care> careList;
    private final Context context;
    private final CareOnClickListener onClickListener;


    public interface CareOnClickListener {

        void onClickCare(CareViewHolder holder, int idx);

    }

    public CareAdapter(List<Care> careList, Context context, CareOnClickListener onClickListener) {
        this.careList = careList;
        this.context = context;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public CareViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_care, parent, false);
        return new CareViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CareViewHolder holder, int position) {

        Care care = careList.get(position);

        holder.data.setText(DateComparator.dateToString(context, care.getData()));

        if (onClickListener != null) {
            holder.itemView.setOnClickListener(view -> onClickListener.onClickCare(holder, position));
        }

    }

    @Override
    public int getItemCount() {
        return this.careList != null ? this.careList.size() : 0;
    }

    public static class CareViewHolder extends RecyclerView.ViewHolder {

        public TextView data;
        public View view;

        public CareViewHolder(View view) {

            super(view);
            this.view = view;
            this.data = (TextView) view.findViewById(R.id.tvCareDate);

        }

    }

}
