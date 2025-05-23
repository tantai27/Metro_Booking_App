package com.example.metro_booking_app;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class TicketDetailActivity extends AppCompatActivity {
    TextView txtDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_detail);

        txtDetails = findViewById(R.id.txtDetails);
        Ticket ticket = (Ticket) getIntent().getSerializableExtra("ticket");

        String info = "Loại vé: " + ticket.getTicketType() + "\n"
                + "Từ: " + ticket.getFrom() + "\n"
                + "Đến: " + ticket.getTo() + "\n"
                + "Ngày đi: " + ticket.getDate() + "\n"
                + "Hiệu lực: " + ticket.getStart() + " → " + ticket.getEnd() + "\n"
                + "Giá: " + ticket.getPrice() + " đ";

        txtDetails.setText(info);
    }
}
