package jira.dialogs;

import jira.base.BasePage;
import jira.pages.UserManagementPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import utils.UIUtils;

public class DeleteUserDialog extends BasePage {

    private static final String deleteUserDialogCss = "div[id*=deleteuser_link_]";
    private static final String deleteUserButtonCss = "input#delete_user_confirm-submit";

    @FindBy(css = deleteUserButtonCss)
    WebElement deleteUserButton;

    @FindBy(css = deleteUserDialogCss)
    WebElement dialog;


    public DeleteUserDialog(WebDriver driver) {
        super(driver);
        UIUtils.waitUntilDialogAppears(driver, dialog);
    }

    public UserManagementPage confirm() {
        deleteUserButton.click();
        return new UserManagementPage(driver);
    }
}
