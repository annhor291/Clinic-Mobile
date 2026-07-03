package com.example.clinicmobile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.clinicmobile.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ConfirmBookingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(getResources().getColor(R.color.bg_top_blue));
        setContentView(R.layout.activity_confirm_booking);

        // Nhận đủ data từ AppointmentActivity
        Long doctorId = getIntent().getLongExtra("doctorId", -1);
        Long timeSlotId = getIntent().getLongExtra("timeSlotId", -1);
        String doctorName = getIntent().getStringExtra("doctorName");
        String date = getIntent().getStringExtra("date");
        String time = getIntent().getStringExtra("time");

        // Bind views
        TextView tvDoctorName = findViewById(R.id.tvDoctorName);
        TextView tvDate = findViewById(R.id.tvDate);
        TextView tvTime = findViewById(R.id.tvTime);
        Button btnConfirm = findViewById(R.id.btnConfirm);
        ImageButton btnBack = findViewById(R.id.btnBack);

        tvDoctorName.setText(doctorName);
        tvDate.setText(date);
        tvTime.setText(time);

        btnBack.setOnClickListener(v -> finish());

        btnConfirm.setOnClickListener(v -> {
            // TODO: gọi API POST /appointments với doctorId, timeSlotId
            // body: { doctorId: doctorId, timeSlotId: timeSlotId }
            Intent intent = new Intent(this, BookingSuccessActivity.class);
            startActivity(intent);
        });

        setupBottomNavigation();

        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.getMenu().setGroupCheckable(0, true, false);
        for (int i = 0; i < bottomNav.getMenu().size(); i++) {
            bottomNav.getMenu().getItem(i).setChecked(false);
        }
        bottomNav.getMenu().setGroupCheckable(0, true, true);
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                startActivity(new Intent(this, MainActivity.class));
                return true;
            }
            if (item.getItemId() == R.id.nav_history) {
                startActivity(new Intent(this, AppointmentHistoryActivity.class));
                return true;
            }
            if (item.getItemId() == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            }
            if (item.getItemId() == R.id.nav_medical_profile) {
                startActivity(new Intent(this, MedicalProfileActivity.class));
                finish(); // hoặc không finish() tùy Activity
                return true;
            }
            return false;
        });
    }
}
