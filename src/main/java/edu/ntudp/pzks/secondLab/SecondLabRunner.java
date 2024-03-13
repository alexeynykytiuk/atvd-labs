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


public class SecondLabRunner {

    private WebDriver chromeDriver;

    private static final String baseUrl = "https://www.nmu.org.ua/ua/";

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
    //will be failed - fix it
    public void testHeaderExists() {
        //find element by id
        WebElement header = chromeDriver.findElement(By.id("heder"));
        //verification
        Assert.assertNotNull(header);
    }


    @Test
    public void testClickOnStudent() {
        //find element by xpath
        WebElement forStudentButton = chromeDriver.findElement(By.xpath("/html/body/center/div[4]/div/div[1]/ul/li[4]/a"));
        //verification
        Assert.assertNotNull(forStudentButton);
        forStudentButton.click();
        //verification page changed
        Assert.assertNotEquals(chromeDriver.getCurrentUrl(), baseUrl);
    }

    @Test
    public void testSearchFieldOnForStudentPage() {
        String studentPageUrl = "content/student_life/students/";
        chromeDriver.get(baseUrl + studentPageUrl);

        // Refresh the page
        chromeDriver.navigate().refresh();

        // Wait for the page to load
        WebDriverWait wait = new WebDriverWait(chromeDriver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains(studentPageUrl));

        // Find element by tagName
        WebElement searchField = chromeDriver.findElement(By.tagName("input"));

        // Verification
        Assert.assertNotNull(searchField);

        // Different params of searchField
        System.out.println(String.format("Name attribute: %s", searchField.getAttribute("name")) +
                String.format("\nID attribute: %s", searchField.getAttribute("id")) +
                String.format("\nType attribute: %s", searchField.getAttribute("type")) +
                String.format("\nValue attribute: %s", searchField.getAttribute("value")) +
                String.format("\nPosition: (%d;%d)", searchField.getLocation().x, searchField.getLocation().y) +
                String.format("\nSize: %dx%d", searchField.getSize().height, searchField.getSize().width));

        // Click on the search field to focus it
        searchField.click();

        // Input value
        String inputValue = "I need info";
        searchField.sendKeys(inputValue);

        // Verification text
        Assert.assertEquals(searchField.getAttribute("value"), inputValue);

        // Click enter
        searchField.sendKeys(Keys.ENTER);

        // Wait for the page to load after the search
        wait.until(ExpectedConditions.not(ExpectedConditions.urlToBe(baseUrl + studentPageUrl)));

        // Verification page changed
        Assert.assertNotEquals(chromeDriver.getCurrentUrl(), baseUrl + studentPageUrl);
    }


    @Test
    public void testSlider() {
        //find element by class name
        WebElement nextButton = chromeDriver.findElement(By.className("next"));
        //find element by css selector
        WebElement nextButtonByCss = chromeDriver.findElement(By.cssSelector("a.next"));
        //verification equality
        Assert.assertEquals(nextButton, nextButtonByCss);

        WebElement previousButton = chromeDriver.findElement(By.className("prev"));

        for (int i = 0; i < 20; i++) {
            //change count of interations just for run
            if (nextButton.getAttribute("class").contains("disabled")) {
                previousButton.click();
                Assert.assertTrue(previousButton.getAttribute("class").contains("disabled"));
                Assert.assertFalse(nextButton.getAttribute("class").contains("disabled"));
            } else {
                nextButton.click();
                Assert.assertTrue(nextButton.getAttribute("class").contains("disabled"));
                Assert.assertFalse(previousButton.getAttribute("class").contains("disabled"));
            }
        }
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        chromeDriver.quit();
    }
}
