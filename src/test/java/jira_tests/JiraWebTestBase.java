package jira_tests;

import jira.base.UserPage;
import jira.pages.LogoutPage;
import jira.pages.MainPage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import utils.JiraConstants;
import utils.WebDriverUtils;

public class JiraWebTestBase {
    public static WebDriver driver;
    public static MainPage mainPage;
    public static Logger log;
    public static String testrailRun = "";


    @BeforeClass
    public void setUp() {
        log = LogManager.getLogger(JiraTests.class);
        driver = WebDriverUtils.getDriver(WebDriverUtils.Browser.CHROME);
        driver.get(JiraConstants.baseURL);
        mainPage = new MainPage(driver);
    }

    @AfterClass
    public void tearDown() {
        if(UserPage.isOpened(driver)) {
            UserPage userPage = new UserPage(driver);
            LogoutPage logoutPage = userPage.logout();
            Assert.assertTrue(logoutPage.isOpened(), "User not logged out successfully.");
        }
        driver.quit();
    }




}
