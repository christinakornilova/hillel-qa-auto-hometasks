package jira.base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utils.UIUtils;

public class BasePage {

    public static Logger log;

    protected WebDriver driver;

    public static final String errorMessageXpath = "//div[@id='usernameerror']";

    @FindBy(xpath = errorMessageXpath)
    WebElement errorMessage;

    @FindBy(xpath = "//div[@id='usernameerror']/p")
    WebElement errorMessageText;

    public BasePage(WebDriver driver) {
        log = LogManager.getLogger(this);
        this.driver = driver;
        UIUtils.waitForJS();
        PageFactory.initElements(driver, this);
        log.info("navigating");
    }

    public boolean isErrorMessageDisplayed() {
        try {
            return errorMessage.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public String getErrorMessageText() {
        if (isErrorMessageDisplayed()) {
            return errorMessageText.getText();
        }
        throw new IllegalStateException("Unable to get error message. The error message is not displayed");
    }

}
