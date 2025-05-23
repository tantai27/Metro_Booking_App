package com.example.metro_booking_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class MyDataBaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MetroBooking.db";
    private static final int DATABASE_VERSION = 1;

    public static final String[] STATION_LIST = {
            "Bến Thành", "Nhà hát Thành phố", "Ba Son", "Công viên Văn Thánh",
            "Tân Cảng", "Thảo Điền", "An Phú", "Rạch Chiếc",
            "Phước Long", "Bình Thái", "Thủ Đức", "Khu Công nghệ cao",
            "Đại học Quốc gia", "Bến xe Suối Tiên"
    };

    public MyDataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng Users
        db.execSQL("CREATE TABLE Users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "email TEXT, " +
                "password TEXT, " +
                "avatar TEXT)");

        // Tạo bảng Trips
        db.execSQL("CREATE TABLE Trips (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "from_location TEXT, to_location TEXT, " +
                "date TEXT, time TEXT, price REAL, " +
                "seats_available INTEGER)");

        // Tạo bảng Bookings
        db.execSQL("CREATE TABLE Bookings (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER, trip_id INTEGER, seat_number TEXT, " +
                "status TEXT, FOREIGN KEY(user_id) REFERENCES Users(id), " +
                "FOREIGN KEY(trip_id) REFERENCES Trips(id))");

        // Tạo bảng Tickets
        db.execSQL("CREATE TABLE Tickets (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "ticket_type TEXT, " +
                "price REAL, " +
                "valid_from TEXT, " +
                "valid_until TEXT, " +
                "from_station TEXT, " +
                "to_station TEXT, " +
                "travel_date TEXT, " +
                "user_id INTEGER, " +
                "FOREIGN KEY(user_id) REFERENCES Users(id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Users");
        db.execSQL("DROP TABLE IF EXISTS Trips");
        db.execSQL("DROP TABLE IF EXISTS Bookings");
        db.execSQL("DROP TABLE IF EXISTS Tickets");
        onCreate(db);
    }

    // ------------------- HÀM THÊM DỮ LIỆU -------------------

    public void addUser(String name, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("email", email);
        values.put("password", password);
        values.put("avatar", "default_avatar.png"); // hoặc tên avatar mặc định trong drawable
        db.insert("Users", null, values);
        db.close();
    }

    public void addTrip(String from, String to, String date, String time, double price, int seatsAvailable) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("from_location", from);
        values.put("to_location", to);
        values.put("date", date);
        values.put("time", time);
        values.put("price", price);
        values.put("seats_available", seatsAvailable);
        db.insert("Trips", null, values);
        db.close();
    }

    public void addBooking(int userId, int tripId, String seatNumber, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put("trip_id", tripId);
        values.put("seat_number", seatNumber);
        values.put("status", status);
        db.insert("Bookings", null, values);
        db.close();
    }

    public void addTicket(String ticketType, double price, String validFrom, String validUntil, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ticket_type", ticketType);
        values.put("price", price);
        values.put("valid_from", validFrom);
        values.put("valid_until", validUntil);
        values.put("user_id", userId);
        db.insert("Tickets", null, values);
        db.close();
    }

    // ------------------- TRUY VẤN DỮ LIỆU -------------------

    public User getUserInfo(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Users WHERE email = ?", new String[]{email});
        if (cursor != null && cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex("name");
            int passwordIndex = cursor.getColumnIndex("password");
            int avatarIndex = cursor.getColumnIndex("avatar");

            if (nameIndex >= 0 && passwordIndex >= 0 && avatarIndex >= 0) {
                String name = cursor.getString(nameIndex);
                String password = cursor.getString(passwordIndex);
                String avatar = cursor.getString(avatarIndex);
                cursor.close();
                return new User(name, email, password, avatar);
            } else {
                Log.e("DB_ERROR", "Thiếu cột trong bảng Users");
            }
        }
        if (cursor != null) cursor.close();
        return null;
    }


    public boolean checkLogin(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Users WHERE email = ? AND password = ?", new String[]{email, password});
        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return isValid;
    }

    public int getUserIdByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM Users WHERE email = ?", new String[]{email});
        int userId = -1;
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        }
        cursor.close();
        db.close();
        return userId;
    }

    public boolean checkEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Users WHERE email = ?", new String[]{email});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    public String getAllAccountsAsString() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Users", null);
        StringBuilder sb = new StringBuilder();

        while (cursor.moveToNext()) {
            String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
            String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
            sb.append("Email: ").append(email).append("\n")
                    .append("Password: ").append(password).append("\n\n");
        }

        cursor.close();
        db.close();
        return sb.toString();
    }

    public void printAllAccounts() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Users", null);
        if (cursor.moveToFirst()) {
            do {
                String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
                String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
                Log.d("DB_USER", "Email: " + email + ", Password: " + password);
            } while (cursor.moveToNext());
        } else {
            Log.d("DB_USER", "Không có tài khoản nào trong database.");
        }
        cursor.close();
        db.close();
    }

    // ------------------- CẬP NHẬT DỮ LIỆU -------------------

    public boolean updatePassword(String email, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("password", newPassword);
        int rows = db.update("Users", values, "email = ?", new String[]{email});
        db.close();
        return rows > 0;
    }

    public boolean updateUserName(String email, String newName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", newName);
        int rows = db.update("Users", values, "email = ?", new String[]{email});
        db.close();
        return rows > 0;
    }

    public boolean updateAvatar(String email, String newAvatar) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("avatar", newAvatar);
        int rows = db.update("Users", values, "email = ?", new String[]{email});
        db.close();
        return rows > 0;
    }

    // ------------------- XÓA DỮ LIỆU -------------------

    public boolean deleteUserByEmail(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete("Users", "email = ?", new String[]{email});
        db.close();
        return rowsDeleted > 0;
    }

    public void createRandomTrips(int numberOfTrips) {
        SQLiteDatabase db = this.getWritableDatabase();
        Random random = new Random();

        for (int i = 0; i < numberOfTrips; i++) {
            int fromIndex, toIndex;
            do {
                fromIndex = random.nextInt(STATION_LIST.length);
                toIndex = random.nextInt(STATION_LIST.length);
            } while (fromIndex == toIndex); // tránh đi từ - đến trùng nhau

            String fromStation = STATION_LIST[fromIndex];
            String toStation = STATION_LIST[toIndex];

            // Tạo ngày hôm nay
            String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

            // Tạo giờ ngẫu nhiên từ 5:00 đến 22:00
            int hour = 5 + random.nextInt(18);  // từ 5 đến 22
            int minute = random.nextInt(2) * 30;  // 0 hoặc 30 phút
            String time = String.format(Locale.getDefault(), "%02d:%02d", hour, minute);

            double price = 9000; // hoặc tính theo khoảng cách

            int seatsAvailable = 147;

            // Thêm chuyến vào DB
            ContentValues values = new ContentValues();
            values.put("from_location", fromStation);
            values.put("to_location", toStation);
            values.put("date", date);
            values.put("time", time);
            values.put("price", price);
            values.put("seats_available", seatsAvailable);
            db.insert("Trips", null, values);
        }

        db.close();
    }

    // Lấy mật khẩu theo email
    public String getPasswordByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String password = null;
        Cursor cursor = db.rawQuery("SELECT password FROM Users WHERE email = ?", new String[]{email});
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
            }
            cursor.close();
        }
        return password;
    }
}
