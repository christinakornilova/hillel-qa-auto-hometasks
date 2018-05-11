package class03;

import java.util.HashSet;

public class Class03 {


    private static boolean sumOfTwoElements(int x, int[] l) {

        for (int i = 0; i < l.length; i ++)
            for (int j = i + 1; j < l.length; j++)
                if (l[i] + l[j] == x)
                    return true;
        return false;
    }

    private static boolean sumOfElements(int[] l, int x) {
        HashSet<Integer> compl = new HashSet<Integer>();
        for (int i: l
             ) {

        }
        return true;
    }

    public static void main(String[] args) {
        int[] l = {6,5,7};
        int x = 0;

        System.out.println(sumOfTwoElements(x, l));
        System.out.println(sumOfElements(l, x));

    }

}
