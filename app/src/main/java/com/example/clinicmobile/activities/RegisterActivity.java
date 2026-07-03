package com.example.clinicmobile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.clinicmobile.R;
import com.example.clinicmobile.dto.request.RegisterRequest;
import com.example.clinicmobile.dto.response.ApiResponse;
import com.example.clinicmobile.dto.response.AuthResponse;
import com.example.clinicmobile.network.ApiClient;
import com.example.clinicmobile.utils.TokenManager;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText etFullName, etEmail, etPhone, etPassword, etConfirmPassword;
    private Button btnRegister;
    private TextView tvLogin;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_register);

        setupEdgeToEdge();
        initViews();
        underlineLoginText();

        btnBack.setOnClickListener(v -> finish());

        btnRegister.setOnClickListener(v -> handleRegister());

        tvLogin.setOnClickListener(v -> finish());
    }

    private void initViews() {
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);
        btnBack = findViewById(R.id.btnBack);
    }

    private void underlineLoginText() {
        SpannableString content = new SpannableString(tvLogin.getText().toString());
        content.setSpan(new UnderlineSpan(), 0, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvLogin.setText(content);
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

    private void handleRegister() {
        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (fullName.isEmpty() || email.isEmpty() || phone.isEmpty()
                || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "Mật khẩu phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show();
            return;
        }

        btnRegister.setEnabled(false);
        btnRegister.setText("Đang đăng ký...");

        RegisterRequest request = new RegisterRequest(fullName, email, phone, password);

        ApiClient.getApiService().register(request).enqueue(
                new Callback<ApiResponse<AuthResponse>>() {

                    @Override
                    public void onResponse(Call<ApiResponse<AuthResponse>> call,
                                           Response<ApiResponse<AuthResponse>> response) {
                        btnRegister.setEnabled(true);
                        btnRegister.setText("ĐĂNG KÝ");

                        if (response.isSuccessful() && response.body() != null
                                && response.body().isSuccess()) {

                            AuthResponse auth = response.body().getData();

                            // Lưu token ngay sau đăng ký
                            TokenManager.saveTokens(
                                    RegisterActivity.this,
                                    auth.getToken(),
                                    auth.getRefreshToken());

                            TokenManager.saveUserInfo(
                                    RegisterActivity.this,
                                    auth.getUserId(),
                                    auth.getEmail(),
                                    auth.getUsername(),
                                    auth.getRole(),
                                    auth.getProfileId());

                            Toast.makeText(RegisterActivity.this,
                                    "Đăng ký thành công!", Toast.LENGTH_SHORT).show();

                            // Sau đăng ký → vào màn hoàn thiện hồ sơ
                            Intent intent = new Intent(RegisterActivity.this,
                                    CompleteProfileActivity.class);
                            intent.putExtra("skippable", true);
                            intent.putExtra("nextAction", CompleteProfileActivity.ACTION_GO_HOME);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();

                        } else {
                            String errorMsg = "Đăng ký thất bại";
                            try {
                                if (response.errorBody() != null) {
                                    JSONObject json = new JSONObject(
                                            response.errorBody().string());
                                    if (json.has("message")) {
                                        errorMsg = json.getString("message");
                                    }
                                } else if (response.body() != null) {
                                    errorMsg = response.body().getMessage();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(RegisterActivity.this,
                                    errorMsg, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<AuthResponse>> call, Throwable t) {
                        btnRegister.setEnabled(true);
                        btnRegister.setText("ĐĂNG KÝ");
                        Toast.makeText(RegisterActivity.this,
                                "Không thể kết nối server: " + t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

}
