package com.example.clinicmobile.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clinicmobile.R;
import com.example.clinicmobile.listeners.OnFunctionClickListener;
import com.example.clinicmobile.model.QuickFunction;

import java.util.List;

public class FunctionAdapter extends RecyclerView.Adapter<FunctionAdapter.FunctionViewHolder> {


    private List<QuickFunction> functions;
    private OnFunctionClickListener listener;

    public FunctionAdapter(List<QuickFunction> functions, OnFunctionClickListener listener) {
        this.functions = functions;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FunctionAdapter.FunctionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_function, parent, false);
        return new FunctionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FunctionAdapter.FunctionViewHolder holder, int position) {
        QuickFunction function = functions.get(position);

        holder.imgMenuIcon.setImageResource(function.getIconResId());
        holder.tvTitle.setText(function.getTitle());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onFunctionClick(function);
            }
        });

    }

    @Override
    public int getItemCount() {
        return functions.size();
    }

    static class FunctionViewHolder extends RecyclerView.ViewHolder {

        ImageView imgMenuIcon;
        TextView tvTitle;

        public FunctionViewHolder(@NonNull View itemView) {
            super(itemView);
            imgMenuIcon = itemView.findViewById(R.id.imgMenuIcon);
            tvTitle = itemView.findViewById(R.id.tvTitle);
        }
    }
}
