package hometask01;

import utils.Utils;

import java.util.Scanner;

public class Candies {


    public static int countCandies(int money, int cost, int wrapsForCandie) {
        int steps = 0;
        int wrappers = 1;

        if (money < cost) return 0;

        double candiesForOneDollar = 1 / (double)cost;
        double additionalCandies = 1 / (double)cost;

        double workMoney = money - (money % cost);

        while (wrappers <= workMoney)
        {
            steps++;
            wrappers = wrappers * wrapsForCandie;
        }

        for (int i = 0; i < steps; i++)
        {
            additionalCandies = additionalCandies / wrapsForCandie;
            candiesForOneDollar = candiesForOneDollar + additionalCandies;
        }

        return (int) (workMoney * candiesForOneDollar);
    }


    public static void main(String[] args) {
        int money, cost, wrapsForCandie;
        Scanner in = new Scanner(System.in);

        money = Utils.inputPositiveIntegerValue("Please enter amount of money in $: ", in);
        cost = Utils.inputPositiveIntegerValue("Please enter candie cost in $: ", in);
        wrapsForCandie = Utils.inputPositiveIntegerValue("Please enter wraps per candies convert value: ", in);

        System.out.println("You can get " + countCandies(money, cost, wrapsForCandie) + " candies.");
    }
}
