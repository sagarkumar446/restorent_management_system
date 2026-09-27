package edu.qsp.restorent_management_system.auth;

public interface Authentication {

    void login(User user);

    void logout();

    boolean isAuthenticated();
}