package com.example.clinicmobile.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.clinicmobile.R;
import com.example.clinicmobile.dto.response.ApiResponse;
import com.example.clinicmobile.dto.response.PatientResponse;
import com.example.clinicmobile.network.ApiClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MedicalProfileActivity  extends AppCompatActivity {

    private TextView tvName, tvPhone, tvDob, tvGender, tvBloodType, tvCccd, tvBhyt, tvAddress;
    private ProgressBar progressBar;
    private Button btnEdit;
    private PatientResponse currentPatient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(
                androidx.core.content.ContextCompat.getColor(this, R.color.bg_top_blue));
        setContentView(R.layout.activity_medical_profile);

        initViews();

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        btnEdit.setOnClickListener(v -> {
            if (currentPatient != null) {
                Intent intent = new Intent(this, EditProfileActivity.class);
                intent.putExtra("patientId", currentPatient.getId());
                intent.putExtra("fullName", currentPatient.getFullName());
                intent.putExtra("phone", currentPatient.getPhone());
                intent.putExtra("dob", currentPatient.getDateOfBirth());
                intent.putExtra("gender", currentPatient.getGender());
                intent.putExtra("address", currentPatient.getAddress());
                intent.putExtra("bloodType", currentPatient.getBloodType());
                intent.putExtra("insuranceNumber", currentPatient.getInsuranceNumber());
                startActivity(intent);
            }
        });

        setupBottomNavigation();
        loadProfile();
    }

    private void initViews() {
        tvName = findViewById(R.id.tvName);
        tvPhone = findViewById(R.id.tvPhone);
        tvDob = findViewById(R.id.tvDob);
        tvGender = findViewById(R.id.tvGender);
        tvBloodType = findViewById(R.id.tvBloodType);
        tvCccd = findViewById(R.id.tvCccd);
        tvBhyt = findViewById(R.id.tvBhyt);
        tvAddress = findViewById(R.id.tvAddress);
        progressBar = findViewById(R.id.progressBar);
        btnEdit = findViewById(R.id.btnEdit);
    }

    private void loadProfile() {
        progressBar.setVisibility(View.VISIBLE);

        ApiClient.getApiService().getMyProfile().enqueue(
                new Callback<ApiResponse<PatientResponse>>() {

                    @Override
                    public void onResponse(Call<ApiResponse<PatientResponse>> call,
                                           Response<ApiResponse<PatientResponse>> response) {
                        progressBar.setVisibility(View.GONE);

                        if (response.isSuccessful() && response.body() != null
                                && response.body().isSuccess()) {

                            currentPatient = response.body().getData();
                            bindData(currentPatient);

                        } else {
                            Toast.makeText(MedicalProfileActivity.this,
                                    "Không thể tải hồ sơ", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<PatientResponse>> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(MedicalProfileActivity.this,
                                "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void bindData(PatientResponse patient) {
        tvName.setText(patient.getFullName() != null
                ? patient.getFullName() : "Chưa cập nhật");
        tvPhone.setText(patient.getPhone() != null
                ? patient.getPhone() : "Chưa cập nhật");
        tvDob.setText(patient.getDateOfBirth() != null
                ? patient.getDateOfBirth() : "Chưa cập nhật");

        String gender = patient.getGender();
        if ("MALE".equals(gender)) tvGender.setText("Nam");
        else if ("FEMALE".equals(gender)) tvGender.setText("Nữ");
        else tvGender.setText("Khác");

        tvBloodType.setText(patient.getBloodType() != null
                ? patient.getBloodType() : "Chưa cập nhật");
        tvCccd.setText("Chưa cập nhật"); // BE chưa có field CCCD
        tvBhyt.setText(patient.getInsuranceNumber() != null
                ? patient.getInsuranceNumber() : "Chưa cập nhật");
        tvAddress.setText(patient.getAddress() != null
                ? patient.getAddress() : "Chưa cập nhật");
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
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
