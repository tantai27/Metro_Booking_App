package com.example.metro_booking_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AccountActivity extends AppCompatActivity {

    TextView tvFullName, tvEmail;
    Button btnEditProfile, btnLogout;
    MyDataBaseHelper dbHelper;
    ImageView btnBack, imgAvatar;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        setTitle("Tài khoản của bạn");

        tvFullName = findViewById(R.id.tvFullName);
        tvEmail = findViewById(R.id.tvEmail);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnLogout = findViewById(R.id.btnLogout);
        btnBack = findViewById(R.id.btnBack);
        dbHelper = new MyDataBaseHelper(this);
        imgAvatar = findViewById(R.id.imgAvatar);

        // Nhận username
        username = getIntent().getStringExtra("username");
        if (username == null) {
            Toast.makeText(this, "Không xác định người dùng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Hiển thị thông tin
        loadUserInfo();

        btnBack.setOnClickListener(v -> finish());

        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(AccountActivity.this, EditProfileActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            Toast.makeText(this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
            Intent logoutIntent = new Intent(AccountActivity.this, LoginActivity.class);
            logoutIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(logoutIntent);
            finish();
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        loadUserInfo(); // gọi hàm để cập nhật lại giao diện
    }

    private void loadUserInfo() {
        User user = dbHelper.getUserInfo(username);
        if (user != null) {
            tvFullName.setText(user.getName());
            tvEmail.setText(user.getEmail());

            // Load avatar
            String avatarName = user.getAvatar();
            int resId = getResources().getIdentifier(avatarName, "drawable", getPackageName());
            if (resId != 0) {
                imgAvatar.setImageResource(resId);
            } else {
                imgAvatar.setImageResource(R.drawable.ic_account); // fallback nếu không tìm thấy
            }
        } else {
            Toast.makeText(this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
        }
    }

}
