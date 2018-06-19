package jira.pages.login;

import jira.LoginFormComponent;
import jira.base.UserPage;
import jira.pages.UserManagementPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import utils.UIUtils;

public class AdministratorAccessLoginPage extends UserPage {
    private static final String headerCss = "section > header > h1";

    private LoginFormComponent loginForm = new LoginFormComponent(driver);

    @FindBy(css = headerCss)
    WebElement header;

    public AdministratorAccessLoginPage(WebDriver driver) {
        super(driver);
        UIUtils.waitUntilElementAppears(header, driver);
    }

    public UserManagementPage confirmAdminPassword(String password) {
        loginForm.setUserPassword(password);
        loginForm.clickLoginButton();
        return new UserManagementPage(driver);
    }
}
