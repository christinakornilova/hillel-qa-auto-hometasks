package jira.pages;

import jira.base.UserPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import utils.UIUtils;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;

public class IssuePage extends UserPage {
    private static final String issueSummaryValCss = "h1[id='summary-val']";
    private static final String issueAttachmentsBrowseButtonCss = "button[class='issue-drop-zone__button']";
    private static final String issueAttachmentsInputCss = "input[class='issue-drop-zone__file ignore-inline-attach']";
    private static final String issueAttachmentLinkCss = "div.attachment-thumb > a";
    private static final String issueAttachmentsMenuCss = "a[id='AJS_DROPDOWN__5']";
    private static final String issueAttachmentsMenuDownloadAllItemCss = "li[id='AJS_DROPDOWN_LISTITEM__79']";

    private static final String issueAttachmentSuccessPopupCss = "div[class='aui-message aui-message-success success closeable shadowed aui-will-close']";


    @FindBy(css = issueSummaryValCss)
    WebElement issueSummary;

    @FindBy(css = issueAttachmentsBrowseButtonCss)
    WebElement attachmentBrowseButton;

    @FindBy(css = issueAttachmentsInputCss)
    WebElement issueAttachamentInput;

    @FindBy(css = issueAttachmentSuccessPopupCss)
    WebElement issueAttachmentSuccessPopup;

    @FindBy(css = issueAttachmentLinkCss)
    WebElement issueAttachmentLink;

    @FindBy(css = issueAttachmentsMenuCss)
    WebElement issueAttachmentsMenu;

    @FindBy(css = issueAttachmentsMenuDownloadAllItemCss)
    WebElement issueAttachmentsMenuDownloadAllItem;


    public String getIssueSummary() {
        System.out.println(issueSummary.getText());
        return issueSummary.getText();
    }

    public IssuePage(WebDriver driver) {
        super(driver);
        UIUtils.waitUntilElementAppears(issueSummary, driver);
    }

    public IssuePage addAttachment(String path) {
        issueAttachamentInput.sendKeys(path);
        UIUtils.waitUntilElementAppears(issueAttachmentSuccessPopup, driver);
        UIUtils.waitUntilPopupWindowIsClosed(driver);
        return new IssuePage(driver);
    }

    public void downloadAttachment(String saveToPath) {
        UIUtils.isElementPresent(By.cssSelector(issueAttachmentLinkCss), driver);

        StringSelection stringSelection = new StringSelection(saveToPath);
        Toolkit.getDefaultToolkit().getSystemClipboard()
                .setContents(stringSelection, null);

        try{
            Actions action = new Actions(driver);
            Robot robot = new Robot();

            //call context menu and select 'Save link as'
            action.contextClick(issueAttachmentLink).build().perform();
            for(int i = 0; i < 3; i++) {
                robot.keyPress(KeyEvent.VK_DOWN);
                robot.keyRelease(KeyEvent.VK_DOWN);
            }
//            robot.keyPress(KeyEvent.VK_DOWN);
//            robot.keyRelease(KeyEvent.VK_DOWN);
//            robot.keyPress(KeyEvent.VK_DOWN);
//            robot.keyRelease(KeyEvent.VK_DOWN);
            UIUtils.waitForSeconds(3);

            //press Tab in 'Save As' dialog
            robot.keyPress(KeyEvent.VK_TAB);
            robot.keyRelease(KeyEvent.VK_TAB);
            UIUtils.waitForSeconds(2);

            //press Enter in 'Save As' dialog
            for(int i = 0; i < 2; i++) {
                robot.keyPress(KeyEvent.VK_ENTER);
                robot.keyRelease(KeyEvent.VK_ENTER);
                UIUtils.waitForSeconds(3);
            }
//            robot.keyPress(KeyEvent.VK_ENTER);
//            robot.keyRelease(KeyEvent.VK_ENTER);
//            UIUtils.waitForSeconds(3);


        } catch(Exception e) {
            log.error("Error saving attachment ", e);
        }
    }

    public void download(String attachmentLink) {
        driver.get(attachmentLink);
    }

    public String getAttachmentLink() {
        return issueAttachmentLink.getAttribute("href");
    }

    public boolean isAttachmentAdded() {
        return UIUtils.isElementPresent(By.cssSelector(issueAttachmentLinkCss), driver);
    }

    public static boolean isOpened(WebDriver driver) {
        return UIUtils.isDialogOpened(driver, issueSummaryValCss);
    }
}
