package com.example.clinicmobile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.clinicmobile.R;
import com.example.clinicmobile.dto.request.AppointmentRequest;
import com.example.clinicmobile.dto.response.ApiResponse;
import com.example.clinicmobile.dto.response.AppointmentResponse;
import com.example.clinicmobile.network.ApiClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmBookingActivity extends AppCompatActivity {

    private Button btnConfirm;
    private Long doctorId, timeSlotId, patientId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(
                androidx.core.content.ContextCompat.getColor(this, R.color.bg_top_blue));
        setContentView(R.layout.activity_confirm_booking);

        doctorId = getIntent().getLongExtra("doctorId", -1);
        timeSlotId = getIntent().getLongExtra("timeSlotId", -1);
        patientId = getIntent().getLongExtra("patientId", -1);
        String doctorName = getIntent().getStringExtra("doctorName");
        String doctorTitle = getIntent().getStringExtra("doctorTitle");
        String date = getIntent().getStringExtra("date");
        String time = getIntent().getStringExtra("time");

        TextView tvDoctorName = findViewById(R.id.tvDoctorName);
        TextView tvDate = findViewById(R.id.tvDate);
        TextView tvTime = findViewById(R.id.tvTime);
        btnConfirm = findViewById(R.id.btnConfirm);
        ImageButton btnBack = findViewById(R.id.btnBack);

        tvDoctorName.setText(doctorTitle != null
                ? doctorTitle + " " + doctorName : doctorName);
        tvDate.setText(date);
        tvTime.setText(time);

        btnBack.setOnClickListener(v -> finish());
        btnConfirm.setOnClickListener(v -> handleConfirm());

        setupBottomNavigation();

        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.getMenu().setGroupCheckable(0, true, false);
        for (int i = 0; i < bottomNav.getMenu().size(); i++) {
            bottomNav.getMenu().getItem(i).setChecked(false);
        }
        bottomNav.getMenu().setGroupCheckable(0, true, true);
    }

    private void handleConfirm() {
        btnConfirm.setEnabled(false);
        btnConfirm.setText("Đang đặt lịch...");

        AppointmentRequest request = new AppointmentRequest(patientId, timeSlotId, null);

        ApiClient.getApiService().createAppointment(request).enqueue(
                new Callback<ApiResponse<AppointmentResponse>>() {

                    @Override
                    public void onResponse(Call<ApiResponse<AppointmentResponse>> call,
                                           Response<ApiResponse<AppointmentResponse>> response) {
                        btnConfirm.setEnabled(true);
                        btnConfirm.setText("XÁC NHẬN ĐẶT LỊCH");

                        if (response.isSuccessful() && response.body() != null
                                && response.body().isSuccess()) {
                            startActivity(new Intent(
                                    ConfirmBookingActivity.this,
                                    BookingSuccessActivity.class));
                            finish();
                        } else {
                            String errorMsg = "Đặt lịch thất bại";
                            try {
                                if (response.errorBody() != null) {
                                    JSONObject json = new JSONObject(
                                            response.errorBody().string());
                                    if (json.has("message"))
                                        errorMsg = json.getString("message");
                                }
                            } catch (Exception e) { e.printStackTrace(); }
                            Toast.makeText(ConfirmBookingActivity.this,
                                    errorMsg, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<AppointmentResponse>> call,
                                          Throwable t) {
                        btnConfirm.setEnabled(true);
                        btnConfirm.setText("XÁC NHẬN ĐẶT LỊCH");
                        Toast.makeText(ConfirmBookingActivity.this,
                                "Lỗi kết nối: " + t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
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
