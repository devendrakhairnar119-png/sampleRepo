package com.example.tests;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.example.pages.LoginPage;

import io.github.bonigarcia.wdm.WebDriverManager;

public class LoginInvalidTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private LoginPage loginPage;

    @BeforeTest
    public void setUp() {
        try {
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            driver.get("https://login.salesforce.com/?locale=in");
            loginPage = new LoginPage(driver);
        } catch (Exception e) {
            throw new RuntimeException("Setup failed: " + e.getMessage(), e);
        }
    }

    @Test
    public void testInvalidLogin() {
        try {
            loginPage.login("invalid.user@example.com", "WrongPassword123!");
            boolean errorShown = wait.until(d -> loginPage.isErrorDisplayed());
            Assert.assertTrue(errorShown, "Expected error message to be displayed for invalid credentials");
            String text = loginPage.getErrorText();
            Assert.assertFalse(text.isEmpty(), "Expected non-empty error text");
        } catch (Exception e) {
            throw new RuntimeException("Invalid login test failed: " + e.getMessage(), e);
        }
    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

}
