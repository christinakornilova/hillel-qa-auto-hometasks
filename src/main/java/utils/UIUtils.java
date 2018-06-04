package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class UIUtils {
    private static final int DEFAULT_WAIT_TIME_IN_SECONDS = 120;
    public static Logger log = LogManager.getLogger(UIUtils.class);

    public static WebElement clearAndFill(WebElement element, String data) {
        element.clear();
        element.sendKeys(data);
        return element;
    }

    public static void waitForJS() {
        new	ExpectedCondition<Boolean>()
        {
            @Override
            public Boolean apply(WebDriver driver)
            {
                return ((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete");
            }
        };
    }

    public static void checkExpectedTitle(String title, WebDriver driver) {
        String driverTitle = driver.getTitle();
        if (!driverTitle.equals(title)) {
            throw new IllegalStateException("This is not '" + title + "' page, but is '" + driverTitle + "'.");
        }
    }

    public static void waitUntilTitleIs(String string, WebDriver driver) {
        new WebDriverWait(driver, DEFAULT_WAIT_TIME_IN_SECONDS)
                .until(ExpectedConditions.titleIs(string));
    }

    public static void waitUntilElementAppears(int seconds, By by, WebDriver driver) {
        new WebDriverWait(driver, seconds).until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    public static void waitUntilElementAppears(By by, WebDriver driver) {
        waitUntilElementAppears(DEFAULT_WAIT_TIME_IN_SECONDS, by, driver);
    }

    public static void waitUntilElementAppears(WebElement element, WebDriver driver) {
        new WebDriverWait(driver, DEFAULT_WAIT_TIME_IN_SECONDS).until(ExpectedConditions.visibilityOf(element));
    }

    public static void waitUntilDialogAppears(WebDriver driver, WebElement element) {
        waitUntilElementAppears(element, driver);
    }

    public static void waitUntilElementDisappears(int seconds, By by, WebDriver driver) {
        new WebDriverWait(driver, seconds)
                .until(ExpectedConditions.invisibilityOfElementLocated(by));
    }

    public static void waitUntilDialogDisappears(WebDriver driver, String dialogCss) {
        waitUntilElementDisappears(DEFAULT_WAIT_TIME_IN_SECONDS, By.cssSelector(dialogCss), driver);
    }

    public static boolean isDialogOpened(WebDriver driver, String cssSelector) {
        return driver.findElements(By.cssSelector(cssSelector)).size() == 1
                && driver.findElement(By.cssSelector(cssSelector)).isDisplayed();
    }

    public static void waitForSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (Exception e) {
            log.error(e.getMessage());
            waitForSeconds(seconds);
        }
    }

    public static void waitUntilPopupWindowIsClosed(WebDriver driver) {
        waitUntilPopupWindowIsClosed(DEFAULT_WAIT_TIME_IN_SECONDS, driver);
    }

    private static void waitUntilPopupWindowIsClosed(int seconds, WebDriver driver) {
        new WebDriverWait(driver, seconds).until(ExpectedConditions.numberOfWindowsToBe(1));
    }

    public static void waitUntilElementToBeInteractable(WebElement element, WebDriver driver) {
        new WebDriverWait(driver, DEFAULT_WAIT_TIME_IN_SECONDS)
                .until(ExpectedConditions.elementToBeClickable(element));
    }

    public static boolean isElementPresent(By by, WebDriver driver) {
        return driver.findElements(by).size() == 1;
    }

}
