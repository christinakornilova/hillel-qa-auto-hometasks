package jira;

import jira.basic.HeaderPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import utils.UIUtils;

public class LogoutPage extends HeaderPage {
    private static final String logoutPageTitle = "Logout - Hillel IT School JIRA";
    private static final String loginAgainLinkCss = "a[href='/login.jsp']";

    @FindBy(css = loginAgainLinkCss)
    WebElement loginAgainLink;

    public LogoutPage(WebDriver driver) {
        super(driver);
        UIUtils.checkExpectedTitle(logoutPageTitle, driver);
    }

    public boolean isOpened() {
        return loginAgainLink.isDisplayed();
    }

    public static String getLogoutPageTitle() {
        return logoutPageTitle;
    }
}
