package jira_tests;

import jira.dialogs.CreateIssueDialog;
import jira.dialogs.DeleteUserDialog;
import jira.testrail.TestRail;
import jira.pages.*;
import jira.pages.login.AdministratorAccessLoginPage;
import org.openqa.selenium.Keys;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;
import utils.JiraConstants;
import utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;


public class JiraTests extends JiraWebTestBase {
//    private static WebDriver driver;
//    private static MainPage mainPage;
//    private static Logger log;

    private static final String project = "General QA Robert (GQR)" + Keys.ENTER;
    private static final String projectVal = "/browse/GQR";
    private static final String issueType = "Ошибка"  + Keys.ENTER;
    private static final String issueSummary = "QA-Auto-Test-Issue-Summary-" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    private static final String errorMessageText = "Sorry, your username and password are incorrect - please try again.";
    private static final String emptySearchResultText = "No users were found to match your search";

    private static String newIssueUrl = "";



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
    @BeforeClass(alwaysRun = true)
    public void initPages() {
        UserDashboardPage dashboard;
        System.out.println("Jira Pages Initialized");
    }

    @BeforeMethod
    public void prepare() {
//        driver = WebDriverUtils.getDriver(WebDriverUtils.Browser.CHROME);
//        driver.get(JiraConstants.baseURL);
//        mainPage = new MainPage(driver);
    }

    @Test(description = "1. Invalid login", priority = -1)
    public  void testUnsuccessfulLogin() {
        mainPage.loginFail(JiraConstants.wrongLogin, JiraConstants.wrongPassword);
        Assert.assertTrue(mainPage.isErrorMessageDisplayed());
        Assert.assertEquals(errorMessageText, mainPage.getErrorMessageText());
    }

    @Test(description = "2. Valid login")
    public void testLogin() {
        login(JiraConstants.login, JiraConstants.password);
    }

    @Test(description = "3. Create issue", dependsOnMethods = { "testLogin" })
    public void testCreateIssue() {
        //login, check that user logged in successfully
        UserDashboardPage dashboard = login(JiraConstants.login, JiraConstants.password);

        //click create issue button
        CreateIssueDialog createIssueDialog = dashboard.createIssue();
        Assert.assertTrue(createIssueDialog.isOpened(driver), "Create issue dialog is not opened.");

        //fill * fields, create issue
        createIssueDialog.createIssue(project, issueType, issueSummary);
        Assert.assertFalse(createIssueDialog.isOpened(driver), "Create issue dialog is still opened.");
//        newIssueUrl = dashboard.getNewIssueLink();
//        UIUtils.waitUntilPopupWindowIsClosed(driver);
//        Assert.assertTrue(dashboard.isOpened(driver), "User dashboard was not displayed after user created an issue.");
    }

    @Test(description = "4. Open issue", dependsOnMethods = { "testCreateIssue" })
    public void testOpenIssue() {
        //open created issue
        UserDashboardPage dashboard = login(JiraConstants.login, JiraConstants.password);
        IssuesReportedByMePage reportedByMePage = dashboard.openIssuesReportedByCurrentUser();
        Assert.assertEquals(issueSummary, reportedByMePage.getLatestIssueTitle());
//        IssuePage lastCreatedIssuePage = dashboard.openIssue(newIssueUrl);
        IssuePage lastCreatedIssuePage = reportedByMePage.openLastCreatedIssue();
        Assert.assertEquals(issueSummary, lastCreatedIssuePage.getIssueSummary());
    }

    @Test(description = "5. Add attachment to issue", dependsOnMethods = { "testOpenIssue" })
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

    @Test(description = "6. Download attachment", dependsOnMethods = { "testAddAttachment" })
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

    @Test(description = "7. Admin. Create user", dependsOnMethods = { "testLogin" })
    public void testCreateUser() {
        //login as admin user
        UserDashboardPage dashboard = login(JiraConstants.adminLogin, JiraConstants.adminPassword);
        //open User management page via menu
        AdministratorAccessLoginPage adminLoginPage = dashboard.openAdminLoginPage();
        UserManagementPage userManagementPage = adminLoginPage.confirmAdminPassword(JiraConstants.adminPassword);
        //create new user
        CreateNewUserPage newUserPage = userManagementPage.createUser();
        userManagementPage = newUserPage.createUser(JiraConstants.newUserEmail, JiraConstants.newUserFullName, JiraConstants.newUserUsername,
                JiraConstants.newUserPassword);
        Assert.assertEquals(JiraConstants.newUserFullName + " has been successfully created", userManagementPage.getUserCreateSuccessPopupText());

        //check that user created: search for new user
        userManagementPage.searchForUser(JiraConstants.newUserFullName);
        Assert.assertEquals(JiraConstants.newUserUsername, userManagementPage.getUsernameById(0));

        //delete newly created user
        DeleteUserDialog deleteUserDialog = userManagementPage.deleteUser(JiraConstants.newUserUsername);
        userManagementPage = deleteUserDialog.confirm();

        //check that user deleted successfully: search result is empty now
        Assert.assertEquals(emptySearchResultText, userManagementPage.getEmptySearchResultText());
    }


    @AfterMethod
    public static void tearDown(ITestResult result) throws Exception {
        String[] caseIds=result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(Test.class).testName().split(",");
        TestRail apiCall=new TestRail();
        if (ITestResult.FAILURE == result.getStatus()) {
            String resultName = result.getName();
            apiCall.setCaseResult(caseIds, testrailRun,true);

        }
        else
            apiCall.setCaseResult(caseIds, testrailRun,false);


//        if(UserPage.isOpened(driver)) {
//            UserPage userPage = new UserPage(driver);
//            LogoutPage logoutPage = userPage.logout();
//            Assert.assertTrue(logoutPage.isOpened(), "User not logged out successfully.");
//        }
//        driver.quit();

    }
}
