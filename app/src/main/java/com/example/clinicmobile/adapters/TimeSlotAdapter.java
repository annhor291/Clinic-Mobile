package com.example.clinicmobile.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clinicmobile.R;
import com.example.clinicmobile.listeners.TimeSlotClickListener;
import com.example.clinicmobile.model.TimeSlot;

import java.util.List;

public class TimeSlotAdapter  extends RecyclerView.Adapter<TimeSlotAdapter.TimeSlotViewHolder>{

    private final List<TimeSlot> slots;

    private final TimeSlotClickListener listener;

    public TimeSlotAdapter(
            List<TimeSlot> slots,
            TimeSlotClickListener listener) {

        this.slots = slots;
        this.listener = listener;
    }


    @NonNull
    @Override
    public TimeSlotAdapter.TimeSlotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_timeslot, parent, false);

        return new TimeSlotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeSlotAdapter.TimeSlotViewHolder holder, int position) {
        TimeSlot slot = slots.get(position);

        holder.tvTime.setText(slot.getStartTime() + " - " + slot.getEndTime());

        holder.tvStatus.setText(slot.getStatus());

        CardView cardView = (CardView) holder.itemView;

        switch (slot.getStatus()) {

            case "AVAILABLE":

                holder.tvStatus.setText("🟢 Còn trống");
                holder.itemView.setAlpha(1f);

                cardView.setCardBackgroundColor(
                        Color.parseColor("#E8F5E9"));

                cardView.setCardElevation(12f);
                cardView.setRadius(20f);

                break;

            case "BOOKED":

                holder.tvStatus.setText("🔴 Đã đặt");
                holder.itemView.setAlpha(0.7f);

                cardView.setCardBackgroundColor(
                        Color.parseColor("#FFEBEE"));

                cardView.setCardElevation(4f);

                break;

            case "BLOCKED":

                holder.tvStatus.setText("🚫 Không khả dụng");
                holder.itemView.setAlpha(0.7f);

                cardView.setCardBackgroundColor(
                        Color.parseColor("#FFF8E1"));

                cardView.setCardElevation(2f);

                break;
        }

        holder.itemView.setOnClickListener(v -> listener.onTimeSlotClick(slot));

    }

    @Override
    public int getItemCount() {
        return slots.size();
    }

    static class TimeSlotViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime;
        TextView tvStatus;

        public TimeSlotViewHolder(
                @NonNull View itemView) {

            super(itemView);

            tvTime = itemView.findViewById(R.id.tvTime);

            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}
