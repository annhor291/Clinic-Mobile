package com.example.clinicmobile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clinicmobile.R;
import com.example.clinicmobile.adapters.DoctorListAdapter;
import com.example.clinicmobile.model.Doctor;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class DoctorListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(getResources().getColor(R.color.bg_top_blue));
        setContentView(R.layout.activity_doctor_list);

        Long specialtyId = getIntent().getLongExtra("specialtyId", -1);
        String specialtyName = getIntent().getStringExtra("specialtyName");

        TextView tvSpecialtyName = findViewById(R.id.tvSpecialtyName);
        tvSpecialtyName.setText("Bác sĩ " + specialtyName);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        setupRecyclerView(specialtyId);
        setupBottomNavigation();

        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.getMenu().setGroupCheckable(0, true, false);
        for (int i = 0; i < bottomNav.getMenu().size(); i++) {
            bottomNav.getMenu().getItem(i).setChecked(false);
        }
        bottomNav.getMenu().setGroupCheckable(0, true, true);
    }

    private void setupRecyclerView(Long specialtyId) {
        // TODO: thay bằng API GET /doctors?specialtyId=specialtyId
        List<Doctor> doctors = new ArrayList<>();
        doctors.add(new Doctor(1L, "BS Nguyễn Văn A", "Tim mạch", 4.9, 10, "Chuyên gia tim mạch với hơn 10 năm kinh nghiệm."));
        doctors.add(new Doctor(2L, "BS Trần Thị B", "Tim mạch", 4.7, 7, "Bác sĩ tim mạch can thiệp, từng công tác tại Viện Tim TP.HCM."));
        doctors.add(new Doctor(3L, "BS Lê Văn C", "Tim mạch", 4.8, 15, "Phó giáo sư, tiến sĩ chuyên ngành tim mạch học."));

        RecyclerView rvDoctors = findViewById(R.id.rvDoctors);
        rvDoctors.setLayoutManager(new LinearLayoutManager(this));
        rvDoctors.setAdapter(new DoctorListAdapter(doctors, doctor -> {
            Intent intent = new Intent(this, DoctorDetailActivity.class);
            intent.putExtra("doctorId", doctor.getId());
            intent.putExtra("doctorName", doctor.getName());
            intent.putExtra("doctorSpecialty", doctor.getSpecialty());
            intent.putExtra("doctorRating", doctor.getRating());
            intent.putExtra("doctorExperience", doctor.getExperienceYears());
            intent.putExtra("doctorDescription", doctor.getDescription());
            startActivity(intent);
        }));
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
