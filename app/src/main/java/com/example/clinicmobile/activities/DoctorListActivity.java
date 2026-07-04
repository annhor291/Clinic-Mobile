package com.example.clinicmobile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clinicmobile.R;
import com.example.clinicmobile.adapters.DoctorListAdapter;
import com.example.clinicmobile.dto.response.ApiResponse;
import com.example.clinicmobile.dto.response.DoctorResponse;
import com.example.clinicmobile.dto.response.PageResponse;
import com.example.clinicmobile.model.Doctor;
import com.example.clinicmobile.network.ApiClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorListActivity extends AppCompatActivity {

    private RecyclerView rvDoctors;
    private ProgressBar progressBar;
    private Long specialtyId;
    private String specialtyName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(
                androidx.core.content.ContextCompat.getColor(this, R.color.bg_top_blue));
        setContentView(R.layout.activity_doctor_list);

        specialtyId = getIntent().getLongExtra("specialtyId", -1);
        specialtyName = getIntent().getStringExtra("specialtyName");

        TextView tvSpecialtyName = findViewById(R.id.tvSpecialtyName);
        tvSpecialtyName.setText("Bác sĩ " + specialtyName);

        rvDoctors = findViewById(R.id.rvDoctors);
        progressBar = findViewById(R.id.progressBar);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        loadDoctors();
        setupBottomNavigation();

        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.getMenu().setGroupCheckable(0, true, false);
        for (int i = 0; i < bottomNav.getMenu().size(); i++) {
            bottomNav.getMenu().getItem(i).setChecked(false);
        }
        bottomNav.getMenu().setGroupCheckable(0, true, true);
    }

    private void loadDoctors() {
        progressBar.setVisibility(View.VISIBLE);

        ApiClient.getApiService()
                .getDoctorsBySpecialty(specialtyId, 0, 20)
                .enqueue(new Callback<ApiResponse<PageResponse<DoctorResponse>>>() {

                    @Override
                    public void onResponse(
                            Call<ApiResponse<PageResponse<DoctorResponse>>> call,
                            Response<ApiResponse<PageResponse<DoctorResponse>>> response) {

                        progressBar.setVisibility(View.GONE);

                        if (response.isSuccessful() && response.body() != null
                                && response.body().isSuccess()) {

                            PageResponse<DoctorResponse> page = response.body().getData();
                            if (page != null && page.getContent() != null
                                    && !page.getContent().isEmpty()) {
                                setupRecyclerView(page.getContent());
                            } else {
                                Toast.makeText(DoctorListActivity.this,
                                        "Không có bác sĩ nào trong chuyên khoa này",
                                        Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(DoctorListActivity.this,
                                    "Không thể tải danh sách bác sĩ",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<ApiResponse<PageResponse<DoctorResponse>>> call,
                            Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(DoctorListActivity.this,
                                "Lỗi kết nối: " + t.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setupRecyclerView(java.util.List<DoctorResponse> doctors) {
        rvDoctors.setLayoutManager(new LinearLayoutManager(this));
        rvDoctors.setAdapter(new DoctorListAdapter(doctors, doctor -> {
            Intent intent = new Intent(this, DoctorDetailActivity.class);
            intent.putExtra("doctorId", doctor.getId());
            intent.putExtra("doctorName", doctor.getFullName());
            intent.putExtra("doctorTitle", doctor.getTitle());
            intent.putExtra("doctorSpecialty", doctor.getSpecialtyName());
            intent.putExtra("doctorExperience",
                    doctor.getExperienceYears() != null ? doctor.getExperienceYears() : 0);
            intent.putExtra("doctorDescription", doctor.getBio());
            intent.putExtra("doctorFee",
                    doctor.getConsultationFee() != null
                            ? doctor.getConsultationFee() : 0.0);
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