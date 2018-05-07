package class02;

import utils.Utils;

import java.math.BigDecimal;
import java.util.Scanner;

public class Addition {

    private static int add(int x, int y)  {
        while(y != 0){
            int carry = x & y;
            x = x ^ y;
            y = carry << 1;
        }
        return x;
    }

    private static BigDecimal sum(int x, int y){
        BigDecimal x1 = new BigDecimal(x);
        BigDecimal y1 = new BigDecimal(y);
        return x1.add(y1);
    }

    public static void main(String[] args) {
        int x, y ;
        Scanner in = new Scanner(System.in);
        x = Utils.inputIntegerValue("first", in);
        y = Utils.inputIntegerValue("second", in);
        System.out.println("Sum is : " + add(x,y));
        System.out.println("Sum is : " + sum(x,y) + ". Using BigDecimal.");
    }
}
