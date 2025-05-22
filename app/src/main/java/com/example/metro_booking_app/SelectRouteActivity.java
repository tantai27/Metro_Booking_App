package com.example.metro_booking_app;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

public class SelectRouteActivity extends AppCompatActivity {

    Spinner spinnerFrom, spinnerTo;
    TextView txtDate;
    ImageView btnPickDate;
    Button btnContinue;

    String[] stations = {
            "Bến Thành", "Nhà hát thành phố", "Ba Son", "Công viên Văn Thánh", "Tân Cảng",
            "Thảo Điền", "An Phú", "Rạch Chiếc", "Phước Long", "Bình Thái",
            "Thủ Đức", "Khu công nghệ cao", "Bến xe Suối Tiên", "Đại học Quốc Gia"
    };

    String selectedDate = "";
    Random random = new Random();  // Dùng để random thời gian và ga

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_route);

        spinnerFrom = findViewById(R.id.spinnerFrom);
        spinnerTo = findViewById(R.id.spinnerTo);
        txtDate = findViewById(R.id.txtDate);
        btnPickDate = findViewById(R.id.btnPickDate);
        btnContinue = findViewById(R.id.btnContinue);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, stations);
        spinnerFrom.setAdapter(adapter);
        spinnerTo.setAdapter(adapter);

        btnPickDate.setOnClickListener(v -> showDatePicker());

        btnContinue.setOnClickListener(v -> {
            String from = spinnerFrom.getSelectedItem().toString();
            String to = spinnerTo.getSelectedItem().toString();

            if (from.equals(to)) {
                Toast.makeText(this, "Điểm đến không được trùng điểm khởi hành!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedDate.isEmpty()) {
                Toast.makeText(this, "Vui lòng chọn ngày đi!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Chuyển sang giao diện xác nhận thanh toán
            Intent intent = new Intent(this, ConfirmTicketActivity.class);
            intent.putExtra("ticketType", "Vé lượt");
            intent.putExtra("from", from);
            intent.putExtra("to", to);
            intent.putExtra("date", selectedDate);
            intent.putExtra("price", 9000);
            startActivity(intent);
        });
    }

    private void showDatePicker() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dp = new DatePickerDialog(this, (view, y, m, d) -> {
            selectedDate = String.format("%04d-%02d-%02d", y, m + 1, d);  // ISO format: yyyy-MM-dd
            txtDate.setText(String.format("%02d/%02d/%04d", d, m + 1, y)); // Hiển thị: dd/MM/yyyy

            // Sinh 200 chuyến metro cho ngày được chọn
            generateRandomTripsForDate(selectedDate);
            //Toast.makeText(this, "Đã tạo 200 chuyến cho ngày " + selectedDate, Toast.LENGTH_SHORT).show();

        }, year, month, day);
        dp.show();
    }

    private void generateRandomTripsForDate(String date) {
        MyDataBaseHelper dbHelper = new MyDataBaseHelper(this);

        for (int i = 0; i < 200; i++) {
            int fromIndex, toIndex;

            do {
                fromIndex = random.nextInt(stations.length);
                toIndex = random.nextInt(stations.length);
            } while (fromIndex == toIndex);  // Không cho trùng ga

            String fromStation = stations[fromIndex];
            String toStation = stations[toIndex];

            int hour = 5 + random.nextInt(18);   // 5h đến 22h (5 + 0~17)
            int minute = random.nextInt(60);
            String time = String.format(Locale.getDefault(), "%02d:%02d", hour, minute);

            dbHelper.addTrip(fromStation, toStation, date, time, 9000.0, 147);
        }
    }
}
