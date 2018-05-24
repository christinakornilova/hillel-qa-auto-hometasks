package hometask03;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.Utils;

import java.io.*;
import java.util.List;

public class ReplaceLastDCharacter {
    private static Logger log = LogManager.getLogger(ReplaceLastDCharacter.class);

    private final static String dDFindRgx = "[Dd](?=[^Dd]*$)";

    private final static String path = "src/test/resources/regex/d.txt";

    public static String readFileContentToString(File input) {
        List<String> list = null;
        try {
            //read file content to string
            FileInputStream fileInputStream = new FileInputStream(input);
            list = Utils.addSingleFileContentToList(fileInputStream);
        } catch (Exception e) {
            log.error("Error :)", e);
        }
        return String.join(" ", list);
    }

    //replace last Dd symbol to ""
    public static String replaceLastDdChar(String s) {
            return s.replaceAll(dDFindRgx,"");
    }

    // write new string with replaced line OVER the same file
    public static void overwriteFile(String inputStr, String path) {
        try {
            FileOutputStream fileOut = new FileOutputStream(path);
            fileOut.write(inputStr.getBytes());
            fileOut.close();
        } catch (Exception e) {
            log.error("Error reading file", e);
        }
    }

    public static void main(String[] args) {
        File input = new File(path);
        String s = readFileContentToString(input);
        s = replaceLastDdChar(s);
//        System.out.println(s);
        overwriteFile(s, path);
    }

}
