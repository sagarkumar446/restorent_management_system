package edu.qsp.restorent_management_system.auth;

public class User {

    private String email;
    private String password;
    private Enum role;

    public User(String email, String password, Enum role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Enum getRole() {
        return role;
    }
}