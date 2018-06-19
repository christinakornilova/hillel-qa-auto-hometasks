package jira.pages;

import jira.base.UserPage;
import jira.pages.IssuePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import utils.UIUtils;

import java.util.List;

public class IssuesReportedByMePage extends UserPage {

//    private static final String headerCss = "h1[header='Reported by me']";

    private static final String saveAsButtonCss = "button[class='aui-button aui-button-light save-as-new-filter']";

    private static final String searchResultsListCss = "div[class='search-results']";
    private static final String issuesListXpath = "//ol[@class='issue-list']/li";
    private static final String lastCreatedIssuePageLinkCss = "a[id='key-val']";
    private static final String issueSuccessPopupCss = "//div[class='aui-message aui-message-success success closeable shadowed aui-will-close']";

//    @FindBy(css = headerCss)
//    WebElement header;

    @FindBy(css = saveAsButtonCss)
    WebElement saveAsButton;

    @FindBy(css = searchResultsListCss)
    WebElement searchResultsList;

    @FindBy(xpath = issuesListXpath)
    List<WebElement> issuesList;

    @FindBy(css = lastCreatedIssuePageLinkCss)
    WebElement lastCreatedIssuePath;

    @FindBy(css = issueSuccessPopupCss)
    WebElement issueSuccessPopup;


    public IssuesReportedByMePage(WebDriver driver) {
        super(driver);
        UIUtils.waitUntilElementAppears(saveAsButton, driver);
        UIUtils.waitUntilElementAppears(searchResultsList, driver);
    }

    public String getLatestIssueTitle() {
        return issuesList.get(0).getAttribute("title");
    }

    public IssuePage openLastCreatedIssue() {
        UIUtils.waitUntilElementAppears(lastCreatedIssuePath, driver);
        lastCreatedIssuePath.click();
        return new IssuePage(driver);
    }

}
