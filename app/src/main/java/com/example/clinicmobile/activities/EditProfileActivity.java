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

import java.util.Calendar;

public class EditProfileActivity extends AppCompatActivity {

    private EditText etFullName, etPhone, etDob, etAddress, etCccd, etBhyt;
    private RadioGroup rgGender;
    private Spinner spinnerBloodType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(getResources().getColor(R.color.bg_top_blue));
        setContentView(R.layout.activity_edit_profile);

        initViews();
        setupBloodTypeSpinner();
        setupDatePicker();
        loadExistingData();

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        Button btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(v -> handleSave());
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
    }

    private void loadExistingData() {
        SharedPreferences prefs = getSharedPreferences("clinic_profile_data", MODE_PRIVATE);

        etFullName.setText(prefs.getString("name", ""));
        etPhone.setText(prefs.getString("phone", ""));
        etDob.setText(prefs.getString("dob", ""));
        etAddress.setText(prefs.getString("address", ""));
        etCccd.setText(prefs.getString("cccd", ""));
        etBhyt.setText(prefs.getString("bhyt", ""));

        String savedBloodType = prefs.getString("bloodType", "");
        String[] bloodTypes = {"A", "B", "AB", "O", "Không rõ"};
        for (int i = 0; i < bloodTypes.length; i++) {
            if (bloodTypes[i].equals(savedBloodType)) {
                spinnerBloodType.setSelection(i);
                break;
            }
        }

        String savedGender = prefs.getString("gender", "Nam");
        if (savedGender.equals("Nữ")) rgGender.check(R.id.rbFemale);
        else if (savedGender.equals("Khác")) rgGender.check(R.id.rbOther);
        else rgGender.check(R.id.rbMale);
    }

    private void setupBloodTypeSpinner() {
        String[] bloodTypes = {"A", "B", "AB", "O", "Không rõ"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, bloodTypes);
        spinnerBloodType.setAdapter(adapter);
    }

    private void setupDatePicker() {
        etDob.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog dialog = new DatePickerDialog(
                    this,
                    (view, year, month, dayOfMonth) -> {
                        String date = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year);
                        etDob.setText(date);
                    },
                    calendar.get(Calendar.YEAR) - 20,
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            dialog.show();
        });
    }

    private void handleSave() {
        String fullName = etFullName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String dob = etDob.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String cccd = etCccd.getText().toString().trim();

        if (TextUtils.isEmpty(fullName) || TextUtils.isEmpty(phone)
                || TextUtils.isEmpty(dob) || TextUtils.isEmpty(address)
                || TextUtils.isEmpty(cccd)) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin bắt buộc", Toast.LENGTH_SHORT).show();
            return;
        }

        String gender = "Khác";
        int checkedId = rgGender.getCheckedRadioButtonId();
        if (checkedId == R.id.rbMale) gender = "Nam";
        else if (checkedId == R.id.rbFemale) gender = "Nữ";

        getSharedPreferences("clinic_profile_data", MODE_PRIVATE)
                .edit()
                .putString("name", fullName)
                .putString("phone", phone)
                .putString("dob", dob)
                .putString("gender", gender)
                .putString("address", address)
                .putString("cccd", cccd)
                .putString("bhyt", etBhyt.getText().toString().trim())
                .putString("bloodType", spinnerBloodType.getSelectedItem().toString())
                .apply();

        // TODO: gắn API PUT /users/profile
        Toast.makeText(this, "Cập nhật hồ sơ thành công!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
