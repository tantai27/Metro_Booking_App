package com.example.metro_booking_app;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ConfirmTicketActivity extends AppCompatActivity {

    TextView txtTicketInfo, txtStartDate, txtEndDate;
    ImageView btnPickStartDate;
    Button btnConfirm;

    String ticketType, from, to, date;
    double price;

    String startDateStr = "", endDateStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_ticket);

        txtTicketInfo = findViewById(R.id.txtTicketInfo);
        txtStartDate = findViewById(R.id.txtStartDate);
        txtEndDate = findViewById(R.id.txtEndDate);
        btnPickStartDate = findViewById(R.id.btnPickStartDate);
        btnConfirm = findViewById(R.id.btnConfirm);

        Intent intent = getIntent();
        ticketType = intent.getStringExtra("ticketType");
        price = intent.getDoubleExtra("price", 0);

        if (ticketType.equals("Vé lượt")) {
            from = intent.getStringExtra("from");
            to = intent.getStringExtra("to");
            date = intent.getStringExtra("date");

            txtTicketInfo.setText("Loại vé: " + ticketType + "\n"
                    + "Từ: " + from + "\nĐến: " + to + "\nNgày: " + date + "\nGiá: " + price + "đ");
            txtStartDate.setText(date);  // Vé lượt dùng ngày đã chọn trước
            btnPickStartDate.setEnabled(false); // Không cho chọn lại
        } else {
            txtTicketInfo.setText("Loại vé: " + ticketType + "\nGiá: " + price + "đ");
            btnPickStartDate.setOnClickListener(v -> showDatePicker());
        }

        btnConfirm.setOnClickListener(v -> {
            if (!ticketType.equals("Vé lượt") && startDateStr.isEmpty()) {
                Toast.makeText(this, "Vui lòng chọn ngày bắt đầu", Toast.LENGTH_SHORT).show();
                return;
            }

            // Lưu thông tin vé vào database
            saveTicketToDatabase();

            // Xác nhận thành công và quay lại
            Toast.makeText(this, "Đặt vé thành công!", Toast.LENGTH_LONG).show();
            finish(); // Quay về trang trước hoặc Main
        });
    }

    private void showDatePicker() {
        Calendar c = Calendar.getInstance();
        int y = c.get(Calendar.YEAR), m = c.get(Calendar.MONTH), d = c.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            Calendar selected = Calendar.getInstance();
            selected.set(year, month, dayOfMonth);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

            startDateStr = sdf.format(selected.getTime());
            txtStartDate.setText(startDateStr);

            Calendar end = (Calendar) selected.clone();
            switch (ticketType) {
                case "Vé tuần":
                    end.add(Calendar.DAY_OF_MONTH, 6); break; // 7 ngày tính từ ngày bắt đầu
                case "Vé tháng":
                    end.add(Calendar.DAY_OF_MONTH, 29); break; // 30 ngày tính từ ngày bắt đầu
                default: break;
            }
            endDateStr = sdf.format(end.getTime());
            txtEndDate.setText("Ngày kết thúc: " + endDateStr);
        }, y, m, d).show();
    }

    private void saveTicketToDatabase() {
        MyDataBaseHelper dbHelper = new MyDataBaseHelper(this);
        ContentValues values = new ContentValues();

        // Lấy user_id từ SharedPreferences hoặc Intent
        int userId = 1;  // Giả sử là ID của người dùng hiện tại (có thể lấy từ đăng nhập hoặc session)

        values.put("ticket_type", ticketType);
        values.put("price", price);
        values.put("valid_from", startDateStr);
        values.put("valid_until", endDateStr);
        values.put("user_id", userId);

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.insert("Tickets", null, values);
        db.close();
    }
}