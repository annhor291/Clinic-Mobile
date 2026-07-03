package com.example.clinicmobile.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clinicmobile.R;
import com.example.clinicmobile.model.DateItem;

import java.util.List;

public class DateAdapter extends RecyclerView.Adapter<DateAdapter.DateViewHolder> {

    private List<DateItem> dates;
    private OnDateClickListener listener;
    private int selectedPosition = 0;

    public interface OnDateClickListener {
        void onDateClick(DateItem date);
    }

    public DateAdapter(List<DateItem> dates, OnDateClickListener listener) {
        this.dates = dates;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_date, parent, false);
        return new DateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DateViewHolder holder, int position) {
        DateItem date = dates.get(position);

        holder.tvDayOfWeek.setText(date.getDayOfWeek());
        holder.tvDayOfMonth.setText(date.getDayOfMonth());

        // Đổi màu chữ khi được chọn
        if (date.isSelected()) {
            holder.tvDayOfWeek.setTextColor(
                    holder.itemView.getContext().getColor(android.R.color.white));
            holder.tvDayOfMonth.setTextColor(
                    holder.itemView.getContext().getColor(android.R.color.white));
        } else {
            holder.tvDayOfWeek.setTextColor(
                    holder.itemView.getContext().getColor(R.color.text_secondary));
            holder.tvDayOfMonth.setTextColor(
                    holder.itemView.getContext().getColor(R.color.text_primary));
        }

        holder.itemView.setSelected(date.isSelected());

        holder.itemView.setOnClickListener(v -> {
            // Bỏ chọn item cũ
            dates.get(selectedPosition).setSelected(false);
            notifyItemChanged(selectedPosition);

            // Chọn item mới
            selectedPosition = holder.getAdapterPosition();
            date.setSelected(true);
            notifyItemChanged(selectedPosition);

            if (listener != null) listener.onDateClick(date);
        });
    }

    @Override
    public int getItemCount() {
        return dates.size();
    }

    static class DateViewHolder extends RecyclerView.ViewHolder {
        TextView tvDayOfWeek, tvDayOfMonth;

        public DateViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDayOfWeek = itemView.findViewById(R.id.tvDayOfWeek);
            tvDayOfMonth = itemView.findViewById(R.id.tvDayOfMonth);
        }
    }
}
