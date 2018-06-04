package hometask03;

public class AllRgxFunctions {
    //
    private final static String gmailOnlyRgx = "^(\\w+([-+.']\\w+)*@gmail\\.com(\\s*,\\s*|\\s*$))*$";
    private final static String dDFindRgx = "[Dd](?=[^Dd]*$)";
    private final static String inputCheckRgx = "^((?:(?:Amount:(\\s)?)?)(?:(?:[1-9]?[0-9])|(?:[0-9][0-9][0-9])|1[0-5][0-9][0-9]|(?:1600)))(?:\\.[0-9][0-9])?$";

    //validates that lines of email addresses contains gmail only
    public static boolean isGmailOnly(String s) {
        return s.matches(gmailOnlyRgx);
    }

    public static void testIsGmail1() {
        if(isGmailOnly("acdc@gmail.com")) {
            System.out.println("isGmailOnly passed");
        } else
            System.out.println("is gmail only failed");
    }

    public static void testIsGmail2() {
        if(isGmailOnly("acdc@yahoo.com")) {
            System.out.println("isGmailOnly failed");
        } else
            System.out.println("is gmail only passed");
    }

    public static void testIsGmail3() {
        if(isGmailOnly("acdc@gmail.com,abcd@gmail.com")) {
            System.out.println("isGmailOnly passed");
        } else
            System.out.println("is gmail only failed");
    }

    public static void testIsGmail4() {
        if(isGmailOnly("acdc@gmail.com,abcd@gmail.com,")) {
            System.out.println("isGmailOnly passed");
        } else
            System.out.println("is gmail only failed");
    }

    public static void testIsGmail5() {
        if(isGmailOnly("acdc@gmail.com,adf@yahoo.com,abcd@gmail.com,")) {
            System.out.println("isGmailOnly passed");
        } else
            System.out.println("is gmail only failed");
    }



    //replace last Dd symbol to ""
    public static String replaceLastDdChar(String s) {
        return s.replaceAll(dDFindRgx,"");
    }

    public static void testreplaceD1() {
        String s = "d";
        if(replaceLastDdChar(s).equals("")) {
            System.out.println("replaceD passed");
        } else
            System.out.println("replaceD failed");
    }

    public static void testreplaceD2() {
        String s = "D";
        if(replaceLastDdChar(s).equals("")) {
            System.out.println("replaceD passed");
        } else
            System.out.println("replaceD failed");
    }

    public static void testreplaceD3() {
        String s = "Lorem ipsum";
        if(replaceLastDdChar(s).equals("Lorem ipsum")) {
            System.out.println("replaceD passed");
        } else
            System.out.println("replaceD failed");
    }

    public static void testreplaceD4() {
        String s = "Deer darling Dd";
        if(replaceLastDdChar(s).equals("Deer darling D")) {
            System.out.println("replaceD passed");
        } else
            System.out.println("replaceD failed");
    }

    public static void testreplaceD5() {
        String s = "double trouble";
        if(replaceLastDdChar(s).equals("ouble trouble")) {
            System.out.println("replaceD passed");
        } else
            System.out.println("replaceD failed");
    }


    //write regex only matches with input like '1235' (no ' ') if and only if number is between [0-1600]
    //'Amount: ' can appear at the beginning, cents number can appear at the end, in example '.05'
    public static boolean isInputCorrect(String s) {
        return s.matches(inputCheckRgx);
    }

    public static void isInputCorrectTest1() {
        String in = "1235";
        if(isInputCorrect(in)) {
            System.out.println("inputCorrect passed");
        } else
            System.out.println("inputCorrect failed");
    }

    public static void isInputCorrectTest2() {
        String in = "1700";
        if(isInputCorrect(in)) {
            System.out.println("inputCorrect passed");
        } else
            System.out.println("inputCorrect failed");
    }

    public static void isInputCorrectTest3() {
        String in = "1235.07";
        if(isInputCorrect(in)) {
            System.out.println("inputCorrect passed");
        } else
            System.out.println("inputCorrect failed");
    }

    public static void main(String[] args) {
        testIsGmail1();
        testIsGmail2();
        testIsGmail3();
        testIsGmail4();
        testIsGmail5();


    }

}
