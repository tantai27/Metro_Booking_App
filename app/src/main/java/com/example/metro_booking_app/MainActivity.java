package com.example.metro_booking_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    LinearLayout btnBuyTicket, btnMyTicket, btnExchangeQR, btnTicketInfo;
    LinearLayout btnRoute, btnMap, btnVirtualTour, btnAccount;

    String loggedInUsername; // Giả định đã đăng nhập

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loggedInUsername = getIntent().getStringExtra("username"); // Nhận từ LoginActivity

        btnBuyTicket = findViewById(R.id.btnBuyTicket);
        btnMyTicket = findViewById(R.id.btnMyTicket);
        btnExchangeQR = findViewById(R.id.btnExchangeQR);
        btnTicketInfo = findViewById(R.id.btnTicketInfo);
        btnRoute = findViewById(R.id.btnRoute);
        btnMap = findViewById(R.id.btnMap);
        btnVirtualTour = findViewById(R.id.btnVirtualTour);
        btnAccount = findViewById(R.id.btnAccount);

        btnBuyTicket.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SelectTicketTypeActivity.class);
//            intent.putExtra("email", email); // nếu cần truyền email người dùng
            startActivity(intent);
        });

        btnMyTicket.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MyTicketsActivity.class);
            startActivity(intent);
        });

        btnExchangeQR.setOnClickListener(v -> {
            // TODO: Chuyển đến màn hình đổi mã lấy vé
        });

        btnTicketInfo.setOnClickListener(v -> {
            // TODO: Chuyển đến màn hình thông tin vé
        });

        btnRoute.setOnClickListener(v -> {
            // TODO: Chuyển đến màn hình hành trình
        });

        btnMap.setOnClickListener(v -> {
            // TODO: Chuyển đến màn hình bản đồ
        });

        btnVirtualTour.setOnClickListener(v -> {
            // TODO: Chuyển đến tour ảo
        });

        btnAccount.setOnClickListener(v -> {
            if (loggedInUsername != null) {
                Intent intent = new Intent(MainActivity.this, AccountActivity.class);
                intent.putExtra("username", loggedInUsername);
                startActivity(intent);

            } else {
                Toast.makeText(this, "Không tìm thấy người dùng", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
