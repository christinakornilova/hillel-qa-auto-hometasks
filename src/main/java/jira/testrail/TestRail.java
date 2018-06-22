package jira.testrail;

import utils.JiraConstants;

import java.util.HashMap;
import java.util.Map;


public class TestRail {

    private Map testResultData = new HashMap();
    private APIClient client;

    public static final int TEST_CASE_PASSED_STATUS = 1;
    public static final int TEST_CASE_FAILED_STATUS = 5;


    public TestRail() {
        testResultData.put("assignedto_id", "2");

        client = new APIClient(JiraConstants.testrailUrl);
        client.setUser(JiraConstants.testrailUsername);
        client.setPassword(JiraConstants.testrailPassword);

    }

    public void setCaseResult(String[] caseIds, String run, boolean isResultFail) throws Exception {

        if (isResultFail) {
            testResultData.put("status_id", TEST_CASE_PASSED_STATUS);
        } else
            testResultData.put("status_id", TEST_CASE_FAILED_STATUS);


        for (int i = 0; i < caseIds.length; i++) {
            testResultData.put("comment", "Test Executed - Status updated automatically.");
            client.sendPost("add_result_for_case/" + run + "/" + caseIds[i], testResultData);
        }
    }

}