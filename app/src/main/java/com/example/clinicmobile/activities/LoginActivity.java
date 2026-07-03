package com.example.clinicmobile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.clinicmobile.R;
import com.example.clinicmobile.dto.request.LoginRequest;
import com.example.clinicmobile.dto.response.ApiResponse;
import com.example.clinicmobile.dto.response.AuthResponse;
import com.example.clinicmobile.network.ApiClient;
import com.example.clinicmobile.utils.ProfileManager;
import com.example.clinicmobile.utils.TokenManager;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvRegister, tvForgotPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_login);

        setupEdgeToEdge();
        initViews();
        setupUnderline();

        btnLogin.setOnClickListener(v -> handleLogin());

        tvRegister.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class)));

        tvForgotPassword.setOnClickListener(v ->
                startActivity(new Intent(this, ForgotPasswordActivity.class)));

        findViewById(R.id.btnGoogle).setOnClickListener(v ->
                Toast.makeText(this, "Đăng nhập Google - Đang phát triển", Toast.LENGTH_SHORT).show());

        findViewById(R.id.btnFacebook).setOnClickListener(v ->
                Toast.makeText(this, "Đăng nhập Facebook - Đang phát triển", Toast.LENGTH_SHORT).show());

        findViewById(R.id.btnZalo).setOnClickListener(v ->
                Toast.makeText(this, "Đăng nhập Zalo - Đang phát triển", Toast.LENGTH_SHORT).show());
    }

    private void initViews() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
    }

    private void setupUnderline() {
        SpannableString content = new SpannableString(tvRegister.getText().toString());
        content.setSpan(new UnderlineSpan(), 0, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvRegister.setText(content);
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

    private void handleLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập email và mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }

        // Hiện loading
        btnLogin.setEnabled(false);
        btnLogin.setText("Đang đăng nhập...");

        LoginRequest request = new LoginRequest(email, password);

        ApiClient.getApiService().login(request).enqueue(new Callback<ApiResponse<AuthResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<AuthResponse>> call,
                                   Response<ApiResponse<AuthResponse>> response) {
                btnLogin.setEnabled(true);
                btnLogin.setText("Đăng nhập");

                if (response.isSuccessful() && response.body() != null
                        && response.body().isSuccess()) {

                    AuthResponse auth = response.body().getData();

                    TokenManager.saveTokens(
                            LoginActivity.this,
                            auth.getToken(),
                            auth.getRefreshToken());

                    TokenManager.saveUserInfo(
                            LoginActivity.this,
                            auth.getUserId(),
                            auth.getEmail(),
                            auth.getUsername(),
                            auth.getRole(),
                            auth.getProfileId());

                    navigateAfterLogin(auth);

                } else {
                    // Đọc message từ errorBody khi BE trả về 4xx
                    String errorMsg = "Đăng nhập thất bại";
                    try {
                        if (response.errorBody() != null) {
                            String errorJson = response.errorBody().string();
                            // Parse thủ công lấy field "message"
                            org.json.JSONObject json = new org.json.JSONObject(errorJson);
                            if (json.has("message")) {
                                errorMsg = json.getString("message");
                            }
                        } else if (response.body() != null) {
                            errorMsg = response.body().getMessage();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(LoginActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<ApiResponse<AuthResponse>> call, Throwable t) {
                btnLogin.setEnabled(true);
                btnLogin.setText("Đăng nhập");
                Toast.makeText(LoginActivity.this,
                        "Không thể kết nối server: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void navigateAfterLogin(AuthResponse auth) {
        // Kiểm tra lần đầu đăng nhập (chưa có prompt hoàn thiện hồ sơ)
        if (!ProfileManager.isPromptShown(LoginActivity.this)) {
            ProfileManager.setPromptShown(LoginActivity.this);
            Intent intent = new Intent(LoginActivity.this, CompleteProfileActivity.class);
            intent.putExtra("skippable", true);
            intent.putExtra("nextAction", CompleteProfileActivity.ACTION_GO_HOME);
            startActivity(intent);
        } else {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
        finish();
    }
}
