package jira_tests;

import jira.base.UserPage;
import jira.dialogs.CreateIssueDialog;
import jira.dialogs.DeleteUserDialog;
import jira.pages.*;
import jira.pages.login.AdministratorAccessLoginPage;
import jira_tests.listeners.TestListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import utils.JiraConstants;
import utils.Utils;
import utils.WebDriverUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

@Listeners(TestListener.class)


public class JiraTests {

    private static final String project = "General QA Robert (GQR)" + Keys.ENTER;
    private static final String issueType = "Ошибка"  + Keys.ENTER;
    private static final String issueSummary = "QA-Auto-Test-Issue-Summary-" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    private static final String errorMessageText = "Sorry, your username and password are incorrect - please try again.";
    private static final String emptySearchResultText = "No users were found to match your search";

    public static WebDriver driver;
    public static MainPage mainPage;
    public static Logger log;


    private UserDashboardPage login(String login, String password) {
        UserDashboardPage dashboard = mainPage.loginSuccessfully(login, password);
        Assert.assertEquals(dashboard.getUserName(), login);
        return dashboard;
    }

    private IssuePage openLastReportedIssue(UserDashboardPage dashboard) {
        IssuesReportedByMePage reportedByMePage = dashboard.openIssuesReportedByCurrentUser();
        IssuePage lastCreatedIssuePage = reportedByMePage.openLastCreatedIssue();
        return lastCreatedIssuePage;
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        log = LogManager.getLogger(JiraTests.class);
        driver = WebDriverUtils.getDriver(WebDriverUtils.Browser.CHROME);
        driver.get(JiraConstants.baseURL);
        mainPage = new MainPage(driver);
    }

    @Test(description = "30. Invalid login", priority = -1)
    public  void testUnsuccessfulLogin() {
        mainPage.loginFail(JiraConstants.wrongLogin, JiraConstants.wrongPassword);
        Assert.assertTrue(mainPage.isErrorMessageDisplayed());
        Assert.assertEquals(errorMessageText, mainPage.getErrorMessageText());
    }

    @Test(description = "31. Valid login", groups = { "Sanity" })
    public void testLogin() {
        login(JiraConstants.login, JiraConstants.password);
    }

    @Test(description = "32. Create issue", dependsOnMethods = { "testLogin" })
    public void testCreateIssue() {
        //login, check that user logged in successfully
        UserDashboardPage dashboard = login(JiraConstants.login, JiraConstants.password);

        //click create issue button
        CreateIssueDialog createIssueDialog = dashboard.createIssue();
        Assert.assertTrue(createIssueDialog.isOpened(driver), "Create issue dialog is not opened.");

        //fill * fields, create issue
        createIssueDialog.createIssue(project, issueType, issueSummary);
        Assert.assertFalse(createIssueDialog.isOpened(driver), "Create issue dialog is still opened.");
    }

    @Test(description = "33. Open issue", dependsOnMethods = { "testCreateIssue" }, groups = { "Sanity", "Issues" })
    public void testOpenIssue() {
        //open created issue
        UserDashboardPage dashboard = login(JiraConstants.login, JiraConstants.password);

        //switch to reported by me page for current user
        IssuesReportedByMePage reportedByMePage = dashboard.openIssuesReportedByCurrentUser();
        Assert.assertEquals(issueSummary, reportedByMePage.getLatestIssueTitle());

        //select last created issue and open it, verify summary
        IssuePage lastCreatedIssuePage = reportedByMePage.openLastCreatedIssue();
        Assert.assertEquals(issueSummary, lastCreatedIssuePage.getIssueSummary());
    }

    @Test(description = "34. Add attachment", dependsOnMethods = { "testOpenIssue" }, groups = { "Issues.Attachments" })
    public void testAddAttachment() {
        //get absolute path to file
        String filePath = System.getProperty("user.dir") + JiraConstants.attachmentFilePath;

        //login
        UserDashboardPage dashboard = login(JiraConstants.login, JiraConstants.password);

        //open created issue
        IssuePage issuePage = openLastReportedIssue(dashboard);
        Assert.assertTrue(issuePage.isOpened(driver));

        //add attachment
        issuePage = issuePage.addAttachment(filePath);

        //check that attachment added successfully
        Assert.assertTrue(issuePage.getAttachmentLink().contains(JiraConstants.attachmentFileName));
    }

    @Test(description = "35. Download attachment", dependsOnMethods = { "testAddAttachment" }, groups = { "Issues.Attachments" })
    public void testDownloadAttachment() {
        UserDashboardPage dashboard = login(JiraConstants.login, JiraConstants.password);

        //open created issue
        IssuePage issuePage = openLastReportedIssue(dashboard);
        //check that issue contains attachment
        Assert.assertTrue(issuePage.isAttachmentAdded());
        //get attachment link, check the it is correct
        String attachmentLink = issuePage.getAttachmentLink();
        Assert.assertTrue(attachmentLink.contains(JiraConstants.attachmentFileName));

        //try to download attachment
        issuePage.downloadAttachment(JiraConstants.attachmentSaveToPath + JiraConstants.attachmentFileName);

        //check that file download successfully
        Assert.assertEquals(JiraConstants.md5Expected,
                Utils.getMD5(JiraConstants.attachmentSaveToPath + JiraConstants.attachmentFileName),
                "MD5 of two files are not equal.");

    }

    @Test(description = "36. Create user", dependsOnMethods = { "testLogin" }, groups = { "Sanity", "Issues" })
    public void testCreateUser() {
        String jiraAdminPwd = Utils.decodeString(JiraConstants.adminEncPassword);
        //login as admin user
        UserDashboardPage dashboard = login(JiraConstants.adminLogin, jiraAdminPwd);
        //open User management page via menu
        AdministratorAccessLoginPage adminLoginPage = dashboard.openAdminLoginPage();
        UserManagementPage userManagementPage = adminLoginPage.confirmAdminPassword(jiraAdminPwd);
        //create new user
        CreateNewUserPage newUserPage = userManagementPage.createUser();
        userManagementPage = newUserPage.createUser(JiraConstants.newUserEmail, JiraConstants.newUserFullName, JiraConstants.newUserUsername,
                JiraConstants.newUserPassword);
        Assert.assertEquals(JiraConstants.newUserFullName + " has been successfully created", userManagementPage.getUserCreateSuccessPopupText());

        //check that user created: search for new user
        userManagementPage.searchForUser(JiraConstants.newUserEmail);
        Assert.assertEquals(JiraConstants.newUserUsername, userManagementPage.getUsernameById(0));

        //delete newly created user
        DeleteUserDialog deleteUserDialog = userManagementPage.deleteUser(JiraConstants.newUserEmail);
        userManagementPage = deleteUserDialog.confirm();

        //check that user deleted successfully: search result is empty now
        Assert.assertEquals(emptySearchResultText, userManagementPage.getEmptySearchResultText());
    }

    @AfterMethod
    public void tearDown() {
        if(UserPage.isOpened(driver)) {
            UserPage userPage = new UserPage(driver);
            LogoutPage logoutPage = userPage.logout();
            Assert.assertTrue(logoutPage.isOpened(), "User not logged out successfully.");
        }
        driver.quit();
    }
}
