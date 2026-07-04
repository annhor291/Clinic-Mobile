package com.example.clinicmobile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.activity.OnBackPressedCallback;
import com.example.clinicmobile.R;
import com.example.clinicmobile.adapters.DoctorAdapter;
import com.example.clinicmobile.adapters.FunctionAdapter;
import com.example.clinicmobile.dto.request.RefreshTokenRequest;
import com.example.clinicmobile.dto.response.ApiResponse;
import com.example.clinicmobile.model.Doctor;
import com.example.clinicmobile.model.QuickFunction;
import com.example.clinicmobile.network.ApiClient;
import com.example.clinicmobile.utils.TokenManager;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    private RecyclerView rvDoctors;
    private RecyclerView rvFunctions;
    private ImageButton btnLogout;
    private DrawerLayout drawerLayout;

    private DoctorAdapter doctorAdapter;
    private FunctionAdapter functionAdapter;

    private List<Doctor> doctors;
    private List<QuickFunction> functions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        setContentView(R.layout.activity_main);

        setupEdgeToEdge();
        initViews();
        setupDrawer();
        loadDummyDoctors();
        loadFunctions();
        setupDoctorRecyclerView();
        setupFunctionRecyclerView();

        btnLogout.setOnClickListener(v -> handleLogout());

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    // Tắt callback tạm thời và gọi hệ thống back mặc định
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }
        });
    }

    private void initViews() {
        rvDoctors = findViewById(R.id.rvDoctors);
        rvFunctions = findViewById(R.id.rvFunctions);
        btnLogout = findViewById(R.id.btnLogout);
        drawerLayout = findViewById(R.id.drawerLayout);
    }

    private void setupDrawer() {
        // Bấm logo mở drawer
        CardView btnOpenDrawer = findViewById(R.id.btnOpenDrawer);
        btnOpenDrawer.setOnClickListener(v ->
                drawerLayout.openDrawer(GravityCompat.START));

        findViewById(R.id.menuProfile).setOnClickListener(v -> {
            drawerLayout.closeDrawer(GravityCompat.START);
            startActivity(new Intent(this, ProfileActivity.class));
        });

        findViewById(R.id.menuChangePassword).setOnClickListener(v -> {
            drawerLayout.closeDrawer(GravityCompat.START);
            startActivity(new Intent(this, ChangePasswordActivity.class));
        });

        findViewById(R.id.menuHistory).setOnClickListener(v -> {
            drawerLayout.closeDrawer(GravityCompat.START);
            startActivity(new Intent(this, AppointmentHistoryActivity.class));
        });

        findViewById(R.id.menuLogout).setOnClickListener(v -> {
            drawerLayout.closeDrawer(GravityCompat.START);
            handleLogout();
        });
    }

    private void setupEdgeToEdge() {
        View contentContainer = findViewById(R.id.contentContainer);
        int originalPaddingTop = contentContainer.getPaddingTop();

        ViewCompat.setOnApplyWindowInsetsListener(contentContainer, (v, insets) -> {
            int statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top;
            v.setPadding(
                    v.getPaddingLeft(),
                    originalPaddingTop + statusBarHeight,
                    v.getPaddingRight(),
                    v.getPaddingBottom()
            );
            return insets;
        });
    }

    private void handleLogout() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_logout, null);

        AlertDialog dialog = new AlertDialog.Builder(this, R.style.RoundedDialog)
                .setView(dialogView)
                .create();

        dialogView.findViewById(R.id.btnCancel).setOnClickListener(v -> dialog.dismiss());

        dialogView.findViewById(R.id.btnConfirmLogout).setOnClickListener(v -> {
            dialog.dismiss();
            callLogoutApi();
        });

        dialog.show();
    }

    private void callLogoutApi() {
        String refreshToken = TokenManager.getRefreshToken(this);

        if (refreshToken == null) {
            navigateToLogin();
            return;
        }

        RefreshTokenRequest request = new RefreshTokenRequest(refreshToken);

        ApiClient.getApiService().logout(request).enqueue(
                new Callback<ApiResponse<Void>>() {

                    @Override
                    public void onResponse(Call<ApiResponse<Void>> call,
                                           Response<ApiResponse<Void>> response) {
                        navigateToLogin();
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                        navigateToLogin();
                    }
                });
    }

    private void navigateToLogin() {
        TokenManager.clearAll(MainActivity.this);
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void loadDummyDoctors() {
        doctors = new ArrayList<>();
        doctors.add(new Doctor(1L, "BS Nguyễn Văn A", "Tim mạch", 4.9, 10, "Chuyên gia tim mạch với hơn 10 năm kinh nghiệm tại các bệnh viện lớn."));
        doctors.add(new Doctor(2L, "BS Trần Văn B", "Da liễu", 4.8, 8, "Bác sĩ da liễu chuyên điều trị các bệnh về da, dị ứng và thẩm mỹ da."));
        doctors.add(new Doctor(3L, "BS Lê Văn C", "Nhi khoa", 4.7, 12, "Bác sĩ nhi khoa giàu kinh nghiệm, chuyên chăm sóc sức khỏe trẻ em từ sơ sinh đến 15 tuổi."));
    }

    private void loadFunctions() {
        functions = new ArrayList<>();
        functions.add(new QuickFunction("Đặt lịch khám", R.drawable.ic_appointment, "booking"));
        functions.add(new QuickFunction("Lịch hẹn", R.drawable.ic_user_appointment, "appointments"));
        functions.add(new QuickFunction("Hồ sơ", R.drawable.ic_acc, "records"));
        functions.add(new QuickFunction("Kết quả khám", R.drawable.ic_medical, "results"));
        functions.add(new QuickFunction("Nhắc tái khám", R.drawable.ic_remider, "reminder"));
        functions.add(new QuickFunction("Thanh toán", R.drawable.ic_payment, "payment"));
        functions.add(new QuickFunction("Tin tức y tế", R.drawable.ic_news, "news"));
        functions.add(new QuickFunction("Hỗ trợ", R.drawable.ic_support, "support"));
    }

    private void setupDoctorRecyclerView() {
        doctorAdapter = new DoctorAdapter(doctors, doctor -> openDoctorDetail(doctor));
        rvDoctors.setLayoutManager(new LinearLayoutManager(this));
        rvDoctors.setAdapter(doctorAdapter);
    }

    private void setupFunctionRecyclerView() {
        functionAdapter = new FunctionAdapter(functions, function -> onFunctionClick(function));
        rvFunctions.setLayoutManager(new GridLayoutManager(this, 4));
        rvFunctions.setAdapter(functionAdapter);
    }

    private void onFunctionClick(QuickFunction function) {
        switch (function.getType()) {
            case "booking":
                if (com.example.clinicmobile.utils.ProfileManager.isProfileCompleted(this)) {
                    startActivity(new Intent(this, SpecialtyActivity.class));
                } else {
                    Intent intent = new Intent(this, CompleteProfileActivity.class);
                    intent.putExtra("skippable", false);
                    intent.putExtra("nextAction", CompleteProfileActivity.ACTION_GO_BOOKING);
                    startActivity(intent);
                }
                break;
            case "appointments":
                startActivity(new Intent(MainActivity.this, AppointmentHistoryActivity.class));
                break;
            case "records":
                startActivity(new Intent(this, MedicalProfileActivity.class));
                break;
            default:
                Toast.makeText(this, function.getTitle() + " - Chức năng đang phát triển", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void openDoctorDetail(Doctor doctor) {
        Intent intent = new Intent(MainActivity.this, DoctorDetailActivity.class);
        intent.putExtra("doctorId", doctor.getId());
        intent.putExtra("doctorName", doctor.getName());
        intent.putExtra("doctorSpecialty", doctor.getSpecialty());
        intent.putExtra("doctorRating", doctor.getRating());
        intent.putExtra("doctorExperience", doctor.getExperienceYears());
        intent.putExtra("doctorDescription", doctor.getDescription());
        startActivity(intent);
    }

}