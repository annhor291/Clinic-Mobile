package com.example.clinicmobile.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clinicmobile.R;
import com.example.clinicmobile.model.Specialty;

import java.util.List;

public class SpecialtyAdapter extends RecyclerView.Adapter<SpecialtyAdapter.SpecialtyViewHolder> {

    private List<Specialty> specialties;
    private OnSpecialtyClickListener listener;

    public interface OnSpecialtyClickListener {
        void onSpecialtyClick(Specialty specialty);
    }

    public SpecialtyAdapter(List<Specialty> specialties, OnSpecialtyClickListener listener) {
        this.specialties = specialties;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SpecialtyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_specialty, parent, false);
        return new SpecialtyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SpecialtyViewHolder holder, int position) {
        Specialty specialty = specialties.get(position);
        holder.tvIcon.setText(specialty.getIcon());
        holder.tvName.setText(specialty.getName());
        holder.tvDescription.setText(specialty.getDescription());
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onSpecialtyClick(specialty);
        });
    }

    @Override
    public int getItemCount() { return specialties.size(); }

    static class SpecialtyViewHolder extends RecyclerView.ViewHolder {
        TextView tvIcon, tvName, tvDescription;

        public SpecialtyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvIcon = itemView.findViewById(R.id.tvIcon);
            tvName = itemView.findViewById(R.id.tvName);
            tvDescription = itemView.findViewById(R.id.tvDescription);
        }
    }
}
