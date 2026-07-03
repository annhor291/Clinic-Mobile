package com.example.clinicmobile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clinicmobile.R;
import com.example.clinicmobile.adapters.AppointmentHistoryAdapter;
import com.example.clinicmobile.model.AppointmentHistory;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class AppointmentHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(getResources().getColor(R.color.bg_top_blue));

        setContentView(R.layout.activity_appointment_history);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        setupBottomNavigation();
        loadAppointments();
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setSelectedItemId(R.id.nav_history);

        bottomNav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return true;
            }
            if (item.getItemId() == R.id.nav_medical_profile) {
                startActivity(new Intent(this, MedicalProfileActivity.class));
                finish();
                return true;
            }
            if (item.getItemId() == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                finish();
                return true;
            }
            return false; // ← đổi thành false
        });
    }

    private void loadAppointments() {
        List<AppointmentHistory> appointments = new ArrayList<>();

        appointments.add(new AppointmentHistory(
                "BS Nguyễn Văn A", "Tim mạch",
                "20/06/2026", "08:00 - 08:30", "🟢 Đã xác nhận"));

        appointments.add(new AppointmentHistory(
                "BS Trần Văn B", "Da liễu",
                "22/06/2026", "09:00 - 09:30", "🔴 Đã hủy"));

        appointments.add(new AppointmentHistory(
                "BS Lê Văn C", "Nhi khoa",
                "25/06/2026", "10:00 - 10:30", "🔵 Đã hoàn thành"));

        RecyclerView rvAppointments = findViewById(R.id.rvAppointments);
        rvAppointments.setLayoutManager(new LinearLayoutManager(this));
        rvAppointments.setAdapter(new AppointmentHistoryAdapter(appointments));
    }

}
