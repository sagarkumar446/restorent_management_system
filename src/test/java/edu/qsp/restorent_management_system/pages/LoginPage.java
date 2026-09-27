package edu.qsp.restorent_management_system.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.boot.test.context.SpringBootTest;

import edu.qsp.restorent_management_system.utils.ConfigReader;
import edu.qsp.restorent_management_system.utils.WaitUtils;

@SpringBootTest 
public class LoginPage extends BasePage { 
    
    WaitUtils wait;
    private By email =
            By.cssSelector("input[type='email']");

    private By password =
            By.cssSelector("input[type='password']");

    private By loginButton =
            By.xpath("//input[@type=\"email\"]");

    public LoginPage(WebDriver driver) {
        super(driver);
        this.wait=new WaitUtils(driver);
    }

    public void enterEmail(String emailValue) {
        driver.findElement(email).sendKeys(emailValue);
    }

    public void enterPassword(String passwordValue) {

        driver.findElement(password)
                .sendKeys(passwordValue);
    }

    public void clickLogin() {
        wait.waitForElementViElement(email);
        driver.findElement(loginButton)
                .click();
    }

    public void login(
            String emailValue,
            String passwordValue) {

        enterEmail(emailValue);
        enterPassword(passwordValue);
        clickLogin();
    }
    public void navigateToLoginPage()
    {
        driver.navigate().to(ConfigReader.getProperty("baseUrl")+"/admin");
    }

    @Override
    public boolean isPageLoaded() {

        return driver.findElement(email)
                .isDisplayed();
    }
}