package com.example.clinicmobile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clinicmobile.R;
import com.example.clinicmobile.adapters.SpecialtyAdapter;
import com.example.clinicmobile.dto.response.ApiResponse;
import com.example.clinicmobile.dto.response.SpecialtyResponse;
import com.example.clinicmobile.model.Specialty;
import com.example.clinicmobile.network.ApiClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SpecialtyActivity extends AppCompatActivity {

    private RecyclerView rvSpecialties;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(
                androidx.core.content.ContextCompat.getColor(this, R.color.bg_top_blue));
        setContentView(R.layout.activity_specialty);

        rvSpecialties = findViewById(R.id.rvSpecialties);
        progressBar = findViewById(R.id.progressBar);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        loadSpecialties();
        setupBottomNavigation();

        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.getMenu().setGroupCheckable(0, true, false);
        for (int i = 0; i < bottomNav.getMenu().size(); i++) {
            bottomNav.getMenu().getItem(i).setChecked(false);
        }
        bottomNav.getMenu().setGroupCheckable(0, true, true);
    }

    private void loadSpecialties() {
        progressBar.setVisibility(View.VISIBLE);

        ApiClient.getApiService().getSpecialties().enqueue(
                new Callback<ApiResponse<List<SpecialtyResponse>>>() {

                    @Override
                    public void onResponse(Call<ApiResponse<List<SpecialtyResponse>>> call,
                                           Response<ApiResponse<List<SpecialtyResponse>>> response) {
                        progressBar.setVisibility(View.GONE);

                        if (response.isSuccessful() && response.body() != null
                                && response.body().isSuccess()) {

                            List<SpecialtyResponse> specialties = response.body().getData();
                            setupRecyclerView(specialties);

                        } else {
                            Toast.makeText(SpecialtyActivity.this,
                                    "Không thể tải danh sách chuyên khoa",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<List<SpecialtyResponse>>> call,
                                          Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(SpecialtyActivity.this,
                                "Lỗi kết nối: " + t.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setupRecyclerView(List<SpecialtyResponse> specialties) {
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
