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
import com.example.clinicmobile.adapters.DateAdapter;
import com.example.clinicmobile.adapters.TimeSlotAdapter;
import com.example.clinicmobile.dto.response.ApiResponse;
import com.example.clinicmobile.dto.response.TimeSlotResponse;
import com.example.clinicmobile.model.DateItem;
import com.example.clinicmobile.model.TimeSlot;
import com.example.clinicmobile.network.ApiClient;
import com.example.clinicmobile.utils.TokenManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppointmentActivity extends AppCompatActivity {

    private Long doctorId;
    private String doctorName;
    private String doctorTitle;
    private String selectedDate;
    private RecyclerView rvTimeSlots;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(
                androidx.core.content.ContextCompat.getColor(this, R.color.bg_top_blue));
        setContentView(R.layout.activity_appointment);

        doctorId = getIntent().getLongExtra("doctorId", -1);
        doctorName = getIntent().getStringExtra("doctorName");
        doctorTitle = getIntent().getStringExtra("doctorTitle");

        TextView tvDoctor = findViewById(R.id.tvDoctor);
        tvDoctor.setText("Đặt lịch với "
                + (doctorTitle != null ? doctorTitle + " " : "")
                + doctorName);

        rvTimeSlots = findViewById(R.id.rvTimeSlots);
        progressBar = findViewById(R.id.progressBar);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        setupDatePicker();
        setupBottomNavigation();

        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.getMenu().setGroupCheckable(0, true, false);
        for (int i = 0; i < bottomNav.getMenu().size(); i++) {
            bottomNav.getMenu().getItem(i).setChecked(false);
        }
        bottomNav.getMenu().setGroupCheckable(0, true, true);
    }

    private void setupDatePicker() {
        List<DateItem> dates = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        String[] dayNames = {"CN", "T2", "T3", "T4", "T5", "T6", "T7"};
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        for (int i = 0; i < 7; i++) {
            String dayOfWeek = dayNames[calendar.get(Calendar.DAY_OF_WEEK) - 1];
            String dayOfMonth = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
            String fullDate = sdf.format(calendar.getTime());
            dates.add(new DateItem(dayOfWeek, dayOfMonth, fullDate, i == 0));
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        selectedDate = dates.get(0).getFullDate();
        loadTimeSlots(selectedDate);

        RecyclerView rvDates = findViewById(R.id.rvDates);
        DateAdapter dateAdapter = new DateAdapter(dates, date -> {
            selectedDate = date.getFullDate();
            loadTimeSlots(selectedDate);
        });

        rvDates.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvDates.setAdapter(dateAdapter);
    }

    private void loadTimeSlots(String date) {
        progressBar.setVisibility(View.VISIBLE);
        rvTimeSlots.setVisibility(View.GONE);

        ApiClient.getApiService()
                .getAvailableTimeSlots(doctorId, date)
                .enqueue(new Callback<ApiResponse<List<TimeSlotResponse>>>() {

                    @Override
                    public void onResponse(Call<ApiResponse<List<TimeSlotResponse>>> call,
                                           Response<ApiResponse<List<TimeSlotResponse>>> response) {
                        progressBar.setVisibility(View.GONE);
                        rvTimeSlots.setVisibility(View.VISIBLE);

                        if (response.isSuccessful() && response.body() != null
                                && response.body().isSuccess()) {

                            List<TimeSlotResponse> slots = response.body().getData();
                            if (slots != null && !slots.isEmpty()) {
                                setupTimeSlots(slots);
                            } else {
                                Toast.makeText(AppointmentActivity.this,
                                        "Không có lịch trống vào ngày này",
                                        Toast.LENGTH_SHORT).show();
                                rvTimeSlots.setAdapter(null);
                            }

                        } else {
                            Toast.makeText(AppointmentActivity.this,
                                    "Không thể tải lịch khám",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<List<TimeSlotResponse>>> call,
                                          Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        rvTimeSlots.setVisibility(View.VISIBLE);
                        Toast.makeText(AppointmentActivity.this,
                                "Lỗi kết nối: " + t.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setupTimeSlots(List<TimeSlotResponse> slotResponses) {
        // Convert TimeSlotResponse sang TimeSlot model cho adapter
        List<TimeSlot> slots = new ArrayList<>();
        for (TimeSlotResponse r : slotResponses) {
            slots.add(new TimeSlot(
                    r.getId(),
                    r.getSlotDate(),
                    r.getStartTime(),
                    r.getEndTime(),
                    r.getStatus()));
        }

        TimeSlotAdapter adapter = new TimeSlotAdapter(slots, slot -> {
            if ("AVAILABLE".equals(slot.getStatus())) {
                Intent intent = new Intent(AppointmentActivity.this,
                        ConfirmBookingActivity.class);
                intent.putExtra("doctorId", doctorId);
                intent.putExtra("doctorName", doctorName);
                intent.putExtra("doctorTitle", doctorTitle);
                intent.putExtra("date", selectedDate);
                intent.putExtra("time", slot.getStartTime() + " - " + slot.getEndTime());
                intent.putExtra("timeSlotId", slot.getId());
                intent.putExtra("patientId", TokenManager.getProfileId(this));
                startActivity(intent);
            } else {
                Toast.makeText(this, "Khung giờ này không khả dụng",
                        Toast.LENGTH_SHORT).show();
            }
        });

        rvTimeSlots.setLayoutManager(new LinearLayoutManager(this));
        rvTimeSlots.setAdapter(adapter);
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
