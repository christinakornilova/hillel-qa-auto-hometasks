package jira_tests.listeners;

import jira.testrail.reporting.TestRail;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.JiraConstants;
import utils.Utils;

import java.util.HashSet;

public class TestListener implements ITestListener {
    public Logger log = LogManager.getLogger(TestListener.class);

    public void onStart(ITestContext context) {
    }

    @Override
    public void onFinish(ITestContext context) {

        HashSet<ITestResult> allResults = new HashSet<>();
        allResults.addAll(context.getSkippedTests().getAllResults());
        allResults.addAll(context.getFailedTests().getAllResults());
        allResults.addAll(context.getPassedTests().getAllResults());

        reportToTestRail(allResults);
    }

    private void reportToTestRail(HashSet<ITestResult> results) {
        String baseURL = JiraConstants.testrailUrl;
        String projectId = "6";
        String runPrefix = "Jira";
        String username = JiraConstants.testrailUsername;
        String password = Utils.decodeString(JiraConstants.testrailEncPassword);

        if (baseURL.isEmpty() || projectId.isEmpty()) {
            log.info("TestRail reporting is not configured.");
            return;
        }

        log.info("Reporting to " + baseURL);

        TestRail trReport = new TestRail(baseURL);
        trReport.setCreds(username, password);

        try {
            trReport.startRun(Integer.parseInt(projectId), runPrefix + " Christina Auto - " + Utils.getTimestamp());

            for (ITestResult result : results) {
                String testDescription = result.getMethod().getDescription();
                try {
                    int caseId = Integer.parseInt(testDescription.substring(0, testDescription.indexOf(".")));
                    trReport.setResult(caseId, result.getStatus());
                } catch (IndexOutOfBoundsException | NumberFormatException e) {
                    log.info(testDescription + " - Case ID missing; not reporting to TestRail.", e);
                }
            }

            trReport.endRun();
            log.info("Sent reports successfully.");
        } catch (Exception e) {
            log.info("Failed to send report to TestRail.", e);
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
        result.getTestContext().getSkippedTests().removeResult(result.getMethod());
    }

    public void onTestSuccess(ITestResult result) {
    }

    public void onTestFailure(ITestResult result) {
    }

    public void onTestSkipped(ITestResult result) {
    }

    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
    }

}
