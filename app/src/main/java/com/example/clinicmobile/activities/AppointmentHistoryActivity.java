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
import com.example.clinicmobile.adapters.AppointmentHistoryAdapter;
import com.example.clinicmobile.dto.response.ApiResponse;
import com.example.clinicmobile.dto.response.AppointmentResponse;
import com.example.clinicmobile.dto.response.PageResponse;
import com.example.clinicmobile.model.AppointmentHistory;
import com.example.clinicmobile.network.ApiClient;
import com.example.clinicmobile.utils.TokenManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppointmentHistoryActivity extends AppCompatActivity {

    private RecyclerView rvAppointments;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(
                androidx.core.content.ContextCompat.getColor(this, R.color.bg_top_blue));
        setContentView(R.layout.activity_appointment_history);

        rvAppointments = findViewById(R.id.rvAppointments);
        progressBar = findViewById(R.id.progressBar);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        loadAppointments();
        setupBottomNavigation();
    }

    private void loadAppointments() {
        Long patientId = TokenManager.getProfileId(this);

        if (patientId == null || patientId == -1) {
            Toast.makeText(this, "Vui lòng hoàn thiện hồ sơ trước",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        ApiClient.getApiService()
                .getMyAppointments(patientId, 0, 20)
                .enqueue(new Callback<ApiResponse<PageResponse<AppointmentResponse>>>() {

                    @Override
                    public void onResponse(
                            Call<ApiResponse<PageResponse<AppointmentResponse>>> call,
                            Response<ApiResponse<PageResponse<AppointmentResponse>>> response) {

                        progressBar.setVisibility(View.GONE);

                        if (response.isSuccessful() && response.body() != null
                                && response.body().isSuccess()) {

                            PageResponse<AppointmentResponse> page = response.body().getData();
                            if (page != null && page.getContent() != null) {
                                setupRecyclerView(page.getContent());
                            }

                        } else {
                            Toast.makeText(AppointmentHistoryActivity.this,
                                    "Không thể tải lịch hẹn",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<ApiResponse<PageResponse<AppointmentResponse>>> call,
                            Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(AppointmentHistoryActivity.this,
                                "Lỗi kết nối: " + t.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setupRecyclerView(List<AppointmentResponse> appointments) {
        // Convert AppointmentResponse sang AppointmentHistory model cho adapter
        List<AppointmentHistory> histories = new ArrayList<>();
        for (AppointmentResponse a : appointments) {
            String statusDisplay;
            switch (a.getStatus() != null ? a.getStatus() : "") {
                case "CONFIRMED": statusDisplay = "🟢 Đã xác nhận"; break;
                case "PENDING":   statusDisplay = "🟡 Chờ xác nhận"; break;
                case "CANCELLED": statusDisplay = "🔴 Đã hủy"; break;
                case "COMPLETED": statusDisplay = "🔵 Đã hoàn thành"; break;
                default:          statusDisplay = a.getStatus() != null ? a.getStatus() : ""; break;
            }

            histories.add(new AppointmentHistory(
                    a.getDoctorName() != null ? a.getDoctorName() : "",
                    a.getSpecialtyName() != null ? a.getSpecialtyName() : "",
                    a.getAppointmentDate() != null ? a.getAppointmentDate() : "",
                    (a.getStartTime() != null ? a.getStartTime() : "")
                            + " - "
                            + (a.getEndTime() != null ? a.getEndTime() : ""),
                    statusDisplay));
        }

        rvAppointments.setLayoutManager(new LinearLayoutManager(this));
        rvAppointments.setAdapter(new AppointmentHistoryAdapter(histories));
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
            return false;
        });
    }

}
