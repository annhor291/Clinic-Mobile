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
import com.example.clinicmobile.utils.ProfileManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DoctorDetailActivity extends AppCompatActivity {

    private Long doctorId;
    private String doctorName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(
                androidx.core.content.ContextCompat.getColor(this, R.color.bg_top_blue));
        setContentView(R.layout.activity_doctor_detail);

        // Nhận data từ DoctorListActivity
        doctorId = getIntent().getLongExtra("doctorId", -1);
        doctorName = getIntent().getStringExtra("doctorName");
        String title = getIntent().getStringExtra("doctorTitle");
        String specialty = getIntent().getStringExtra("doctorSpecialty");
        int experience = getIntent().getIntExtra("doctorExperience", 0);
        String bio = getIntent().getStringExtra("doctorDescription");
        double fee = getIntent().getDoubleExtra("doctorFee", 0.0);

        // Bind views
        TextView tvName = findViewById(R.id.tvName);
        TextView tvSpecialty = findViewById(R.id.tvSpecialty);
        TextView tvRating = findViewById(R.id.tvRating);
        TextView tvExperience = findViewById(R.id.tvExperience);
        TextView tvDescription = findViewById(R.id.tvDescription);
        Button btnBook = findViewById(R.id.btnBook);
        ImageButton btnBack = findViewById(R.id.btnBack);

        // Hiện tên có title (BS., TS., PGS.TS...)
        tvName.setText(title != null ? title + " " + doctorName : doctorName);
        tvSpecialty.setText(specialty != null ? specialty : "");
        tvRating.setText(fee > 0
                ? String.format("💰 %,.0f đ", fee)
                : "💰 Liên hệ");
        tvExperience.setText(experience + " năm");
        tvDescription.setText(bio != null ? bio : "Chưa có thông tin giới thiệu");

        btnBack.setOnClickListener(v -> finish());

        btnBook.setOnClickListener(v -> {
            if (!ProfileManager.isProfileCompleted(this)) {
                Intent intent = new Intent(this, CompleteProfileActivity.class);
                intent.putExtra("skippable", false);
                intent.putExtra("nextAction", CompleteProfileActivity.ACTION_GO_BOOKING);
                startActivity(intent);
                return;
            }
            Intent intent = new Intent(this, AppointmentActivity.class);
            intent.putExtra("doctorId", doctorId);
            intent.putExtra("doctorName", doctorName);
            intent.putExtra("doctorTitle", title);
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
            if (item.getItemId() == R.id.nav_medical_profile) {
                startActivity(new Intent(this, MedicalProfileActivity.class));
                finish();
                return true;
            }
            if (item.getItemId() == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            }
            return false;
        });
    }

}
