package facebook;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import utils.Utils;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FacebookUsersLogParsing {
    //hardcoded log file location
    final static String logPath = "target/facebook-logs/test";

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

    private static String getFBIdByCurlAndParse(String fbUrl, int i) {
        String fbId;
        try {
            //get url content to string and search for 'entity_id' and get all numbers after it
            fbUrl = fbUrl.replaceAll("(?<=\\?).*", "").replaceAll("\\?","");

            Process p1 = Runtime.getRuntime().exec(new String[] { "curl", "-v", fbUrl, "2>&1" });
            InputStream input = p1.getInputStream();
            Process p2 = Runtime.getRuntime().exec(new String[] { "grep", "-o", "entity_id.*]" });
            OutputStream output1 = p2.getOutputStream();
            IOUtils.copy(input, output1);
            output1.close(); // signals grep to finish

            List<String> result = IOUtils.readLines(p2.getInputStream(), Charset.defaultCharset());
            fbId = result.toString().substring(0,28).replaceAll("[^0-9]", "");
            log.debug("id #" + i + " : " + fbId);

        } catch (Exception e) {
            fbId = "-1: : broken page " + fbUrl;
            log.error("Unable to process url " + fbUrl + " id #" + i + " : " + fbId, e);
        }
        return fbId;
    }

    private static String getFBIdUsingJsoup(String fbUrl, int i) {
        Document doc;
        String fbId = "";
        String fbUrlSimple = fbUrl.replaceAll("(?<=\\?).*", "").replaceAll("\\?","");
        try {
            doc = Jsoup.connect(fbUrlSimple).timeout(30*1000).get();

            //get user's photo link
            Element photoLink = doc.select("div.photoContainer > div > a").first();
            String pLink = photoLink.attr("abs:href");

            //search for 'fbId='
            int index = fbId.indexOf("fbid=");
            fbId = pLink.substring(index, index + 30).replaceAll("[^0-9]", "");
            log.debug("photo link: " + pLink + " user id: " + fbId);
        } catch (Exception e) {
            fbId = "-1: : broken page";
            log.error("Unable to process url " + fbUrl + " id #" + i + " : " + fbId, e);
        }
        return fbId;
    }

    private static String getFbIdJsoupFBLogin(String fbUrl, int i) {

        fbUrl = fbUrl.replaceAll("(?<=\\?).*", "").replaceAll("\\?","");
        String fbId;

        try {
            String URL_LOGIN = "https://www.facebook.com/login.php";
            String EMAIL = "fbalsarino@gmail.com";
            String PASSWORD = "slicetesting";
            Connection.Response res = Jsoup.connect(URL_LOGIN).timeout(120000).data("email", EMAIL).data("pass", PASSWORD).method(Connection.Method.POST).execute();
            Map<String, String> loginCookies = res.cookies();

            Document doc = Jsoup.connect(fbUrl).cookies(loginCookies)
                    .timeout(120000).get();

            StringBuffer sb = new StringBuffer();
            Elements links = doc.getAllElements();
            for (Element link : links) {
                log.debug(link);
                sb.append(link.toString());
            }
            fbId = sb.toString();
            int index = fbId.indexOf("fbid=");
            log.debug("index: " + index + " fbId.length: " + fbId.length());
            fbId = fbId.substring(index, index + 30).replaceAll("[^0-9]", "");
            log.debug("fbId = " + fbId);
        } catch (Exception e) {
            fbId = "-1: : broken page";
            log.error("Unable to process url " + fbUrl  + "id #" + i + " : " + fbId, e);
        }
        return fbId;
    }

    private static List<FacebookUser> fillFacebookUserCollection(List<String> filesContent) {
        List<FacebookUser> fbUsersList = new ArrayList<>();
        FacebookUser fbUser;
        for (int i = 0; i < filesContent.size(); i++) {
            //get index of ',' char
            String fbId = "";
            String names = filesContent.get(i);
            String fbUrl = filesContent.get(i);

            int index = names.indexOf(",");
            //get substring from 0 to index (first and last names)
            names = names.substring(0, index);

            //first and last name
            String userFirstName = "";
            String userLastName = "";
            int in = names.indexOf(",");
            //get substring from 0 to index (first and last names)
            names = names.substring(0, index);
            in = names.indexOf(" ");

            userFirstName = names.substring(0,in);
            log.debug(userFirstName);
            userLastName = names.substring(in, names.length()).trim();
            log.debug(userLastName);

            //get substring from index+1 it will be the url
            fbUrl = fbUrl.substring(index + 1);
            //if link has user id
            if (fbUrl.contains("id=")) {
                Pattern pattern = Pattern.compile("(?<=id\\=).*(?=&)");
                Matcher matcher = pattern.matcher(fbUrl);
                if (matcher.find())
                {
                    fbUrl = matcher.group(0);
                    log.debug("id #" + i + " : " + fbUrl);
                }
            } else {
                //link contains username
                //add method which get http page by link and search for 'referrer_profile_id=' and get all numbers after it
                fbId = getFBIdByCurlAndParse(fbUrl, i); //very slow, need to use several threads?

                //Both do not work properly because of updated in April, 2018 FB TOS (prohibit pages scrapping)
//                fbId = getFBIdUsingJsoup(fbUrl, i);
//                fbId = getFbIdJsoupFBLogin(fbUrl);

            }

            fbUser = new FacebookUser(userFirstName, userLastName, fbId);
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
            writeToFile(fbUsersList, destination);
            log.info("All data proceed");
        } catch (Exception e) {
            log.error("Unable to process and/or write file. ", e);
        }

    }


}
