package com.example.clinicmobile.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.clinicmobile.R;
import com.example.clinicmobile.dto.request.ChangePasswordRequest;
import com.example.clinicmobile.dto.response.ApiResponse;
import com.example.clinicmobile.network.ApiClient;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {

    private TextInputEditText etCurrentPassword, etNewPassword, etConfirmNewPassword;
    private Button btnChangePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(getResources().getColor(R.color.bg_top_blue));
        setContentView(R.layout.activity_change_password);

        etCurrentPassword = findViewById(R.id.etCurrentPassword);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmNewPassword = findViewById(R.id.etConfirmNewPassword);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        btnChangePassword = findViewById(R.id.btnChangePassword);
        btnChangePassword.setOnClickListener(v -> handleChangePassword());
    }

    private void handleChangePassword() {
        String current = etCurrentPassword.getText().toString().trim();
        String newPass = etNewPassword.getText().toString().trim();
        String confirm = etConfirmNewPassword.getText().toString().trim();

        if (current.isEmpty() || newPass.isEmpty() || confirm.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        if (newPass.length() < 6) {
            Toast.makeText(this, "Mật khẩu mới phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPass.equals(confirm)) {
            Toast.makeText(this, "Mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show();
            return;
        }

        btnChangePassword.setEnabled(false);
        btnChangePassword.setText("Đang xử lý...");

        ChangePasswordRequest request = new ChangePasswordRequest(current, newPass, confirm);

        ApiClient.getApiService().changePassword(request).enqueue(
                new Callback<ApiResponse<Void>>() {

                    @Override
                    public void onResponse(Call<ApiResponse<Void>> call,
                                           Response<ApiResponse<Void>> response) {
                        btnChangePassword.setEnabled(true);
                        btnChangePassword.setText("ĐỔI MẬT KHẨU");

                        if (response.isSuccessful() && response.body() != null
                                && response.body().isSuccess()) {
                            Toast.makeText(ChangePasswordActivity.this,
                                    "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            String errorMsg = "Đổi mật khẩu thất bại";
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
                            Toast.makeText(ChangePasswordActivity.this,
                                    errorMsg, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                        btnChangePassword.setEnabled(true);
                        btnChangePassword.setText("ĐỔI MẬT KHẨU");
                        Toast.makeText(ChangePasswordActivity.this,
                                "Không thể kết nối server: " + t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}
