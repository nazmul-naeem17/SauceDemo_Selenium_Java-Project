package com.saucedemo;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class SauceDemoTest {

    private WebDriver driver;

    @BeforeMethod
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    public void testLoginAddPLogout() throws InterruptedException {

        // Login
        driver.get("https://www.saucedemo.com/");
        Thread.sleep(2000);
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();
        
        System.out.println("Logged in successfully...");
        Thread.sleep(3000);

        // Add Product
        WebElement product = driver.findElement(By.className("inventory_item"));
        String productName = product.findElement(By.className("inventory_item_name")).getText();
        product.findElement(By.className("btn_inventory")).click();

        System.out.println("Added item to cart...");
        Thread.sleep(3000);

        // Verify Cart
        driver.findElement(By.className("shopping_cart_link")).click();
        String cartProductName = driver.findElement(By.className("inventory_item_name")).getText();
        Assert.assertEquals(cartProductName, productName, "Product name in cart does not match!");
        
        System.out.println("Verified item in cart...");
        Thread.sleep(3000);

        // Logout
        driver.findElement(By.id("react-burger-menu-btn")).click();
        
        Thread.sleep(2000);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement logoutLink = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("logout_sidebar_link")));
        
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", logoutLink);

        WebElement loginButton = driver.findElement(By.id("login-button"));
        Assert.assertTrue(loginButton.isDisplayed(), "User was not logged out successfully.");
        
        System.out.println("✅ Logged out. Closing in 3 seconds...");
        Thread.sleep(3000);
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}