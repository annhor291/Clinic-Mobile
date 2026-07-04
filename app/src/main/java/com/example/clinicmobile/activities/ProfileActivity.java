package com.example.clinicmobile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.clinicmobile.R;
import com.example.clinicmobile.dto.request.RefreshTokenRequest;
import com.example.clinicmobile.dto.response.ApiResponse;
import com.example.clinicmobile.network.ApiClient;
import com.example.clinicmobile.utils.TokenManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(
                androidx.core.content.ContextCompat.getColor(this, R.color.bg_top_blue));
        setContentView(R.layout.activity_profile);

        // Hiển thị thông tin từ TokenManager
        TextView tvName = findViewById(R.id.tvName);
        TextView tvEmail = findViewById(R.id.tvEmail);
        TextView tvRole = findViewById(R.id.tvRole);

        tvName.setText(TokenManager.getUserName(this) != null
                && !TokenManager.getUserName(this).isEmpty()
                ? TokenManager.getUserName(this) : "Người dùng");

        tvEmail.setText(TokenManager.getUserEmail(this) != null
                ? TokenManager.getUserEmail(this) : "");

        tvRole.setText("PATIENT".equals(TokenManager.getUserRole(this))
                ? "Bệnh nhân" : TokenManager.getUserRole(this));

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        Button btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> handleLogout());

        setupBottomNavigation();
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
                        // Dù BE trả về gì cũng logout phía client
                        navigateToLogin();
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                        // Mất kết nối vẫn logout phía client
                        navigateToLogin();
                    }
                });
    }

    private void navigateToLogin() {
        // Xóa toàn bộ token + thông tin user
        TokenManager.clearAll(this);

        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setSelectedItemId(R.id.nav_profile);

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
                startActivity(new Intent(this, MedicalProfileActivity.class));
                finish();
                return true;
            }
            if (item.getItemId() == R.id.nav_profile) {
                return true;
            }
            return false;
        });
    }
}
