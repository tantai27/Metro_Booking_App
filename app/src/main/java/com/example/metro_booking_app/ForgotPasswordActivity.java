package com.example.metro_booking_app;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ForgotPasswordActivity extends AppCompatActivity {

    private MyDataBaseHelper dbHelper;
    private String currentOtp = "";
    private String userEmailForgot = "";
    private CountDownTimer countDownTimer;
    private static final int RESEND_TIME = 10; // 10 giây để test
    private Button btnSendOtp;
    private TextView tvResendOtp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password); // layout rỗng hoặc đơn giản

        dbHelper = new MyDataBaseHelper(this);

        // Khi mở activity -> hiện popup forgot password
        showForgotPasswordDialog();
    }

    private void showForgotPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_forgot_password, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);

        dialog.show();

        EditText etForgotEmail = dialogView.findViewById(R.id.etForgotEmail);
        btnSendOtp = dialogView.findViewById(R.id.btnSendOtp);
        EditText etOtp = dialogView.findViewById(R.id.etOtp);
        Button btnBack = dialogView.findViewById(R.id.btnBack);
        Button btnVerifyOtp = dialogView.findViewById(R.id.btnVerifyOtp);
        tvResendOtp = dialogView.findViewById(R.id.tvResendOtp);

        tvResendOtp.setVisibility(View.GONE); // Ẩn resend lúc đầu

        btnSendOtp.setOnClickListener(v -> {
            String email = etForgotEmail.getText().toString().trim();
            if (email.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập email.", Toast.LENGTH_SHORT).show();
            } else {
                if (dbHelper.checkEmailExists(email)) {
                    currentOtp = generateRandomOtp();
                    sendOtpEmail(email);
                    userEmailForgot = email;
                    Toast.makeText(this, "Đã gửi OTP tới email của bạn.", Toast.LENGTH_SHORT).show();
                    startCountdown(); // 👉 bắt đầu đếm ngược sau khi gửi
                } else {
                    Toast.makeText(this, "Email này chưa đăng ký tài khoản.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvResendOtp.setOnClickListener(v -> {
            if (!userEmailForgot.isEmpty()) {
                currentOtp = generateRandomOtp();
                sendOtpEmail(userEmailForgot);
                Toast.makeText(this, "OTP mới đã được gửi!", Toast.LENGTH_SHORT).show();
                startCountdown(); // 👉 bắt đầu đếm lại
            } else {
                Toast.makeText(this, "Bạn cần nhập email trước.", Toast.LENGTH_SHORT).show();
            }
        });

        btnVerifyOtp.setOnClickListener(v -> {
            String otpInput = etOtp.getText().toString().trim();
            if (otpInput.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập OTP.", Toast.LENGTH_SHORT).show();
            } else {
                if (otpInput.equals(currentOtp)) {
                    Toast.makeText(this, "Xác thực OTP thành công!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();

                    Intent intent = new Intent(ForgotPasswordActivity.this, ResetPasswordActivity.class);
                    intent.putExtra("email", userEmailForgot);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "OTP không đúng. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnBack.setOnClickListener(v -> {
            if (countDownTimer != null) {
                countDownTimer.cancel();
            }
            dialog.dismiss();
            finish();
        });
    }

    private void startCountdown() {
        btnSendOtp.setEnabled(false);
        tvResendOtp.setVisibility(View.GONE);

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        countDownTimer = new CountDownTimer(RESEND_TIME * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                btnSendOtp.setText("Gửi lại (" + millisUntilFinished / 1000 + "s)");
            }

            @Override
            public void onFinish() {
                btnSendOtp.setEnabled(true);
                btnSendOtp.setText("Gửi OTP");
                tvResendOtp.setVisibility(View.VISIBLE);
            }
        }.start();
    }

    private void sendOtpEmail(String email) {
        String subject = "Mã OTP đặt lại mật khẩu Metro Booking App";
        String message = "Xin chào,\n\nMã OTP đặt lại mật khẩu của bạn là: " + currentOtp + "\n\nMetro Booking App.";

        new SendMailTask(email, subject, message, new SendMailTask.MailCallback() {
            @Override
            public void onMailSent(boolean success) {
                runOnUiThread(() -> {
                    if (!success) {
                        Toast.makeText(ForgotPasswordActivity.this, "Gửi email thất bại. Vui lòng kiểm tra kết nối.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).send();
    }

    private String generateRandomOtp() {
        int otp = (int) (Math.random() * 900000) + 100000;
        return String.valueOf(otp);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
