package edu.ntudp.pzks.secondLab;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class SecondLabIndivid {

    private WebDriver chromeDriver;

    private static final String baseUrl = "https://github.com/";

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        //Run driver
        WebDriverManager.chromedriver().setup();
        ChromeOptions chromeOptions = new ChromeOptions();
        //set fullscreen
        chromeOptions.addArguments("--start-fullscreen");
        //setup wait for loading elements
        chromeOptions.setImplicitWaitTimeout(Duration.ofSeconds(15));
        this.chromeDriver = new ChromeDriver(chromeOptions);
    }

    @BeforeMethod
    public void preconditions() {
        //open main page
        chromeDriver.get(baseUrl);
    }

    @Test
    public void testClick() {
        WebElement clickable = chromeDriver.findElement(By.cssSelector("a.btn-mktg.home-campaign-enterprise.btn-muted-mktg"));
        clickable.click();
    }

    @Test
    public void testEnterData() {
        chromeDriver.get(baseUrl);
        // Find the search field
        WebElement search = chromeDriver.findElement(By.cssSelector("button.header-search-button"));
        search.click();
        // Verification
        Assert.assertNotNull(search);
        // Input value
        WebElement searchButton = chromeDriver.findElement(By.id("query-builder-test"));
        searchButton.click();
        String input = "selenium";
        searchButton.sendKeys(input);
        // Verification text
        Assert.assertEquals(searchButton.getAttribute("value"), input);
        // Click enter
        searchButton.sendKeys(Keys.ENTER);
        // Add a wait to ensure the page has loaded after pressing enter
        WebDriverWait wait = new WebDriverWait(chromeDriver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.urlContains("search"));
    }

    @Test
    public void testClickWithXPATH() {
        String path = "//button[contains(text(),'Product')]";
        //find element by xpath
        WebElement signInButton = chromeDriver.findElement(By.xpath(path));
        //verification
        Assert.assertNotNull(signInButton);
        signInButton.click();
    }

    @Test
    public void testCheck() {
        WebElement button = chromeDriver.findElement(By.xpath("/html/body/div[1]/div[1]/header/div/div[2]/div/nav/ul/li[2]/button"));
        button.click();
        WebElement buttonEducation = chromeDriver.findElement(By.xpath("/html/body/div[1]/div[1]/header/div/div[2]/div/nav/ul/li[2]/div/div[1]/ul/li[4]/a"));
        if (buttonEducation.isSelected()) {
            buttonEducation.click();
        } else {
            System.out.println(String.format("\n Rezult = Education is not selected"));
        }
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        chromeDriver.quit();
    }
}

