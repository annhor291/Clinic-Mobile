package com.example.clinicmobile.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.clinicmobile.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MedicalProfileActivity  extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(getResources().getColor(R.color.bg_top_blue));
        setContentView(R.layout.activity_medical_profile);

        loadProfileData();

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        Button btnEdit = findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(v ->
                startActivity(new Intent(this, EditProfileActivity.class)));

        setupBottomNavigation();
    }

    private void loadProfileData() {
        SharedPreferences prefs = getSharedPreferences("clinic_profile_data", MODE_PRIVATE);

        TextView tvName = findViewById(R.id.tvName);
        TextView tvDob = findViewById(R.id.tvDob);
        TextView tvGender = findViewById(R.id.tvGender);
        TextView tvBloodType = findViewById(R.id.tvBloodType);
        TextView tvCccd = findViewById(R.id.tvCccd);
        TextView tvBhyt = findViewById(R.id.tvBhyt);
        TextView tvPhone = findViewById(R.id.tvPhone);
        TextView tvAddress = findViewById(R.id.tvAddress);

        tvName.setText(prefs.getString("name", "Chưa cập nhật"));
        tvDob.setText(prefs.getString("dob", "Chưa cập nhật"));
        tvGender.setText(prefs.getString("gender", "Chưa cập nhật"));
        tvPhone.setText(prefs.getString("phone", "Chưa cập nhật"));
        tvAddress.setText(prefs.getString("address", "Chưa cập nhật"));
        tvBloodType.setText(prefs.getString("bloodType", "Chưa cập nhật"));
        tvCccd.setText(prefs.getString("cccd", "Chưa cập nhật"));
        tvBhyt.setText(prefs.getString("bhyt", "Chưa cập nhật"));
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);

        // Set selected trước khi gắn listener để không trigger callback
        bottomNav.setSelectedItemId(R.id.nav_medical_profile);

        bottomNav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return true;
            }
            if (item.getItemId() == R.id.nav_history) {
                startActivity(new Intent(this, AppointmentHistoryActivity.class));
                finish();
                return true;
            }
            if (item.getItemId() == R.id.nav_medical_profile) {
                // Đang ở chính màn này, không làm gì cả
                return true;
            }
            if (item.getItemId() == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                finish();
                return true;
            }
            return false;
        });
    }
}
