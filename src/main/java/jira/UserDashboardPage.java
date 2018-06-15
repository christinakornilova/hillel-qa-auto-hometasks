package jira;

import jira.basic.UserPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import utils.UIUtils;


public class UserDashboardPage extends UserPage {

    private static final String dashboardDropdownCss = "a[href='/secure/Dashboard.jspa']";

    private static final String issuesDropdownCss = "a[id='find_link']";
    private static final String issuesReportedByMeItemCss = "a[id='filter_lnk_reported_lnk']";

    private static final String createButtonCss = "a[id='create_link']";

    private static final String activityStreamCss = "div[class='dashboard-item-header']";



    @FindBy(css = dashboardDropdownCss)
    WebElement dashboardDropdown;

    @FindBy(css = issuesDropdownCss)
    WebElement issuesDropdown;

    @FindBy (css = issuesReportedByMeItemCss)
    WebElement issuesReportedByMe;

    @FindBy(css = createButtonCss)
    WebElement createButton;


    @FindBy(css = activityStreamCss)
    WebElement activityStream;

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
        UIUtils.waitUntilElementAppears(issuesDropdown, driver);
        issuesDropdown.click();
        UIUtils.waitUntilElementAppears(issuesReportedByMe, driver);
        issuesReportedByMe.click();
        return new IssuesReportedByMePage(driver);
    }


}
