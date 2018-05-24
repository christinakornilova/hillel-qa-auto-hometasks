package hometask03;

public class EmailLineSingleProvider {
    private final static String eList1 = "email1@gmail.com,email2@gmail.com,email3@gmail.com,email4@gmail.com";
    private final static String eList2 = "email1@gmail.com,email2@yahoo.com,email3@gmail.com,email4@gmail.com";

//    private final static String rgx = "^((.+)@gmail\\.com(\\s*,\\s*|\\s*$))*$";
    private final static String rgx = "^(\\w+([-+.']\\w+)*@gmail\\.com(\\s*,\\s*|\\s*$))*$";

    public static void main(String[] args) {
        System.out.println("eList string matches regexp (expected: true) : " + eList1.matches(rgx));
        System.out.println("eList2 string matches regexp (expected: false) : " + eList2.matches(rgx));
    }
}
