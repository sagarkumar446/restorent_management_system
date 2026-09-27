package edu.qsp.restorent_management_system.utils;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
public class WaitUtils {
    private WebDriverWait wait;
    public WaitUtils(WebDriver driver)
    {
         this.wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(10)
        );

    }
    public WebElement waitForElementViElement(By locator)
    {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }
    
}
