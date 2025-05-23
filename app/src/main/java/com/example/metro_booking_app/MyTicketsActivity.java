package com.example.metro_booking_app;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MyTicketsActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<Ticket> ticketList;
    TicketAdapter adapter;
    MyDataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tickets);

        listView = findViewById(R.id.listViewTickets);
        dbHelper = new MyDataBaseHelper(this);
        ticketList = new ArrayList<>();

        loadTicketsFromDB();

        adapter = new TicketAdapter(this, ticketList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((AdapterView<?> parent, android.view.View view, int position, long id) -> {
            Ticket ticket = ticketList.get(position);
            Intent intent = new Intent(this, TicketDetailActivity.class);
            intent.putExtra("ticket", ticket);  // Ticket implements Serializable
            startActivity(intent);
        });
    }

    private void loadTicketsFromDB() {
        ticketList.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Tickets WHERE user_id = ?", new String[]{"1"});

        while (cursor.moveToNext()) {
            Ticket t = new Ticket();
            t.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            t.setTicketType(cursor.getString(cursor.getColumnIndexOrThrow("ticket_type")));
            t.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow("price")));
            t.setStart(cursor.getString(cursor.getColumnIndexOrThrow("valid_from")));
            t.setEnd(cursor.getString(cursor.getColumnIndexOrThrow("valid_until")));
            t.setFrom(cursor.getString(cursor.getColumnIndexOrThrow("from_station")));
            t.setTo(cursor.getString(cursor.getColumnIndexOrThrow("to_station")));
            t.setDate(cursor.getString(cursor.getColumnIndexOrThrow("travel_date")));
            ticketList.add(t);
        }
        cursor.close();
        db.close();
    }
}