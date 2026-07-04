package com.example.clinicmobile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clinicmobile.R;
import com.example.clinicmobile.adapters.DoctorListAdapter;
import com.example.clinicmobile.adapters.SpecialtyAdapter;
import com.example.clinicmobile.dto.response.ApiResponse;
import com.example.clinicmobile.dto.response.DoctorResponse;
import com.example.clinicmobile.dto.response.PageResponse;
import com.example.clinicmobile.dto.response.SpecialtyResponse;
import com.example.clinicmobile.network.ApiClient;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    private TextInputEditText etSearch;
    private RecyclerView rvResults;
    private ProgressBar progressBar;
    private TextView tvEmpty;
    private TabLayout tabLayout;

    private List<SpecialtyResponse> allSpecialties = new ArrayList<>();
    private List<DoctorResponse> allDoctors = new ArrayList<>();
    private int currentTab = 0; // 0 = chuyên khoa, 1 = bác sĩ

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(
                androidx.core.content.ContextCompat.getColor(this, R.color.bg_top_blue));
        setContentView(R.layout.activity_search);

        etSearch = findViewById(R.id.etSearch);
        rvResults = findViewById(R.id.rvResults);
        progressBar = findViewById(R.id.progressBar);
        tvEmpty = findViewById(R.id.tvEmpty);
        tabLayout = findViewById(R.id.tabLayout);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // Setup tabs
        tabLayout.addTab(tabLayout.newTab().setText("Chuyên khoa"));
        tabLayout.addTab(tabLayout.newTab().setText("Bác sĩ"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentTab = tab.getPosition();
                String keyword = etSearch.getText().toString().trim();
                filterAndShow(keyword);
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });

        // Nhận keyword từ MainActivity nếu có
        String keyword = getIntent().getStringExtra("keyword");
        if (keyword != null && !keyword.isEmpty()) {
            etSearch.setText(keyword);
        }

        // Search khi bấm nút tìm kiếm trên bàn phím
        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String kw = etSearch.getText().toString().trim();
                filterAndShow(kw);
            }
            return true;
        });

        // Load data trước, sau đó filter
        loadAllData();
    }

    private void loadAllData() {
        progressBar.setVisibility(View.VISIBLE);

        // Load chuyên khoa
        ApiClient.getApiService().getSpecialties().enqueue(
                new Callback<ApiResponse<List<SpecialtyResponse>>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<List<SpecialtyResponse>>> call,
                                           Response<ApiResponse<List<SpecialtyResponse>>> response) {
                        if (response.isSuccessful() && response.body() != null
                                && response.body().isSuccess()) {
                            allSpecialties = response.body().getData();
                        }
                        loadDoctors();
                    }
                    @Override
                    public void onFailure(Call<ApiResponse<List<SpecialtyResponse>>> call, Throwable t) {
                        loadDoctors();
                    }
                });
    }

    private void loadDoctors() {
        // Load tất cả bác sĩ (không filter theo chuyên khoa)
        ApiClient.getApiService().getDoctorsBySpecialty(null, 0, 50).enqueue(
                new Callback<ApiResponse<PageResponse<DoctorResponse>>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<PageResponse<DoctorResponse>>> call,
                                           Response<ApiResponse<PageResponse<DoctorResponse>>> response) {
                        progressBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null
                                && response.body().isSuccess()
                                && response.body().getData() != null) {
                            allDoctors = response.body().getData().getContent();
                        }
                        // Sau khi load xong thì filter theo keyword
                        String keyword = etSearch.getText().toString().trim();
                        filterAndShow(keyword);
                    }
                    @Override
                    public void onFailure(Call<ApiResponse<PageResponse<DoctorResponse>>> call,
                                          Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        filterAndShow("");
                    }
                });
    }

    private void filterAndShow(String keyword) {
        rvResults.setLayoutManager(new LinearLayoutManager(this));

        if (currentTab == 0) {
            // Filter chuyên khoa
            List<SpecialtyResponse> filtered = keyword.isEmpty()
                    ? allSpecialties
                    : allSpecialties.stream()
                    .filter(s -> s.getName().toLowerCase()
                            .contains(keyword.toLowerCase()))
                    .collect(Collectors.toList());

            if (filtered.isEmpty()) {
                tvEmpty.setVisibility(View.VISIBLE);
                rvResults.setVisibility(View.GONE);
            } else {
                tvEmpty.setVisibility(View.GONE);
                rvResults.setVisibility(View.VISIBLE);
                rvResults.setAdapter(new SpecialtyAdapter(filtered, specialty -> {
                    Intent intent = new Intent(this, DoctorListActivity.class);
                    intent.putExtra("specialtyId", specialty.getId());
                    intent.putExtra("specialtyName", specialty.getName());
                    startActivity(intent);
                }));
            }

        } else {
            // Filter bác sĩ
            List<DoctorResponse> filtered = keyword.isEmpty()
                    ? allDoctors
                    : allDoctors.stream()
                    .filter(d -> (d.getFullName() != null
                            && d.getFullName().toLowerCase()
                            .contains(keyword.toLowerCase()))
                            || (d.getSpecialtyName() != null
                            && d.getSpecialtyName().toLowerCase()
                            .contains(keyword.toLowerCase())))
                    .collect(Collectors.toList());

            if (filtered.isEmpty()) {
                tvEmpty.setVisibility(View.VISIBLE);
                rvResults.setVisibility(View.GONE);
            } else {
                tvEmpty.setVisibility(View.GONE);
                rvResults.setVisibility(View.VISIBLE);
                rvResults.setAdapter(new DoctorListAdapter(filtered, doctor -> {
                    Intent intent = new Intent(this, DoctorDetailActivity.class);
                    intent.putExtra("doctorId", doctor.getId());
                    intent.putExtra("doctorName", doctor.getFullName());
                    intent.putExtra("doctorTitle", doctor.getTitle());
                    intent.putExtra("doctorSpecialty", doctor.getSpecialtyName());
                    intent.putExtra("doctorExperience",
                            doctor.getExperienceYears() != null
                                    ? doctor.getExperienceYears() : 0);
                    intent.putExtra("doctorDescription", doctor.getBio());
                    intent.putExtra("doctorFee",
                            doctor.getConsultationFee() != null
                                    ? doctor.getConsultationFee() : 0.0);
                    startActivity(intent);
                }));
            }
        }
    }
}
