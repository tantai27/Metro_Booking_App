package com.example.metro_booking_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PaymentActivity extends AppCompatActivity {

    Button btnBackToHome;
    TextView tvTicketId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        btnBackToHome = findViewById(R.id.btnBackToHome);
        tvTicketId = findViewById(R.id.tvTicketId);

        // Nhận dữ liệu từ Intent
        long ticketId = getIntent().getLongExtra("ticket_id", -1);
        String ticketType = getIntent().getStringExtra("ticket_type");

        // Hiển thị dữ liệu
        if (ticketId != -1 && ticketType != null) {
            tvTicketId.setText("Mã vé: " + ticketId + "\nLoại vé: " + ticketType);
        } else {
            tvTicketId.setText("Không nhận được thông tin vé");
        }

        // Xử lý nút quay lại màn hình chính
        btnBackToHome.setOnClickListener(v -> {
            Intent intent = new Intent(PaymentActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }
}
