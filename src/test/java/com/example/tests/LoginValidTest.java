package com.example.tests;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.example.pages.LoginPage;

import io.github.bonigarcia.wdm.WebDriverManager;

public class LoginValidTest {

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
    public void testValidLogin() {
        try {
            String user = System.getProperty("sf.username");
            String pass = System.getProperty("sf.password");
            if (user == null || pass == null) {
                throw new IllegalArgumentException("Missing system properties: -Dsf.username and -Dsf.password");
            }
            loginPage.login(user, pass);
            boolean loggedIn = wait.until(d -> !d.getCurrentUrl().contains("login.salesforce.com"));
            Assert.assertTrue(loggedIn, "Expected to be navigated away from login page after valid credentials");
        } catch (Exception e) {
            throw new RuntimeException("Valid login test failed: " + e.getMessage(), e);
        }
    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

}
