package utils;

public class JiraConstants {
    public static final String baseURL = "http://jira.hillel.it:8080/secure/Dashboard.jspa";
    public static final String login = "ckornilova";
    public static final String password = "hillel123";
    public static final String wrongLogin = "invalidlogin";
    public static final String wrongPassword = "wrong_password";
    public static final String attachmentSaveToPath = "/Users/Christina/Downloads/";
    public static final String attachmentFileName = "test_log.txt";
    public static final String attachmentFilePath = "/src/test/resources/testlog/test_log.txt";
    public static final byte[] md5Expected = Utils.getMD5(System.getProperty("user.dir") + attachmentFilePath);

    //Jira admin creds
    public static final String adminLogin = "autorob";
    public static final String adminEncPassword = "Zm9yYXV0b3Rlc3Rz";

    //Jira new user
    public static final String newUserEmail = "testjuser18@gmail.com";
    public static final String newUserFullName = "AUTOTEST JiraUser";
    public static final String newUserUsername = "testjuser18";
    public static final String newUserPassword = "123hillel";

    //testrail
    public static final String testrailUsername = "rvalek@intersog.com";
    public static final String testrailEncPassword = "aGlsbGVsl";
    public static final String testrailUrl = "https://hillelmanold.testrail.io/";
}
