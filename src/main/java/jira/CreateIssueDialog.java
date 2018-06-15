package jira;

import jira.basic.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import utils.UIUtils;

public class CreateIssueDialog extends BasePage {
    private static final String createIssueDialogCss = "div[id='create-issue-dialog']";

    private static final String projectDropdownCss = "input[id='project-field']";
    private static final String issueTypeDropdownCss = "input[id='issuetype-field']";
    private static final String summaryCss = "input[id='summary']";
    private static final String assignToMeLinkCss = "a[id='assign-to-me-trigger']";
//    private static final String descriptionCss = "";
    private static final String submitCreateIssueCss = "input[id='create-issue-submit']";
    private static final String cancelCreateIssueCss = "a[class='cancel']";


    @FindBy(css = createIssueDialogCss)
    WebElement createIssueDialog;

    @FindBy(css = projectDropdownCss)
    WebElement projectDropdown;


    @FindBy(css = issueTypeDropdownCss)
    WebElement issueTypeDropdown;

    @FindBy(css = summaryCss)
    WebElement issueSummaryField;

    @FindBy(css = assignToMeLinkCss)
    WebElement assignToMeLink;

//    @FindBy(css = descriptionCss)
//    WebElement issueDescriptionField;

    @FindBy(css = submitCreateIssueCss)
    WebElement createIssueButton;

    @FindBy(css = cancelCreateIssueCss)
    WebElement cancelIssueButton;


    public CreateIssueDialog(WebDriver driver) {
        super(driver);
        UIUtils.waitUntilElementAppears(issueSummaryField, driver);
    }

    public boolean isOpened(WebDriver driver) {
        return UIUtils.isDialogOpened(driver, createIssueDialogCss);
    }

    public void setProjectType(String project) {
        projectDropdown.click();
        UIUtils.clearAndFill(projectDropdown, project);
    }

    public void setIssueType(String issueType) {
        UIUtils.clearAndFill(issueTypeDropdown, issueType);
    }

    public void setSummary(String summary) {
        UIUtils.clearAndFill(issueSummaryField, summary);
    }

//    public void setDescription(String description) {
//        UIUtils.clearAndFill(issueDescriptionField, description);
//    }

    public void assignIssueToMe() {
        assignToMeLink.click();
    }

    public void submit() {
        createIssueButton.click();
    }

    public UserDashboardPage cancel() {
        cancelIssueButton.click();
        return new UserDashboardPage(driver);
    }

    public UserDashboardPage createIssue(String project, String issueType, String summary) {
        UIUtils.waitUntilElementToBeInteractable(projectDropdown, driver);
        setProjectType(project);
        UIUtils.waitUntilElementToBeInteractable(issueTypeDropdown, driver);
        setIssueType(issueType);
        UIUtils.waitUntilElementToBeInteractable(issueSummaryField, driver);
        setSummary(summary);
        UIUtils.waitUntilElementToBeInteractable(assignToMeLink, driver);
        assignIssueToMe();
        submit();
        UIUtils.waitUntilDialogDisappears(driver, createIssueDialogCss);
        return new UserDashboardPage(driver);
    }



}
