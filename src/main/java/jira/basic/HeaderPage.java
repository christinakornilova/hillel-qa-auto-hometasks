package jira.basic;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HeaderPage extends BasePage {
    private static final String logoCss = "h1[id='logo']";
    private static final String dashboardsMenuCss = "a[id='home_link']";
    private static final String serchFieldCss = "input[id='quickSearchInput']";
    private static final String logInLinkCss = "a[class='aui-nav-link login-link']";

    @FindBy(css = logoCss)
    WebElement jiraMainLogo;

    public HeaderPage(WebDriver driver) {
        super(driver);
    }

}
