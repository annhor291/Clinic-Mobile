package com.example.clinicmobile.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clinicmobile.R;
import com.example.clinicmobile.dto.response.SpecialtyResponse;
import com.example.clinicmobile.model.Specialty;

import java.util.List;

public class SpecialtyAdapter extends RecyclerView.Adapter<SpecialtyAdapter.SpecialtyViewHolder> {

    private List<SpecialtyResponse> specialties;
    private OnSpecialtyClickListener listener;

    public interface OnSpecialtyClickListener {
        void onSpecialtyClick(SpecialtyResponse specialty);
    }

    public SpecialtyAdapter(List<SpecialtyResponse> specialties,
                            OnSpecialtyClickListener listener) {
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
        SpecialtyResponse specialty = specialties.get(position);
        holder.tvName.setText(specialty.getName());
        holder.tvDescription.setText(specialty.getDescription() != null
                ? specialty.getDescription() : "");

        // Hiện số bác sĩ nếu có
        if (specialty.getTotalDoctors() != null && specialty.getTotalDoctors() > 0) {
            holder.tvIcon.setText("🏥");
            holder.tvDescription.setText(
                    (specialty.getDescription() != null ? specialty.getDescription() : "")
                            + "\n" + specialty.getTotalDoctors() + " bác sĩ");
        } else {
            holder.tvIcon.setText("🏥");
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onSpecialtyClick(specialty);
        });
    }

    @Override
    public int getItemCount() {
        return specialties != null ? specialties.size() : 0;
    }

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
