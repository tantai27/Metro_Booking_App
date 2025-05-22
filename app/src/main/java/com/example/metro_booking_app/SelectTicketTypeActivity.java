package com.example.metro_booking_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SelectTicketTypeActivity extends AppCompatActivity {

    Button btnVeLuot, btnVeNgay, btnVeTuan, btnVeThang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_ticket_type);

        btnVeLuot = findViewById(R.id.btnVeLuot);
        btnVeNgay = findViewById(R.id.btnVeNgay);
        btnVeTuan = findViewById(R.id.btnVeTuan);
        btnVeThang = findViewById(R.id.btnVeThang);

        btnVeLuot.setOnClickListener(v -> {
            Intent intent = new Intent(this, SelectRouteActivity.class);
            startActivity(intent);
        });

        btnVeNgay.setOnClickListener(v -> openConfirmActivity("Vé ngày", 40000));
        btnVeTuan.setOnClickListener(v -> openConfirmActivity("Vé 3 ngày", 90000));
        btnVeThang.setOnClickListener(v -> openConfirmActivity("Vé tháng", 300000));
    }

    private void openConfirmActivity(String ticketType, int price) {
        Intent intent = new Intent(this, ConfirmTicketActivity.class);
        intent.putExtra("ticketType", ticketType);
        intent.putExtra("price", price);
        startActivity(intent);
    }
}