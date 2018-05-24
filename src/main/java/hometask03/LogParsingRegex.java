package hometask03;

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


    private final static String fullRgx = "^([A-Z][a-z][a-z])\\s*?([0-9]+)\\s*?([0-9][0-9]:[0-9][0-9]:[0-9][0-9])\\s+?|(Activity:)+\\s+?|(\\[Login Username:\\.*)([^\\[])+\\s+?|(\\[Data Object:\\.*)([^\\[])+\\s+?|(\\[Records:\\.*)([^\\[])+\\s+?|(\\[Labels:\\.*)([^\\[])+\\s+?|(\\[User Action:\\.*)([^\\[])+\\s+?|(\\[User Action Status:\\.*)([^\\[])+\\s+?|(\\[Service type:\\.*)([^\\[])+\\s+?|(\\[Mapping Ids:\\.*)([^\\[])+\\s+?|(\\[URI:\\.*).*";
    private final static Pattern fullPattern = Pattern.compile(fullRgx);


    //method to match 1 string
    public static String matchStringToLogParametersStructure(String s) {
        LogParameters lp = new LogParameters();

        Matcher matcherFullPattern = fullPattern.matcher(s);
        String str = "";
        while(matcherFullPattern.find()){
            str = str + matcherFullPattern.group() + " ";
        }
//        System.out.println(matcher.groupCount());
//        while (matcher.find()) {
//            lp.setTimestamp(matcher.group("timestamp"));
//            lp.setActivity(matcher.group("activity"));
//            lp.setLoginUsername(matcher.group("loginUsername"));
//            lp.setDataObject(matcher.group("dataObject"));
//            lp.setRecords(matcher.group("records"));
//            lp.setLabels(matcher.group("labels"));
//            lp.setUserAction(matcher.group("userAction"));
//            lp.setUserActionStatus(matcher.group("userActionStatus"));
//            lp.setServiceType(matcher.group("service"));
//            lp.setMappingIds(matcher.group("mappingIds"));
//            lp.setUri(matcher.group("uri"));
//        }
//        System.out.println(lp.getTimestamp().toString() + lp.getActivity().toString() + lp.getUri().toString()  + lp.getLoginUsername().toString()
//                + lp.getDataObject().toString() + lp.getRecords().toString() + lp.getLabels().toString() + lp.getUserAction().toString()
//                + lp.getUserActionStatus().toString() + lp.getServiceType().toString() + lp.getMappingIds().toString());
//        return lp;
        return str;
    }

    //parse file line by line and create list of parsed lines
//    public static List<LogParameters> parseLogFile(List<String> logfile) {
//        LogParameters logParameters;
//        List<LogParameters> list = new ArrayList<>();
//        for (int i = 0; i < logfile.size(); i++) {
//            logParameters = matchStringToLogParametersStructure(logfile.get(i));
//            list.add(logParameters);
//        }
//        return list;
//    }

    public static List<String> parseLogFile(List<String> logfile) {
//        LogParameters logParameters;
        List<String> list = new ArrayList<>();
        for (int i = 0; i < logfile.size(); i++) {
            list.add(matchStringToLogParametersStructure(logfile.get(i)));
//            logParameters = matchStringToLogParametersStructure(logfile.get(i));
//            list.add(logParameters);
        }
        return list;
    }



//    public static List<LogParameters> parseLogFileIntoLogParametersList(List<String> logfile) {
//        Matcher matcher;
//        LogParameters ld = new LogParameters();
//        List<LogParameters> logParsed = new ArrayList<>();
//        for (int i = 0; i < logfile.size(); i++) {
//            matcher = patternFullLog.matcher(logfile.get(i));
//            while (matcher.find()) {
////                System.out.println(matcher.group());
//                ld.setTimestamp(matcher.group("timestamp"));
//                System.out.println(ld.getTimestamp());
//                ld.setActivity(matcher.group("activity"));
////                System.out.println(ld.getActivity());
//                ld.setLoginUsername(matcher.group("loginUsername"));
////                System.out.println(ld.getLoginUsername());
//                ld.setDataObject(matcher.group("dataObject"));
////                System.out.println(ld.getDataObject());
//                ld.setRecords(matcher.group("records"));
////                System.out.println(ld.getRecords());
//                ld.setLabels(matcher.group("labels"));
////                System.out.println(ld.getLabels());
//                ld.setUserAction(matcher.group("userAction"));
////                System.out.println(ld.getUserAction());
//                ld.setUserActionStatus(matcher.group("userActionStatus"));
////                System.out.println(ld.getUserActionStatus());
//                ld.setServiceType(matcher.group("service"));
////                System.out.println(ld.getServiceType());
//                ld.setMappingIds(matcher.group("mappingIds"));
////                System.out.println(ld.getMappingIds());
//                ld.setUri(matcher.group("uri"));
////                System.out.println(ld.getUri());
//                logParsed.add(ld);
//            }
////            ld.printLogParameterValue();
//        }
//        return logParsed;
//    }


    public static void main(String[] args) {
        try {
            //set destination
            File destination = new File("target/regex_log.txt");

            //file to list of String
            List<String> input = Utils.writeAllFilesContentToArrayList("src/test/resources/regex/");

            //method to parse 1 string
            //get string and match, add entry, add to list
//
//
//            List<LogParameters> logParsed = parseLogFileIntoLogParametersList(input);
            List<String> logParsed = parseLogFile(input);
            Utils.writeLogParametersToFile(logParsed, destination);
//
//            System.out.println(mFull.groupCount());
//            while(mFull.find()) {
//                System.out.println(mFull.group());
//            }


//            matcher = patternFullLog.matcher(logfile);
//                while (matcher.find()) {
//                    System.out.println(matcher.group());
//                    ld.setTimestamp(matcher.group("timestamp"));
//                    ld.setActivity(matcher.group("activity"));
//                    ld.setLoginUsername(matcher.group("loginUsername"));
//                    ld.setDataObject(matcher.group("dataObject"));
//                    ld.setRecords(matcher.group("records"));
//                    ld.setLabels(matcher.group("labels"));
//                    ld.setUserAction(matcher.group("userAction"));
//                    ld.setUserActionStatus(matcher.group("userActionData"));
//                    ld.setServiceType(matcher.group("service"));
//                    ld.setMappingIds(matcher.group("mappingIds"));
//                    ld.setUri(matcher.group("uri"));
//                    logParsed.add(ld);
//            }


//            for(LogParameters ldp : logParsed) {
//                System.out.println(ldp.getTimestamp() + " " + ldp.getActivity());
//            }




    } catch (Exception ex) {

        }
    }
}
