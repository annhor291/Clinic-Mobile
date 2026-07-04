package com.example.clinicmobile.activities;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.clinicmobile.R;
import com.example.clinicmobile.dto.request.PatientCreateRequest;
import com.example.clinicmobile.dto.response.ApiResponse;
import com.example.clinicmobile.dto.response.PatientResponse;
import com.example.clinicmobile.network.ApiClient;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {

    private TextInputEditText etFullName, etPhone, etDob, etAddress, etBhyt;
    private RadioGroup rgGender;
    private Spinner spinnerBloodType;
    private Button btnSave;
    private Long patientId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(
                androidx.core.content.ContextCompat.getColor(this, R.color.bg_top_blue));
        setContentView(R.layout.activity_edit_profile);

        patientId = getIntent().getLongExtra("patientId", -1);

        initViews();
        setupBloodTypeSpinner();
        setupDatePicker();
        loadDataFromIntent();

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        btnSave.setOnClickListener(v -> handleSave());
    }

    private void initViews() {
        etFullName = findViewById(R.id.etFullName);
        etPhone = findViewById(R.id.etPhone);
        etDob = findViewById(R.id.etDob);
        etAddress = findViewById(R.id.etAddress);
        etBhyt = findViewById(R.id.etBhyt);
        rgGender = findViewById(R.id.rgGender);
        spinnerBloodType = findViewById(R.id.spinnerBloodType);
        btnSave = findViewById(R.id.btnSave);
    }

    private void loadDataFromIntent() {
        etFullName.setText(getIntent().getStringExtra("fullName"));
        etPhone.setText(getIntent().getStringExtra("phone"));
        etDob.setText(getIntent().getStringExtra("dob"));
        etAddress.setText(getIntent().getStringExtra("address"));
        etBhyt.setText(getIntent().getStringExtra("insuranceNumber"));

        // Set giới tính
        String gender = getIntent().getStringExtra("gender");
        if ("FEMALE".equals(gender)) rgGender.check(R.id.rbFemale);
        else if ("OTHER".equals(gender)) rgGender.check(R.id.rbOther);
        else rgGender.check(R.id.rbMale);

        // Set nhóm máu
        String bloodType = getIntent().getStringExtra("bloodType");
        String[] bloodTypes = {"A", "B", "AB", "O", "Không rõ"};
        for (int i = 0; i < bloodTypes.length; i++) {
            if (bloodTypes[i].equals(bloodType)) {
                spinnerBloodType.setSelection(i);
                break;
            }
        }
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
                            etDob.setText(String.format("%04d-%02d-%02d",
                                    year, month + 1, day)),
                    cal.get(Calendar.YEAR) - 20,
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    private void handleSave() {
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

        btnSave.setEnabled(false);
        btnSave.setText("Đang lưu...");

        PatientCreateRequest request = new PatientCreateRequest(
                fullName, phone, gender, dob, address, bloodType, insuranceNumber);

        ApiClient.getApiService().updateProfile(patientId, request).enqueue(
                new Callback<ApiResponse<PatientResponse>>() {

                    @Override
                    public void onResponse(Call<ApiResponse<PatientResponse>> call,
                                           Response<ApiResponse<PatientResponse>> response) {
                        btnSave.setEnabled(true);
                        btnSave.setText("LƯU THAY ĐỔI");

                        if (response.isSuccessful() && response.body() != null
                                && response.body().isSuccess()) {
                            Toast.makeText(EditProfileActivity.this,
                                    "Cập nhật hồ sơ thành công!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            String errorMsg = "Cập nhật thất bại";
                            try {
                                if (response.errorBody() != null) {
                                    JSONObject json = new JSONObject(
                                            response.errorBody().string());
                                    if (json.has("message"))
                                        errorMsg = json.getString("message");
                                }
                            } catch (Exception e) { e.printStackTrace(); }
                            Toast.makeText(EditProfileActivity.this,
                                    errorMsg, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<PatientResponse>> call, Throwable t) {
                        btnSave.setEnabled(true);
                        btnSave.setText("LƯU THAY ĐỔI");
                        Toast.makeText(EditProfileActivity.this,
                                "Lỗi kết nối: " + t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}
