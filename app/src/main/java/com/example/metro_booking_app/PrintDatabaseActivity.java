package com.example.metro_booking_app;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class PrintDatabaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_database);

        MyDataBaseHelper dbHelper = new MyDataBaseHelper(this);
        String allAccounts = dbHelper.getAllAccountsAsString(); // Viết thêm hàm này trong MyDataBaseHelper

        TextView tvContent = findViewById(R.id.tvDatabaseContent);
        tvContent.setText(allAccounts);
    }
}
