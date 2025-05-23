package com.example.metro_booking_app;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ConfirmTicketActivity extends AppCompatActivity {

    TextView txtTicketInfo, txtStartDate, txtEndDate, txtLoaiVeText, txtHSD;

    ImageView btnPickStartDate, btnBack;
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
        btnConfirm = findViewById(R.id.btnConfirm);
        btnBack = findViewById(R.id.btnBack);
        btnPickStartDate = findViewById(R.id.btnPickDate);
        TextView txtLoaiVe = findViewById(R.id.txtLoaiVe);
        txtHSD = findViewById(R.id.txtHSD);

        View notchLeft = findViewById(R.id.notchLeft);
        View notchRight = findViewById(R.id.notchRight);
        notchLeft.bringToFront();
        notchRight.bringToFront();

        Intent intent = getIntent();
        ticketType = intent.getStringExtra("ticketType");
        price = intent.getDoubleExtra("price", 0);
        btnConfirm.setText("MUA NGAY: " + (int) price + " ₫");

        if (ticketType.equals("Vé lượt")) {
            from = intent.getStringExtra("from");
            to = intent.getStringExtra("to");
            date = intent.getStringExtra("date");

            txtTicketInfo.setVisibility(View.VISIBLE);
            txtLoaiVe.setVisibility(View.GONE);
            txtHSD.setVisibility(View.GONE);

            txtTicketInfo.setText("Loại vé: " + ticketType + "\n"
                    + "Từ: " + from + "\nĐến: " + to + "\nNgày: " + date + "\nGiá: " + price + "đ");

            startDateStr = date;
            endDateStr = date;
            txtStartDate.setText("Bắt đầu: " + startDateStr);
            txtEndDate.setText("Kết thúc: " + endDateStr);
            btnPickStartDate.setEnabled(false);
        } else {
            txtTicketInfo.setVisibility(View.GONE);
            txtLoaiVe.setVisibility(View.VISIBLE);
            txtHSD.setVisibility(View.VISIBLE);

            txtLoaiVe.setText("Loại vé: " + ticketType);
            if (ticketType.equals("Vé ngày")) {
                txtHSD.setText("HSD: 24h kể từ thời điểm kích hoạt");
            } else if (ticketType.equals("Vé 3 ngày")) {
                txtHSD.setText("HSD: 72h kể từ thời điểm kích hoạt");
            } else if (ticketType.equals("Vé tháng")) {
                txtHSD.setText("HSD: 30 ngày kể từ thời điểm kích hoạt");
            }

            btnPickStartDate.setEnabled(true);
            btnPickStartDate.setOnClickListener(v -> showDatePicker());
        }

        btnBack.setOnClickListener(v -> {
            Intent backIntent = new Intent(ConfirmTicketActivity.this, SelectTicketTypeActivity.class);
            backIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(backIntent);
            finish();
        });

        btnConfirm.setOnClickListener(v -> {
            saveTicketToDatabase();
            Toast.makeText(this, "Đặt vé thành công!", Toast.LENGTH_LONG).show();
            finish();
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

            Calendar end = (Calendar) selected.clone();  // Clone ngày bắt đầu

            if (ticketType != null) {
                switch (ticketType) {
                    case "Vé lượt":
                        txtTicketInfo.setText("Loại vé: Vé lượt\nHSD: Dùng 1 lần, theo tuyến đã chọn");
                        end = null; // Không cần ngày kết thúc
                        break;

                    case "Vé ngày":
                        txtTicketInfo.setText("Loại vé: Vé ngày\nHSD: 24h kể từ thời điểm kích hoạt");
                        end.add(Calendar.DAY_OF_MONTH, 1);
                        break;

                    case "Vé 3 ngày":
                        txtTicketInfo.setText("Loại vé: Vé 3 ngày\nHSD: 72h kể từ thời điểm kích hoạt");
                        end.add(Calendar.DAY_OF_MONTH, 3);
                        break;

                    case "Vé tháng":
                        txtTicketInfo.setText("Loại vé: Vé tháng\nHSD: 30 ngày kể từ thời điểm kích hoạt");
                        end.add(Calendar.DAY_OF_MONTH, 30);
                        break;
                }
            }

            txtStartDate.setText("Ngày bắt đầu: " + startDateStr);
            if (end != null) {
                endDateStr = sdf.format(end.getTime());
                txtEndDate.setText("Ngày kết thúc: " + endDateStr);
            } else {
                txtEndDate.setText("Ngày kết thúc: Không áp dụng");
            }

        }, y, m, d).show();
    }

    private void saveTicketToDatabase() {
        MyDataBaseHelper dbHelper = new MyDataBaseHelper(this);
        ContentValues values = new ContentValues();

        int userId = 1;  // mặc định user ID

        values.put("ticket_type", ticketType);
        values.put("price", price);
        values.put("valid_from", startDateStr);
        values.put("valid_until", endDateStr);
        values.put("user_id", userId);

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.insert("Tickets", null, values);
        db.close();
        if (ticketType.equals("Vé lượt")) {
            values.put("from_station", from);
            values.put("to_station", to);
            values.put("travel_date", date);
        }
    }
}
