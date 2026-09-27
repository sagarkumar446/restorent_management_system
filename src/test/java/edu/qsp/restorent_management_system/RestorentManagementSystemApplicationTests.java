package edu.qsp.restorent_management_system;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;

import edu.qsp.restorent_management_system.driver.DriverFactory;
import edu.qsp.restorent_management_system.utils.ConfigReader;

public class RestorentManagementSystemApplicationTests {
    protected WebDriver driver;
    @BeforeEach
    public void setUp() {

        driver = DriverFactory.createDriver();

        driver.manage().window().maximize();

        driver.get(
                ConfigReader.getProperty("baseUrl")
        );
    }
    @AfterEach
    public void tearDown() {

        if (driver != null) {
            driver.quit();
        }
    }


}
