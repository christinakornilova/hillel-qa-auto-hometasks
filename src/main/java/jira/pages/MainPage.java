package jira.pages;

import jira.LoginFormComponent;
import jira.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.UIUtils;

public class MainPage extends BasePage {

    private static final String mainPageTitleText = "System Dashboard - Hillel IT School JIRA";

    //Login form
    LoginFormComponent loginForm = new LoginFormComponent(driver);

    public MainPage(WebDriver driver) {
        super(driver);
        UIUtils.checkExpectedTitle(mainPageTitleText, driver);
    }

    public UserDashboardPage loginSuccessfully(String username, String password) {
        loginForm.loginSuccessfully(username, password);
        return new UserDashboardPage(driver);
    }

    public MainPage loginFail(String username, String invalidPassword) {
        loginForm.loginFail(username, invalidPassword);
        UIUtils.waitUntilElementAppears(By.xpath(errorMessageXpath), driver);
        return new MainPage(driver);
    }
}
