package random.number;

import utils.Utils;

import java.util.Scanner;

public class RandomNumberGuess {

    private static String compare(int userValue, int appValue) {
        if (appValue == userValue) {
            return "Values are equal. You win :)";
        } else if (appValue > userValue) {
            return "App value is greater. Try again.";
        } else
            return "App value is smaller. Try again.";
    }

    public static void main(String[] args) {

        try {

            int appRandomValue = (int)(Math.random() * 10);
            int userValue;
            Scanner in = new Scanner(System.in);

//            System.out.println("app: " + appRandomValue);

            do {
                userValue = Utils.inputPositiveIntValueInterval("Please, enter value from 0 to 9: ", in, 0, 9);
                System.out.println(compare(userValue, appRandomValue));
            } while (!(appRandomValue==userValue));

        } catch (Exception e) {
            System.err.println("Invalid entered data. Try to enter values from 0 to 9.");
        }
    }
}
