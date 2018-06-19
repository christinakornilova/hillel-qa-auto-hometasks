package utils;

public class JiraConstants {
    public static final String baseURL = "http://jira.hillel.it:8080/secure/Dashboard.jspa";
    public static final String login = "ckornilova";
    public static final String password = "hillel123";
    public static final String adminLogin = "autorob";
    public static final String adminPassword = "forautotests";
    public static final String wrongLogin = "test";
    public static final String wrongPassword = "wrong_password";
    public static final String attachmentSaveToPath = "/Users/Christina/Downloads/";
    public static final String attachmentFileName = "test_log.txt";
    public static final String attachmentFilePath = "/src/test/resources/testlog/test_log.txt";
    public static final byte[] md5Expected = Utils.getMD5(System.getProperty("user.dir") + attachmentFilePath);

    public static final String newUserEmail = "testjuser18@gmail.com";
    public static final String newUserFullName = "AUTOTEST JiraUser";
    public static final String newUserUsername = "testjuser18";
    public static final String newUserPassword = "123hillel";
}
