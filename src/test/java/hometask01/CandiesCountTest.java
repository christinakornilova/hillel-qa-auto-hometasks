package hometask01;

import org.junit.Assert;
import org.junit.Test;

public class CandiesCountTest {


    @Test
    public void test01() {
        Assert.assertEquals(22, Candies.countCandies(15, 1, 3));
    }

    @Test
    public void test02() {
        Assert.assertEquals(22, Candies.countCandies(100, 5, 10));
    }

    @Test
    public void test03() {
        Assert.assertEquals(7, Candies.countCandies(10, 2, 3));
    }

    @Test
    public void test04() {
        Assert.assertEquals(19, Candies.countCandies(50, 3, 5));
    }

    @Test
    public void test05() {
        Assert.assertEquals(6, Candies.countCandies(17, 3, 5));
    }
}
