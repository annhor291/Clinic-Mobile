package com.example.clinicmobile.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clinicmobile.R;
import com.example.clinicmobile.listeners.OnDoctorClickListener;
import com.example.clinicmobile.model.Doctor;

import java.util.List;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder> {

    private List<Doctor> doctors;

    public DoctorAdapter(List<Doctor> doctors) {
        this.doctors = doctors;
    }

    @NonNull
    @Override
    public DoctorAdapter.DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_doctor, parent, false);

            return new DoctorViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull DoctorAdapter.DoctorViewHolder holder, int position) {

        Doctor doctor = doctors.get(position);

        holder.tvDoctorName.setText(doctor.getName());
        holder.tvSpecialty.setText(doctor.getSpecialty());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDoctorClick(doctor);
            }
        });
    }

    @Override
    public int getItemCount() {
        return doctors.size();
    }

    static class DoctorViewHolder extends RecyclerView.ViewHolder {

        TextView tvDoctorName;
        TextView tvSpecialty;

        public DoctorViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDoctorName =
                    itemView.findViewById(R.id.tvDoctorName);

            tvSpecialty =
                    itemView.findViewById(R.id.tvSpecialty);
        }
    }



    private OnDoctorClickListener listener;

    public DoctorAdapter(
            List<Doctor> doctors,
            OnDoctorClickListener listener) {

        this.doctors = doctors;
        this.listener = listener;
    }
}
