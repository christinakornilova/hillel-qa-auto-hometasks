package jira_tests;

import jira.*;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.UIUtils;
import utils.WebDriverUtils;

import java.text.SimpleDateFormat;
import java.util.Date;


public class JiraSanityTests {
    private static WebDriver driver;
    private static MainPage mainPage;

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

    @BeforeMethod
    public static void prepare() {
        driver = WebDriverUtils.getDriver(WebDriverUtils.Browser.CHROME);
        driver.get(baseURL);
        mainPage = new MainPage(driver);
    }

    @Test(description = "Valid login")
    public static void testLogin() {
        UserDashboardPage dashboard = mainPage.login(login, password);
        Assert.assertEquals(dashboard.getUserName(), login);
        LogoutPage logoutPage = dashboard.logout();
        Assert.assertTrue(logoutPage.isOpened(), "User not logged out successfully.");
    }

    @Test(description = "Create and Open created issue")
    public static void testCreateAndOpenIssue() throws InterruptedException {
        //login, check that user logged in successfully
        UserDashboardPage dashboard = mainPage.login(login, password);
        Assert.assertEquals(dashboard.getUserName(), login);

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
        LogoutPage logoutPage = reportedByMePage.logout();
        Assert.assertTrue(logoutPage.isOpened(), "User not logged out successfully.");
    }

    @Test(description = "Invalid login")
    public static void testUnsuccessfullLogin() {
        mainPage.invalidLogin(wrongLogin, wrongPassword);
        Assert.assertTrue(mainPage.isErrorMessageDisplayed());
        Assert.assertEquals(errorMessageText, mainPage.getErrorMessageText());
    }


    @AfterClass
    public static void tearDown() {
        driver.quit();
    }
}
