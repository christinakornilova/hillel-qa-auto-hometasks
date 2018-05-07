package utils;

import java.util.Scanner;

public class Utils {

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

}
