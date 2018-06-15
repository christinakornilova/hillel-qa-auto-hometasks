package jira_tests;

import jira.*;
import jira.basic.UserPage;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import utils.JiraConstants;
import utils.UIUtils;
import utils.Utils;
import utils.WebDriverUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class JiraTests {
    private static WebDriver driver;
    private static MainPage mainPage;
    private static Logger log;

    private static final String project = "General QA Robert (GQR)" + Keys.ENTER;
    private static final String projectVal = "/browse/GQR";
    private static final String issueType = "Ошибка"  + Keys.ENTER;
    private static final String issueSummary = "QA-Auto-Test-Issue-Summary-" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    private static final String errorMessageText = "Sorry, your username and password are incorrect - please try again.";

    private static final String attachmentFilePath = "/src/test/resources/testlog/test_log.txt";
    private static final String attachmentFileName = "test_log.txt";
    private static final String attachmentSaveToPath = "/Users/Christina/Downloads/";


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


    @BeforeClass
    public void setUp() {
        log = LogManager.getLogger(JiraTests.class);
    }

    @BeforeMethod
    public void prepare() {
        driver = WebDriverUtils.getDriver(WebDriverUtils.Browser.CHROME);
        driver.get(JiraConstants.baseURL);
        mainPage = new MainPage(driver);
    }

    //@Test priority: Lower priorities will be scheduled first.
    @Test(description = "Invalid login", priority = -1)
    public  void testUnsuccessfullLogin() {
        mainPage.loginFail(JiraConstants.wrongLogin, JiraConstants.wrongPassword);
        Assert.assertTrue(mainPage.isErrorMessageDisplayed());
        Assert.assertEquals(errorMessageText, mainPage.getErrorMessageText());
    }

    @Test(description = "Valid login", priority = 0)
    public void testLogin() {
        login(JiraConstants.login, JiraConstants.password);
    }

    @Test(description = "Create issue", priority = 1)
    public void testCreateIssue() {
        //login, check that user logged in successfully
        UserDashboardPage dashboard = login(JiraConstants.login, JiraConstants.password);

        //click create issue button
        CreateIssueDialog createIssueDialog = dashboard.createIssue();
        Assert.assertTrue(createIssueDialog.isOpened(driver), "Create issue dialog is not opened.");

        //fill * fields, create issue
        createIssueDialog.createIssue(project, issueType, issueSummary);
        Assert.assertFalse(createIssueDialog.isOpened(driver), "Create issue dialog is still opened.");
        UIUtils.waitUntilPopupWindowIsClosed(driver);
    }

    @Test(description = "Open issue", priority = 2)
    public void testOpenIssue() {
        //open created issue
        UserDashboardPage dashboard = login(JiraConstants.login, JiraConstants.password);
        IssuesReportedByMePage reportedByMePage = dashboard.openIssuesReportedByCurrentUser();
        Assert.assertEquals(issueSummary, reportedByMePage.getLatestIssueTitle());
        IssuePage lastCreatedIssuePage = reportedByMePage.openLastCreatedIssue();
        Assert.assertEquals(issueSummary, lastCreatedIssuePage.getIssueSummary());
    }

    @Test(description = "Add attachment to issue", priority = 3)
    public void testAddAttachment() {
        //get absolute path to file
        String filePath = System.getProperty("user.dir") + attachmentFilePath;

        //login
        UserDashboardPage dashboard = login(JiraConstants.login, JiraConstants.password);

        //open created issue
        IssuePage issuePage = openLastReportedIssue(dashboard);
        Assert.assertTrue(issuePage.isOpened(driver));

        //add attachment
        issuePage = issuePage.addAttachment(filePath);
        //check that attachment added successfully
        Assert.assertTrue(issuePage.getAttachmentLink().contains(attachmentFileName));
    }

    @Test(description = "Download attachment", priority = 4)
    public void testDownloadAttachment() {
        UserDashboardPage dashboard = login(JiraConstants.login, JiraConstants.password);

        //open created issue
        IssuePage issuePage = openLastReportedIssue(dashboard);
        //check that issue contains attachment
        Assert.assertTrue(issuePage.isAttachmentAdded());
        //get attachment link, check the it is correct
        String attachmentLink = issuePage.getAttachmentLink();
        Assert.assertTrue(attachmentLink.contains(attachmentFileName));

        //try to download attachment
        issuePage.downloadAttachment(attachmentSaveToPath + attachmentFileName);

        //check that file download successfully
        Assert.assertEquals(Utils.getMD5(attachmentSaveToPath + attachmentFileName),
                Utils.getMD5(System.getProperty("user.dir") + attachmentFilePath),
                "MD5 of two files are not equal.");

    }

    @AfterMethod
    public static void tearDown() {
        if(UserPage.isOpened(driver)) {
            UserPage userPage = new UserPage(driver);
            LogoutPage logoutPage = userPage.logout();
            Assert.assertTrue(logoutPage.isOpened(), "User not logged out successfully.");
        }
        driver.quit();
    }
}
