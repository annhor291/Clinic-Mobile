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

public class DoctorDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(getResources().getColor(R.color.bg_top_blue));
        setContentView(R.layout.activity_doctor_detail);

        // Nhận data từ MainActivity
        Long doctorId = getIntent().getLongExtra("doctorId", -1);
        String name = getIntent().getStringExtra("doctorName");
        String specialty = getIntent().getStringExtra("doctorSpecialty");
        double rating = getIntent().getDoubleExtra("doctorRating", 0);
        int experience = getIntent().getIntExtra("doctorExperience", 0);
        String description = getIntent().getStringExtra("doctorDescription");

        // Bind views
        TextView tvName = findViewById(R.id.tvName);
        TextView tvSpecialty = findViewById(R.id.tvSpecialty);
        TextView tvRating = findViewById(R.id.tvRating);
        TextView tvExperience = findViewById(R.id.tvExperience);
        TextView tvDescription = findViewById(R.id.tvDescription);
        Button btnBook = findViewById(R.id.btnBook);
        ImageButton btnBack = findViewById(R.id.btnBack);

        tvName.setText(name);
        tvSpecialty.setText(specialty);
        tvRating.setText("⭐ " + rating);
        tvExperience.setText(experience + " năm");
        tvDescription.setText(description);

        btnBack.setOnClickListener(v -> finish());

        btnBook.setOnClickListener(v -> {
            if (com.example.clinicmobile.utils.ProfileManager.isProfileCompleted(this)) {
                Intent intent = new Intent(DoctorDetailActivity.this, AppointmentActivity.class);
                intent.putExtra("doctorId", doctorId);
                intent.putExtra("doctorName", name);
                startActivity(intent);
            } else {
                Intent intent = new Intent(DoctorDetailActivity.this, CompleteProfileActivity.class);
                intent.putExtra("skippable", false);
                intent.putExtra("nextAction", CompleteProfileActivity.ACTION_GO_BOOKING);
                startActivity(intent);
            }

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
