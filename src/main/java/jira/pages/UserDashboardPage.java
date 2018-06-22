package jira.pages;

import jira.dialogs.CreateIssueDialog;
import jira.base.UserPage;
import jira.pages.login.AdministratorAccessLoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import utils.UIUtils;
import utils.Utils;

import java.util.List;


public class UserDashboardPage extends UserPage {

    private static final String dashboardDropdownCss = "a[href='/secure/Dashboard.jspa']";

    private static final String issuesDropdownCss = "a[id='find_link']";
    private static final String issuesReportedByMeItemCss = "a[id='filter_lnk_reported_lnk']";

    private static final String createButtonCss = "a[id='create_link']";
    private static final String newIssuesLinkCss = "a.issue-created-key";

    private static final String activityStreamCss = "div[class='dashboard-item-header']";

    private static final String jiraAdministrationDropdownCss = "a[id='admin_menu']";
    private static final String userManagementItemCss = "a[id='admin_users_menu']";


    @FindBy(css = newIssuesLinkCss)
    List<WebElement> newIssuesLink;

    @FindBy(css = dashboardDropdownCss)
    WebElement dashboardDropdown;

    @FindBy(css = issuesDropdownCss)
    WebElement issuesDropdown;

    @FindBy(css = issuesReportedByMeItemCss)
    WebElement issuesReportedByMe;

    @FindBy(css = createButtonCss)
    WebElement createButton;

    @FindBy(css = activityStreamCss)
    WebElement activityStream;

    @FindBy(css = jiraAdministrationDropdownCss)
    WebElement administrationDropdown;

    @FindBy(css = userManagementItemCss)
    WebElement userManagement;

    public UserDashboardPage(WebDriver driver) {
        super(driver);
        UIUtils.waitUntilElementAppears(By.cssSelector(dashboardDropdownCss), driver);
        UIUtils.waitUntilElementAppears(userFullname, driver);
        UIUtils.waitUntilElementAppears(By.cssSelector(activityStreamCss), driver);
    }

    public static boolean isOpened(WebDriver driver) {
        return UIUtils.isElementPresent(By.cssSelector(activityStreamCss), driver);
    }

    public String getUserName() {
        return userFullname.getAttribute("data-username");
    }

    public CreateIssueDialog createIssue() {
        createButton.click();
        return new CreateIssueDialog(driver);
    }

    public IssuesReportedByMePage openIssuesReportedByCurrentUser() {
        Utils.openPageUsingMenuDropdown(issuesDropdown, issuesReportedByMe, driver);
        return new IssuesReportedByMePage(driver);
    }

    public AdministratorAccessLoginPage openAdminLoginPage() {
        Utils.openPageUsingMenuDropdown(administrationDropdown, userManagement, driver);
        return new AdministratorAccessLoginPage(driver);
    }

    public String getNewIssueLink() {
        return newIssuesLink.get(0).getAttribute("href");
    }

    public IssuePage openIssue(String newIssueUrl) {
        driver.get(newIssueUrl);
        return new IssuePage(driver);
    }


}
