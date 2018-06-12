package jira_tests;

import jira.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.UIUtils;
import utils.WebDriverUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class JiraSanityTests {
    private static WebDriver driver;
    private static MainPage mainPage;
    private static Logger log;

    private static final String login = "ckornilova";
    private static final String password = "hillel123";
    private static final String wrongLogin = "test";
    private static final String wrongPassword = "wrong_password";
    private static final String baseURL = "http://jira.hillel.it:8080/secure/Dashboard.jspa";
    private static final String project = "General QA Robert (GQR)" + Keys.ENTER;
    private static final String projectVal = "/browse/GQR";
    private static final String issueType = "Ошибка"  + Keys.ENTER;
    private static final String issueSummary = "QA-Auto-Test-Issue-Summary-" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    private static final String errorMessageText = "Sorry, your username and password are incorrect - please try again.";
    private static String issueAttachedFileCss = "//a[contains(.,'test_log.txt')]";
    private static final String attachmentFilePath = "/src/test/resources/testlog/test_log.txt";
    private static final String attachmentFileName = "test_log.txt";


    private UserDashboardPage login(String login, String password) {
        UserDashboardPage dashboard = mainPage.login(login, password);
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
        log = LogManager.getLogger(JiraSanityTests.class);
    }

    @BeforeMethod
    public void prepare() {
        driver = WebDriverUtils.getDriver(WebDriverUtils.Browser.CHROME);
        driver.get(baseURL);
        mainPage = new MainPage(driver);
    }

    @Test(description = "Valid login")
    public void testLogin() {
        UserDashboardPage dashboard = login(login, password);
        LogoutPage logoutPage = dashboard.logout();
        Assert.assertTrue(logoutPage.isOpened(), "User not logged out successfully.");
    }

    @Test(description = "Create and Open created issue")
    public void testCreateAndOpenIssue() throws InterruptedException {
        //login, check that user logged in successfully
        UserDashboardPage dashboard = login(login, password);

        //click create issue button
        CreateIssueDialog createIssueDialog = dashboard.clickCreateButton();
        Assert.assertTrue(createIssueDialog.isOpened(driver), "Create issue dialog is not opened.");

        //fill * fields, create issue
        dashboard = createIssueDialog.createIssue(project, issueType, issueSummary);
        Assert.assertFalse(createIssueDialog.isOpened(driver), "Create issue dialog is still opened.");

        //open created issue
        UIUtils.waitUntilPopupWindowIsClosed(driver);
        IssuesReportedByMePage reportedByMePage = dashboard.openIssuesReportedByCurrentUser();
        Assert.assertEquals(issueSummary, reportedByMePage.getLatestIssueTitle());
        IssuePage lastCreatedIssuePage = reportedByMePage.openLastCreatedIssue();
        Assert.assertEquals(issueSummary, lastCreatedIssuePage.getIssueSummary());

        //logout
        LogoutPage logoutPage = lastCreatedIssuePage.logout();
        Assert.assertTrue(logoutPage.isOpened(), "User not logged out successfully.");
    }

    @Test(description = "Invalid login")
    public  void testUnsuccessfullLogin() {
        mainPage.invalidLogin(wrongLogin, wrongPassword);
        Assert.assertTrue(mainPage.isErrorMessageDisplayed());
        Assert.assertEquals(errorMessageText, mainPage.getErrorMessageText());
    }

    @Test(description = "Add attachment to issue and then Download it")
    public void testAddAndDownloadAttachment() {
        //get absolute path to file
        String filePath = System.getProperty("user.dir") + attachmentFilePath;
        String saveToPath = System.getProperty("user.dir") + "/target";

        //login
        UserDashboardPage dashboard = login(login, password);

        //open created issue
        IssuePage issuePage = openLastReportedIssue(dashboard);
        Assert.assertTrue(issuePage.isOpened(driver));

        //add attachment
        issuePage = issuePage.addAttachment(filePath);
        //check that attachment added successfully
        Assert.assertTrue(issuePage.getAttachmentLink().contains(attachmentFileName));


        //download attachment
        issuePage.downloadAttachment(saveToPath + "/saved_attachment.txt");

//        UIUtils.waitForSeconds(5);

        //check saved file
        //create new browser tab
//        ((JavascriptExecutor)driver).executeScript("window.open()");
//        ArrayList<String> tabs = new ArrayList<> (driver.getWindowHandles());
//        UIUtils.waitForSeconds(15);
//
//        //switch to new tab
//        driver.switchTo().window(tabs.get(1));
//        driver.get(System.getProperty("user.dir") + "/target");
//
//        UIUtils.waitForSeconds(15);
//
//        driver.switchTo().window(tabs.get(0)); // switch back to main screen

        //logout
        issuePage.logout();
    }

    @AfterClass
    public static void tearDown() {
        driver.quit();
    }
}
