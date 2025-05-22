package com.example.metro_booking_app;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class EditProfileActivity extends AppCompatActivity {

    EditText edtFullName, edtEmail;
    Button btnSave, btnChangePassword, btnDeleteAccount;
    ImageView btnBack, imgAvatar;
    Spinner spinnerAvatar;
    MyDataBaseHelper dbHelper;
    String username;

    int[] avatarResIds = {
            R.drawable.a1, R.drawable.a2, R.drawable.a3, R.drawable.a4, R.drawable.a5,
            R.drawable.a6, R.drawable.a7, R.drawable.a8, R.drawable.a9, R.drawable.a10,
            R.drawable.a11, R.drawable.a12, R.drawable.a13, R.drawable.a14, R.drawable.a15,
            R.drawable.a16, R.drawable.a17, R.drawable.a18, R.drawable.a19, R.drawable.a20
    };
    String[] avatarNames = {
            "a1", "a2", "a3", "a4", "a5",
            "a6", "a7", "a8", "a9", "a10",
            "a11", "a12", "a13", "a14", "a15",
            "a16", "a17", "a18", "a19", "a20"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Ánh xạ
        edtFullName = findViewById(R.id.edtFullName);
        edtEmail = findViewById(R.id.edtEmail);
        edtEmail.setEnabled(false);
        btnSave = findViewById(R.id.btnSave);
        btnBack = findViewById(R.id.btnBack);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        btnDeleteAccount = findViewById(R.id.btnDeleteAccount);
        spinnerAvatar = findViewById(R.id.spinnerAvatar);
        imgAvatar = findViewById(R.id.imgAvatar);

        dbHelper = new MyDataBaseHelper(this);
        username = getIntent().getStringExtra("username");

        setupAvatarSpinner();
        loadUserInfo();

        btnBack.setOnClickListener(v -> finish());

        btnSave.setOnClickListener(v -> updateProfile());

        btnChangePassword.setOnClickListener(v -> {
            Toast.makeText(this, "Chức năng đang phát triển", Toast.LENGTH_SHORT).show();
        });

        btnDeleteAccount.setOnClickListener(v -> confirmDeleteAccount());
    }

    private void setupAvatarSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, avatarNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAvatar.setAdapter(adapter);

        spinnerAvatar.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                imgAvatar.setImageResource(avatarResIds[position]);
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) { }
        });
    }

    private void loadUserInfo() {
        User user = dbHelper.getUserInfo(username);
        if (user != null) {
            edtFullName.setText(user.getName());
            edtEmail.setText(user.getEmail());
            String avatarName = user.getAvatar();

            for (int i = 0; i < avatarNames.length; i++) {
                if (avatarNames[i].equals(avatarName)) {
                    spinnerAvatar.setSelection(i);
                    imgAvatar.setImageResource(avatarResIds[i]);
                    break;
                }
            }
        }
    }

    private void updateProfile() {
        String newName = edtFullName.getText().toString().trim();
        String selectedAvatar = avatarNames[spinnerAvatar.getSelectedItemPosition()];

        if (newName.isEmpty()) {
            Toast.makeText(this, "Tên không được để trống", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", newName);
        values.put("avatar", selectedAvatar);

        int result = db.update("Users", values, "email = ?", new String[]{username});
        db.close();

        if (result > 0) {
            Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
        }
    }

    private void confirmDeleteAccount() {
        new AlertDialog.Builder(this)
                .setTitle("Xóa tài khoản")
                .setMessage("Bạn có chắc muốn xóa tài khoản này không?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    int result = db.delete("users", "username = ?", new String[]{username});
                    db.close();

                    if (result > 0) {
                        Toast.makeText(this, "Đã xóa tài khoản", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(this, LoginActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    } else {
                        Toast.makeText(this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
