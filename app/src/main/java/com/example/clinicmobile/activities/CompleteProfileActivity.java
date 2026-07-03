package com.example.clinicmobile.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import com.example.clinicmobile.R;
import com.example.clinicmobile.dto.request.PatientCreateRequest;
import com.example.clinicmobile.dto.response.ApiResponse;
import com.example.clinicmobile.dto.response.PatientResponse;
import com.example.clinicmobile.network.ApiClient;
import com.example.clinicmobile.utils.ProfileManager;
import com.example.clinicmobile.utils.TokenManager;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompleteProfileActivity extends AppCompatActivity {

    public static final int ACTION_GO_HOME = 0;
    public static final int ACTION_GO_BOOKING = 1;

    private TextInputEditText etFullName, etPhone, etDob, etAddress, etCccd, etBhyt;
    private RadioGroup rgGender;
    private Spinner spinnerBloodType;
    private Button btnComplete;
    private boolean skippable;
    private int nextAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        getWindow().setStatusBarColor(getResources().getColor(R.color.bg_top_blue));
        setContentView(R.layout.activity_complete_profile);

        skippable = getIntent().getBooleanExtra("skippable", true);
        nextAction = getIntent().getIntExtra("nextAction", ACTION_GO_HOME);

        initViews();
        setupBloodTypeSpinner();
        setupDatePicker();

        TextView tvSkip = findViewById(R.id.tvSkip);
        tvSkip.setText("Để sau");
        tvSkip.setVisibility(skippable ? View.VISIBLE : View.GONE);
        tvSkip.setOnClickListener(v -> showSkipDialog());

        btnComplete.setOnClickListener(v -> handleComplete());
    }

    private void initViews() {
        etFullName = findViewById(R.id.etFullName);
        etPhone = findViewById(R.id.etPhone);
        etDob = findViewById(R.id.etDob);
        etAddress = findViewById(R.id.etAddress);
        etCccd = findViewById(R.id.etCccd);
        etBhyt = findViewById(R.id.etBhyt);
        rgGender = findViewById(R.id.rgGender);
        spinnerBloodType = findViewById(R.id.spinnerBloodType);
        btnComplete = findViewById(R.id.btnComplete);
    }

    private void showSkipDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_skip_profile, null);
        AlertDialog dialog = new AlertDialog.Builder(this, R.style.RoundedDialog)
                .setView(dialogView).create();
        dialogView.findViewById(R.id.btnCompleteNow).setOnClickListener(v -> dialog.dismiss());
        dialogView.findViewById(R.id.btnLater).setOnClickListener(v -> {
            dialog.dismiss();
            goToHome();
        });
        dialog.show();
    }

    private void setupBloodTypeSpinner() {
        String[] bloodTypes = {"A", "B", "AB", "O", "Không rõ"};
        spinnerBloodType.setAdapter(new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, bloodTypes));
    }

    private void setupDatePicker() {
        etDob.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            new DatePickerDialog(this,
                    (view, year, month, day) ->
                            etDob.setText(String.format("%04d-%02d-%02d", year, month + 1, day)),
                    cal.get(Calendar.YEAR) - 20,
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    private void handleComplete() {
        String fullName = etFullName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String dob = etDob.getText().toString().trim();
        String address = etAddress.getText().toString().trim();

        if (TextUtils.isEmpty(fullName) || TextUtils.isEmpty(phone)
                || TextUtils.isEmpty(dob) || TextUtils.isEmpty(address)) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin bắt buộc",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        String gender = "OTHER";
        int checkedId = rgGender.getCheckedRadioButtonId();
        if (checkedId == R.id.rbMale) gender = "MALE";
        else if (checkedId == R.id.rbFemale) gender = "FEMALE";

        String bloodType = spinnerBloodType.getSelectedItem().toString();
        String insuranceNumber = etBhyt.getText().toString().trim();

        btnComplete.setEnabled(false);
        btnComplete.setText("Đang lưu...");

        PatientCreateRequest request = new PatientCreateRequest(
                fullName, phone, gender, dob, address, bloodType, insuranceNumber);

        Long userId = TokenManager.getUserId(this);

        ApiClient.getApiService().createProfile(userId, request).enqueue(
                new Callback<ApiResponse<PatientResponse>>() {

                    @Override
                    public void onResponse(Call<ApiResponse<PatientResponse>> call,
                                           Response<ApiResponse<PatientResponse>> response) {
                        btnComplete.setEnabled(true);
                        btnComplete.setText("HOÀN THÀNH");

                        if (response.isSuccessful() && response.body() != null
                                && response.body().isSuccess()) {

                            PatientResponse patient = response.body().getData();

                            // Lưu profileId vào TokenManager
                            TokenManager.saveUserInfo(
                                    CompleteProfileActivity.this,
                                    TokenManager.getUserId(CompleteProfileActivity.this),
                                    TokenManager.getUserEmail(CompleteProfileActivity.this),
                                    TokenManager.getUserName(CompleteProfileActivity.this),
                                    TokenManager.getUserRole(CompleteProfileActivity.this),
                                    patient.getId());

                            // Lưu SharedPreferences để hiện offline
                            saveToPrefs(patient);

                            ProfileManager.setProfileCompleted(CompleteProfileActivity.this, true);
                            Toast.makeText(CompleteProfileActivity.this,
                                    "Hoàn thiện hồ sơ thành công!", Toast.LENGTH_SHORT).show();

                            if (nextAction == ACTION_GO_BOOKING) {
                                startActivity(new Intent(CompleteProfileActivity.this,
                                        SpecialtyActivity.class));
                            } else {
                                goToHome();
                            }
                            finish();

                        } else {
                            String errorMsg = "Lưu hồ sơ thất bại";
                            try {
                                if (response.errorBody() != null) {
                                    JSONObject json = new JSONObject(
                                            response.errorBody().string());
                                    if (json.has("message")) errorMsg = json.getString("message");
                                }
                            } catch (Exception e) { e.printStackTrace(); }
                            Toast.makeText(CompleteProfileActivity.this,
                                    errorMsg, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<PatientResponse>> call, Throwable t) {
                        btnComplete.setEnabled(true);
                        btnComplete.setText("HOÀN THÀNH");
                        Toast.makeText(CompleteProfileActivity.this,
                                "Không thể kết nối server: " + t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void saveToPrefs(PatientResponse patient) {
        getSharedPreferences("clinic_profile_data", MODE_PRIVATE).edit()
                .putString("name", patient.getFullName())
                .putString("phone", patient.getPhone())
                .putString("dob", patient.getDateOfBirth())
                .putString("gender", patient.getGender())
                .putString("address", patient.getAddress())
                .putString("bloodType", patient.getBloodType())
                .putString("bhyt", patient.getInsuranceNumber())
                .apply();
    }

    private void goToHome() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
