package com.example.clinicmobile.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clinicmobile.R;
import com.example.clinicmobile.model.Doctor;

import java.util.List;

public class DoctorListAdapter extends RecyclerView.Adapter<DoctorListAdapter.DoctorListViewHolder>{

    private List<Doctor> doctors;
    private OnDoctorClickListener listener;

    public interface OnDoctorClickListener {
        void onDoctorClick(Doctor doctor);
    }

    public DoctorListAdapter(List<Doctor> doctors, OnDoctorClickListener listener) {
        this.doctors = doctors;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DoctorListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_doctor_list, parent, false);
        return new DoctorListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorListViewHolder holder, int position) {
        Doctor doctor = doctors.get(position);
        holder.tvDoctorName.setText(doctor.getName());
        holder.tvSpecialty.setText(doctor.getSpecialty());
        holder.tvRating.setText("⭐ " + doctor.getRating());
        holder.tvExperience.setText(doctor.getExperienceYears() + " năm kinh nghiệm");
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onDoctorClick(doctor);
        });
    }

    @Override
    public int getItemCount() { return doctors.size(); }

    static class DoctorListViewHolder extends RecyclerView.ViewHolder {
        TextView tvDoctorName, tvSpecialty, tvRating, tvExperience;

        public DoctorListViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDoctorName = itemView.findViewById(R.id.tvDoctorName);
            tvSpecialty = itemView.findViewById(R.id.tvSpecialty);
            tvRating = itemView.findViewById(R.id.tvRating);
            tvExperience = itemView.findViewById(R.id.tvExperience);
        }
    }
}
