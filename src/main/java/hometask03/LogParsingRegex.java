package hometask03;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogParsingRegex {

//    final static String timestampRgx = "(?<timestamp>^([A-Z][a-z][a-z])\\s*?([0-9]+)\\s*?([0-9][0-9]:[0-9][0-9]:[0-9][0-9]))";
//    final static String activityPartRgx = "(?<activity>(Activity:)+)";
//    final static String loginUsernameRgx = "(?<loginUsername>(\\[Login Username:\\.*)([^\\[])+])";
//    final static String dataObjectRgx = "(?<dataObject>(\\[Data Object:\\.*)([^\\[])+)";
//    final static String recordsRgx = "(?<records>(\\[Records:\\.*)([^\\[])+)";
//    final static String labelsRgx = "(?<labels>(\\[Labels:\\.*)([^\\[])+)";
//    final static String userActionRgx = "(?<userAction>(\\[User Action:\\.*)([^\\[])+)";
//    final static String userActionStatusRgx = "(?<userActionStatus>(\\[User Action Status:\\.*)([^\\[]))";
//    final static String serviceTypeRgx = "(?<service>(\\[Service type:\\.*)([^\\[])+)";
//    final static String mappingIdsRgx = "(?<mappingIds>(\\[Mapping Ids:\\.*)([^\\[])+)";
//    final static String uriRgx = "(?<uri>(\\[URI:\\.*).*)";

//    private static Pattern patternFullLog = Pattern.compile(timestampRgx + "\\s+?|" + activityPartRgx + "\\s+?|"
//            + loginUsernameRgx + "\\s+?|" + dataObjectRgx + "\\s+?|"+ recordsRgx+ "\\s+?|" + userActionRgx + "\\s+?|"
//            + userActionStatusRgx + "\\s+?|" + labelsRgx + "\\s+?|" + serviceTypeRgx + "\\s+?|" + mappingIdsRgx + "\\s+?|" + uriRgx);
//    Matcher matcher = patternFullLog.matcher(s);

    private static Logger log = LogManager.getLogger(LogParsingRegex.class);

    private final static String fullRgx = "^([A-Z][a-z][a-z])\\s*?([0-9]+)\\s*?([0-9][0-9]:[0-9][0-9]:[0-9][0-9])\\s+?|(Activity:)+\\s+?|(\\[Login Username:\\.*)([^\\[])+\\s+?|(\\[Data Object:\\.*)([^\\[])+\\s+?|(\\[Records:\\.*)([^\\[])+\\s+?|(\\[Labels:\\.*)([^\\[])+\\s+?|(\\[User Action:\\.*)([^\\[])+\\s+?|(\\[User Action Status:\\.*)([^\\[])+\\s+?|(\\[Service type:\\.*)([^\\[])+\\s+?|(\\[Mapping Ids:\\.*)([^\\[])+\\s+?|(\\[URI:\\.*).*";
    private final static Pattern fullPattern = Pattern.compile(fullRgx);


    //match 1 string
    public static String matchStringToLogParametersStructure(String s) {
        Matcher matcherFullPattern = fullPattern.matcher(s);
        String str = "";
        while(matcherFullPattern.find()){
            str = str + matcherFullPattern.group() + " ";
        }
        return str;
    }

    public static List<String> filterListWithRgx(List<String> logfile) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < logfile.size(); i++) {
            list.add(matchStringToLogParametersStructure(logfile.get(i)));
        }
        return list;
    }

    public static void main(String[] args) {
        try {
            //set destination
            File destination = new File("target/regex_log.txt");

            //write file content to list
            List<String> input = Utils.writeAllFilesContentToArrayList("src/test/resources/regex/");

            //filter list using regexp
            List<String> logParsed = filterListWithRgx(input);

            //create output file
            Utils.writeLogParametersToFile(logParsed, destination);

    } catch (Exception e) {
            log.error("Unable to parse and/or filter log file ", e);
        }
    }
}
