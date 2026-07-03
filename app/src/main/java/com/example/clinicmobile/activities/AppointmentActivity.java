package com.example.clinicmobile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clinicmobile.R;
import com.example.clinicmobile.adapters.DateAdapter;
import com.example.clinicmobile.adapters.TimeSlotAdapter;
import com.example.clinicmobile.model.DateItem;
import com.example.clinicmobile.model.TimeSlot;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AppointmentActivity extends AppCompatActivity {

    private Long doctorId;
    private String doctorName;
    private String selectedDate;
    private TimeSlotAdapter timeSlotAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(getResources().getColor(R.color.bg_top_blue));
        setContentView(R.layout.activity_appointment);

        doctorId = getIntent().getLongExtra("doctorId", -1);
        doctorName = getIntent().getStringExtra("doctorName");

        TextView tvDoctor = findViewById(R.id.tvDoctor);
        tvDoctor.setText("Đặt lịch với " + doctorName);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        setupDatePicker();
        setupTimeSlots(getDefaultDate());
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

        RecyclerView rvDates = findViewById(R.id.rvDates);
        DateAdapter dateAdapter = new DateAdapter(dates, date -> {
            selectedDate = date.getFullDate();
            // TODO: gọi API lấy time slot theo doctorId + selectedDate
            setupTimeSlots(selectedDate);
        });

        rvDates.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvDates.setAdapter(dateAdapter);
    }

    private String getDefaultDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(Calendar.getInstance().getTime());
    }

    private void setupTimeSlots(String date) {
        // TODO: thay bằng API call GET /timeslots?doctorId=doctorId&date=date
        List<TimeSlot> slots = new ArrayList<>();
        slots.add(new TimeSlot(1L, date, "08:00", "08:30", "AVAILABLE"));
        slots.add(new TimeSlot(2L, date, "09:00", "09:30", "BOOKED"));
        slots.add(new TimeSlot(3L, date, "10:00", "10:30", "BLOCKED"));
        slots.add(new TimeSlot(4L, date, "11:00", "11:30", "AVAILABLE"));

        RecyclerView rvTimeSlots = findViewById(R.id.rvTimeSlots);
        timeSlotAdapter = new TimeSlotAdapter(slots, slot -> {
            if ("AVAILABLE".equals(slot.getStatus())) {
                Intent intent = new Intent(AppointmentActivity.this, ConfirmBookingActivity.class);
                intent.putExtra("doctorId", doctorId);
                intent.putExtra("doctorName", doctorName);
                intent.putExtra("date", selectedDate);
                intent.putExtra("time", slot.getStartTime() + " - " + slot.getEndTime());
                intent.putExtra("timeSlotId", slot.getId());
                startActivity(intent);
            } else {
                Toast.makeText(this, "Khung giờ này không khả dụng", Toast.LENGTH_SHORT).show();
            }
        });

        rvTimeSlots.setLayoutManager(new LinearLayoutManager(this));
        rvTimeSlots.setAdapter(timeSlotAdapter);
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
