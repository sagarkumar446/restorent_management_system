package edu.qsp.restorent_management_system.auth;

import org.openqa.selenium.WebDriver;

import edu.qsp.restorent_management_system.pages.LoginPage;

public class UIAuthentication implements Authentication {

    private WebDriver driver;

    public UIAuthentication(WebDriver driver) {
        this.driver = driver;
    }

    @Override
    public void login(User user) {

        LoginPage loginPage =
                new LoginPage(driver);

        loginPage.login(
                user.getEmail(),
                user.getPassword()
        );
    }

    @Override
    public void logout() {

        // We will implement after
        // inspecting your actual application.
    }

    @Override
    public boolean isAuthenticated() {

        // We will implement after
        // inspecting your actual application.
        return false;
    }
}