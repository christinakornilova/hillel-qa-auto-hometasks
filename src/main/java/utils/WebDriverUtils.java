package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


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

                //Set file 'save to' directory (for download attachment Jira test)
                Map<String, Object> chromePreferences = new Hashtable<>();

                chromePreferences.put("download.default_directory", JiraConstants.attachmentSaveToPath);

                //Below chrome preference settings will disable popup dialog when download file (for download attachment Jira test)
                chromePreferences.put("download.prompt_for_download", "false");
                chromePreferences.put("profile.default_content_settings.popups", 0);
                chromePreferences.put("download.directory_upgrade", "true");
                chromePreferences.put("safebrowsing.enabled", "true");

                chromeOptions.setExperimentalOption("prefs", chromePreferences);
                chromeOptions.addArguments("--test-type", "--start-maximized", "--incognito");

                driver = new ChromeDriver(chromeOptions);
                driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
                break;
            default:
                break;
        }
        drivers.add(driver);
        return driver;
    }

}
