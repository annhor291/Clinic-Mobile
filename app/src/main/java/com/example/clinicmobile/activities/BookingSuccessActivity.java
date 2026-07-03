package com.example.clinicmobile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.clinicmobile.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BookingSuccessActivity extends AppCompatActivity {

    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(getResources().getColor(R.color.bg_top_blue));

        setContentView(R.layout.activity_booking_success);

        TextView tvCountdown = findViewById(R.id.tvCountdown);
        Button btnGoHome = findViewById(R.id.btnGoHome);

        btnGoHome.setOnClickListener(v -> goHome());

        // Đếm ngược 3 giây rồi tự về Home
        countDownTimer = new CountDownTimer(5000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000) + 1;
                tvCountdown.setText("Tự động về trang chủ sau " + seconds + " giây...");
            }

            @Override
            public void onFinish() {
                goHome();
            }

        }.start();
    }

    private void goHome() {
        Intent intent = new Intent(BookingSuccessActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Hủy timer khi activity bị destroy, tránh memory leak
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
