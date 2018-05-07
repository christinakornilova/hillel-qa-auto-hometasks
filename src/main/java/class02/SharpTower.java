package class02;

import utils.Utils;

import java.util.Scanner;

public class SharpTower {

    public static void drawTower(int value) {
        String line = "## ##";
        int m = value - 1;

        for (int j = 0; j < value; j++) {
            for (int i = 0; i < m; i++) {
                System.out.print(" ");
            }
            System.out.print(line);
            System.out.println();
            line = "#" + line + "#";
            m -= 1;
        }
    }

    public static void main(String[] args) {
        int value = -1;
        Scanner in = new Scanner(System.in);

        while (!(value > 0)) {
            value = Utils.inputPositiveIntegerValue("Please enter tower height ( >= 1): ", in);
        }

        drawTower(value);
    }
}
