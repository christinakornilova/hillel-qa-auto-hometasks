package jira.basic;

import jira.LogoutPage;
import jira.UserDashboardPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import utils.UIUtils;

public class UserPage extends BasePage {
    private static final String logoCss = "h1[id='logo']";

    protected static final String userFullnameCss = "a[id='header-details-user-fullname']";

    private static final String logOutCss = "a[id='log_out']";

    @FindBy(css = logoCss)
    WebElement logo;

    @FindBy(css = userFullnameCss)
    protected WebElement userFullname;

    @FindBy(css = logOutCss)
    WebElement logOutLink;

    public UserPage(WebDriver driver) {
        super(driver);
    }

    public UserDashboardPage openDashboard() {
        logo.click();
        return new UserDashboardPage(driver);
    }

    public LogoutPage logout() {
        userFullname.click();
        UIUtils.waitUntilElementAppears(logOutLink, driver);
        logOutLink.click();
        UIUtils.waitUntilTitleIs(LogoutPage.getLogoutPageTitle(), driver);
        return new LogoutPage(driver);
    }

    public boolean isUserLoggedIn() {
        return driver.findElements(By.cssSelector(userFullnameCss)).size() == 1;
    }

    public static boolean isOpened(WebDriver driver) {
        return UIUtils.isElementPresent(By.cssSelector(logoCss), driver) && UIUtils.isElementPresent(By.cssSelector(logOutCss), driver);
    }
}
