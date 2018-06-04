package jira;

import jira.basic.UserPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import utils.UIUtils;

public class IssuePage extends UserPage {
    private static final String issueSummaryValCss = "h1[id='summary-val']";

    @FindBy(css = issueSummaryValCss)
    WebElement issueSummary;

    public String getIssueSummary() {
        System.out.println(issueSummary.getText());
        return issueSummary.getText();
    }

    public IssuePage(WebDriver driver) {
        super(driver);
        UIUtils.waitUntilElementAppears(issueSummary, driver);
    }
}
