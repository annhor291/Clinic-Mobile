package com.example.clinicmobile.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clinicmobile.R;
import com.example.clinicmobile.dto.response.DoctorResponse;
import com.example.clinicmobile.model.Doctor;

import java.util.List;

public class DoctorListAdapter extends RecyclerView.Adapter<DoctorListAdapter.DoctorListViewHolder>{

    private List<DoctorResponse> doctors;
    private OnDoctorClickListener listener;

    public interface OnDoctorClickListener {
        void onDoctorClick(DoctorResponse doctor);
    }

    public DoctorListAdapter(List<DoctorResponse> doctors, OnDoctorClickListener listener) {
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
        DoctorResponse doctor = doctors.get(position);

        holder.tvDoctorName.setText(
                doctor.getTitle() != null
                        ? doctor.getTitle() + " " + doctor.getFullName()
                        : doctor.getFullName());

        holder.tvSpecialty.setText(
                doctor.getSpecialtyName() != null ? doctor.getSpecialtyName() : "");

        holder.tvRating.setText("⭐ Phí khám: "
                + (doctor.getConsultationFee() != null
                ? String.format("%,.0f đ", doctor.getConsultationFee())
                : "Liên hệ"));

        holder.tvExperience.setText(
                doctor.getExperienceYears() != null
                        ? doctor.getExperienceYears() + " năm kinh nghiệm"
                        : "");

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onDoctorClick(doctor);
        });
    }

    @Override
    public int getItemCount() {
        return doctors != null ? doctors.size() : 0;
    }

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
