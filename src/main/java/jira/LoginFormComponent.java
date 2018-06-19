package jira;

import jira.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import utils.UIUtils;

public class LoginFormComponent extends BasePage{
    //Login form
    private static final String userLoginCss = "input#login-form-username";
    private static final String userPasswordCss = "input#login-form-password";
    private static final String adminPasswordFieldCss = "input#login-form-authenticatePassword";
    private static final String loginButtonCss = "input#login";
    private static final String adminConfirmLoginButtonCss = "input#login-form-submit";
    private static final String errorMessageCss = "div#usernameerror";

    @FindBy(css = userLoginCss)
    WebElement userNameField;

   @FindAll({
           @FindBy(css = userPasswordCss),
           @FindBy(css = adminPasswordFieldCss)
   })
   WebElement userPasswordField;


    @FindAll({
            @FindBy(css = loginButtonCss),
            @FindBy(css = adminConfirmLoginButtonCss)
    })
    WebElement loginButton;


    public LoginFormComponent(WebDriver driver) {
        super(driver);
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

    private void login(String login, String password) {
        setUserLogin(login);
        setUserPassword(password);
        loginButton.click();
    }

    public BasePage loginSuccessfully(String username, String password) {
        login(username, password);
        return new BasePage(driver);
    }

    public BasePage loginFail(String username, String invalidPassword) {
        login(username, invalidPassword);
        UIUtils.waitUntilElementAppears(By.cssSelector(errorMessageCss), driver);
        return new BasePage(driver);
    }
}
