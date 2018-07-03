package facebook;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.Utils;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FacebookUsersLogParsing {
    final static String logPath = "target/facebook-logs/";

    private static Logger log = LogManager.getLogger(FacebookUsersLogParsing.class);


    private static File writeToFile(List<FacebookUser> result, File destination) {
        try (BufferedWriter bw = new BufferedWriter (new FileWriter(destination))) {
            for (int i = 0; i < result.size(); i++) {
                bw.write (result.get(i).getFirstName() + "," + result.get(i).getLastName() + "," + result.get(i).getId() + "\n");
            }
            bw.close ();
        } catch (IOException e) {
            log.error("Unable to write file. ", e);
        }
        log.info("General file reated successfully.");
        return destination;
    }

    public static String readStringFromURL(String requestURL) throws IOException
    {
        try (Scanner scanner = new Scanner(new URL(requestURL).openStream(),
                StandardCharsets.UTF_8.toString()))
        {
            scanner.useDelimiter("\\A");
            return scanner.hasNext() ? scanner.next() : "";
        }
    }

    private static List<FacebookUser> fillFacebookUserCollection(List<String> filesContent) {
        List<FacebookUser> fbUsersList = new ArrayList<>();
        FacebookUser fbUser;
        for (int i = 0; i < filesContent.size(); i++) {
            //get index of ',' char
            String fbUrl, names = filesContent.get(i);
            fbUrl = names;
            int index = names.indexOf(",");
            //get substring from 0 to index (first and last names)
            names = names.substring(0, index);
            //get substring from index+1 it will be the url
            fbUrl = fbUrl.substring(index + 1);
            //if link has user id
            if (fbUrl.contains("id=")) {
                Pattern pattern = Pattern.compile("(?<=id\\=).*(?=&)");
                Matcher matcher = pattern.matcher(fbUrl);
                if (matcher.find())
                {
                    fbUrl = matcher.group(0);
                    log.info("id #" + i + " : " + fbUrl);
                }
            } else {
                //unstable, too many java.io.FileNotFoundException: should try something else
                //https://stackoverflow.com/questions/7720767/how-to-get-users-id-by-users-profile-url

                //link contains username
                //add method which get http page by link and search for 'referrer_profile_id=' and get all numbers after it
                String content;
                try {
                    //get url content to string
                    content = readStringFromURL(fbUrl);

                    //search for 'referrer_profile_id=' and get all numbers after it
                    Pattern pattern = Pattern.compile("(?<=referrer_profile_id=).*?(?=\")");
                    Matcher matcher = pattern.matcher(content);
                    if (matcher.find())
                    {
                        fbUrl = matcher.group(0);
                        log.info("from url id #" + i + " : " + fbUrl);
                    }
                } catch (Exception e) {
                    fbUrl = "-1: broken page";
                    log.info("from url id #" + i + " : " + fbUrl);
//                    log.error("Unable to process url", e);
                }
            }
            fbUser = new FacebookUser(names, names, fbUrl);
            fbUsersList.add(i, fbUser);
        }
        return fbUsersList;
    }

    public static void main(String[] args) {
        File destination = new File("target/file.log");

        try {
            //get list of files in directory then put each file content to arraylist of String
            List<String> result = Utils.writeAllFilesContentToArrayList(logPath);
            log.info("All files were read.");

            //process links if necessary, update collection objects properly
            List<FacebookUser> fbUsersList = fillFacebookUserCollection(result);

            //write collection objects to file
//            writeToFile(fbUsersList, destination);
            log.info("All data proceed");
        } catch (Exception e) {
            log.error("Unable to process and/or write file. ", e);
        }

    }


}
