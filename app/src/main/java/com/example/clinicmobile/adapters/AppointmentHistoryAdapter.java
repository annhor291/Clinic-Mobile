package com.example.clinicmobile.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clinicmobile.R;
import com.example.clinicmobile.model.AppointmentHistory;

import java.util.List;

public class AppointmentHistoryAdapter extends RecyclerView.Adapter<AppointmentHistoryAdapter.ViewHolder> {

    private List<AppointmentHistory> appointments;

    public AppointmentHistoryAdapter(
            List<AppointmentHistory> appointments) {

        this.appointments = appointments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(
                        R.layout.item_appointment_history,
                        parent,
                        false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position) {

        AppointmentHistory item =
                appointments.get(position);

        holder.tvDoctorName.setText(
                item.getDoctorName());

        holder.tvSpecialty.setText(
                item.getSpecialty());

        holder.tvDate.setText(
                item.getDate());

        holder.tvTime.setText(
                item.getTime());

        holder.tvStatus.setText(
                item.getStatus());
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    static class ViewHolder
            extends RecyclerView.ViewHolder {

        TextView tvDoctorName;
        TextView tvSpecialty;
        TextView tvDate;
        TextView tvTime;
        TextView tvStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDoctorName =
                    itemView.findViewById(
                            R.id.tvDoctorName);

            tvSpecialty =
                    itemView.findViewById(
                            R.id.tvSpecialty);

            tvDate =
                    itemView.findViewById(
                            R.id.tvDate);

            tvTime =
                    itemView.findViewById(
                            R.id.tvTime);

            tvStatus =
                    itemView.findViewById(
                            R.id.tvStatus);
        }
    }
}
