package edu.qsp.restorent_management_system.tests;




import org.junit.jupiter.api.Test;

import edu.qsp.restorent_management_system.RestorentManagementSystemApplicationTests;
import edu.qsp.restorent_management_system.pages.LoginPage;
public class LoginTest extends RestorentManagementSystemApplicationTests {
  @Test 
   public  void loginTest()
   {

   LoginPage loginPage=new LoginPage(driver);
   loginPage.navigateToLoginPage();
   loginPage.login("admin@tfc.com", "admin");


   }
}