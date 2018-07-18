package utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Utils {

    private static Logger log = LogManager.getLogger(Utils.class);

    public static int inputPositiveIntValueInterval(String s, Scanner in, int min, int max){
        String posIntValue;
        do {
            System.out.print(s);
            posIntValue = in.next();
        } while (!isInt(posIntValue) || Integer.valueOf(posIntValue) < min || Integer.valueOf(posIntValue) > max);
        return Integer.parseInt(posIntValue);
    }

    public static int inputPositiveIntegerValue(String s, Scanner in){
        String posIntValue;
        do {
            System.out.print(s);
            posIntValue = in.next();
        } while (!isInt(posIntValue) || !isPositive(posIntValue));
        return Integer.parseInt(posIntValue);
    }

    private static boolean isPositive(String x) {
        try {
            return (Integer.parseInt(x) > 0);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isInt(String x) throws NumberFormatException {
        try {
            Integer.parseInt(x);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static int inputIntegerValue(String s, Scanner in) {
        String value;
        do {
            System.out.println("Please enter " + s + " number: ");
            value = in.next();
        } while (!isInt(value) );
        return Integer.parseInt(value);
    }

    //get files list from directory
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

    //write file content to array of String line by line
    public static String[] addSingleFileContentToArray(FileInputStream fileInputStream) {
        Scanner in = null;
        try{
            in = new Scanner(fileInputStream);
        }catch(Exception e){
            log.error("Unable to read line.", e);
        }
        List<String> lines = new ArrayList<>();
        String line;

        while(in.hasNext()) {
            line = in.nextLine();
            lines.add(line);
        }
        return lines.toArray(new String[]{});
    }

    public static List<String> addSingleFileContentToList(FileInputStream fileInputStream) {
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
        return lines;
    }

    public static List<String> writeAllFilesContentToArrayList(String path) {
        String[] fileContent;
        ArrayList<String[]> arrayOfFilesContentsList = new ArrayList<>();
        FileInputStream fileInputStream = null;
        try {
            ArrayList<File> filesList = getListOfFiles(path);
            for (int i = 0; i < filesList.size(); i++) {
                log.info(filesList.get(i));
                fileInputStream = new FileInputStream(filesList.get(i));
                log.info("Total file size to read (in bytes) : "  + fileInputStream.available());
                fileContent = addSingleFileContentToArray(fileInputStream);
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

        public static File writeLogParametersToFile(List<String> list, File destination) {
        try (BufferedWriter bw = new BufferedWriter (new FileWriter(destination))) {
            for (int i = 0; i < list.size(); i++) {
                bw.write (list.get(i) + "\n");
            }
            bw.close ();
        } catch (IOException e) {
            log.error("Unable to write file. ", e);
        }
        log.info("General file created successfully.");
        return destination;
    }

    public static byte[] getMD5(String input) {
        // https://stackoverflow.com/questions/304268/getting-a-files-md5-checksum-in-java
        MessageDigest md;
        byte[] digest = null;
        try {
            InputStream in = Files.newInputStream(Paths.get(input));
            md = MessageDigest.getInstance("MD5");
            DigestInputStream dis = new DigestInputStream(in, md);
            while (dis.read() != -1) ;
            dis.close();
            digest = md.digest();
        }
        catch (IOException e) {
            log.error("Unable to process file: " + e);
        }
        catch (NoSuchAlgorithmException ex) {
            log.error("Could not find MD5 alg " + ex);
        }

        return digest;
    }

    public static void openPageUsingMenuDropdown(WebElement menuElement, WebElement menuItem, WebDriver driver) {
        UIUtils.waitUntilElementAppears(menuElement, driver);
        menuElement.click();
        UIUtils.waitUntilElementAppears(menuItem, driver);
        menuItem.click();
    }

    public static String getTimestamp() {
        return new SimpleDateFormat("yy/MM/dd HH:mm").format(new Date());
    }

    public static String decodeString(String encodedString) {
        return new String(Base64.decodeBase64(encodedString.getBytes()));
    }
}
