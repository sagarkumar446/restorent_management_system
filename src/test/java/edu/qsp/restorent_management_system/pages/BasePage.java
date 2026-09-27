package edu.qsp.restorent_management_system.pages;

import org.openqa.selenium.WebDriver;

import edu.qsp.restorent_management_system.utils.ConfigReader;



public abstract class BasePage {

    protected WebDriver driver;

    public BasePage(WebDriver driver) {
        this.driver = driver;
    }
    public String  getBaseUrl()
    {
          return ConfigReader.getProperty("baseUrl").toString();
    }
    public abstract boolean isPageLoaded();
}