package com.example.metro_booking_app;

public class User {
    private int id;
    private String name;
    private String email;
    private String password;
    private String avatar;

    public User(int id, String name, String email, String password, String avatar) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.avatar = avatar;
    }

    // Constructor không có id (nếu cần tạo user mới trước khi lưu vào DB)
    public User(String name, String email, String password, String avatar) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.avatar = avatar;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setId(int id) {
        this.id = id;
    }
}
