package com.example.clinicmobile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clinicmobile.R;
import com.example.clinicmobile.adapters.SpecialtyAdapter;
import com.example.clinicmobile.model.Specialty;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class SpecialtyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(getResources().getColor(R.color.bg_top_blue));
        setContentView(R.layout.activity_specialty);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        setupRecyclerView();
        setupBottomNavigation();

        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.getMenu().setGroupCheckable(0, true, false);
        for (int i = 0; i < bottomNav.getMenu().size(); i++) {
            bottomNav.getMenu().getItem(i).setChecked(false);
        }
        bottomNav.getMenu().setGroupCheckable(0, true, true);
    }

    private void setupRecyclerView() {
        // TODO: thay bằng API GET /specialties
        List<Specialty> specialties = new ArrayList<>();
        specialties.add(new Specialty(1L, "Tim mạch", "Chẩn đoán và điều trị các bệnh về tim, mạch máu", "❤️"));
        specialties.add(new Specialty(2L, "Da liễu", "Điều trị các bệnh về da, tóc, móng", "🧴"));
        specialties.add(new Specialty(3L, "Nhi khoa", "Chăm sóc sức khỏe trẻ em từ sơ sinh đến 15 tuổi", "👶"));
        specialties.add(new Specialty(4L, "Thần kinh", "Chẩn đoán và điều trị bệnh lý hệ thần kinh", "🧠"));
        specialties.add(new Specialty(5L, "Xương khớp", "Điều trị các bệnh về xương, khớp, cơ", "🦴"));
        specialties.add(new Specialty(6L, "Tiêu hóa", "Điều trị bệnh lý dạ dày, đại tràng, gan mật", "🫀"));
        specialties.add(new Specialty(7L, "Mắt", "Khám và điều trị các bệnh về mắt", "👁️"));
        specialties.add(new Specialty(8L, "Tai mũi họng", "Điều trị các bệnh về tai, mũi, họng", "👂"));

        RecyclerView rvSpecialties = findViewById(R.id.rvSpecialties);
        rvSpecialties.setLayoutManager(new LinearLayoutManager(this));
        rvSpecialties.setAdapter(new SpecialtyAdapter(specialties, specialty -> {
            Intent intent = new Intent(this, DoctorListActivity.class);
            intent.putExtra("specialtyId", specialty.getId());
            intent.putExtra("specialtyName", specialty.getName());
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
