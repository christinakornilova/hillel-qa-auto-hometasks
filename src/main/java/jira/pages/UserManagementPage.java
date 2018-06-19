package jira.pages;

import jira.base.UserPage;
import jira.dialogs.DeleteUserDialog;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import utils.JiraConstants;
import utils.UIUtils;
import utils.Utils;

import java.util.List;

public class UserManagementPage extends UserPage {
    private static final String createUserButtonCss = "a#create_user";
    private static final String searchUserFieldCss = "input#user-filter-userSearchFilter";
    private static final String emptySearchResultTextCss = "div.jira-adbox.jira-adbox-medium.no-results.user-browser__row-empty > h3";

    private static final String userDataRowCss = "tr.vcard.user-row";

    private static final String moreActionsDropdownCss = "a[href*=user-actions-"  + JiraConstants.newUserUsername + "]";
    private static final String deleteUserItemCss = "a#deleteuser_link_" + JiraConstants.newUserUsername;

    private static final String userCreateSuccessPopupCss = "span.user-created-flag-single";

    @FindBy(css = createUserButtonCss)
    WebElement createUserButton;

    @FindBy(css = searchUserFieldCss)
    WebElement searchUserField;

    @FindBy(css = userDataRowCss)
    List<WebElement> userDataRow;

    @FindBy(css = emptySearchResultTextCss)
    WebElement emptySearchResultText;

    @FindBy(css = userCreateSuccessPopupCss)
    WebElement userCreateSuccessPopup;

    @FindBy(css = moreActionsDropdownCss)
    WebElement moreActionsDropdown;

    @FindBy(css = deleteUserItemCss)
    WebElement deleteUserItem;

    public UserManagementPage(WebDriver driver) {
        super(driver);
        UIUtils.waitUntilElementToBeInteractable(createUserButton, driver);
    }

    public CreateNewUserPage createUser() {
        createUserButton.click();
        return new CreateNewUserPage(driver);
    }

    public void searchForUser(String username) {
        UIUtils.clearAndFill(searchUserField, username + Keys.ENTER);
    }

    public String getUsernameById(int id) {
        return userDataRow.get(id).getAttribute("data-user");
    }

    public DeleteUserDialog deleteUser(String username) {
        searchForUser(username);
        Utils.openPageUsingMenuDropdown(moreActionsDropdown, deleteUserItem, driver);
        return new DeleteUserDialog(driver);
    }

    public String getUserCreateSuccessPopupText() {
        UIUtils.waitUntilElementAppears(userCreateSuccessPopup, driver);
        return userCreateSuccessPopup.getText().replaceAll("\\n ", "");
    }

    public String getEmptySearchResultText() {
        return emptySearchResultText.getText();
    }
}
