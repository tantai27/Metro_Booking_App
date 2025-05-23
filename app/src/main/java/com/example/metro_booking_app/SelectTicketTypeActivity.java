package com.example.metro_booking_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SelectTicketTypeActivity extends AppCompatActivity {

    Button btnVeLuot, btnVeNgay, btnVeTuan, btnVeThang;
    ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_ticket_type);

        btnVeLuot = findViewById(R.id.btnVeLuot);
        btnVeNgay = findViewById(R.id.btnVeNgay);
        btnVeTuan = findViewById(R.id.btnVeTuan);
        btnVeThang = findViewById(R.id.btnVeThang);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        btnVeLuot.setOnClickListener(v -> {
            Intent intent = new Intent(this, SelectRouteActivity.class);
            intent.putExtra("ticketType", "Vé lượt");
            intent.putExtra("price", 9000.0);  // ép kiểu double rõ ràng
            startActivity(intent);
        });

        btnVeNgay.setOnClickListener(v -> openConfirmActivity("Vé ngày", 40000));
        btnVeTuan.setOnClickListener(v -> openConfirmActivity("Vé 3 ngày", 90000));
        btnVeThang.setOnClickListener(v -> openConfirmActivity("Vé tháng", 300000));
    }

    private void openConfirmActivity(String ticketType, int price) {
        Intent intent = new Intent(this, ConfirmTicketActivity.class);
        intent.putExtra("ticketType", ticketType);    // truyền loại vé
        intent.putExtra("price", (double)price);              // truyền giá đúng
        startActivity(intent);
    }
}