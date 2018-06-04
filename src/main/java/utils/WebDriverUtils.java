package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.ArrayList;
import java.util.List;


public class WebDriverUtils {
    public static List<org.openqa.selenium.WebDriver> drivers = new ArrayList<org.openqa.selenium.WebDriver>();

    public enum Browser {
        CHROME
    }

    public static WebDriver getDriver(Browser browser) {
        return initDriver(browser);
    }

    private static WebDriver initDriver(Browser browser) {
        WebDriver driver = null;

        String browserDriversPath = "/usr/local/bin/";
        String driverFileName = "";
        switch (browser) {
            case CHROME:
                driverFileName = "chromedriver";
                System.setProperty("webdriver.chrome.driver", browserDriversPath + driverFileName);
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.setHeadless(false);
                driver = new ChromeDriver(chromeOptions);
                break;
            default:
                break;
        }
        drivers.add(driver);
        return driver;
    }

}
