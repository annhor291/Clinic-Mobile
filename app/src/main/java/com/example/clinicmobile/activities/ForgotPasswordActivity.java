package com.example.clinicmobile.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.clinicmobile.R;
import com.example.clinicmobile.dto.request.ForgotPasswordRequest;
import com.example.clinicmobile.dto.response.ApiResponse;
import com.example.clinicmobile.network.ApiClient;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {

    private TextInputEditText etEmail;
    private Button btnSendEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(getResources().getColor(R.color.bg_top_blue));
        setContentView(R.layout.activity_forgot_password);

        etEmail = findViewById(R.id.etEmail);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        btnSendEmail = findViewById(R.id.btnSendEmail);
        btnSendEmail.setOnClickListener(v -> handleSendEmail());
    }

    private void handleSendEmail() {
        String email = etEmail.getText().toString().trim();

        if (email.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        btnSendEmail.setEnabled(false);
        btnSendEmail.setText("Đang gửi...");

        ForgotPasswordRequest request = new ForgotPasswordRequest(email);

        ApiClient.getApiService().forgotPassword(request).enqueue(
                new Callback<ApiResponse<Void>>() {

                    @Override
                    public void onResponse(Call<ApiResponse<Void>> call,
                                           Response<ApiResponse<Void>> response) {
                        btnSendEmail.setEnabled(true);
                        btnSendEmail.setText("GỬI EMAIL ĐẶT LẠI");

                        if (response.isSuccessful() && response.body() != null
                                && response.body().isSuccess()) {
                            Toast.makeText(ForgotPasswordActivity.this,
                                    "Đã gửi email đặt lại mật khẩu. Vui lòng kiểm tra hộp thư!",
                                    Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            String errorMsg = "Gửi email thất bại";
                            try {
                                if (response.errorBody() != null) {
                                    JSONObject json = new JSONObject(
                                            response.errorBody().string());
                                    if (json.has("message")) {
                                        errorMsg = json.getString("message");
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(ForgotPasswordActivity.this,
                                    errorMsg, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                        btnSendEmail.setEnabled(true);
                        btnSendEmail.setText("GỬI EMAIL ĐẶT LẠI");
                        Toast.makeText(ForgotPasswordActivity.this,
                                "Không thể kết nối server: " + t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}
