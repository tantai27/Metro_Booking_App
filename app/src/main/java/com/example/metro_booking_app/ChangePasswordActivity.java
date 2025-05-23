package com.example.metro_booking_app;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText etCurrentPassword, etNewPassword, etConfirmPassword;
    private Button btnResetPassword;
    private ImageView btnBack;  // Nút quay lại

    private MyDataBaseHelper dbHelper;
    private String currentEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        etCurrentPassword = findViewById(R.id.etCurrentPassword);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnResetPassword = findViewById(R.id.btnResetPassword);
        btnBack = findViewById(R.id.btnBack);  // Gắn ánh xạ

        dbHelper = new MyDataBaseHelper(this);

        currentEmail = getIntent().getStringExtra("username");
        if (currentEmail == null || currentEmail.isEmpty()) {
            Toast.makeText(this, "Không nhận được thông tin người dùng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        btnResetPassword.setOnClickListener(v -> handleChangePassword());

        btnBack.setOnClickListener(v -> {
            // Quay lại AccountActivity
            Intent intent = new Intent(ChangePasswordActivity.this, AccountActivity.class);
            intent.putExtra("username", currentEmail); // Truyền lại email nếu cần
            startActivity(intent);
            finish();
        });
    }

    private void handleChangePassword() {
        String currentPassword = etCurrentPassword.getText().toString().trim();
        String newPassword = etNewPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(currentPassword) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        String savedPassword = dbHelper.getPasswordByEmail(currentEmail);
        if (savedPassword == null) {
            Toast.makeText(this, "Lỗi lấy dữ liệu người dùng", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!currentPassword.equals(savedPassword)) {
            Toast.makeText(this, "Mật khẩu hiện tại không đúng", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(this, "Mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show();
            return;
        }

        if (newPassword.equals(currentPassword)) {
            Toast.makeText(this, "Mật khẩu mới không được trùng với mật khẩu hiện tại", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean updateSuccess = dbHelper.updatePassword(currentEmail, newPassword);
        if (updateSuccess) {
            Toast.makeText(this, "Đổi mật khẩu thành công", Toast.LENGTH_LONG).show();

            // ✅ Quay lại AccountActivity
            Intent intent = new Intent(ChangePasswordActivity.this, AccountActivity.class);
            intent.putExtra("username", currentEmail); // Gửi lại thông tin nếu cần
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Đổi mật khẩu thất bại", Toast.LENGTH_SHORT).show();
        }
    }
}
