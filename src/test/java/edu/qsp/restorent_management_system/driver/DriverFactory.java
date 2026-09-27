package edu.qsp.restorent_management_system.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class DriverFactory {

    public static WebDriver createDriver() {

        return new ChromeDriver();
    }
}