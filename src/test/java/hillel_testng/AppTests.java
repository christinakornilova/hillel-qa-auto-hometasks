package hillel_testng;

import org.junit.Assert;
import org.testng.annotations.Test;

import static hometask03.AllRgxFunctions.isGmailOnly;

public class AppTests {

    

    @Test
    public static void test() {
        Assert.assertTrue(isGmailOnly("acdc@gmail.com"));
    }
}
