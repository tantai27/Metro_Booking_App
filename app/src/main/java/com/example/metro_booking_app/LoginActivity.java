package com.example.metro_booking_app;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText edtEmail, edtPassword;
    Button btnLogin, btnListAccount;
    LinearLayout btnGoogleLogin;
    TextView txtToRegister, tvForgotPassword;
    MyDataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Gán sau khi setContentView
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        txtToRegister = findViewById(R.id.txtRegister);
        btnGoogleLogin = findViewById(R.id.btnGoogleLogin);
        //btnListAccount = findViewById(R.id.btnListAccount);
        tvForgotPassword = findViewById(R.id.txtForgotPassword);
        dbHelper = new MyDataBaseHelper(this);

        // Xử lý nút Đăng nhập
        btnLogin.setOnClickListener(view -> {
            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            if (checkUserCredentials(email, password)) {
                Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();

                // Gửi email sang MainActivity
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("username", email);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Sai email hoặc mật khẩu", Toast.LENGTH_SHORT).show();
            }
        });

        // Chuyển sang màn hình đăng ký
        txtToRegister.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });

        // Tạm thời cho nút Google
        btnGoogleLogin.setOnClickListener(v ->
                Toast.makeText(this, "Tính năng đang được phát triển", Toast.LENGTH_SHORT).show()
        );

        tvForgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });

//        btnListAccount.setOnClickListener(v -> {
//            Intent intent = new Intent(this, PrintDatabaseActivity.class);
//            startActivity(intent);
//        });

    }

    private boolean checkUserCredentials(String email, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Users WHERE email=? AND password=?", new String[]{email, password});
        boolean result = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return result;
    }
}
