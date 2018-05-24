package regexp;

public class Regexp {

    public static void main(String[] args) {
        String schools = "Name: Hillel, City: Odessa; NAme: Stanford, City: Stanford";
        schools.matches(".*,.*"); // accepts regex, returns true or false
        System.out.println(schools.matches(".*,.*"));
        System.out.println(schools.matches(".*;.*"));
        System.out.println(schools.matches(".*___.*"));


//        System.out.println(schools.replace(schools.replaceAll("Name: ([^,]+)", "Name: *$1*"));
    }

}
