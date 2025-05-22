package com.example.metro_booking_app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Build;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Color;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    EditText etName, etEmail, etPassword;
    Button btnRegister, btnToLogin;
    TextView tvForgotPassword;
    MyDataBaseHelper dbHelper;
    private String tempName = "", tempEmail = "", tempPassword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = new MyDataBaseHelper(this);

        // Ánh xạ các thành phần
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnToLogin = findViewById(R.id.btnToLogin);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);

        // Đăng ký người dùng mới
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else {
                    if (dbHelper.checkEmailExists(email)) {
                        Toast.makeText(RegisterActivity.this, "Email đã được sử dụng. Vui lòng dùng email khác.", Toast.LENGTH_SHORT).show();
                    } else {
                        // 👉 Lưu thông tin tạm để add sau khi xác thực OTP
                        tempName = name;
                        tempEmail = email;
                        tempPassword = password;

                        currentOtp = generateRandomOtp();
                        sendOtpEmail(email);
                        showOtpDialog();
                    }
                }
            }
        });

        // Chuyển sang màn hình đăng nhập
        btnToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        tvForgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }
    // 👉 Các hàm riêng bên ngoài onCreate()

    private String currentOtp = ""; // Lưu OTP hiện tại

    // Hàm gửi email OTP
    private void sendOtpEmail(String userEmail) {
        String subject = "Mã OTP xác thực đăng ký Metro Booking App";
        String message = "Xin chào,\n\nMã OTP của bạn là: " + currentOtp + "\n\nMetro Booking App.";

        new SendMailTask(userEmail, subject, message, new SendMailTask.MailCallback() {
            @Override
            public void onMailSent(boolean success) {
                runOnUiThread(() -> {
                    if (success) {
                        Toast.makeText(RegisterActivity.this, "Email xác thực đã gửi thành công!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Gửi email thất bại. Vui lòng kiểm tra kết nối mạng.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).send();
    }

    // Hàm hiển thị dialog nhập OTP
    private void showOtpDialog() {
        // Inflate layout
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_otp, null);
        EditText inputOtp = dialogView.findViewById(R.id.etInputOtp);

        androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(false)
                .create();

        dialog.setOnShowListener(dialogInterface -> {
            Button btnConfirm = dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE);
            Button btnCancel = dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE);
        });

        dialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE, "Xác nhận", (d, which) -> {
            String enteredOtp = inputOtp.getText().toString().trim();
            if (enteredOtp.equals(currentOtp)) {
                // 👉 Chỉ lúc này mới lưu user vào database
                dbHelper.addUser(tempName, tempEmail, tempPassword);

                Toast.makeText(RegisterActivity.this, "Đăng ký thành công, vui lòng đăng nhập!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(RegisterActivity.this, "Sai OTP, vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                showOtpDialog(); // Hiện lại để nhập lại
            }
        });

        dialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE, "Hủy", (d, which) -> {
            Toast.makeText(RegisterActivity.this, "Bạn đã hủy xác thực.", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_NEUTRAL, "Gửi lại OTP", (d, which) -> {
            currentOtp = generateRandomOtp();
            sendOtpEmail(etEmail.getText().toString().trim());  // gửi lại OTP tới email đã nhập
            Toast.makeText(RegisterActivity.this, "Đã gửi lại OTP!", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            showOtpDialog(); // Show lại dialog mới
        });

        dialog.show();
    }

    private String generateRandomOtp() {
        int otp = (int) (Math.random() * 900000) + 100000;
        return String.valueOf(otp);
    }
}
