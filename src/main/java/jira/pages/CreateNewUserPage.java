package jira.pages;

import jira.base.UserPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import utils.UIUtils;

public class CreateNewUserPage extends UserPage {
    private static final String headerCss = "h2";
    private static final String emailFieldCss = "input#user-create-email";
    private static final String fullNameFieldCss = "input#user-create-fullname";
    private static final String usernameFieldCss = "input#user-create-username";
    private static final String passwordFieldCss = "input#password";
    private static final String createUserButtonCss = "input#user-create-submit";
    private static final String cancelButtonCss = "a#user-create-cancel";

    @FindBy(css = headerCss)
    WebElement header;

    @FindBy(css = emailFieldCss)
    WebElement emailField;

    @FindBy(css = fullNameFieldCss)
    WebElement fullNameField;

    @FindBy(css = usernameFieldCss)
    WebElement usernameField;

    @FindBy(css = passwordFieldCss)
    WebElement passwordField;

    @FindBy(css = createUserButtonCss)
    WebElement createUserButton;

    @FindBy(css = cancelButtonCss)
    WebElement cancelButton;


    public CreateNewUserPage(WebDriver driver) {
        super(driver);
        UIUtils.waitUntilElementAppears(header, driver);
        UIUtils.waitUntilElementToBeInteractable(createUserButton, driver);
    }

    public UserManagementPage createUser(String email, String fullName, String username, String password) {
        UIUtils.clearAndFill(emailField, email);
        UIUtils.clearAndFill(fullNameField, fullName);
        UIUtils.clearAndFill(usernameField, username);
        UIUtils.clearAndFill(passwordField, password);
        createUserButton.click();
        return new UserManagementPage(driver);
    }


}
