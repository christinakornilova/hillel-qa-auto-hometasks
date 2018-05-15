package hometask02;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.*;

public class LogParser {

    private final static String logFilesPath = "/Users/Christina/Documents/Skype_Files/logs";

    private static Logger log = LogManager.getLogger(LogParser.class);

    public static String[] getFileContentToArray(FileInputStream fileInputStream) {
        Scanner in = null;
        try{
            in = new Scanner(fileInputStream);
        }catch(Exception e){
            e.printStackTrace();
        }
        List<String> lines = new ArrayList<>();
        String line;

        while(in.hasNext()) {
            line = in.nextLine();
            lines.add(line);
        }
        return lines.toArray(new String[]{});
    }

    public static ArrayList<File> getListOfFiles(String directoryName) {
        File directory = new File(directoryName);
        File[] fList = directory.listFiles();
        ArrayList<File> files = new ArrayList<>();
        for (File file : fList) {
            if (file.isFile()) {
                files.add(file);
            } else if (file.isDirectory()) {
                getListOfFiles(file.getAbsolutePath());
            }
        }
        return files;
    }

    public static List<String> writeAllFilesContentToArrayList(String path) {
        String[] fileContent;
        ArrayList<String[]> arrayOfFilesContentsList = new ArrayList<>();
        FileInputStream fileInputStream = null;
        try {
            ArrayList<File> filesList = getListOfFiles(path);
            for (int i = 0; i < filesList.size(); i++) {
                log.debug(filesList.get(i));
                fileInputStream = new FileInputStream(filesList.get(i));
                log.debug("Total file size to read (in bytes) : "  + fileInputStream.available());
                fileContent = getFileContentToArray(fileInputStream);
                arrayOfFilesContentsList.add(fileContent);
            }

        } catch (Exception e) {
            log.error("Unable to read log files. ", e);
        } finally {
            try {
                if (fileInputStream != null)
                    fileInputStream.close();
            } catch (Exception e) {
                log.error("File input stream close exception. ", e);
            }
        }

        List<String> result = new ArrayList<>();
        for (int i = 0; i < arrayOfFilesContentsList.size(); i++) {
            fileContent = arrayOfFilesContentsList.get(i);
            for(int j = 0; j < fileContent.length; j++)
                    result.add(fileContent[j]);
        }
        return result;
    }

    public static List<LogData> removeUnnecessaryInfo(List<String> result) {
        String s;
        List<LogData> logDataList = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            s = result.get(i);
            logDataList.add(new LogData(s.substring(0,15), s.substring(15)));
        }
        return logDataList;
    }

    public static List<LogData> sortAndFilterListData(List<LogData> logDataList) {
        LogData ld;
        List<LogData> sfLogDataList = new ArrayList<>();

        Collections.sort(logDataList, LogData.dateTimeComparator);
        for (int i = 0; i < logDataList.size(); i++) {
            if(logDataList.get(i).getInfo().contains("Captured transactions:")) {
                while (i+1 < logDataList.size() && !logDataList.get(i+1).getInfo().contains("Captured transactions:")) {
                    ld = new LogData(logDataList.get(i+1).getDateTime(), logDataList.get(i+1).getInfo());
                    if (!ld.getInfo().contains("Log time:") && !ld.getInfo().contains("Asset id:") && !ld.getInfo().contains("transactions:") && !ld.getInfo().contains("Warning:")) {
                        if (ld.getInfo().indexOf("transaction") != -1) {
                            ld.setInfo((ld.getInfo().substring(ld.getInfo().indexOf("transaction"))).substring(14));
                        }
                        sfLogDataList.add(ld);
                    }
                    i+=1;
                }
            }
        }
        return sfLogDataList;
    }

    public static List<LogData> concatSameTimeTransactions(List<LogData> sfLogDataList) {
        String s = "";
        List<LogData> sfGroupLogDataList = new ArrayList<>();

        for (int i = 0; i < sfLogDataList.size(); i++) {
            while (i+1 < sfLogDataList.size() && sfLogDataList.get(i).getDateTime().equals(sfLogDataList.get(i+1).getDateTime())) {
                s = sfLogDataList.get(i).getInfo() + "," + (sfLogDataList.get(i+1).getInfo()).trim();
                i += 1 ;
            }
            sfGroupLogDataList.add(new LogData(sfLogDataList.get(i).getDateTime(), s));
        }
        return sfGroupLogDataList;
    }

    public static File writeToFile(List<LogData> result, File destination) {
        try (BufferedWriter bw = new BufferedWriter (new FileWriter (destination))) {
            for (int i = 0; i < result.size(); i++) {
                bw.write (result.get(i).getDateTime() + result.get(i).getInfo() + "\n");
            }
            bw.close ();
        } catch (IOException e) {
            log.error("Unable to write file. ", e);
        }
        log.info("General file reated successfully.");
        return destination;
    }

    public static void main(String[] args) {
        File destination = new File("target/file.log");
        try {
            List<String> result = writeAllFilesContentToArrayList(logFilesPath);
            log.info("All files were read.");
            List<LogData> logDataList = removeUnnecessaryInfo(result);
            logDataList = sortAndFilterListData(logDataList);
            log.info("Log files content were filtered and sorted successfully.");
            logDataList = concatSameTimeTransactions(logDataList);
            writeToFile(logDataList, destination);
            log.info("Same time transactions were grouped by time, and successfully stored to: " + destination.getPath());
        } catch (Exception e) {
            log.error("Unable to process and/or write file. ", e);
        }
    }
}
