package jira;

import jira.basic.HeaderPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import utils.UIUtils;

public class MainPage extends HeaderPage {

    private static final String mainPageTitleText = "System Dashboard - Hillel IT School JIRA";

    //Login form
    private static final String userLoginCss = "input[id='login-form-username']";
    private static final String userPasswordCss = "input[id='login-form-password']";
    private static final String loginButtonCss = "input[id='login']";

    @FindBy(css = userLoginCss)
    WebElement userNameField;

    @FindBy (css = userPasswordCss)
    WebElement userPasswordField;

    @FindBy (css = loginButtonCss)
    WebElement loginButton;



    public MainPage(WebDriver driver) {
        super(driver);
        UIUtils.checkExpectedTitle(mainPageTitleText, driver);
    }

    public void setUserLogin(String username) {
        UIUtils.clearAndFill(userNameField, username);
    }

    public void setUserPassword(String password) {
        UIUtils.clearAndFill(userPasswordField, password);
    }

    public void clickLoginButton() {
        loginButton.click();
    }

    public UserDashboardPage login(String login, String password) {
        setUserLogin(login);
        setUserPassword(password);
        clickLoginButton();
        return new UserDashboardPage(driver);
    }

    public MainPage invalidLogin(String invalidLogin, String invalidPassword) {
        setUserLogin(invalidLogin);
        setUserPassword(invalidPassword);
        clickLoginButton();
        UIUtils.waitUntilElementAppears(By.xpath(errorMessageXpath), driver);
        return new MainPage(driver);
    }
}
