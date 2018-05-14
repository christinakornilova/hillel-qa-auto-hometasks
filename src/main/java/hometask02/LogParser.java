package hometask02;

import java.io.*;
import java.util.*;

public class LogParser {

    private final static String logFilesPath = /*"src/main/resources/logs";*/ "/Users/Christina/Documents/Skype_Files/logs";

//    public static Logger log = LogManager.getLogger(LogParser.class);

    //get all files list
    public static File[] getLogFilesList() {
        return new File(logFilesPath).listFiles();
    }

//    public static String getFileContent(FileInputStream fileInputStream) throws IOException, FileNotFoundException {
//        try(BufferedReader bufferedReader =
//                     new BufferedReader(new InputStreamReader(fileInputStream))) {
//            StringBuilder stringBuilder = new StringBuilder();
//            String line;
//            while((line = bufferedReader.readLine()) != null ) {
//                stringBuilder.append(line);
//                stringBuilder.append('\n');
//            }
//            return stringBuilder.toString();
//        }
//    }

    //using files list get each file content to array of String
    public static String[] getFileContentToArray(FileInputStream fileInputStream) throws IOException, FileNotFoundException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
        List<String> lines = new ArrayList<>();
        String line = bufferedReader.readLine();

        while(line != null){
//            System.out.println(line);
            line = bufferedReader.readLine();
            lines.add(line);
        }
        bufferedReader.close();
        return lines.toArray(new String[]{});
    }

    public static List<String> filterByDate(String[] fileContent, ArrayList<String[]> arrayOfFilesContentsList, String date) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < arrayOfFilesContentsList.size(); i++){ //System.out.println(fileContent[i]);
            fileContent = arrayOfFilesContentsList.get(i);
            for(int j = 0; j < fileContent.length-1; j++)
                if (fileContent[j].contains(date)) {
                result.add(fileContent[j]);
//                    System.out.println(fileContent[j]);
                } //else System.out.println("No such pattern");
        }
        return result;
    }

    public static List<String> fileContentToList(String[] fileContent, ArrayList<String[]> arrayOfFilesContentsList) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < arrayOfFilesContentsList.size(); i++){ //System.out.println(fileContent[i]);
            fileContent = arrayOfFilesContentsList.get(i);
            for(int j = 0; j < fileContent.length-1; j++)
//                if (fileContent[j].contains(date)) {
                    result.add(fileContent[j]);
//                    System.out.println(fileContent[j]);
//                } //else System.out.println("No such pattern");
        }
        return result;
    }

    private static File writeListToFile(List<String> result, File destination) {
        try (BufferedWriter bw = new BufferedWriter (new FileWriter (destination))) {
            for (String line : result) {
                bw.write (line + "\n");
            }
            bw.close ();
        } catch (IOException e) {
            e.printStackTrace ();
        }
        return destination;
    }

    private static File writeToFile(List<LogData> result, File destination) {
        try (BufferedWriter bw = new BufferedWriter (new FileWriter (destination))) {
            for (int i = 0; i < result.size(); i++) {
                bw.write (result.get(i).getDateTime() + result.get(i).getInfo() + "\n");
            }
            bw.close ();
        } catch (IOException e) {
            e.printStackTrace ();
        }
        return destination;
    }



    public static void main(String[] args) {
//        String date = "Feb  1";
        FileInputStream fileInputStream = null;
        String[] fileContent = new String[]{};
        ArrayList<String[]> arrayOfFilesContentsList = new ArrayList<>();
        File destination = new File("target/logsSort/file.log");

        try {
            File[] filesList = getLogFilesList();
            for (int i = 0; i < filesList.length; i++) {
                System.out.println(filesList[i]);
                fileInputStream = new FileInputStream(filesList[i]);
                System.out.println("Total file size to read (in bytes) : "  + fileInputStream.available());
//                content.add(getFileContent(fileInputStream));
                fileContent = getFileContentToArray(fileInputStream);
                arrayOfFilesContentsList.add(fileContent);
            }
            System.out.println("All files were read.");


//            Collections.sort(arrayOfFilesContentsList, Comparator.comparing(strings -> strings[1]));
//            for (String[] sa : arrayOfFilesContentsList) {
//                File destination = new File("target/logsSort/file.log");
//                System.out.println(destination.getAbsolutePath());
////                System.out.println(Arrays.toString(sa));
//                try{
//                    FileWriter fw = new FileWriter(destination);
//                    fw.write(Arrays.toString(sa));
//                    fw.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            Collections.sort(result);


//            List<String> result = filterByDate(fileContent, arrayOfFilesContentsList, date);
//            List<String> result = fileContentToList(fileContent, arrayOfFilesContentsList);

            List<String> result = fileContentToList(fileContent, arrayOfFilesContentsList);
            List<LogData> logDataList = new ArrayList<>();
            List<LogData> sfLogDataList = new ArrayList<>();
//            List<LogData> sfLogDataList = new LinkedList<>();
            List<LogData> sfGroupLogDataList = new ArrayList<>();

            String s;

            for (int i = 0; i < result.size(); i++) {
                s = result.get(i);
                logDataList.add(new LogData(s.substring(0,15), s.substring(15)));
            }
               // System.out.println(result.get(i));

            LogData ld;

            Collections.sort(logDataList, LogData.dateTimeComparator);
            for (int i = 0; i < logDataList.size(); i++) {
                if(logDataList.get(i).getInfo().contains("Captured transactions:")) {
                    try{
                        ld = new LogData(logDataList.get(i+1).getDateTime(), logDataList.get(i+1).getInfo());
                        if(!ld.getInfo().contains("Log time:") && !ld.getInfo().contains("Asset id:") && !ld.getInfo().contains("transactions:") && !ld.getInfo().contains("Warning:")) {
//                            s = ld.getInfo();
//                            int y = s.indexOf("transaction");
                            if (ld.getInfo().indexOf("transaction") != -1)
                                ld.setInfo((ld.getInfo().substring(ld.getInfo().indexOf("transaction"))).substring(14));
//                            s = s.substring(14);
//                            ld.setInfo(s);
                            sfLogDataList.add(ld);
                        } //else {
//                            ld = new LogData(logDataList.get(i+2).getDateTime(), logDataList.get(i+2).getInfo());
//                            sfLogDataList.add(ld);
//                        }
                    } catch (Exception e) {
                    e.printStackTrace();
                }
//                System.out.println(logDataList.get(i).getDateTime() + logDataList.get(i).getInfo());

                }
            }


//            List<LogData> sfLogDataList2 = new ArrayList<>();
//            for (LogData ldata:sfLogDataList
//                 ) {
//                s = ldata.getInfo();
//                int y = s.indexOf("transaction");
//                if (y != -1) s = s.substring(y);
//                s = s.substring(14);
//                ldata.setInfo(s);
//                sfLogDataList2.add(ldata);
//            }
            s = "";

            for (int i = 0; i < sfLogDataList.size() - 2; i++) {
                while (sfLogDataList.get(i).getDateTime().equals(sfLogDataList.get(i+1).getDateTime())) {
                    s = sfLogDataList.get(i).getInfo() + "," + sfLogDataList.get(i+1).getInfo();
                    i +=1 ;
                }
                sfGroupLogDataList.add(new LogData(sfLogDataList.get(i).getDateTime(), s));
            }



//            sfGroupLogDataList.stream().forEach(System.out::println);
//            writeToFile(sfGroupLogDataList, destination);

                //System.out.println(sfLogDataList.get(i).getDateTime() + sfLogDataList.get(i).getInfo());



//            writeToFile(sfLogDataList, destination);

            writeToFile(sfGroupLogDataList, destination);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileInputStream != null)
                    fileInputStream.close();
            } catch (Exception e) {
                System.out.println("File input stream close exception " + e.getMessage());
            }
        }

    }


}
